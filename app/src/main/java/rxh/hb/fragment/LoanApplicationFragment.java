package rxh.hb.fragment;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xiningmt.wdb.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import rxh.hb.adapter.LoanApplicationLVAdapter;
import rxh.hb.adapter.MyLoanLVAdapter;
import rxh.hb.base.BaseFragment;
import rxh.hb.jsonbean.LoanApplicationEntity;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.volley.util.VolleySingleton;

/**
 * Created by Administrator on 2016/10/11.
 * 借款申请
 */
public class LoanApplicationFragment extends BaseFragment {

    View view;
    @Bind(R.id.lv)
    PullToRefreshListView lv;

    List<LoanApplicationEntity> data = new ArrayList<LoanApplicationEntity>();
    LoanApplicationLVAdapter adapter;

    int page = 1;
    RequestQueue mRequestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_loan_application, null);
        ButterKnife.bind(this, view);
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(
                getActivity().getApplicationContext()).getRequestQueue();
        initview();
        getinformation(String.valueOf(page));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GETINFO");
        }
    }

    public void initview() {
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(
                        getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // 这里写下拉刷新的任务
                page = 1;
                getinformation(String.valueOf(page));

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // 这里写上拉加载更多的任务
                getinformation(String.valueOf(page));
            }
        });
        adapter = new LoanApplicationLVAdapter(data, getActivity());
        lv.setAdapter(adapter);
    }

    public void getinformation(String pageNumber) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("pageNumber", pageNumber);
        map.put("pageSize", "10");
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest getcode = new JsonObjectRequest(Request.Method.POST,
                new CreatUrl().creaturl("authorization/payment", "appjk"), paramMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String code = response.getString("code");
                            if (new RequestReturnProcessing(getActivity(), code).processing() == 200) {
                                if (page == 1) {
                                    data.clear();
                                    data.addAll(JsonUtils.getloan(response.toString()));
                                    adapter = new LoanApplicationLVAdapter(data, getActivity());
                                    lv.setAdapter(adapter);
                                } else {
                                    data.addAll(JsonUtils.getloan(response.toString()));
                                    adapter.notifyDataSetChanged();
                                }
                                lv.onRefreshComplete();
                                lv.onRefreshComplete();
                                page++;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
                headers.put("Authorization", MyApplication.getToken());
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        getcode.setTag("GETINFO");
        VolleySingleton.getVolleySingleton(getActivity().getApplicationContext())
                .addToRequestQueue(getcode);

    }

}
