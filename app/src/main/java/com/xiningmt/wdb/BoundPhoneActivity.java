package com.xiningmt.wdb;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
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
import de.greenrobot.event.EventBus;
import rxh.hb.base.BaseActivity;
import rxh.hb.eventbusentity.MyAccountActivityEntity;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.utils.StatusCode;
import rxh.hb.volley.util.VolleySingleton;

/**
 * Created by Administrator on 2016/10/27.
 */
public class BoundPhoneActivity extends BaseActivity {


    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.phone_number)
    EditText phone_pumber;
    @Bind(R.id.verification_code)
    EditText verification_code;
    @Bind(R.id.verification_code_btn)
    Button verification_code_btn;
    @Bind(R.id.submit)
    Button submit;
    @Bind(R.id.old_phone_number)
    TextView old_phone_number;

    RequestQueue mRequestQueue;
    private TimeCount time;
    String phone_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获得实例对象
        phone_num = getIntent().getStringExtra("phone_num");
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
        setContentView(R.layout.activity_bound_phone);
        ButterKnife.bind(this);
        title.setText("绑定手机");
        time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
        back.setOnClickListener(this);
        verification_code_btn.setOnClickListener(this);
        submit.setOnClickListener(this);
        old_phone_number.setText(phone_num);
        getverificationcode(phone_num);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.verification_code_btn:
                break;
            case R.id.submit:
                if (verification_code.getText().toString() == null || verification_code.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phone_pumber.getText().toString() == null || phone_pumber.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "新手机号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                getinformation(verification_code.getText().toString(), phone_pumber.getText().toString());
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
        loading("获取中", "true");
        JsonObjectRequest getcode = new JsonObjectRequest(Request.Method.POST,
                new CreatUrl().creaturl("account", "sendCode"), paramMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismiss();
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

    public void getinformation(String code, String phone_num) {
        loading("绑定中", "true");
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", code);
        map.put("phone", phone_num);
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest getif = new JsonObjectRequest(Request.Method.POST,
                new CreatUrl().creaturl("authorization/info",
                        "updatePhone"), paramMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismiss();
                        try {
                            String code = response.getString("code");
                            if (new RequestReturnProcessing(
                                    BoundPhoneActivity.this, code)
                                    .processing() == 200) {
                                EventBus.getDefault().post(new MyAccountActivityEntity("BoundPhoneActivity"));
                                Toast.makeText(getApplicationContext(),
                                        "手机绑定成功", Toast.LENGTH_LONG).show();
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
