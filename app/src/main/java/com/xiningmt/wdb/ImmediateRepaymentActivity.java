package com.xiningmt.wdb;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.PopupWindow.OnDismissListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import rxh.hb.adapter.ImmediateRepaymentLVAdapter;
import rxh.hb.adapter.MyLoanLVAdapter;
import rxh.hb.base.BaseActivity;
import rxh.hb.eventbusentity.IRAEntity;
import rxh.hb.eventbusentity.WalletEntity;
import rxh.hb.jsonbean.FinancialProjectDetailsActivityBeanDetails;
import rxh.hb.jsonbean.ImmediateRepaymentEntity;
import rxh.hb.utils.ChangeUtils;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.volley.util.VolleySingleton;

/**
 * Created by Administrator on 2016/10/11.
 * 我要还款界面
 */
public class ImmediateRepaymentActivity extends BaseActivity implements OnDismissListener, ImmediateRepaymentLVAdapter.OnCheckClickLitener {
    private List<String> ids;

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.lv)
    PullToRefreshListView lv;
    @Bind(R.id.repayment_amount)
    TextView repayment_amount;
    @Bind(R.id.immediate_repayment)
    TextView immediate_repayment;

    List<ImmediateRepaymentEntity> data = new ArrayList<ImmediateRepaymentEntity>();
    ImmediateRepaymentLVAdapter adapter;

    int page = 1;
    RequestQueue mRequestQueue;
    int sectorflag = -1;
    String loanids;

    //支付需要用到的东西
    View view;
    String tok;
    Dialog photochoosedialog;
    private PopupWindow popupWindow;
    private View popView, parent;
    private TextView the_facility_is_the_payment_password;
    private EditText paypassword;
    private Button cancel, confirm, bank_card_payment, the_balance_of_payments;
    FinancialProjectDetailsActivityBeanDetails fd = new FinancialProjectDetailsActivityBeanDetails();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loanids = getIntent().getStringExtra("loanids");
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(
                this.getApplicationContext()).getRequestQueue();
        initview();
        getinformation(String.valueOf(page));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GETINFO");
            mRequestQueue.cancelAll("GETLVIF");
            mRequestQueue.cancelAll("GETU");
            mRequestQueue.cancelAll("PAY");
        }
    }

    public void initview() {
        setContentView(R.layout.activity_immediate_repayment);
        ButterKnife.bind(this);
        parent = findViewById(R.id.parent);
        back.setOnClickListener(this);
        immediate_repayment.setOnClickListener(this);
        title.setText("我要还款");
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(
                        getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // 这里写下拉刷新的任务
                page = 1;
                getinformation(String.valueOf(page));

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // 这里写上拉加载更多的任务
                getinformation(String.valueOf(page));
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.immediate_repayment:
                if (ids != null && ids.size() != 0) {
                    getuserstate();
                } else {
                    Toast.makeText(getApplicationContext(), "请选择还款项目", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bank_card_payment:
                photochoosedialog.dismiss();
                if(data.get(0).getLoanids()!=null){
                    getinformation(data.get(0).getLoanids(), String.valueOf(tMoney));
                }
                break;
            case R.id.the_balance_of_payments:
                photochoosedialog.dismiss();
                initPopupWindow();
                backgroundAlpha(0.5f);
                // 为popWindow添加动画效果
                popupWindow.setAnimationStyle(R.style.popWindow_animation);
                // 点击弹出泡泡窗口
                popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
                break;
            case R.id.the_facility_is_the_payment_password:
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ModifyThePaymentPasswordActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.cancel:
                popupWindow.dismiss();
                break;
            case R.id.confirm:
                // 余额支付确认按钮
                if (paypassword.getText().toString().length() > 0) {
                    pay();
                    confirm.setEnabled(false);
                } else {
                    Toast.makeText(getApplicationContext(), "请输入支付密码", Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }
    }


    public void getinformation(String pageNumber) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("loanids", loanids);
        map.put("pageNumber", pageNumber);
        map.put("pageSize", "10");
        JSONObject paramMap = new JSONObject(map);
        loading("加载中", "true");
        JsonObjectRequest getcode = new JsonObjectRequest(Request.Method.POST,
                new CreatUrl().creaturl("authorization/payment", "repay"), paramMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismiss();
                        try {
                            String code = response.getString("code");
                            if (new RequestReturnProcessing(getApplicationContext(), code).processing() == 200) {
                                if (page == 1) {
                                    data.clear();
                                    data.addAll(JsonUtils.getrepay(response.toString()));
                                    Log.d("ImmediateRepaymentActiv", response.toString());

                                    adapter = new ImmediateRepaymentLVAdapter(data, getApplicationContext());
                                    adapter.setOnCheckClickLitener(ImmediateRepaymentActivity.this);
                                    lv.setAdapter(adapter);
                                } else {
                                    data.addAll(JsonUtils.getrepay(response.toString()));
                                    adapter.notifyDataSetChanged();
                                }
                                lv.onRefreshComplete();
                                lv.onRefreshComplete();
                                page++;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismiss();
                Toast.makeText(getApplicationContext(),
                        error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", MyApplication.getToken());
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        getcode.setTag("GETINFO");
        VolleySingleton.getVolleySingleton(getApplicationContext())
                .addToRequestQueue(getcode);

    }


    /**
     * 初始化popupWindow
     */
    private void initPopupWindow() {
        popView = getLayoutInflater().inflate(R.layout.activity_confirm_investment_amount_dialog, null);
        TextView the_amount_paid = (TextView) popView.findViewById(R.id.the_amount_paid);
        the_amount_paid.setText(ChangeUtils.get_money(tMoney));
        the_facility_is_the_payment_password = (TextView) popView
                .findViewById(R.id.the_facility_is_the_payment_password);
        the_facility_is_the_payment_password.setOnClickListener(this);
        if (fd.getPaypassword().equals("1")) {
            the_facility_is_the_payment_password.setVisibility(View.GONE);
        } else {
            the_facility_is_the_payment_password.setVisibility(View.VISIBLE);
        }
        paypassword = (EditText) popView.findViewById(R.id.paypassword);
        cancel = (Button) popView.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        confirm = (Button) popView.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        // popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setOnDismissListener(this);
    }

    // 选择支付手段
    private void showphotochooseDialog() {
        view = getLayoutInflater().inflate(R.layout.activity_confirm_investment_amount_pop_window, null);
        photochoosedialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        photochoosedialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = photochoosedialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        photochoosedialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        photochoosedialog.setCanceledOnTouchOutside(true);
        photochoosedialog.show();
        bank_card_payment = (Button) view.findViewById(R.id.bank_card_payment);
        bank_card_payment.setOnClickListener(this);
        the_balance_of_payments = (Button) view.findViewById(R.id.the_balance_of_payments);
        the_balance_of_payments.setText("余额支付(" + ChangeUtils.get_money(tMoney) + ")元");
        the_balance_of_payments.setOnClickListener(this);
        Button quxiao = (Button) view.findViewById(R.id.quxiao);
        quxiao.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                photochoosedialog.dismiss();
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        this.getWindow().setAttributes(lp);
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */

    @Override
    public void onDismiss() {
        backgroundAlpha(1f);

    }


    // 银行还款 type 2
    public void getinformation(String loanIds, String money) {
        StringBuilder str=new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            str.append(ids.get(i));
            if(i<ids.size()-1){
                str.append(",");
            }

        }

        loading("获取中", "true");
        Map<String, String> map = new HashMap<String, String>();
        map.put("loanids", loanIds);
        map.put("paymentIdss",  str.toString());
        map.put("zmoney", money);
        map.put("typea", "1");
        map.put("type", "2");

        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest getif = new JsonObjectRequest(Request.Method.POST,

                new CreatUrl().creaturl("authorization", "payment/repayWay"), paramMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismiss();
                        try {
                            String code = response.getString("code");
                            String tn = response.getString("obj").trim();
                            if (new RequestReturnProcessing(ImmediateRepaymentActivity.this, code)
                                    .processing() == 200) {
                                String serverMode = "00";
                                UPPayAssistEx.startPayByJAR(ImmediateRepaymentActivity.this, PayActivity.class,
                                        null, null, tn, serverMode);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismiss();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", MyApplication.getToken());
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        getif.setTag("GETIF");
        VolleySingleton.getVolleySingleton(getApplicationContext()).addToRequestQueue(getif);
    }

    // 获取用户的状态
    public void getuserstate() {
        loading("获取中", "true");
        JsonObjectRequest getif = new JsonObjectRequest(Request.Method.POST,
                new CreatUrl().creaturl("authorization/getLoanLogin", "getLoanLogin"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismiss();
                        try {
                            tok = response.getString("tok");
                            String code = response.getString("code");
                            if (new RequestReturnProcessing(ImmediateRepaymentActivity.this, code)
                                    .processing() == 200) {
                                fd = JsonUtils.getu(response.toString());
                                showphotochooseDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismiss();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", MyApplication.getToken());
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        getif.setTag("GETU");
        VolleySingleton.getVolleySingleton(getApplicationContext()).addToRequestQueue(getif);
    }

    // 余额还款 type 1
    public void pay() {

        StringBuilder str=new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            str.append(ids.get(i));
            if(i<ids.size()-1){
                str.append(",");
            }

        }
        loading("支付中", "true");
        Map<String, String> map = new HashMap<String, String>();
        map.put("tok", tok);
        map.put("loanids", data.get(0).getLoanids());
        map.put("paymentIdss", str.toString());
        map.put("zmoney", ChangeUtils.get_money(tMoney));
        map.put("paypassword", paypassword.getText().toString());
        map.put("type", "1");
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest getif = new JsonObjectRequest(Request.Method.POST,
                //        new CreatUrl().creaturl("authorization", "order/createLoanOrder"), paramMap,
                new CreatUrl().creaturl("authorization", "payment/repayWay"), paramMap,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismiss();
                        try {
                            String code = response.getString("code");
                            if (new RequestReturnProcessing(ImmediateRepaymentActivity.this, code)
                                    .processing() == 200) {
                                Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_LONG).show();
                                EventBus.getDefault().post(new WalletEntity("login"));
                                EventBus.getDefault().post(new IRAEntity());
                                EventBus.getDefault().post("刷新");
                                popupWindow.dismiss();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                confirm.setEnabled(true);
                dismiss();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", MyApplication.getToken());
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        getif.setTag("PAY");
        // getif.setRetryPolicy(new DefaultRetryPolicy(6 * 1000, 1, 1.0f));
        VolleySingleton.getVolleySingleton(getApplicationContext()).addToRequestQueue(getif);
    }


//    @Override
//    public void onCheckClick(int position) {
//
//        repayment_amount.setText(Html.fromHtml("待支付"
//                + "<font color='#FB6006'>"
//                + ChangeUtils.get_money(Double.valueOf(data.get(sectorflag).getZmoney())) + "</font>元"));
//
//    }


    @Override
    public void onCheckClick(int postion, boolean isCheackd) {
        //单选
//        if(isCheackd){
//            sectorflag=postion;
//                    repayment_amount.setText(Html.fromHtml("待支付"
//                + "<font color='#FB6006'>"
//                + ChangeUtils.get_money(Double.valueOf(data.get(postion).getZmoney())) + "</font>元"));
//        }else {
//            sectorflag=-1;
//            repayment_amount.setText(Html.fromHtml("待支付"
//                    + "<font color='#FB6006'>"
//                    + "</font>0元"));
//        }

        //多选
        data.get(postion).setCheacked(isCheackd);
        adapter.notifyDataSetChanged();
        instat();
    }


    /**
     * 选择时计算还款数
     */
    double tMoney=0;

    private void instat() {
        ids = new ArrayList<>();
        tMoney = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isCheacked()) {
                ids.add(data.get(i).getIds());
                tMoney += Double.parseDouble(data.get(i).getZmoney());
            }

        }

        repayment_amount.setText(Html.fromHtml("待支付"
                + "<font color='#FB6006'>"
                + ChangeUtils.get_money(tMoney) + "</font>元"));
    }
}
