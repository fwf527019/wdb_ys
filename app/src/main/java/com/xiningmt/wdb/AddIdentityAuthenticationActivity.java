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

import de.greenrobot.event.EventBus;

import rxh.hb.base.BaseActivity;
import rxh.hb.eventbusentity.MyAccountActivityEntity;
import rxh.hb.jsonbean.AddIdentityAuthenticationActivityBean;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.utils.ShowDialog;
import rxh.hb.utils.StatusCode;
import rxh.hb.view.HPEditText;
import rxh.hb.view.XEditText;
import rxh.hb.volley.util.VolleySingleton;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddIdentityAuthenticationActivity extends BaseActivity {

	String flag;
	RequestQueue mRequestQueue;
	private ImageView back;
	private Button next;
	private TextView title, go;
	private EditText real_name, work_unit, home_address;
	private HPEditText id_number;
	private EditText work_unit_telephone;
	AddIdentityAuthenticationActivityBean addIdentityAuthenticationActivityBean = new AddIdentityAuthenticationActivityBean();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_identity_authentication);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// 获得实例对象
		mRequestQueue = VolleySingleton.getVolleySingleton(
				this.getApplicationContext()).getRequestQueue();
		flag = getIntent().getStringExtra("flag");
		initview();
		getcardinformation();
		inittitle();
		setenabled(false);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll("GETIF");
			mRequestQueue.cancelAll("ADDIF");
		}
	}

	public void getcardinformation() {
		loading("加载中","true");
		JsonObjectRequest getif = new JsonObjectRequest(Method.POST,
				new CreatUrl().creaturl("authorization/info", "getCard"), null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						dismiss();
						try {
							String code = response.getString("code");
							if (new RequestReturnProcessing(
									getApplicationContext(), code).processing() == 200) {
								addIdentityAuthenticationActivityBean = JsonUtils
										.getAddIdentityAuthenticationActivityBean(response
												.toString());
								initdata();
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

	public void addcardinformation() {
		loading("上传中","true");
		Map<String, String> map = new HashMap<String, String>();
		map.put("card", id_number.getText().toString().replaceAll(" +", ""));
		map.put("name", real_name.getText().toString());
		map.put("workunit", work_unit.getText().toString());
		map.put("tel",
				work_unit_telephone.getText().toString().replaceAll(" +", ""));
		map.put("site", home_address.getText().toString());
		JSONObject paramMap = new JSONObject(map);
		JsonObjectRequest addif = new JsonObjectRequest(Method.POST,
				new CreatUrl().creaturl("authorization/info", "saveCard"),
				paramMap, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						dismiss();
						try {
							String code = response.getString("code");
							if (new RequestReturnProcessing(
									getApplicationContext(), code).processing() == 200) {
								setenabled(false);
								next.setVisibility(View.GONE);
								EventBus.getDefault()
										.post(new MyAccountActivityEntity(
												"AddIdentityAuthenticationActivity"));
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
		addif.setTag("ADDIF");
		VolleySingleton.getVolleySingleton(getApplicationContext())
				.addToRequestQueue(addif);
	}

	public void initview() {
		title = (TextView) findViewById(R.id.title);
		go = (TextView) findViewById(R.id.go);
		go.setOnClickListener(this);
		real_name = (EditText) findViewById(R.id.real_name);
		id_number = (HPEditText) findViewById(R.id.id_number);
		work_unit = (EditText) findViewById(R.id.work_unit);
		work_unit_telephone = (EditText) findViewById(R.id.work_unit_telephone);
		home_address = (EditText) findViewById(R.id.home_address);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		next = (Button) findViewById(R.id.next);
		next.setOnClickListener(this);
		next.setVisibility(View.GONE);
	}

	public void inittitle() {
		title.setText("身份认证");
		go.setText("编辑");
	}

	public void initdata() {
		if (addIdentityAuthenticationActivityBean.getName() != null) {
			real_name.setText("真实姓名："
					+ addIdentityAuthenticationActivityBean.getName());
		}
		if (addIdentityAuthenticationActivityBean.getCard() != null) {
			id_number.setText("身份证号："
					+ addIdentityAuthenticationActivityBean.getCard());
		}
		if (addIdentityAuthenticationActivityBean.getWorkunit() != null) {
			work_unit.setText("公司名称："
					+ addIdentityAuthenticationActivityBean.getWorkunit());
		}
		if (addIdentityAuthenticationActivityBean.getTel() != null) {
			work_unit_telephone.setText("公司电话："
					+ addIdentityAuthenticationActivityBean.getTel());
		}
		if (addIdentityAuthenticationActivityBean.getSite() != null) {
			home_address.setText("家庭住址："
					+ addIdentityAuthenticationActivityBean.getSite());
		}

	}

	public void bj() {
		if (addIdentityAuthenticationActivityBean.getName() != null) {
			real_name.setText(addIdentityAuthenticationActivityBean.getName());
		}
		if (addIdentityAuthenticationActivityBean.getCard() != null) {
			id_number.setText(addIdentityAuthenticationActivityBean.getCard());
		}
		if (addIdentityAuthenticationActivityBean.getWorkunit() != null) {
			work_unit.setText(addIdentityAuthenticationActivityBean
					.getWorkunit());
		}
		if (addIdentityAuthenticationActivityBean.getTel() != null) {
			work_unit_telephone.setText(addIdentityAuthenticationActivityBean
					.getTel());
		}
		if (addIdentityAuthenticationActivityBean.getSite() != null) {
			home_address.setText(addIdentityAuthenticationActivityBean
					.getSite());
		}
	}

	public void setenabled(boolean flag) {
		real_name.setEnabled(flag);
		id_number.setEnabled(flag);
		work_unit.setEnabled(flag);
		work_unit_telephone.setEnabled(flag);
		home_address.setEnabled(flag);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.go:
			if (go.getText().toString().equals("编辑")) {
				go.setText("取消");
				setenabled(true);
				bj();
				next.setVisibility(View.VISIBLE);
			} else {
				go.setText("编辑");
				setenabled(false);
				next.setVisibility(View.GONE);
			}

			break;
		case R.id.next:
			if (!real_name.getText().toString().equals("")
					&& !id_number.getText().toString().equals("")
					&& !work_unit.getText().toString().equals("")
					&& !work_unit_telephone.getText().toString().equals("")
					&& !home_address.getText().toString().equals("")) {
				addcardinformation();
			} else {
				Toast.makeText(getApplicationContext(), "所有输入框不能为空",
						Toast.LENGTH_LONG).show();
			}

			break;
		default:
			break;
		}

	}

}
