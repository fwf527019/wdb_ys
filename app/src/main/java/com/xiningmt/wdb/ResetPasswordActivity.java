package com.xiningmt.wdb;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import rxh.hb.base.BaseActivity;
import rxh.hb.jsonbean.AllRegisterActivityBean;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ResetPasswordActivity extends BaseActivity {

	private ImageView back;
	private TextView title;
	private Button submit;
	private EditText reset_password, reset_password_again;
	RequestQueue mRequestQueue;
	String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_reset_password);
		username = getIntent().getStringExtra("username");
		mRequestQueue = VolleySingleton.getVolleySingleton(
				this.getApplicationContext()).getRequestQueue();
		initview();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll("RP");
		}
	}

	public void resetpassword(final String loginName, final String password) {
		loading("验证中","true");
		Map<String, String> map = new HashMap<String, String>();
		map.put("loginName", loginName);
		map.put("password", password);
		JSONObject paramMap = new JSONObject(map);
		JsonObjectRequest resetpassword = new JsonObjectRequest(Method.POST,
				new CreatUrl().creaturl("account", "resetPass"), paramMap,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						dismiss();
						try {
							String status = response.getString("status");
							String code = response.getString("code");
							if (status.equals("0") && code.equals("200")) {
								Intent intent = new Intent();
								intent.putExtra("user_name", username);
								intent.setClass(getApplicationContext(),
										LoginActivity.class);
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
		// StringRequest resetpassword = new StringRequest(Method.POST,
		// "http://182.150.28.148:8212/mobile/account/resetPass",
		// new Response.Listener<String>() {
		//
		// @Override
		// public void onResponse(String response) {
		// showDialog.dismissmydialog();
		// AllRegisterActivityBean reisterbean = new AllRegisterActivityBean();
		// reisterbean = JsonUtils.register(response);
		// if (reisterbean.getStatus().equals("0")
		// && reisterbean.getCode().equals("200")) {
		// startActivity(new Intent(getApplicationContext(),
		// LoginActivity.class));
		// finish();
		// }
		//
		// }
		// }, new Response.ErrorListener() {
		// @Override
		// public void onErrorResponse(VolleyError error) {
		// showDialog.dismissmydialog();
		// Toast.makeText(getApplicationContext(), "请求失败，请稍后重试",
		// Toast.LENGTH_LONG).show();
		// }
		// }) {
		// @Override
		// protected Map<String, String> getParams() throws AuthFailureError {
		// Map<String, String> map = new HashMap<String, String>();
		// map.put("loginName", loginName);
		// map.put("password", password);
		// return map;
		// }
		// };
		resetpassword.setTag("RP");
		VolleySingleton.getVolleySingleton(getApplicationContext())
				.addToRequestQueue(resetpassword);

	}

	public void initview() {
		reset_password = (EditText) findViewById(R.id.reset_password);
		reset_password_again = (EditText) findViewById(R.id.reset_password_again);
		title = (TextView) findViewById(R.id.title);
		title.setText("重置密码");
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.submit:
			if (!reset_password.getText().toString().equals("")
					&& !reset_password_again.getText().toString().equals("")) {
				if (reset_password.getText().toString()
						.equals(reset_password_again.getText().toString())) {
					resetpassword(username, reset_password.getText().toString());
				} else {
					Toast.makeText(getApplicationContext(), "两次输入密码不一致，请重新输入",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "密码框不能为空，请认真填写",
						Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}

	}

}
