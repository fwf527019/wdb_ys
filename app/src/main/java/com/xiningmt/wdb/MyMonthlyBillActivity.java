package com.xiningmt.wdb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.xiningmt.wdb.R.string;

import rxh.hb.adapter.MyMonthlyBillActivityLVAdapter;
import rxh.hb.base.BaseActivity;
import rxh.hb.jsonbean.ActivityCenterActivityLVBean;
import rxh.hb.jsonbean.MyMonthlyBillBean;
import rxh.hb.utils.ChangeUtils;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.ShowDialog;
import rxh.hb.volley.util.VolleySingleton;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class MyMonthlyBillActivity extends BaseActivity implements OnDismissListener {

    private ImageView back;
    private TextView title, investment_this_month, this_month_of_payment, cumulative_recharge_this_month,
            cumulative_cash_withdrawals_this_month;
    private PopupWindow popupWindow;
    private View popView, parent;
    private ListView lv;
    RequestQueue mRequestQueue;
    List<String> lvBeans = new ArrayList<String>();
    List<MyMonthlyBillBean> monBeans = new ArrayList<MyMonthlyBillBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(this.getApplicationContext()).getRequestQueue();
        initdata();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GETIF");
        }
    }

    public void initview(String year, String month) {
        setContentView(R.layout.activity_my_monthly_bill);
        parent = findViewById(R.id.parent);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        title.setText(year + "-" + month + "账单");
        title.setOnClickListener(this);
        investment_this_month = (TextView) findViewById(R.id.investment_this_month);
        this_month_of_payment = (TextView) findViewById(R.id.this_month_of_payment);
        cumulative_recharge_this_month = (TextView) findViewById(R.id.cumulative_recharge_this_month);
        cumulative_cash_withdrawals_this_month = (TextView) findViewById(R.id.cumulative_cash_withdrawals_this_month);
        initPopupWindow();
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                popupWindow.dismiss();
                title.setText(lvBeans.get(arg2) + "账单");
                String a[] = lvBeans.get(arg2).split("-");
                getinformation(a[0], a[1]);

            }
        });
    }

    public void inittext(List<MyMonthlyBillBean> monBean) {
        if (monBean.size() > 0) {
            investment_this_month.setText(ChangeUtils.get_money(Double.valueOf(monBean.get(0).getInvestmoney())));
            this_month_of_payment.setText(ChangeUtils.get_money(Double.valueOf(monBean.get(0).getRepaymentmoney())));
            cumulative_recharge_this_month.setText(ChangeUtils.get_money(Double.valueOf(monBean.get(0).getVouchermoney())));
            cumulative_cash_withdrawals_this_month.setText(ChangeUtils.get_money(Double.valueOf(monBean.get(0).getDrawithmoney())));
        } else {
            investment_this_month.setText("0.00");
            this_month_of_payment.setText("0.00");
            cumulative_recharge_this_month.setText("0.00");
            cumulative_cash_withdrawals_this_month.setText("0.00");
        }

    }

    public void initdata() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String date = sdf.format(new java.util.Date());
        String s = new String(date);
        String a[] = s.split("-");
        base_title.setText(a[0] + "-" + a[1] + "账单");
        for (int i = 2016; i < Integer.parseInt(a[0]) + 1; i++) {
            if (a[0].equals(String.valueOf(i))) {
                for (int j = 1; j < Integer.parseInt(a[1]) + 1; j++) {
                    if (j < 10) {
                        lvBeans.add(String.valueOf(i) + "-0" + String.valueOf(j));
                    } else {
                        lvBeans.add(String.valueOf(i) + "-" + String.valueOf(j));
                    }
                }
            } else {
                for (int j = 1; j < 13; j++) {
                    if (j < 10) {
                        lvBeans.add(String.valueOf(i) + "-0" + String.valueOf(j));
                    } else {
                        lvBeans.add(String.valueOf(i) + "-" + String.valueOf(j));
                    }

                }
            }

        }
        getinformation(a[0], a[1]);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.title:
                backgroundAlpha(0.5f);
                // 为popWindow添加动画效果
                popupWindow.setAnimationStyle(R.style.popWindow_animation);
                // 点击弹出泡泡窗口
                popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.base_back:
                finish();
                break;

            default:
                break;
        }

    }

    public void getinformation(final String year, final String month) {
        loading("加载中", "true");
        Map<String, String> map = new HashMap<String, String>();
        map.put("year", year);
        map.put("month", month);
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest getif = new JsonObjectRequest(Method.POST,
                new CreatUrl().creaturl("authorization/mbill", "monthBill"), paramMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismiss();
                        try {
                            String code = response.getString("code");
                            if (Integer.parseInt(code) == 200) {
                                initview(year, month);
                                monBeans = JsonUtils.getmonBean(response.toString());
                                inittext(monBeans);
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismiss();
                r_load.setText("服务器或网络出现异常");

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

    /**
     * 初始化popupWindow
     */
    private void initPopupWindow() {
        popView = getLayoutInflater().inflate(R.layout.activity_my_monthly_bill_popwindow, null);
        lv = (ListView) popView.findViewById(R.id.lv);
        lv.setAdapter(new MyMonthlyBillActivityLVAdapter(lvBeans, MyMonthlyBillActivity.this));
        lv.setSelection(lvBeans.size());
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(this);
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

}
