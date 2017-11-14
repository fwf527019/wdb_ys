package com.xiningmt.wdb;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import rxh.hb.adapter.IntegralDetailLVAdapter;
import rxh.hb.adapter.MyIntegralGVAdapterr;
import rxh.hb.base.BaseActivity;
import rxh.hb.jsonbean.IntegralDetailsEntity;
import rxh.hb.presenter.ActivityCenterActivityPresenter;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.volley.util.VolleySingleton;

/**
 * Created by Administrator on 2016/10/11.
 */
public class IntegralDetailActivity extends BaseActivity {


    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.lv)
    PullToRefreshListView lv;

    IntegralDetailLVAdapter adapter;
    List<IntegralDetailsEntity> data = new ArrayList<IntegralDetailsEntity>();

    int page = 1;
    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(getApplicationContext()).getRequestQueue();
        initview();
        getinformation(String.valueOf(page));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GETINFO");
        }
    }

    public void initview() {
        setContentView(R.layout.activity_integral_detail);
        ButterKnife.bind(this);
        title.setText("积分明细");
        back.setOnClickListener(this);
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(
                        getApplicationContext(), System.currentTimeMillis(),
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    public void getinformation(String pageNumber) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("pageNumber", pageNumber);
        map.put("pageSize", "10");
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest getcode = new JsonObjectRequest(Request.Method.POST,
                new CreatUrl().creaturl("authorization", "integral/record"), paramMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String code = response.getString("code");
                            if (new RequestReturnProcessing(getApplicationContext(), code).processing() == 200) {
                                if (page == 1) {
                                    data.clear();
                                    data.addAll(JsonUtils.getmyintegraldetails(response.toString()));
                                    adapter = new IntegralDetailLVAdapter(data, getApplicationContext());
                                    lv.setAdapter(adapter);
                                } else {
                                    data.addAll(JsonUtils.getmyintegraldetails(response.toString()));
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
                Toast.makeText(getApplicationContext(),
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
        VolleySingleton.getVolleySingleton(getApplicationContext())
                .addToRequestQueue(getcode);

    }
}
