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
import rxh.hb.allinterface.PlatformRepaymentInterface;
import rxh.hb.jsonbean.PlatformRepaymentLVBean;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.volley.util.VolleySingleton;
import android.content.Context;

public class PlatformRepaymentPresenter {

	Context context;
	PlatformRepaymentInterface platformRepaymentInterface;

	public PlatformRepaymentPresenter(Context context,
			PlatformRepaymentInterface platformRepaymentInterface) {
		this.context = context;
		this.platformRepaymentInterface = platformRepaymentInterface;
	}

	public void getlvinformation(String pageNumber) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("pageNumber", pageNumber);
		map.put("pageSize", "10");
		JSONObject paramMap = new JSONObject(map);
		JsonObjectRequest getif = new JsonObjectRequest(Method.POST,
				new CreatUrl().creaturl("paymentPlatform", "paymentList"),
				paramMap, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							String code = response.getString("code");
							if (new RequestReturnProcessing(context, code)
									.processing() == 200) {
								List<PlatformRepaymentLVBean> pLvBeans = new ArrayList<PlatformRepaymentLVBean>();
								pLvBeans = JsonUtils.getplvBeans(response
										.toString());
								platformRepaymentInterface
										.getinformation(pLvBeans);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						platformRepaymentInterface.error();
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
		getif.setTag("GETIF");
		VolleySingleton.getVolleySingleton(context).addToRequestQueue(getif);
	}

}
