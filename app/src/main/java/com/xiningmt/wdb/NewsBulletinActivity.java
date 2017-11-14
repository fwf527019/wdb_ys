package com.xiningmt.wdb;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.adapter.ActivityCenterActivityLVAdapter;
import rxh.hb.adapter.NewsBulletinActivityLVAdapter;
import rxh.hb.allinterface.NewsBulletinActivityInterface;
import rxh.hb.base.BaseActivity;
import rxh.hb.jsonbean.NewsBulletinActivityBean;
import rxh.hb.presenter.NewsBulletinActivityPresenter;
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

public class NewsBulletinActivity extends BaseActivity implements
		NewsBulletinActivityInterface {

	int page = 1;
	private ImageView back;
	private TextView title;
	RequestQueue mRequestQueue;
	private PullToRefreshListView lv;
	NewsBulletinActivityLVAdapter newsBulletinActivityLVAdapter;
	List<NewsBulletinActivityBean> lvBeans = new ArrayList<NewsBulletinActivityBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_bulletin);
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
				intent.putExtra("title", lvBeans.get(position).getTitle());
				intent.putExtra("author_and_time", lvBeans.get(position)
						.getCreatetime());
				intent.putExtra("imgurl", lvBeans.get(position).getPath());
				intent.putExtra("content", lvBeans.get(position).getContent());
				intent.putExtra("ids",lvBeans.get(position).getIds());

				intent.setClass(getApplicationContext(),
						NewsBulletinDetailsActivity1.class);
//				intent.setClass(getApplicationContext(),
//						NewsBulletinDetailsActivity.class);

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
				new NewsBulletinActivityPresenter(NewsBulletinActivity.this,
						NewsBulletinActivity.this).getlvinformation(String
						.valueOf(page));

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 这里写上拉加载更多的任务
				new NewsBulletinActivityPresenter(NewsBulletinActivity.this,
						NewsBulletinActivity.this).getlvinformation(String
						.valueOf(page));

			}
		});
	}

	public void initdata() {
		title.setText("新闻公告");
		loading("加载中","true");
		new NewsBulletinActivityPresenter(getApplicationContext(), this)
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
			List<NewsBulletinActivityBean> newsBulletinActivityBeans) {
		dismiss();
		if (page == 1) {
			lvBeans.clear();
			lvBeans.addAll(newsBulletinActivityBeans);
			newsBulletinActivityLVAdapter = new NewsBulletinActivityLVAdapter(
					lvBeans, getApplicationContext());
			lv.setAdapter(newsBulletinActivityLVAdapter);
		} else {
			lvBeans.addAll(newsBulletinActivityBeans);
			newsBulletinActivityLVAdapter.notifyDataSetChanged();
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
