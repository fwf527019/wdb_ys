package rxh.hb.fragment;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.RequestQueue;
import com.xiningmt.wdb.ActivityCenterActivity;
import com.xiningmt.wdb.FinancialProjectDetailsActivity;
import com.xiningmt.wdb.NewsBulletinActivity;
import com.xiningmt.wdb.PlatformRepaymentActivity;
import com.xiningmt.wdb.R;
import com.xiningmt.wdb.ServiceCentreSecondActivity;

import de.greenrobot.event.EventBus;
import rxh.hb.adapter.TheHomePageLVAdapter;
import rxh.hb.allinterface.TheHomePageFragmentInterface;
import rxh.hb.base.BaseFragment;
import rxh.hb.eventbusentity.BannerEntity;
import rxh.hb.jsonbean.TheHomePageFragmentBean;
import rxh.hb.jsonbean.TheHomePageFragmentimgBean;
import rxh.hb.presenter.TheHomePageFragmentPresenter;
import rxh.hb.volley.util.VolleySingleton;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TheHomePageFragment extends BaseFragment implements
		TheHomePageFragmentInterface {
	int page = 1;
	TheHomePageFragmentPresenter theHomePageFragmentPresenter;
	RequestQueue mRequestQueue;
	private View view, headview;
	private ListView lv;
	private LayoutInflater mInflater = null;
	private LinearLayout activity_center_ll, news_bulletin_ll,
			platform_repayment_ll, service_centre_ll;
	private TextView activity_center_tv, news_bulletin_tv,
			platform_repayment_tv, service_centre_tv, load;
	List<TheHomePageFragmentBean> theHomePageFragmentLVBeans = new ArrayList<TheHomePageFragmentBean>();
	int i;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		theHomePageFragmentPresenter = new TheHomePageFragmentPresenter(this,
				getActivity());
		// 获得实例对象
		mRequestQueue = VolleySingleton.getVolleySingleton(
				this.getActivity().getApplicationContext()).getRequestQueue();
		mInflater = LayoutInflater.from(getActivity());
		view = inflater.inflate(R.layout.fragment_the_home_page, null);
		initview();
		if (isNetworkConnected()) {
			load.setVisibility(View.GONE);
			initdata();
		}
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll("GETIF");
			mRequestQueue.cancelAll("GETIMG");
		}
	}

	public void initview() {
		headview = mInflater
				.inflate(R.layout.fragment_the_home_page_head, null);
		load = (TextView) view.findViewById(R.id.load);
		load.setOnClickListener(this);
		lv = (ListView) view.findViewById(R.id.lv);
		// 获取将要绑定的数据设置到data中
		lv.addHeaderView(headview);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				Intent intent = new Intent();
				intent.putExtra("ids",
						theHomePageFragmentLVBeans.get(position - 1).getIds());
				intent.setClass(getActivity(),
						FinancialProjectDetailsActivity.class);
				startActivity(intent);

			}
		});
		lv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 当不滚动时
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					// 判断是否滚动到底部
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						// 加载更多功能的代码
						theHomePageFragmentPresenter.getlvinformation(String
								.valueOf(page));
					}
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

			}
		});
		activity_center_tv = (TextView) headview
				.findViewById(R.id.activity_center_tv);
		activity_center_tv.setVisibility(View.GONE);
		news_bulletin_tv = (TextView) headview
				.findViewById(R.id.news_bulletin_tv);
		news_bulletin_tv.setVisibility(View.GONE);
		platform_repayment_tv = (TextView) headview
				.findViewById(R.id.platform_repayment_tv);
		platform_repayment_tv.setVisibility(View.GONE);
		service_centre_tv = (TextView) headview
				.findViewById(R.id.service_centre_tv);
		service_centre_tv.setVisibility(View.GONE);
		activity_center_ll = (LinearLayout) headview
				.findViewById(R.id.activity_center_ll);
		activity_center_ll.setOnClickListener(this);
		news_bulletin_ll = (LinearLayout) headview
				.findViewById(R.id.news_bulletin_ll);
		news_bulletin_ll.setOnClickListener(this);
		platform_repayment_ll = (LinearLayout) headview
				.findViewById(R.id.platform_repayment_ll);
		platform_repayment_ll.setOnClickListener(this);
		service_centre_ll = (LinearLayout) headview
				.findViewById(R.id.service_centre_ll);
		service_centre_ll.setOnClickListener(this);

	}

	public void initdata() {
		theHomePageFragmentPresenter.getlvinformation(String.valueOf(page));
		theHomePageFragmentPresenter.getimg();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_center_ll:
			startActivity(new Intent(getActivity(),
					ActivityCenterActivity.class));
			break;
		case R.id.news_bulletin_ll:
			startActivity(new Intent(getActivity(), NewsBulletinActivity.class));

			break;
		case R.id.platform_repayment_ll:
			startActivity(new Intent(getActivity(),
					PlatformRepaymentActivity.class));

			break;
		case R.id.service_centre_ll:
			startActivity(new Intent(getActivity(),
					ServiceCentreSecondActivity.class));
			break;
		case R.id.load:
			if (isNetworkConnected()) {
				load.setVisibility(View.GONE);
				initdata();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void getlvinformation(
			List<TheHomePageFragmentBean> theHomePageFragmentBeans) {
		page++;
		theHomePageFragmentLVBeans.addAll(theHomePageFragmentBeans);
		TheHomePageLVAdapter lvAdapter = new TheHomePageLVAdapter(
				theHomePageFragmentLVBeans, getActivity());
		lv.setAdapter(lvAdapter);

	}

	@Override
	public void error() {
		// TODO Auto-generated method stub

	}

	@Override
	public void imgerror() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getimgurl(
			List<TheHomePageFragmentimgBean> theHomePageFragmentBeans) {
		EventBus.getDefault().post(new BannerEntity(theHomePageFragmentBeans));

	}

}
