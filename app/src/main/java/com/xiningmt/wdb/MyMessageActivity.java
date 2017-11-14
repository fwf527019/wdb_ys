package com.xiningmt.wdb;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.adapter.ActivityCenterActivityLVAdapter;
import rxh.hb.adapter.MyMessageActivityLVAdapter;
import rxh.hb.allinterface.MyMessageActivityInterface;
import rxh.hb.base.BaseActivity;
import rxh.hb.jsonbean.MyMessageActivityLVBean;
import rxh.hb.presenter.MyMessageActivityPresenter;
import rxh.hb.utils.ShowDialog;

import com.android.volley.RequestQueue;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyMessageActivity extends BaseActivity implements MyMessageActivityInterface {

    int page = 1;
    RequestQueue mRequestQueue;
    private ImageView back;
    private TextView title;
    private PullToRefreshListView lv;
    MyMessageActivityLVAdapter myMessageActivityLVAdapter;
    List<MyMessageActivityLVBean> LVBeans = new ArrayList<MyMessageActivityLVBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNetworkConnected()) {
            initview();
            initdata();
        } else {
            initbaseview();
            base_title.setText("我的消息");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GETIF");
        }
    }

    public void initview() {
        setContentView(R.layout.activity_my_message);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        lv = (PullToRefreshListView) findViewById(R.id.lv);
//        lv.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
//                Intent intent = new Intent();
//                intent.putExtra("time", LVBeans.get(position).getCreatetime());
//                intent.putExtra("title", LVBeans.get(position).getTitle());
//                intent.putExtra("content", LVBeans.get(position).getContent());
//                intent.setClass(getApplicationContext(), MyMessageDetailsActivity.class);
//                startActivity(intent);
//
//            }
//        });
        lv.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                // 这里写下拉刷新的任务
                new MyMessageActivityPresenter(MyMessageActivity.this, MyMessageActivity.this)
                        .getlvinformation(String.valueOf(page));

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 这里写上拉加载更多的任务
                new MyMessageActivityPresenter(MyMessageActivity.this, MyMessageActivity.this)
                        .getlvinformation(String.valueOf(page));

            }
        });

    }

    public void initdata() {
        title.setText("我的消息");
        loading("加载中", "true");
        new MyMessageActivityPresenter(MyMessageActivity.this, MyMessageActivity.this)
                .getlvinformation(String.valueOf(page));
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
            case R.id.r_load:
                if (isNetworkConnected()) {
                    initview();
                    initdata();
                }
            default:
                break;
        }

    }

    @Override
    public void getinformation(List<MyMessageActivityLVBean> myMessageActivityLVBeans) {
        dismiss();
        if (page == 1) {
            LVBeans.clear();
            LVBeans.addAll(myMessageActivityLVBeans);
            myMessageActivityLVAdapter = new MyMessageActivityLVAdapter(LVBeans, getApplicationContext());
            lv.setAdapter(myMessageActivityLVAdapter);
        } else {
            LVBeans.addAll(myMessageActivityLVBeans);
            myMessageActivityLVAdapter.notifyDataSetChanged();
        }
        lv.onRefreshComplete();
        lv.onRefreshComplete();
        page++;
    }

    @Override
    public void error() {
        dismiss();
    }

}
