package com.xiningmt.wdb;

import java.util.List;

import rxh.hb.adapter.ServiceCentreExpendListViewAdapter;
import rxh.hb.allinterface.ServiceCentreInterface;
import rxh.hb.base.BaseActivity;
import rxh.hb.jsonbean.ServiceCentreParentBean;
import rxh.hb.presenter.SecurityFeaturePresenter;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class ServiceCentreSecondNextActivity extends BaseActivity implements
		ServiceCentreInterface {

	String ids;
	private ExpandableListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_centre_second_next);
		ids = getIntent().getStringExtra("id");
		initview();
		initdata();
	}

	public void initview() {
		lv = (ExpandableListView) findViewById(R.id.expandablelv);
		lv.setDivider(null);
		lv.setGroupIndicator(null);
	}

	public void initdata() {
		new SecurityFeaturePresenter(ServiceCentreSecondNextActivity.this, this)
				.getlvinformation(ids);

	}

	@Override
	public void getinformation(
			List<ServiceCentreParentBean> serviceCentreParentBeans) {
		lv.setAdapter(new ServiceCentreExpendListViewAdapter(
				serviceCentreParentBeans, getApplicationContext()));
	
	}

	@Override
	public void error() {

	}

}
