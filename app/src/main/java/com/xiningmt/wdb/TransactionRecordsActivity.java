//package com.xiningmt.wdb;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import rxh.hb.base.BaseActivity;
//import rxh.hb.base.BaseFragmentActivity;
//
//import rxh.hb.fragment.TransferFragment;
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class TransactionRecordsActivity extends BaseActivity {
//
//	private ImageView back;
//	private TextView title;
//	private TextView transfer, borrowing;
//	List<Fragment> fragments = new ArrayList<Fragment>();
//	ViewPager vp;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		if (isNetworkConnected()) {
//			initview();
//		} else {
//			initbaseview();
//			base_title.setText("我的项目");
//		}
//	}
//
//	public void initview() {
//		setContentView(R.layout.activity_transaction_records);
//		title = (TextView) findViewById(R.id.title);
//		title.setText("我的项目");
//		back = (ImageView) findViewById(R.id.back);
//		back.setOnClickListener(this);
//		vp = (ViewPager) findViewById(R.id.viewpager);
//		fragments.add(new TransferFragment());
//		fragments.add(new BorrowingFragment());
//		FragAdapter adapter = new FragAdapter(getSupportFragmentManager(),
//				fragments);
//		transfer = (TextView) findViewById(R.id.transfer);
//		transfer.setOnClickListener(this);
//		borrowing = (TextView) findViewById(R.id.borrowing);
//		borrowing.setOnClickListener(this);
//		showmenubg(1);
//		// 设定适配器
//		vp.setAdapter(adapter);
//		vp.setOnPageChangeListener(new OnPageChangeListener() {
//
//			@Override
//			public void onPageSelected(int arg0) {
//				showmenubg(arg0 + 1);
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//
//			}
//		});
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.back:
//			finish();
//			break;
//		case R.id.base_back:
//			finish();
//			break;
//		case R.id.transfer:
//			vp.setCurrentItem(0);
//			showmenubg(1);
//			break;
//		case R.id.borrowing:
//			vp.setCurrentItem(1);
//			showmenubg(2);
//			break;
//		case R.id.r_load:
//			if (isNetworkConnected()) {
//				initview();
//			}
//			break;
//		default:
//			break;
//		}
//
//	}
//
//	@SuppressWarnings("deprecation")
//	@SuppressLint("NewApi")
//	public void showmenubg(int i) {
//		if (i == 1) {
//			transfer.setBackground(getResources().getDrawable(
//					R.drawable.activity_my_project_leftbutton_down));
//			transfer.setTextColor(this.getResources().getColor(R.color.white));
//			borrowing.setBackground(getResources().getDrawable(
//					R.drawable.activity_my_project_rightbutton_up));
//			borrowing.setTextColor(this.getResources().getColor(
//					R.color.base_top));
//		} else if (i == 2) {
//
//			transfer.setBackground(getResources().getDrawable(
//					R.drawable.activity_my_project_leftbutton_up));
//			transfer.setTextColor(this.getResources()
//					.getColor(R.color.base_top));
//			borrowing.setBackground(getResources().getDrawable(
//					R.drawable.activity_my_project_rightbutton_down));
//			borrowing.setTextColor(this.getResources().getColor(R.color.white));
//		}
//	}
//
//	public class FragAdapter extends FragmentPagerAdapter {
//		public FragmentManager fm;
//		public List<Fragment> list = new ArrayList<Fragment>();
//
//		public FragAdapter(FragmentManager fm) {
//			super(fm);
//		}
//
//		public FragAdapter(FragmentManager fm, List<Fragment> fragments) {
//			super(fm);
//			this.fm = fm;
//			this.list = fragments;
//		}
//
//		@Override
//		public Fragment getItem(int arg0) {
//			Fragment fragment = null;
//			fragment = list.get(arg0);
//			Bundle bundle = new Bundle();
//			bundle.putString("id", "" + arg0);
//			fragment.setArguments(bundle);
//			return fragment;
//		}
//
//		@Override
//		public int getCount() {
//			return list.size();
//		}
//
//		@Override
//		public Fragment instantiateItem(ViewGroup container, int position) {
//			Fragment fragment = (Fragment) super.instantiateItem(container,
//					position);
//			fm.beginTransaction().show(fragment).commit();
//			return fragment;
//		}
//
//		@Override
//		public void destroyItem(ViewGroup container, int position, Object object) {
//
//			Fragment fragment = list.get(position);
//			fm.beginTransaction().hide(fragment).commit();
//		}
//
//	}
//
//}
