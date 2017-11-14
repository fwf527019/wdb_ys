package com.xiningmt.wdb;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import rxh.hb.base.BaseActivity;
import rxh.hb.jsonbean.AllRegisterActivityBean;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.ShowDialog;
import rxh.hb.utils.StatusCode;
import rxh.hb.volley.util.VolleySingleton;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ForgetPasswordActivity extends BaseActivity {

    private TimeCount time;
    private ImageView back;
    private TextView title;
    private EditText verification_code, register;
    private Button verification_code_btn, next;
    RequestQueue mRequestQueue;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        username = getIntent().getStringExtra("username");
        mRequestQueue = VolleySingleton.getVolleySingleton(
                this.getApplicationContext()).getRequestQueue();
        initview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GETCODE");
        }
    }

    public void initview() {
        title = (TextView) findViewById(R.id.title);
        title.setText("找回登录密码");
        register = (EditText) findViewById(R.id.register);
        register.setText(username);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        verification_code = (EditText) findViewById(R.id.verification_code);
        verification_code_btn = (Button) findViewById(R.id.verification_code_btn);
        time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
        verification_code_btn.setOnClickListener(this);
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(this);
    }

    public void getverificationcode(final String num) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", num);
        map.put("type", "2");
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest getcode = new JsonObjectRequest(Method.POST,
                new CreatUrl().creaturl("account", "sendCode"), paramMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            String code = response.getString("code");
                            if (code.equals("200")) {
                                time.start();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        new StatusCode().getstatus(code),
                                        Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.toString(), Toast.LENGTH_LONG).show();
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
        // StringRequest getcode = new StringRequest(Method.POST,
        // "http://182.150.28.148:8212/mobile/account/sendCode",
        // new Response.Listener<String>() {
        //
        // @Override
        // public void onResponse(String response) {
        // time.start();
        // AllRegisterActivityBean reisterbean = new AllRegisterActivityBean();
        // reisterbean = JsonUtils.register(response);
        // Toast.makeText(
        // getApplicationContext(),
        // new StatusCode().getstatus(reisterbean
        // .getCode()), Toast.LENGTH_LONG).show();
        //
        // }
        // }, new Response.ErrorListener() {
        // @Override
        // public void onErrorResponse(VolleyError error) {
        // Toast.makeText(getApplicationContext(), "请求失败，请稍后重试",
        // Toast.LENGTH_LONG).show();
        // }
        // }) {
        // @Override
        // protected Map<String, String> getParams() throws AuthFailureError {
        // Map<String, String> map = new HashMap<String, String>();
        // map.put("phone", num);
        // map.put("type", "2");
        // return map;
        // }
        // };
        getcode.setTag("GETCODE");
        VolleySingleton.getVolleySingleton(getApplicationContext())
                .addToRequestQueue(getcode);

    }

    public void checkverificationcode(final String num, final String code) {
        loading("验证中", "true");
        Map<String, String> map = new HashMap<String, String>();
        map.put("loginName", num);
        map.put("code", code);
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest getcode = new JsonObjectRequest(Method.POST,
                new CreatUrl().creaturl("account", "checkCode"), paramMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismiss();
                        time.start();
                        try {
                            String status = response.getString("status");
                            String code = response.getString("code");
                            if (status.equals("0") && code.equals("200")) {
                                Intent intent = new Intent();

                                intent.putExtra("username", username);
                                intent.setClass(getApplicationContext(),
                                        ResetPasswordActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        new StatusCode().getstatus(code),
                                        Toast.LENGTH_LONG).show();
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
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        getcode.setTag("GETCODE");
        VolleySingleton.getVolleySingleton(getApplicationContext())
                .addToRequestQueue(getcode);

    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            verification_code_btn.setText("重新验证");
            verification_code_btn.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            verification_code_btn.setClickable(false);
            verification_code_btn.setText(millisUntilFinished / 1000 + "秒");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.verification_code_btn:
                getverificationcode(register.getText().toString());
                break;
            case R.id.next:
                if(register.getText()!=null&&register.getText().toString().startsWith("1")&&register.getText().toString().length()==11){
                    username=register.getText().toString();
                }else {
                    Toast.makeText(this, "手机号码错误", Toast.LENGTH_SHORT).show();
                }
                if (!verification_code.getText().toString().equals("")) {
                    checkverificationcode(register.getText().toString(), verification_code.getText()
                            .toString());
                } else {
                    Toast.makeText(getApplicationContext(), "验证码不能为空",
                            Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }

    }
}
