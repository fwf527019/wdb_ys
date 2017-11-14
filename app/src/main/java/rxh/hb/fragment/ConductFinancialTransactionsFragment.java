package rxh.hb.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import rxh.hb.adapter.ConductFinancialTransactionsPageAdapter;
import rxh.hb.jsonbean.ConductFinancialTransactionsFragmentBean;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.volley.util.VolleySingleton;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.xiningmt.wdb.R;

public class ConductFinancialTransactionsFragment extends Fragment {


    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    List<ConductFinancialTransactionsFragmentBean> cBeans = new ArrayList<ConductFinancialTransactionsFragmentBean>();
    ConductFinancialTransactionsPageAdapter adapter;
    RequestQueue mRequestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_conduct_financial_transactions, container, false);
//        // 获得实例对象
//        mRequestQueue = VolleySingleton.getVolleySingleton(getActivity())
//                .getRequestQueue();
        initview();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GETT");
        }
    }

    public void initview() {
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        adapter = new ConductFinancialTransactionsPageAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
    }



    public void getTitle() {
        JsonObjectRequest gett = new JsonObjectRequest(Method.POST,
                new CreatUrl().creaturl("loan", "getType"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        cBeans = JsonUtils.gettitle(response.toString());
                        adapter = new ConductFinancialTransactionsPageAdapter(getChildFragmentManager());
                        viewPager.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        error.toString(), Toast.LENGTH_LONG).show();

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

        gett.setTag("GETT");
        VolleySingleton.getVolleySingleton(getActivity()).addToRequestQueue(
                gett);

    }

}
