package com.xiningmt.wdb;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.PopupWindow.OnDismissListener;

import rxh.hb.base.BaseActivity;
import rxh.hb.eventbusentity.CIAEntity;
import rxh.hb.eventbusentity.WalletEntity;
import rxh.hb.jsonbean.FinancialProjectDetailsActivityBean;
import rxh.hb.jsonbean.FinancialProjectDetailsActivityBeanDetails;
import rxh.hb.utils.ChangeUtils;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.utils.ShowDialog;
import rxh.hb.utils.StatusCode;
import rxh.hb.volley.util.VolleySingleton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

import de.greenrobot.event.EventBus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmInvestmentAmountActivity extends BaseActivity implements OnDismissListener {

    private TextView title, name, yuqinianhua, qitoujinetv, shengyujinetv, licaiqixiantv,
            the_facility_is_the_payment_password;
    private ImageView back;
    private Button immediate_investment, bank_card_payment, the_balance_of_payments, cancel, confirm;
    RequestQueue mRequestQueue;
    private EditText jine, paypassword;
    private String ids, titletv, nianhua, qitoujine, licaiqixian, tok;
    View view;
    Dialog photochoosedialog;
    private PopupWindow popupWindow;
    private View popView, parent;
    Double sy;
    FinancialProjectDetailsActivityBean cbBean = new FinancialProjectDetailsActivityBean();
    FinancialProjectDetailsActivityBeanDetails fd = new FinancialProjectDetailsActivityBeanDetails();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ids = getIntent().getStringExtra("id");
        titletv = getIntent().getStringExtra("title");
        nianhua = getIntent().getStringExtra("nianhua");
        qitoujine = getIntent().getStringExtra("qitoujine");
        licaiqixian = getIntent().getStringExtra("licaiqixian");
        base_title.setText("投资");
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(this.getApplicationContext()).getRequestQueue();
        getlvinformation(ids);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GETIF");
            mRequestQueue.cancelAll("GETLVIF");
            mRequestQueue.cancelAll("GETU");
            mRequestQueue.cancelAll("PAY");
        }
    }

    public void initview() {
        setContentView(R.layout.activity_confirm_investment_amount);
        parent = findViewById(R.id.parent);
        title = (TextView) findViewById(R.id.title);
        name = (TextView) findViewById(R.id.name);
        yuqinianhua = (TextView) findViewById(R.id.yuqinianhua);
        qitoujinetv = (TextView) findViewById(R.id.qitoujine);
        shengyujinetv = (TextView) findViewById(R.id.shengyujine);
        licaiqixiantv = (TextView) findViewById(R.id.licaiqixian);
        jine = (EditText) findViewById(R.id.jine);
        setPricePoint(jine);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        immediate_investment = (Button) findViewById(R.id.immediate_investment);
        immediate_investment.setOnClickListener(this);
    }

    public void initdata() {
        title.setText("投资");
        name.setText(titletv);
        yuqinianhua.setText("预期年化" + ChangeUtils.get_money(Double.parseDouble(nianhua)) + "%");
        qitoujinetv.setText("起投金额" + qitoujine + "元");
        licaiqixiantv.setText("投资期限" + licaiqixian);
        if (cbBean.getPusm() == null) {
            sy = Double.parseDouble(cbBean.getMoneys());
        } else {
            sy = Double.parseDouble(cbBean.getMoneys()) - Double.parseDouble(cbBean.getPusm());
        }
        shengyujinetv.setText("剩余金额" + ChangeUtils.get_money(sy) + "元");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String str = data.getExtras().getString("pay_result");
        if (str != null) {
            if (str.equals("cancel")) {
                Toast.makeText(getApplicationContext(), "你取消了本次支付", Toast.LENGTH_LONG).show();
            } else if (str.equals("success")) {
                Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_LONG).show();
                EventBus.getDefault().post(new CIAEntity());
                finish();
            } else if (str.equals("fail")) {
                Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_LONG).show();
            } else if (str.equals("mtp") && requestCode == 1 && resultCode == RESULT_OK) {
                the_facility_is_the_payment_password.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.base_back:
                finish();
                break;
            case R.id.immediate_investment:
                if(jine.getText().toString().length()==0){
                    Toast.makeText(getApplicationContext(), "金额不能为空", Toast.LENGTH_LONG).show();
                }
                else {
                    //判断输入金额是否是起投金额的整数倍
                    if (Double.parseDouble(jine.getText().toString()) % Double.parseDouble(qitoujine) == 0) {
                        if (MyApplication.getToken() != null) {
                            if (!jine.getText().toString().equals("")) {
                                if (Double.parseDouble(jine.getText().toString()) >= Double.parseDouble(qitoujine)) {
                                    //剩余金额比较
                                    if (Double.parseDouble(jine.getText().toString()) <= sy) {
                                        getuserstate();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "投资金额大于产品剩余金额", Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "输入金额小于起投金额", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "金额不能为空", Toast.LENGTH_LONG).show();
                            }
                            // “00” – 银联正式环境
                            // “01” – 银联测试环境，该环境中不发生真实交易
                        } else {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "请输入起投金额的整数倍", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.bank_card_payment:
                photochoosedialog.dismiss();
                getinformation(ids, jine.getText().toString());
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
                } else {
                    Toast.makeText(getApplicationContext(), "请输入支付密码", Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }

    }

    /**
     * 初始化popupWindow
     */
    private void initPopupWindow() {
        popView = getLayoutInflater().inflate(R.layout.activity_confirm_investment_amount_dialog, null);
        TextView the_amount_paid = (TextView) popView.findViewById(R.id.the_amount_paid);
        the_amount_paid.setText(jine.getText().toString());
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
        popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
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
        photochoosedialog.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        Window window = photochoosedialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = LayoutParams.MATCH_PARENT;
        wl.height = LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        photochoosedialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        photochoosedialog.setCanceledOnTouchOutside(true);
        photochoosedialog.show();
        bank_card_payment = (Button) view.findViewById(R.id.bank_card_payment);
        bank_card_payment.setOnClickListener(this);
        the_balance_of_payments = (Button) view.findViewById(R.id.the_balance_of_payments);
        the_balance_of_payments.setText("余额支付(" + ChangeUtils.get_money(Double.parseDouble(fd.getMoneys())) + ")元");
        the_balance_of_payments.setOnClickListener(this);
        Button quxiao = (Button) view.findViewById(R.id.quxiao);
        quxiao.setOnClickListener(new OnClickListener() {

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

    // 截取字符串，限制输入
    public static void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (s.toString().contains(".")) {
                        if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                            s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                            editText.setText(s);
                            editText.setSelection(s.length());
                        }
                    }
                    if (s.toString().trim().substring(0).equals(".")) {
                        s = "0" + s;
                        editText.setText(s);
                        editText.setSelection(2);
                    }

                    if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                        if (!s.toString().substring(1, 2).equals(".")) {
                            editText.setText(s.subSequence(0, 1));
                            editText.setSelection(1);
                            return;
                        }
                    }

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

    }

    // 获取单个投资项目详情
    public void getlvinformation(String ids) {
        loading("获取中", "true");
        Map<String, String> map = new HashMap<String, String>();
        map.put("ids", ids);
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest getif = new JsonObjectRequest(Method.POST, new CreatUrl().creaturl("loan", "getLoan"),
                paramMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismiss();
                try {
                    String code = response.getString("code");
                    if (new RequestReturnProcessing(ConfirmInvestmentAmountActivity.this, code)
                            .processing() == 200) {
                        cbBean = JsonUtils.getBeans(response.toString());
                        initview();
                        initdata();
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
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        getif.setTag("GETLVIF");
        VolleySingleton.getVolleySingleton(getApplicationContext()).addToRequestQueue(getif);
    }

    // 获取tn
    public void getinformation(String loanIds, String money) {
        loading("获取中", "true");
        Map<String, String> map = new HashMap<String, String>();
        map.put("loanIds", loanIds);
        map.put("money", money);
        map.put("type", "1");
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest getif = new JsonObjectRequest(Method.POST,
                new CreatUrl().creaturl("authorization", "order/createLoanOrder"), paramMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismiss();
                        try {
                            String code = response.getString("code");
                            String tn = response.getString("obj").trim();
                            if (new RequestReturnProcessing(ConfirmInvestmentAmountActivity.this, code)
                                    .processing() == 200) {
                                String serverMode = "00";
                                UPPayAssistEx.startPay(ConfirmInvestmentAmountActivity.this, null, null, tn, serverMode);
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
        JsonObjectRequest getif = new JsonObjectRequest(Method.POST,
                new CreatUrl().creaturl("authorization/getLoanLogin", "getLoanLogin"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismiss();
                        try {
                            tok = response.getString("tok");
                            String code = response.getString("code");
                            if (new RequestReturnProcessing(ConfirmInvestmentAmountActivity.this, code)
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

    // 用户采用自己账户进行支付
    public void pay() {
        confirm.setEnabled(false);
        loading("支付中", "true");
        Map<String, String> map = new HashMap<String, String>();
        map.put("tok", tok);
        map.put("loanIds", ids);
        map.put("money", jine.getText().toString());
        map.put("paypassword", paypassword.getText().toString());
        map.put("type", "2");
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest getif = new JsonObjectRequest(Method.POST,
                new CreatUrl().creaturl("authorization", "order/createLoanOrder"), paramMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismiss();
                        confirm.setEnabled(true);
                        try {
                            String code = response.getString("code");
                            if (new RequestReturnProcessing(ConfirmInvestmentAmountActivity.this, code)
                                    .processing() == 200) {
                                Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_LONG).show();
                                EventBus.getDefault().post(new WalletEntity("login"));
                                EventBus.getDefault().post(new CIAEntity());
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

}
