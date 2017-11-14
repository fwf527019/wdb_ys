package com.xiningmt.wdb;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import rxh.hb.base.BaseActivity;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.utils.StatusCode;
import rxh.hb.volley.util.VolleySingleton;

//修改支付密码
public class SetThePaymentPasswordActivity extends BaseActivity {

    @Bind(R.id.phone_number)
    TextView phone_number;
    @Bind(R.id.verification_code)
    EditText verification_code;
    @Bind(R.id.verification_code_btn)
    Button verification_code_btn;
    private ImageView back;
    private TextView title;
    private EditText old_payment_password, new_payment_password;
    private Button submit;
    RequestQueue mRequestQueue;

    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_the_payment_password);
        ButterKnife.bind(this);
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(
                this.getApplicationContext()).getRequestQueue();
        initview();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GETIF");
            mRequestQueue.cancelAll("GETCODE");
        }
    }

    public void initview() {
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        title.setText("修改支付密码");
        phone_number.setText(getIntent().getStringExtra("phone_num"));
        time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
        old_payment_password = (EditText) findViewById(R.id.old_payment_password);
        new_payment_password = (EditText) findViewById(R.id.new_payment_password);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        verification_code_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.verification_code_btn:
                if (phone_number.getText().toString() == null || phone_number.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                getverificationcode(phone_number.getText().toString());
                break;
            case R.id.submit:
                if (verification_code.getText().toString() == null || verification_code.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (5 < old_payment_password.getText().toString().length()
                        && old_payment_password.getText().toString().length() < 21
                        && 5 < new_payment_password.getText().toString().length()
                        && new_payment_password.getText().toString().length() < 21) {
                    if (old_payment_password.getText().toString().equals(new_payment_password.getText().toString())) {
                        getinformation(verification_code.getText().toString(), old_payment_password.getText().toString(),
                                new_payment_password.getText().toString());
                    } else {
                        Toast.makeText(getApplicationContext(), "两次密码不一致",
                                Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "密码长度不对",
                            Toast.LENGTH_LONG).show();
                }
                break;

            default:
                break;
        }

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

    public void getverificationcode(String num) {
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
        getcode.setTag("GETCODE");
        VolleySingleton.getVolleySingleton(getApplicationContext())
                .addToRequestQueue(getcode);

    }

    public void getinformation(String code, String oldpassword, String newpassword) {
        loading("加载中", "true");
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", code);
        map.put("paypassword", oldpassword);
        map.put("paypasswordone", newpassword);
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest getif = new JsonObjectRequest(Method.POST,
                new CreatUrl().creaturl("authorization/payPassword",
                        "payUpdatePassword"), paramMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismiss();
                        try {
                            String code = response.getString("code");
                            if (new RequestReturnProcessing(
                                    SetThePaymentPasswordActivity.this, code)
                                    .processing() == 200) {
                                Toast.makeText(getApplicationContext(),
                                        "支付密码修改成功", Toast.LENGTH_LONG).show();
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
        getif.setTag("GETIF");
        VolleySingleton.getVolleySingleton(getApplicationContext())
                .addToRequestQueue(getif);
    }

}
