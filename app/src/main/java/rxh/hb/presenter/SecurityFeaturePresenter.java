package rxh.hb.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;

import rxh.hb.allinterface.ServiceCentreInterface;
import rxh.hb.jsonbean.ActivityCenterActivityLVBean;
import rxh.hb.jsonbean.SBResponsData;
import rxh.hb.jsonbean.ServiceCentreChildBean;
import rxh.hb.jsonbean.ServiceCentreParentBean;
import rxh.hb.jsonbean.ServiceCentreSecondNextActivitySBBean;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.volley.util.VolleySingleton;
import android.content.Context;
import android.util.Log;

public class SecurityFeaturePresenter {

	Context context;
	ServiceCentreInterface serviceCentreInterface;

	public SecurityFeaturePresenter(Context context,
			ServiceCentreInterface serviceCentreInterface) {
		this.context = context;
		this.serviceCentreInterface = serviceCentreInterface;
	}

	public void getlvinformation(String type) {
		// List<ServiceCentreParentBean> serviceCentreParentBeans = new
		// ArrayList<ServiceCentreParentBean>();
		// for (int i = 0; i < 1; i++) {
		// String problem = "银行卡无法绑定？";
		// List<ServiceCentreChildBean> serviceCentreChildBeans = new
		// ArrayList<ServiceCentreChildBean>();
		// for (int j = 0; j < 1; j++) {
		// ServiceCentreChildBean serviceCentreChildBean = new
		// ServiceCentreChildBean();
		// serviceCentreChildBean.setIntroduces("详情请拨打右上角稳当宝客服电话");
		// serviceCentreChildBeans.add(serviceCentreChildBean);
		// }
		// ServiceCentreParentBean serviceCentreParentBean = new
		// ServiceCentreParentBean(
		// problem, serviceCentreChildBeans);
		// serviceCentreParentBeans.add(serviceCentreParentBean);
		// }
		// serviceCentreInterface.getinformation(serviceCentreParentBeans);
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type);
		map.put("pageNumber", "1");
		map.put("pageSize", "100");
		JSONObject paramMap = new JSONObject(map);
		JsonObjectRequest getif = new JsonObjectRequest(Method.POST,
				new CreatUrl().creaturl("help", "list"), paramMap,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							String code = response.getString("code");
							if (new RequestReturnProcessing(context, code)
									.processing() == 200) {
								Log.d("SecurityFeaturePresente", "response:" + response);
								List<SBResponsData.ObjBean.ListBean> sbBeans = new ArrayList<SBResponsData.ObjBean.ListBean>();
//								sbBeans = JsonUtils.getSbBeans(response
//										.toString());
                                sbBeans = JSON.parseObject(response.toString(),SBResponsData.class).getObj().getList();

								List<ServiceCentreParentBean> serviceCentreParentBeans = new ArrayList<ServiceCentreParentBean>();
								for (int i = 0; i < sbBeans.size(); i++) {
									String problem = sbBeans.get(i).getTitle();
									List<ServiceCentreChildBean> serviceCentreChildBeans = new ArrayList<ServiceCentreChildBean>();
									for (int j = 0; j < 1; j++) {
										ServiceCentreChildBean serviceCentreChildBean = new ServiceCentreChildBean();
										serviceCentreChildBean
												.setIntroduces(sbBeans.get(i)
														.getContent());
										serviceCentreChildBeans
												.add(serviceCentreChildBean);
									}
									ServiceCentreParentBean serviceCentreParentBean = new ServiceCentreParentBean(
											problem, serviceCentreChildBeans);
									serviceCentreParentBeans
											.add(serviceCentreParentBean);
								}
								serviceCentreInterface
										.getinformation(serviceCentreParentBeans);

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						serviceCentreInterface.error();
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
