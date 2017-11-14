package com.xiningmt.wdb;

import android.content.Intent;
import android.os.Bundle;
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
import rxh.hb.base.BaseActivity;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.volley.util.VolleySingleton;

/**
 * Created by Administrator on 2016/10/13.
 */
public class MemberCertificationTwoActivity extends BaseActivity {


    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.pay_password)
    EditText pay_password;
    @Bind(R.id.repat_pay_password)
    EditText repat_pay_password;
    @Bind(R.id.next)
    Button next;

    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(this.getApplicationContext()).getRequestQueue();
        initview();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("UP");
        }
    }


    public void initview() {
        setContentView(R.layout.activity_member_certification_two);
        ButterKnife.bind(this);
        title.setText("会员认证");
        back.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.next:
                if (pay_password.getText().toString() == null || pay_password.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "支付密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (repat_pay_password.getText().toString() == null || repat_pay_password.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "确认支付密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                upload();
                break;
            default:
                break;
        }
    }

    public void upload() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("paypassword", pay_password.getText().toString());
        map.put("paypasswordone", repat_pay_password.getText().toString());
        JSONObject paramMap = new JSONObject(map);
        loading("设置中", "true");
        JsonObjectRequest getif = new JsonObjectRequest(Request.Method.POST,
                new CreatUrl().creaturl("authorization/payPassword", "paySetPassword"), paramMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismiss();
                try {
                    String code = response.getString("code");
                    if (new RequestReturnProcessing(getApplicationContext(), code).processing() == 200) {
                        startActivity(new Intent(getApplicationContext(), MemberCertificationThreeActivity.class));
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
                headers.put("Authorization", MyApplication.getToken());
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        getif.setTag("UP");
        VolleySingleton.getVolleySingleton(getApplicationContext()).addToRequestQueue(getif);
    }

}
