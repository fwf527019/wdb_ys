package com.xiningmt.wdb;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import rxh.hb.base.BaseActivity;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.volley.util.VolleySingleton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//设置支付密码
public class ModifyThePaymentPasswordActivity extends BaseActivity {

	private ImageView back;
	private TextView title;
	private EditText payment_password, payment_password_rapet;
	private Button submit;
	RequestQueue mRequestQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_the_payment_password);
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
		}
	}

	public void initview() {
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		title.setText("设置支付密码");
		payment_password = (EditText) findViewById(R.id.payment_password);
		payment_password_rapet = (EditText) findViewById(R.id.payment_password_rapet);
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
			if (5 < payment_password.getText().toString().length()
					&& payment_password.getText().toString().length() < 21
					&& 5 < payment_password_rapet.getText().toString().length()
					&& payment_password_rapet.getText().toString().length() < 21) {
				if (payment_password.getText().toString()
						.equals(payment_password_rapet.getText().toString())) {
					getinformation(payment_password_rapet.getText().toString());
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

	public void getinformation(String password) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("paypassword", password);
		JSONObject paramMap = new JSONObject(map);
		JsonObjectRequest getif = new JsonObjectRequest(Method.POST,
				new CreatUrl().creaturl("authorization/payPassword",
						"paySetPassword"), paramMap,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							String code = response.getString("code");
							if (new RequestReturnProcessing(
									ModifyThePaymentPasswordActivity.this, code)
									.processing() == 200) {
								Toast.makeText(getApplicationContext(),
										"支付密码设置成功", Toast.LENGTH_LONG).show();
								Intent intent = new Intent();
								intent.putExtra("pay_result", "mtp");
								setResult(RESULT_OK, intent);
								ModifyThePaymentPasswordActivity.this.finish();
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
