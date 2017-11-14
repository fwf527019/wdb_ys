package com.xiningmt.wdb;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import de.greenrobot.event.EventBus;

import rxh.hb.allinterface.LoginActivityInterface;
import rxh.hb.base.BaseActivity;
import rxh.hb.db.DBAdapter;
import rxh.hb.eventbusentity.WalletEntity;
import rxh.hb.jsonbean.LoginBean;
import rxh.hb.jsonbean.MyAccountActivityBean;
import rxh.hb.presenter.LoginActivityPresenter;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.utils.ShowDialog;
import rxh.hb.volley.util.VolleySingleton;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements
        LoginActivityInterface {

    private ImageView back;
    private TextView title, username, register, forget_password;
    private EditText password;
    private Button login;
    private SharedPreferences sp;
    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(
                this.getApplicationContext()).getRequestQueue();
        sp = getSharedPreferences("user", 0);
        initview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("LOGIN");
        }
    }

    public void initview() {
        setContentView(R.layout.activity_login);
        title = (TextView) findViewById(R.id.title);
        title.setText("登录");
        username = (TextView) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        if (sp.getString("user_name", null) != null) {
            username.setText(sp.getString("user_name", null));
        }
        if (sp.getString("password", null) != null) {
            password.setText(sp.getString("password", null));
        }
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        forget_password = (TextView) findViewById(R.id.forget_password);
        forget_password.setOnClickListener(this);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go:
                Editor editor = sp.edit();
                editor.putString("user_name", null);
                editor.putString("password", null);
                editor.commit();
                startActivity(new Intent(getApplicationContext(),
                        RegisterActivity.class));
                LoginActivity.this.finish();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.register:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                break;
            case R.id.forget_password:
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),
                        ForgetPasswordActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.login:
                if (!password.getText().toString().equals("")) {
                    loading("登录中", "true");
                    new LoginActivityPresenter(LoginActivity.this,
                            LoginActivity.this).login(username.getText().toString(), password.getText()
                            .toString());
                } else {
                    Toast.makeText(getApplicationContext(), "密码不能为空",
                            Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void getloginback(LoginBean loginBean) {
        dismiss();
        if (new RequestReturnProcessing(getApplicationContext(),
                loginBean.getCode()).processing() == 200) {
            Editor editor = sp.edit();
            editor.putString("user_name", username.getText().toString());
            editor.putString("password", password.getText().toString());
            editor.putString("token", loginBean.getObj());
            editor.putInt("LoginType", loginBean.getType());
            editor.commit();
            MyApplication.setToken(loginBean.getObj());
            EventBus.getDefault().post(new WalletEntity("login"));
            getRname();
            finish();
        }

    }

    @Override
    public void error(VolleyError error) {
        dismiss();
        Toast.makeText(getApplicationContext(), error.toString(),
                Toast.LENGTH_LONG).show();

    }


    private void getRname() {
        JsonObjectRequest getif = new JsonObjectRequest(Request.Method.POST,
                new CreatUrl().creaturl("authorization/info", "getInfo"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String code = response.getString("code");
                    if (new RequestReturnProcessing(LoginActivity.this, code).processing() == 200) {
                        MyAccountActivityBean myAccountActivityBean = JsonUtils.getinMyAccountActivityBean(response.toString());
                        Editor editor = sp.edit();
                        editor.putString("Ruser_name", myAccountActivityBean.getNames());
                        editor.commit();
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
}
