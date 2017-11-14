package com.xiningmt.wdb;

import java.security.PublicKey;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import rxh.hb.base.BaseActivity;
import rxh.hb.jsonbean.ActivityCenterActivityLVBean;
import rxh.hb.jsonbean.WithdrawalsBean;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.utils.ShowDialog;
import rxh.hb.volley.util.VolleySingleton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WithdrawalsActivity extends BaseActivity {

    private ImageView back;
    private TextView title, credit_card_number, card_type, prompt, the_facility_is_the_payment_password;
    private EditText the_withdrawal_amount, password;
    private Button withdrawals;
    private RelativeLayout rl;
    RequestQueue mRequestQueue;
    WithdrawalsBean withdrawalsBean = new WithdrawalsBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(this.getApplicationContext()).getRequestQueue();
        base_title.setText("提现");
        getinformation();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GETIF");
            mRequestQueue.cancelAll("GETUP");
        }
    }

    public void initview() {
        setContentView(R.layout.activity_withdrawals);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        title.setText("提现");
        rl = (RelativeLayout) findViewById(R.id.rl);
        rl.setOnClickListener(this);
        the_withdrawal_amount = (EditText) findViewById(R.id.the_withdrawal_amount);
        setPricePoint(the_withdrawal_amount);
        // 银行卡号
        credit_card_number = (TextView) findViewById(R.id.credit_card_number);
        // 银行卡类型
        card_type = (TextView) findViewById(R.id.card_type);
        prompt = (TextView) findViewById(R.id.prompt);
        withdrawals = (Button) findViewById(R.id.withdrawals);
        withdrawals.setOnClickListener(this);
        the_facility_is_the_payment_password = (TextView) findViewById(R.id.the_facility_is_the_payment_password);
        the_facility_is_the_payment_password.setOnClickListener(this);
        password = (EditText) findViewById(R.id.password);
    }

    public void inittv(WithdrawalsBean withdrawalsBean) {
        initview();
        if (withdrawalsBean.getNums() != null) {
            credit_card_number.setText(withdrawalsBean.getNums());
        } else {
            credit_card_number.setText("请绑定银行卡");
            card_type.setVisibility(View.GONE);
        }
        if (withdrawalsBean.getBankname() != null) {
            card_type.setText(withdrawalsBean.getBankname());
        }
        String num = withdrawalsBean.getDrawithtime();

        int drawTime= Integer.parseInt(withdrawalsBean.getDrawTime());
        int end = drawTime - Integer.parseInt(num);
        String drawMax=withdrawalsBean.getDrawMax();
        String drawRate=withdrawalsBean.getDrawRate();

        DecimalFormat df = new DecimalFormat("0");
        if (withdrawalsBean.getMoneys() != null) {
            the_withdrawal_amount.setHint("本次最多可提现" + df.format(Double.parseDouble(withdrawalsBean.getMoneys())) + "元");
        }
        if (withdrawalsBean.getDrawithtime() != null) {
            if (end >= 0) {
                if(drawMax!=null){
                    prompt.setText((Html.fromHtml("本月还可以免费提现" + "<font color=#FB6006>" + String.valueOf(end)
                            + "</font>笔(每月可免费提现"+drawTime+"笔，第"+(drawTime+1)+"笔开始收取"+drawRate+"%的手续费，最高每笔收取"+drawMax+"元)。")));
                }else {
                    prompt.setText((Html.fromHtml("本月还可以免费提现" + "<font color=#FB6006>" + String.valueOf(end)
                            + "</font>笔(每月可免费提现"+drawTime+"笔，第"+(drawTime+1)+"笔开始收取"+drawRate+"%的手续费。")));
                }


            } else {
                if(drawMax!=null){
                    prompt.setText((Html.fromHtml("本月还可以免费提现" + "<font color=#FB6006>" + "0"
                            + "</font>笔(每月可免费提现"+drawTime+"笔，第"+(drawTime+1)+"笔开始收取"+drawRate+"%的手续费，最高每笔收取"+drawMax+"元)。")));
                }else {
                    prompt.setText((Html.fromHtml("本月还可以免费提现" + "<font color=#FB6006>" + "0"
                            + "</font>笔(每月可免费提现"+drawTime+"笔，第"+(drawTime+1)+"笔开始收取"+drawRate+"%的手续费。")));
                }

            }
        } else {
            if(drawMax!=null){
                prompt.setText((Html.fromHtml("本月还可以免费提现" + "<font color=#FB6006>" + end + "</font>笔(每月可免费提现"+drawTime+"笔，第"+(drawTime+1)+"笔开始收取"+drawRate+"%的手续费，最高每笔收取"+drawMax+"元)。")));
            }else {
                prompt.setText((Html.fromHtml("本月还可以免费提现" + "<font color=#FB6006>" + end + "</font>笔(每月可免费提现"+drawTime+"笔，第"+(drawTime+1)+"笔开始收取"+drawRate+"%的手续费。")));
            }
        }
        if (withdrawalsBean.getPaypassword().equals("0")) {
            //没有设置支付密码
            the_facility_is_the_payment_password.setVisibility(View.VISIBLE);
        } else {
            the_facility_is_the_payment_password.setVisibility(View.GONE);
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
            case R.id.rl:
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), BankCardManagementActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.the_facility_is_the_payment_password:
                Intent intent2 = new Intent();
                intent2.setClass(getApplicationContext(), ModifyThePaymentPasswordActivity.class);
                startActivityForResult(intent2, 2);
                break;
            case R.id.withdrawals:
                if (card_type.getText().toString().length() > 0) {
                    if (the_withdrawal_amount.getText().toString().length() > 0) {
                        if (password.getText().toString().length() > 0) {
                            if (Double.parseDouble(withdrawalsBean.getMoneys()) >= Double
                                    .parseDouble(the_withdrawal_amount.getText().toString())) {
                                upload();
                            } else {
                                Toast.makeText(getApplicationContext(), "提现金额不能大于你的剩余最大金额", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "提现金额不能为空", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "请先绑定银行卡", Toast.LENGTH_LONG).show();
                }

                break;

            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // requestCode标示请求的标示 resultCode表示有数据
        if (requestCode == 1 && resultCode == RESULT_OK) {
            card_type.setVisibility(View.VISIBLE);
            credit_card_number.setText(data.getStringExtra("bank_card_number"));
            card_type.setText(data.getStringExtra("bank_of_deposit"));
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            the_facility_is_the_payment_password.setVisibility(View.GONE);
        }
    }

    public void getinformation() {
        loading("加载中", "true");
        JsonObjectRequest getif = new JsonObjectRequest(Method.POST,
                new CreatUrl().creaturl("authorization/withdraw", "condition"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismiss();
                        try {
                            String code = response.getString("code");
                            if (new RequestReturnProcessing(WithdrawalsActivity.this, code).processing() == 200) {
                                withdrawalsBean = JsonUtils.getw(response.toString());
                                inittv(withdrawalsBean);
                            } else {
                                finish();
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

    public void upload() {
        loading("申请中", "true");
        Map<String, String> map = new HashMap<String, String>();
        map.put("money", the_withdrawal_amount.getText().toString());
        map.put("password", password.getText().toString());
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest getif = new JsonObjectRequest(Method.POST,
                new CreatUrl().creaturl("authorization/withdraw", "withdraw"), paramMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismiss();
                        try {
                            String code = response.getString("code");
                            if (new RequestReturnProcessing(WithdrawalsActivity.this, code).processing() == 200) {
                                Toast.makeText(getApplicationContext(), "提现成功", Toast.LENGTH_SHORT).show();
                                // 数据是使用Intent返回
                                Intent intent1 = new Intent();
                                // 把返回数据存入Intent
                                intent1.putExtra("withdrawals", response.getString("obj"));
                                // 设置返回数据
                                WithdrawalsActivity.this.setResult(RESULT_OK, intent1);
                                // 关闭Activity
                                WithdrawalsActivity.this.finish();
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
        getif.setTag("GETUP");
        VolleySingleton.getVolleySingleton(getApplicationContext()).addToRequestQueue(getif);
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

}
