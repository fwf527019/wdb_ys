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

import rxh.hb.base.BaseActivity;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.utils.ShowDialog;
import rxh.hb.volley.util.VolleySingleton;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ModifyTheLoginPasswordActivity extends BaseActivity {

    private ImageView back;
    private TextView title, user_no;
    private EditText old_password, new_password, new_password_again;
    private Button submit;
    RequestQueue mRequestQueue;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_the_login_password);
        sp = getSharedPreferences("user", 0);
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(
                this.getApplicationContext()).getRequestQueue();
        initview();
        initdata();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("MP");
        }
    }

    public void initview() {
        title = (TextView) findViewById(R.id.title);
        user_no = (TextView) findViewById(R.id.user_no);
        old_password = (EditText) findViewById(R.id.old_password);
        new_password = (EditText) findViewById(R.id.new_password);
        new_password_again = (EditText) findViewById(R.id.new_password_again);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }

    public void initdata() {
        title.setText("修改密码");
        if (sp.getString("user_name", null) != null) {
            user_no.setText(sp.getString("user_name", null));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                if ((new_password_again.getText().toString()).equals(new_password.getText().toString())) {

                    // 执行网络请求方法
                    ModifyPassword(old_password.getText().toString(), new_password
                            .getText().toString());

                } else {
                    Toast.makeText(getApplicationContext(), "两次密码不同，请重新输入！",
                            Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    public void ModifyPassword(String opass, String npass) {
        loading("上传中", "true");
        Map<String, String> map = new HashMap<String, String>();
        map.put("opass", opass);
        map.put("npass", npass);
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest mp = new JsonObjectRequest(Method.POST,

                new CreatUrl().creaturl("authorization/info", "updatePass"),
                paramMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismiss();
                try {
                    String code = response.getString("code");
                    if (new RequestReturnProcessing(
                            getApplicationContext(), code).processing() == 200) {
                        Toast.makeText(getApplicationContext(), "修改成功",
                                Toast.LENGTH_LONG).show();
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
        mp.setTag("MP");
        VolleySingleton.getVolleySingleton(getApplicationContext())
                .addToRequestQueue(mp);

    }

}
