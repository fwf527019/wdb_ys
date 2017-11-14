package com.xiningmt.wdb;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import rxh.hb.adapter.MyIntegralGVAdapterr;
import rxh.hb.adapter.MyLoanLVAdapter;
import rxh.hb.base.BaseActivity;
import rxh.hb.fragment.MyIntegralDialogFragment;
import rxh.hb.jsonbean.MyIntegralEntity;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.volley.util.VolleySingleton;

/**
 * Created by Administrator on 2016/10/11.
 */
public class MyIntegralActivity extends BaseActivity implements MyIntegralDialogFragment.MyIntegralDialogListener {


    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.go)
    TextView go;
    @Bind(R.id.integral)
    TextView integral;
    @Bind(R.id.gv)
    PullToRefreshGridView gv;

    MyIntegralGVAdapterr adapter;
    List<MyIntegralEntity> data = new ArrayList<MyIntegralEntity>();

    int page = 1;
    RequestQueue mRequestQueue;
    int exchangeposition;
    MyIntegralDialogFragment dialogFragment;

    String integral_string = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        integral_string = getIntent().getStringExtra("integral");
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
            mRequestQueue.cancelAll("EX");
        }
    }

    public void initview() {
        setContentView(R.layout.activity_my_integral);
        ButterKnife.bind(this);
        title.setText("我的积分");
        go.setText("积分细则");
        integral.setText("可用积分:" + integral_string);
        go.setOnClickListener(this);
        back.setOnClickListener(this);
        gv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // 这里写下拉刷新的任务
                page = 1;
                getinformation(String.valueOf(page));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                // 这里写上拉加载更多的任务
                getinformation(String.valueOf(page));
            }
        });
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                exchangeposition = i;
                showdialog(data.get(i).getContent());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.go:
                startActivity(new Intent(getApplicationContext(), IntegralDetailActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void exchange() {
        exchange(data.get(exchangeposition).getIds(), data.get(exchangeposition).getNum());
    }

    public void showdialog(String ts) {
        dialogFragment = new MyIntegralDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tisi", ts);
        dialogFragment.setArguments(bundle);
        getSupportFragmentManager().executePendingTransactions();
        if (!dialogFragment.isAdded()) {
            dialogFragment.show(getSupportFragmentManager(), "dialogFragment");
        }

    }


    public void getinformation(String pageNumber) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("pageNumber", pageNumber);
        map.put("pageSize", "10");
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest getcode = new JsonObjectRequest(Request.Method.POST,
                new CreatUrl().creaturl("authorization", "integral/goods"), paramMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String code = response.getString("code");
                            if (new RequestReturnProcessing(getApplicationContext(), code).processing() == 200) {
                                if (page == 1) {
                                    data.clear();
                                    data.addAll(JsonUtils.getmyintegral(response.toString()));
                                    adapter = new MyIntegralGVAdapterr(data, getApplicationContext());
                                    gv.setAdapter(adapter);
                                } else {
                                    data.addAll(JsonUtils.getmyintegral(response.toString()));
                                    adapter.notifyDataSetChanged();
                                }
                                gv.onRefreshComplete();
                                gv.onRefreshComplete();
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


    public void exchange(String goodsid, String num) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("goodsid", goodsid);
        map.put("num", num);
        JSONObject paramMap = new JSONObject(map);
        final JsonObjectRequest ex = new JsonObjectRequest(Request.Method.POST,
                new CreatUrl().creaturl("authorization", "integral/exchange"), paramMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String code = response.getString("code");
                            if (new RequestReturnProcessing(getApplicationContext(), code).processing() == 200) {
                                Toast.makeText(getApplicationContext(), "兑换成功", Toast.LENGTH_SHORT).show();
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
        ex.setTag("EX");
        VolleySingleton.getVolleySingleton(getApplicationContext())
                .addToRequestQueue(ex);

    }

}
