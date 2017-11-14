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

import de.greenrobot.event.EventBus;

import rxh.hb.allinterface.InvestmentFragmentActivityInterface;
import rxh.hb.eventbusentity.WalletEntity;
import rxh.hb.jsonbean.InvestmentFragmentActivityLVBean;
import rxh.hb.jsonbean.LoginBean;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.volley.util.VolleySingleton;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class InvestmentFragmentActivityPresenter {

    Context context;
    InvestmentFragmentActivityInterface investmentFragmentActivityInterface;

    public InvestmentFragmentActivityPresenter(
            InvestmentFragmentActivityInterface investmentFragmentActivityInterface,
            Context context) {
        this.investmentFragmentActivityInterface = investmentFragmentActivityInterface;
        this.context = context;
    }

    public void getlvinformation(String page, String type) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("pageNumber", page);
        map.put("pageSize", "10");
        map.put("type", type);
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest getif = new JsonObjectRequest(Method.POST,
                new CreatUrl().creaturl("loan", "getLoanList"), paramMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String code = response.getString("code");
                            if (Integer.parseInt(code) == 200) {
                                List<InvestmentFragmentActivityLVBean> investmentFragmentActivityLVBeans = new ArrayList<InvestmentFragmentActivityLVBean>();
                                investmentFragmentActivityLVBeans = JsonUtils
                                        .getcontent(response.toString());
                                investmentFragmentActivityInterface
                                        .getlvinformation(investmentFragmentActivityLVBeans);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                investmentFragmentActivityInterface.error();
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
