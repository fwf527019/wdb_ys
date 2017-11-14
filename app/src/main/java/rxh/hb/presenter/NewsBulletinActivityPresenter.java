package rxh.hb.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import rxh.hb.allinterface.NewsBulletinActivityInterface;
import rxh.hb.jsonbean.ActivityCenterActivityLVBean;
import rxh.hb.jsonbean.NewsBulletinActivityBean;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.volley.util.VolleySingleton;
import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.xiningmt.wdb.R;

public class NewsBulletinActivityPresenter {

	Context context;
	NewsBulletinActivityInterface newsBulletinActivityInterface;

	public NewsBulletinActivityPresenter(Context context,
			NewsBulletinActivityInterface newsBulletinActivityInterface) {
		this.context = context;
		this.newsBulletinActivityInterface = newsBulletinActivityInterface;
	}

	public void getlvinformation(String pageNumber) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("pageNumber", pageNumber);
		map.put("pageSize", "10");
		JSONObject paramMap = new JSONObject(map);
		JsonObjectRequest getif = new JsonObjectRequest(Method.POST,
				new CreatUrl().creaturl("news", "list"), paramMap,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							String code = response.getString("code");
							if (new RequestReturnProcessing(context, code)
									.processing() == 200) {
								List<NewsBulletinActivityBean> newsBulletinActivityBeans = new ArrayList<NewsBulletinActivityBean>();
								newsBulletinActivityBeans = JsonUtils
										.getBulletinActivityBeans(response
												.toString());
								newsBulletinActivityInterface
										.getinformation(newsBulletinActivityBeans);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						newsBulletinActivityInterface.error();
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
