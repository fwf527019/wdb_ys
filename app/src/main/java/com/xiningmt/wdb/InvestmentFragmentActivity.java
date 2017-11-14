package com.xiningmt.wdb;

import java.util.ArrayList;
import java.util.List;
import rxh.hb.adapter.InvestmentFragmentActivityLVAdapter;
import rxh.hb.allinterface.InvestmentFragmentActivityInterface;
import rxh.hb.base.BaseActivity;
import rxh.hb.jsonbean.InvestmentFragmentActivityLVBean;
import rxh.hb.presenter.InvestmentFragmentActivityPresenter;
import rxh.hb.volley.util.VolleySingleton;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class InvestmentFragmentActivity extends BaseActivity implements InvestmentFragmentActivityInterface {

	int page = 1;
	String ids;
	RequestQueue mRequestQueue;
	private PullToRefreshListView lv;
	InvestmentFragmentActivityLVAdapter ia;
	List<InvestmentFragmentActivityLVBean> lvBeans = new ArrayList<InvestmentFragmentActivityLVBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_investment_fragment);
		ids = getIntent().getStringExtra("id");
		// 获得实例对象
		mRequestQueue = VolleySingleton.getVolleySingleton(this.getApplicationContext()).getRequestQueue();
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
		lv = (PullToRefreshListView) findViewById(R.id.lv);
		lv.setEmptyView(findViewById(R.id.img));
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("ids", lvBeans.get(position).getIds());
				intent.setClass(getApplicationContext(), FinancialProjectDetailsActivity.class);
				startActivity(intent);

			}
		});
		lv.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				page = 1;
				// 这里写下拉刷新的任务
				new InvestmentFragmentActivityPresenter(InvestmentFragmentActivity.this,
						InvestmentFragmentActivity.this).getlvinformation(String.valueOf(page), ids);

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// 这里写上拉加载更多的任务
				new InvestmentFragmentActivityPresenter(InvestmentFragmentActivity.this,
						InvestmentFragmentActivity.this).getlvinformation(String.valueOf(page), ids);

			}
		});
	}

	public void initdata() {
		new InvestmentFragmentActivityPresenter(this, InvestmentFragmentActivity.this)
				.getlvinformation(String.valueOf(page), ids);
	}

	@Override
	public void getlvinformation(List<InvestmentFragmentActivityLVBean> investmentFragmentActivityLVBeans) {
		if (page == 1) {
			lvBeans.clear();
			lvBeans.addAll(investmentFragmentActivityLVBeans);
			ia = new InvestmentFragmentActivityLVAdapter(lvBeans, InvestmentFragmentActivity.this);
			lv.setAdapter(ia);
		} else {
			lvBeans.addAll(investmentFragmentActivityLVBeans);
			ia.notifyDataSetChanged();
		}
		lv.onRefreshComplete();
		lv.onRefreshComplete();
		page++;

	}

	@Override
	public void error() {
		lv.onRefreshComplete();
		lv.onRefreshComplete();

	}

}
