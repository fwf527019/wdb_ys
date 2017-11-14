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
import com.xiningmt.wdb.R;

import rxh.hb.allinterface.ActivityCenterActivityInterface;
import rxh.hb.jsonbean.ActivityCenterActivityLVBean;
import rxh.hb.jsonbean.TheHomePageFragmentBean;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.volley.util.VolleySingleton;
import android.content.Context;
import android.util.Log;

public class ActivityCenterActivityPresenter {

	Context context;
	ActivityCenterActivityInterface activityCenterActivityInterface;

	public ActivityCenterActivityPresenter(Context context,
			ActivityCenterActivityInterface activityCenterActivityInterface) {
		this.context = context;
		this.activityCenterActivityInterface = activityCenterActivityInterface;
	}

	public void getlvinformation(String pageNumber) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("pageNumber", pageNumber);
		map.put("pageSize", "10");
		JSONObject paramMap = new JSONObject(map);
		JsonObjectRequest getif = new JsonObjectRequest(Method.POST,
				new CreatUrl().creaturl("meeting", "list"), paramMap,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							String code = response.getString("code");
							if (new RequestReturnProcessing(context, code)
									.processing() == 200) {
								List<ActivityCenterActivityLVBean> activityCenterActivityLVBeans = new ArrayList<ActivityCenterActivityLVBean>();
								activityCenterActivityLVBeans = JsonUtils
										.getActivityLVBeans(response.toString());
								activityCenterActivityInterface
										.getinformation(activityCenterActivityLVBeans);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						activityCenterActivityInterface.error();
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
