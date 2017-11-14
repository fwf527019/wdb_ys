package com.xiningmt.wdb;

import java.util.List;

import rxh.hb.adapter.HistoricalRepaymentActivityLVAdatper;
import rxh.hb.adapter.PlatformRepaymentActivityLVAdapter;
import rxh.hb.allinterface.HistoricalRepaymentInterface;
import rxh.hb.base.BaseActivity;
import rxh.hb.jsonbean.HistoricalRepaymentLVBean;
import rxh.hb.presenter.HistoricalRepaymentPresenter;
import rxh.hb.presenter.PlatformRepaymentPresenter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HistoricalRepaymentActivity extends BaseActivity implements
        HistoricalRepaymentInterface {

    private ImageView back;
    private TextView title;
    private PullToRefreshListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_repayment);
        initview();
        initdata();
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
                // 这里写下拉刷新的任务

                Toast.makeText(getApplicationContext(), "下拉刷新",
                        Toast.LENGTH_LONG).show();
                lv.onRefreshComplete();

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // 这里写上拉加载更多的任务
                Toast.makeText(getApplicationContext(), "上拉加载",
                        Toast.LENGTH_LONG).show();
                lv.onRefreshComplete();

            }
        });
    }

    public void initdata() {
        title.setText("历史还款");
        new HistoricalRepaymentPresenter(getApplicationContext(), this)
                .getlvinformation("");
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
            List<HistoricalRepaymentLVBean> historicalRepaymentLVBeans) {
        HistoricalRepaymentActivityLVAdatper lvAdapter = new HistoricalRepaymentActivityLVAdatper(
                historicalRepaymentLVBeans, getApplicationContext());
        lv.setAdapter(lvAdapter);

    }

    @Override
    public void error() {
        // TODO Auto-generated method stub

    }

}
