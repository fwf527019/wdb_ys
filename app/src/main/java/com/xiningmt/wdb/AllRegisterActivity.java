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
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AllRegisterActivity extends BaseActivity {

    private TimeCount time;
    private TextView title;
    private ImageView back;
    private Button get_verification_code, submit;
    private EditText register, verification_code, reset_password;
    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_register);
        mRequestQueue = VolleySingleton.getVolleySingleton(this.getApplicationContext()).getRequestQueue();
        initview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GETCODE");
            mRequestQueue.cancelAll("REGISTER");
        }
    }

    public void initview() {
        title = (TextView) findViewById(R.id.title);
        title.setText("注册");
        register = (EditText) findViewById(R.id.register);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        get_verification_code = (Button) findViewById(R.id.get_verification_code);
        get_verification_code.setOnClickListener(this);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        verification_code = (EditText) findViewById(R.id.verification_code);
        reset_password = (EditText) findViewById(R.id.reset_password);
        time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
    }

    public void getverificationcode(String num) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", num);
        map.put("type", "1");
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest getcode = new JsonObjectRequest(Method.POST, new CreatUrl().creaturl("account", "sendCode"),
                paramMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    String code = response.getString("code");
                    if (code.equals("200")) {
                        time.start();
                    }
                    Toast.makeText(getApplicationContext(), new StatusCode().getstatus(code), Toast.LENGTH_LONG)
                            .show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        getcode.setTag("GETCODE");
        VolleySingleton.getVolleySingleton(getApplicationContext()).addToRequestQueue(getcode);

    }

    public void register(final String loginName, final String password, final String code) {
        loading("注册中","true");
        Map<String, String> map = new HashMap<String, String>();
        map.put("loginName", loginName);
        map.put("password", password);
        map.put("code", code);
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest register = new JsonObjectRequest(Method.POST, new CreatUrl().creaturl("account", "register"),
                paramMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               dismiss();
                time.start();
                try {
                    String status = response.getString("status");
                    String code = response.getString("code");
                    if (status.equals("0") && code.equals("200")) {
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), LoginActivity.class);
                        intent.putExtra("user_name", loginName);
                        startActivity(intent);
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
        register.setTag("REGISTER");
        VolleySingleton.getVolleySingleton(getApplicationContext()).addToRequestQueue(register);

    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            get_verification_code.setText("重新验证");
            get_verification_code.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            get_verification_code.setClickable(false);
            get_verification_code.setText(millisUntilFinished / 1000 + "秒");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.get_verification_code:
                if (!register.getText().toString().equals("")) {
                    if (isMobileNO(register.getText().toString())) {
                        getverificationcode(register.getText().toString());
                    } else {
                        Toast.makeText(getApplicationContext(), "手机号码格式错误", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "手机号码不能为空", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.submit:
                if (!verification_code.getText().toString().equals("") && !reset_password.getText().toString().equals("")) {

                    register(register.getText().toString(), reset_password.getText().toString(),
                            verification_code.getText().toString());

                } else {
                    Toast.makeText(getApplicationContext(), "输入框不能为空，请认真填写", Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }

    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }
}
