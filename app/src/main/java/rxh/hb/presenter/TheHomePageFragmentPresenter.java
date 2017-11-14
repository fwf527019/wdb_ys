package rxh.hb.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiningmt.wdb.LoginActivity;
import com.xiningmt.wdb.MainActivity;

import rxh.hb.allinterface.TheHomePageFragmentInterface;
import rxh.hb.jsonbean.InvestmentFragmentActivityLVBean;
import rxh.hb.jsonbean.LoginBean;
import rxh.hb.jsonbean.TheHomePageFragmentBean;
import rxh.hb.jsonbean.TheHomePageFragmentimgBean;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.volley.util.VolleySingleton;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class TheHomePageFragmentPresenter {

    Context context;
    TheHomePageFragmentInterface theHomePageFragmentInterface;

    public TheHomePageFragmentPresenter(TheHomePageFragmentInterface l, Context context) {
        this.context = context;
        theHomePageFragmentInterface = l;
    }

    public void getlvinformation(String page) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("pageNumber", page);
        map.put("pageSize", "10");
        map.put("isRecom", "1");
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest getif = new JsonObjectRequest(Method.POST, new CreatUrl().creaturl("loan", "getLoanList"),
                paramMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("TheHomePageFragmentPres", response.toString());
                try {
                    String code = response.getString("code");
                    if (Integer.parseInt(code) == 200) {
                        List<TheHomePageFragmentBean> theHomePageFragmentBeans = new ArrayList<TheHomePageFragmentBean>();
                        theHomePageFragmentBeans = JsonUtils.gethome(response.toString());
                        theHomePageFragmentInterface.getlvinformation(theHomePageFragmentBeans);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                theHomePageFragmentInterface.error();
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

    public void getimg() {
        JsonObjectRequest getimg = new JsonObjectRequest(Method.POST, new CreatUrl().getImgurl("mobile", "banner"),
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<TheHomePageFragmentimgBean> theHomePageFragmentBeans = new ArrayList<TheHomePageFragmentimgBean>();
                theHomePageFragmentBeans = JsonUtils.getHomePageFragmentimgBeans(response.toString());
                theHomePageFragmentInterface.getimgurl(theHomePageFragmentBeans);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                theHomePageFragmentInterface.imgerror();
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
        getimg.setTag("GETIMG");
        VolleySingleton.getVolleySingleton(context).addToRequestQueue(getimg);
    }

}
