package com.xiningmt.wdb;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.adapter.ActivityCenterActivityLVAdapter;
import rxh.hb.adapter.PlatformRepaymentActivityLVAdapter;
import rxh.hb.allinterface.PlatformRepaymentInterface;
import rxh.hb.base.BaseActivity;
import rxh.hb.jsonbean.ActivityCenterActivityLVBean;
import rxh.hb.jsonbean.PlatformRepaymentLVBean;
import rxh.hb.presenter.ActivityCenterActivityPresenter;
import rxh.hb.presenter.PlatformRepaymentPresenter;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PlatformRepaymentActivity extends BaseActivity implements
		PlatformRepaymentInterface {

	int page = 1;
	RequestQueue mRequestQueue;
	private ImageView back;
	private TextView title, go;
	PlatformRepaymentActivityLVAdapter lvAdapter;
	private PullToRefreshListView lv;
	List<PlatformRepaymentLVBean> LVBeans = new ArrayList<PlatformRepaymentLVBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_platform_repayment);
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
		go = (TextView) findViewById(R.id.go);
		go.setVisibility(View.GONE);
		go.setOnClickListener(this);
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
				page = 1;
				// 这里写下拉刷新的任务
				new PlatformRepaymentPresenter(PlatformRepaymentActivity.this, PlatformRepaymentActivity.this)
				.getlvinformation(String.valueOf(page));

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 这里写上拉加载更多的任务
				new PlatformRepaymentPresenter(PlatformRepaymentActivity.this, PlatformRepaymentActivity.this)
				.getlvinformation(String.valueOf(page));
			}
		});
	}

	public void initdata() {
		title.setText("平台还款");
		go.setText("历史还款");
		loading("加载中","true");
		new PlatformRepaymentPresenter(PlatformRepaymentActivity.this, PlatformRepaymentActivity.this)
				.getlvinformation(String.valueOf(page));
	}

	@Override
	public void getinformation(
			List<PlatformRepaymentLVBean> platformRepaymentLVBeans) {
//		lvAdapter = new PlatformRepaymentActivityLVAdapter(
//				platformRepaymentLVBeans, getApplicationContext());
//		lv.setAdapter(lvAdapter);
		dismiss();
		if (page == 1) {
			LVBeans.clear();
			LVBeans.addAll(platformRepaymentLVBeans);
			lvAdapter = new PlatformRepaymentActivityLVAdapter(
					LVBeans, getApplicationContext());
			lv.setAdapter(lvAdapter);
		} else {
			LVBeans.addAll(platformRepaymentLVBeans);
			lvAdapter.notifyDataSetChanged();
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.go:
			startActivity(new Intent(getApplicationContext(),
					HistoricalRepaymentActivity.class));
			break;

		default:
			break;
		}

	}

}
