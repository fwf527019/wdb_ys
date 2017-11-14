package com.xiningmt.wdb;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.adapter.ActivityCenterActivityLVAdapter;
import rxh.hb.allinterface.ActivityCenterActivityInterface;
import rxh.hb.base.BaseActivity;
import rxh.hb.jsonbean.ActivityCenterActivityLVBean;
import rxh.hb.presenter.ActivityCenterActivityPresenter;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.ShowDialog;
import rxh.hb.volley.util.VolleySingleton;

import com.android.volley.RequestQueue;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ActivityCenterActivity extends BaseActivity implements ActivityCenterActivityInterface {

    int page = 1;
    RequestQueue mRequestQueue;
    private ImageView back;
    private TextView title;
    private PullToRefreshListView lv;
    ActivityCenterActivityLVAdapter activityCenterActivityLVAdapter;
    List<ActivityCenterActivityLVBean> LVBeans = new ArrayList<ActivityCenterActivityLVBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_center);
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(
                this.getApplicationContext()).getRequestQueue();
        initview();
        initdata();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GETIF");
        }
    }

    public void initview() {
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        lv = (PullToRefreshListView) findViewById(R.id.lv);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position,
                                    long id) {
                Intent intent = new Intent();
                intent.putExtra("ids", LVBeans.get(position).getIds());
                intent.setClass(getApplicationContext(),
                        ActivityCenterDetailsActivity1.class);
                startActivity(intent);

            }
        });
        lv.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(
                        getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                page = 1;
                // 这里写下拉刷新的任务
                new ActivityCenterActivityPresenter(
                        ActivityCenterActivity.this,
                        ActivityCenterActivity.this).getlvinformation(String
                        .valueOf(page));

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // 这里写上拉加载更多的任务
                new ActivityCenterActivityPresenter(
                        ActivityCenterActivity.this,
                        ActivityCenterActivity.this).getlvinformation(String
                        .valueOf(page));

            }
        });
    }


    public void initdata() {
        title.setText("活动中心");
        loading("加载中", "true");
        new ActivityCenterActivityPresenter(getApplicationContext(), this)
                .getlvinformation(String.valueOf(page));
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

    @Override
    public void getinformation(
            List<ActivityCenterActivityLVBean> activityCenterActivityLVBeans) {
        dismiss();
        if (page == 1) {
            LVBeans.clear();
            LVBeans.addAll(activityCenterActivityLVBeans);
            activityCenterActivityLVAdapter = new ActivityCenterActivityLVAdapter(
                    LVBeans, getApplicationContext());
            lv.setAdapter(activityCenterActivityLVAdapter);
        } else {
            LVBeans.addAll(activityCenterActivityLVBeans);
            activityCenterActivityLVAdapter.notifyDataSetChanged();
        }
        lv.onRefreshComplete();
        lv.onRefreshComplete();
        page++;
    }

    @Override
    public void error() {
        dismiss();
        lv.onRefreshComplete();
        lv.onRefreshComplete();

    }

}
