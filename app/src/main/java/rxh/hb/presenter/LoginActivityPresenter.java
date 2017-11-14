package rxh.hb.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import rxh.hb.allinterface.LoginActivityInterface;

import rxh.hb.jsonbean.InvestmentFragmentActivityLVBean;
import rxh.hb.jsonbean.LoginBean;

import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.volley.util.VolleySingleton;
import android.content.Context;
import android.util.Log;

public class LoginActivityPresenter {

	Context context;
	LoginActivityInterface loginActivityInterface;

	public LoginActivityPresenter(
			LoginActivityInterface loginActivityInterface, Context context) {
		this.loginActivityInterface = loginActivityInterface;
		this.context = context;

	}

	public void login(final String loginName, final String password) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("loginName", loginName);
		map.put("password", password);
		JSONObject paramMap = new JSONObject(map);
		JsonObjectRequest login = new JsonObjectRequest(Method.POST,
				new CreatUrl().creaturl("account", "login"), paramMap,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						LoginBean loginBean = new LoginBean();
						GsonBuilder gsonb = new GsonBuilder();
						Gson gson = gsonb.create();
						loginBean = gson.fromJson(response.toString(),
								LoginBean.class);
						loginActivityInterface.getloginback(loginBean);

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						loginActivityInterface.error(error);

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
		// StringRequest login = new StringRequest(Method.POST,
		// "http://182.150.28.148:8212/mobile/account/login",
		// new Response.Listener<String>() {
		//
		// @Override
		// public void onResponse(String response) {
		// LoginBean login = new LoginBean();
		// login = JsonUtils.getLoginBean(response);
		// loginActivityInterface.getloginback(login);
		//
		// }
		// }, new Response.ErrorListener() {
		// @Override
		// public void onErrorResponse(VolleyError error) {
		// loginActivityInterface.error();
		//
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
		login.setTag("LOGIN");
		VolleySingleton.getVolleySingleton(context).addToRequestQueue(login);

	}
}
