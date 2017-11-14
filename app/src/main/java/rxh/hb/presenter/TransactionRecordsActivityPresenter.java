package rxh.hb.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import rxh.hb.allinterface.TransactionRecordsActivityInterface;
import rxh.hb.jsonbean.TransactionRecordsActivityLVBean2;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.volley.util.VolleySingleton;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;

public class TransactionRecordsActivityPresenter {

	Context context;
	TransactionRecordsActivityInterface transactionRecordsActivityInterface;

	public TransactionRecordsActivityPresenter(
			Context context,
			TransactionRecordsActivityInterface transactionRecordsActivityInterface) {
		this.context = context;
		this.transactionRecordsActivityInterface = transactionRecordsActivityInterface;
	}

	public void getlvinformation(String pageNumber, String type) {
		Map<String, String> map = new HashMap<String, String>();
	//	map.put("type", type);
		map.put("pageNumber", pageNumber);
		map.put("pageSize", "10");
		JSONObject paramMap = new JSONObject(map);
		JsonObjectRequest getif = new JsonObjectRequest(
				Method.POST,
				new CreatUrl().creaturl("authorization/myproject", "mypro"),
				paramMap, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d("TransactionRecordsActiv", response.toString());
						try {
							String code = response.getString("code");
							if (new RequestReturnProcessing(context, code)
									.processing() == 200) {
								List<TransactionRecordsActivityLVBean2> mp = new ArrayList<TransactionRecordsActivityLVBean2>();
								mp = JsonUtils.getmpsBeans(response.toString());
								transactionRecordsActivityInterface
										.getinformation(mp);

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						transactionRecordsActivityInterface.error();
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
		VolleySingleton.getVolleySingleton(context).addToRequestQueue(getif);

	}

}
