package com.xiningmt.wdb;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

import rxh.hb.base.BaseActivity;
import rxh.hb.jsonbean.MyAccountActivityBean;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.view.SmoothCheckBox;
import rxh.hb.volley.util.VolleySingleton;

public class FinanceActivity extends BaseActivity implements OnDismissListener {

    private TextView title;
    private ImageView back;
    private EditText financing_amount, financing_period, name, phone_number;
    private CheckBox mortgage, credit_loan, bond_transfer;
    private Button submit;
    int loan_flag = 0;
    RequestQueue mRequestQueue;
   private  SharedPreferences sp;
    private PopupWindow popupWindow;
    private View popView, parent;
    private int LoginType=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);
        mRequestQueue = VolleySingleton.getVolleySingleton(this.getApplicationContext()).getRequestQueue();
         sp = getSharedPreferences("user", 0);
        initview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("UPLOAD");
        }
    }

    public void initview() {

        parent = findViewById(R.id.parent);
        title = (TextView) findViewById(R.id.title);
        title.setText("我要借款");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        financing_amount = (EditText) findViewById(R.id.financing_amount);
        financing_period = (EditText) findViewById(R.id.financing_period);
        name = (EditText) findViewById(R.id.name);
        phone_number = (EditText) findViewById(R.id.phone_number);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        mortgage = (CheckBox) findViewById(R.id.mortgage);
        mortgage.setChecked(true);
        credit_loan = (CheckBox) findViewById(R.id.credit_loan);
        bond_transfer = (CheckBox) findViewById(R.id.bond_transfer);
        loan_flag = 2;
        if(sp.getString("user_name", null)!=null){
            phone_number.setText(sp.getString("user_name", null));
        }else {
            phone_number.setText("");
        }

        if(sp.getString("Ruser_name", null)!=null){
            name.setText(sp.getString("Ruser_name", null));
        }else {
            name.setText("");
        }
        //会员类型（1个人，2机构）
        if(sp.getInt("LoginType", 0)==1){
            LoginType=1;
        }else if(sp.getInt("LoginType", 0)==2){
            LoginType=2;
        }


        // 抵押贷款   2
        mortgage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
                if (isChecked) {
                    loan_flag = 2;
                    credit_loan.setChecked(false);
                    bond_transfer.setChecked(false);
                }

                Log.d("FinanceActivity", "isChecked111:" + isChecked);
                Log.d("FinanceActivity", "loan_flag111:" + loan_flag);
            }
        });

        // 信用贷款  1
        credit_loan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
                if (isChecked) {
                    loan_flag = 1;
                    mortgage.setChecked(false);
                    bond_transfer.setChecked(false);
                }

                Log.d("FinanceActivity", "isChecked222:" + isChecked);
                Log.d("FinanceActivity", "loan_flag222:" + loan_flag);
            }
        });
        //债券流转  3
        bond_transfer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
                if (isChecked) {
                    loan_flag = 3;
                    mortgage.setChecked(false);
                    credit_loan.setChecked(false);
                }
                Log.d("FinanceActivity", "isChecked333:" + isChecked);
                Log.d("FinanceActivity", "loan_flag333:" + loan_flag);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                if ((mortgage.isChecked() == false) && (credit_loan.isChecked() == false) && (bond_transfer.isChecked() == false)) {
                    loan_flag = 0;
                }
                if (financing_amount.getText().toString().length() < 1) {
                    Toast.makeText(getApplicationContext(), "融资金额不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                //个人金额
                if(LoginType==1&&(Long.parseLong(financing_amount.getText().toString())<1000||Long.parseLong(financing_amount.getText().toString())>200000)){
                    Toast.makeText(getApplicationContext(), "个人融资金额1千至20万", Toast.LENGTH_LONG).show();
                    return;
                }
                //企业机构金额

                if(LoginType==2&&(Long.parseLong(financing_amount.getText().toString())<5000||Long.parseLong(financing_amount.getText().toString())>1000000)){
                    Toast.makeText(getApplicationContext(), "机构融资金额5千至100万", Toast.LENGTH_LONG).show();
                    return;
                }
                if (financing_period.getText().toString().length() < 1) {
                    Toast.makeText(getApplicationContext(), "融资期限不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (Integer.parseInt(financing_period.getText().toString())<1||Integer.parseInt(financing_period.getText().toString())>12) {
                    Toast.makeText(getApplicationContext(), "融资期限必须为1-12个月", Toast.LENGTH_LONG).show();
                    return;
                }

                if (loan_flag == 0) {
                    Toast.makeText(getApplicationContext(), "融资类型不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (name.getText().toString().length() < 1) {
                    Toast.makeText(getApplicationContext(), "联系人不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if ((!phone_number.getText().toString().startsWith("1"))&&(phone_number.getText().toString().length()!=11)) {
                    Toast.makeText(getApplicationContext(), "请输入正确的手机号码", Toast.LENGTH_LONG).show();
                    return;
                }
                if (MyApplication.getToken() == null) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else {
                    upload(financing_amount.getText().toString(), financing_period.getText().toString(),
                            String.valueOf(loan_flag), name.getText().toString(), phone_number.getText().toString());
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
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        popView = getLayoutInflater().inflate(R.layout.activity_finance_dialog, null);
        Button sign_out = (Button) popView.findViewById(R.id.sign_out);
        sign_out.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                finish();
            }
        });
        popupWindow = new PopupWindow(popView, width / 3 * 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOutsideTouchable(false);
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

    public void upload(String financing_amount, String financing_period, String loan_flag, String name, String phone) {
        loading("上传中", "true");
        Map<String, String> map = new HashMap<String, String>();
        map.put("money", financing_amount);
        map.put("brolin", financing_period);
        map.put("type", String.valueOf(loan_flag));
        map.put("linkman", name);
        map.put("phone", phone);
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest register = new JsonObjectRequest(Method.POST,
                new CreatUrl().creaturl("authorization/rongzi", "saveRongzi"), paramMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("FinanceActivity", "response:" + response);
                        dismiss();
                        try {
                            String status = response.getString("status");
                            String code = response.getString("code");
                            if (status.equals("0") && code.equals("200")) {
                                initPopupWindow();
                                backgroundAlpha(0.5f);
                                // 为popWindow添加动画效果
                                popupWindow.setAnimationStyle(R.style.popWindow_animation);
                                // 点击弹出泡泡窗口
                                popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
                            } else {
                                Toast.makeText(getApplicationContext(), "数据库录入失败，请重新提交", Toast.LENGTH_LONG).show();
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
        register.setTag("UPLOAD");
        VolleySingleton.getVolleySingleton(getApplicationContext()).addToRequestQueue(register);

    }


}
