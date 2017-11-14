package com.xiningmt.wdb;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.adapter.ActivityCenterActivityLVAdapter;
import rxh.hb.adapter.MyProjectActivityLVAdapter;
import rxh.hb.allinterface.MyProjectActivityInterface;
import rxh.hb.base.BaseActivity;
import rxh.hb.jsonbean.ActivityCenterActivityLVBean;
import rxh.hb.jsonbean.TransferThePossessionOfFragmentLVBean;
import rxh.hb.presenter.ActivityCenterActivityPresenter;
import rxh.hb.presenter.MyProjectActivityPresenter;
import rxh.hb.utils.ShowDialog;
import rxh.hb.volley.util.VolleySingleton;

import com.android.volley.RequestQueue;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyProjectTwoActivity extends BaseActivity implements MyProjectActivityInterface {

    int page = 1;
    RequestQueue mRequestQueue;
    private ImageView back;
    private TextView title;
    private PullToRefreshListView lv;
    MyProjectActivityLVAdapter adapter;
    List<TransferThePossessionOfFragmentLVBean> LVBeans = new ArrayList<TransferThePossessionOfFragmentLVBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(this.getApplicationContext()).getRequestQueue();
        base_title.setText("交易记录");
        initdata();
        initview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GETIF");
        }
    }

    public void initview() {
        setContentView(R.layout.activity_my_project_two);
        title = (TextView) findViewById(R.id.title);
        title.setText("交易记录");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        lv = (PullToRefreshListView) findViewById(R.id.lv);
        lv.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                page = 1;
                // 这里写下拉刷新的任务
                new MyProjectActivityPresenter(MyProjectTwoActivity.this, MyProjectTwoActivity.this)
                        .getlvinformation(String.valueOf(page));

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 这里写上拉加载更多的任务
                new MyProjectActivityPresenter(MyProjectTwoActivity.this, MyProjectTwoActivity.this)
                        .getlvinformation(String.valueOf(page));

            }
        });
    }

    public void initdata() {
        loading("加载中", "true");
        new MyProjectActivityPresenter(MyProjectTwoActivity.this, this).getlvinformation(String.valueOf(page));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.base_back:
                finish();
                break;
            default:
                break;
        }

    }

    @Override
    public void getlvinformation(List<TransferThePossessionOfFragmentLVBean> transferThePossessionOfFragmentLVBeans) {
        dismiss();

        if (page == 1) {
            LVBeans.clear();
            LVBeans.addAll(transferThePossessionOfFragmentLVBeans);
            adapter = new MyProjectActivityLVAdapter(LVBeans, getApplicationContext());
            lv.setAdapter(adapter);
        } else {
            LVBeans.addAll(transferThePossessionOfFragmentLVBeans);
            adapter.notifyDataSetChanged();
        }
        lv.onRefreshComplete();
        lv.onRefreshComplete();
        page++;
    }

    @Override
    public void error() {
        dismiss();
        if (lv != null) {
            lv.onRefreshComplete();
            lv.onRefreshComplete();
        }
        r_load.setText("服务器或网络出现异常");
    }

}
