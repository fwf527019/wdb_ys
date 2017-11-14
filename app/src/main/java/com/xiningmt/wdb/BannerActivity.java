package com.xiningmt.wdb;

import rxh.hb.base.BaseActivity;
import rxh.hb.utils.CreatUrl;
import rxh.hb.volley.util.VolleySingleton;

import com.android.volley.toolbox.ImageLoader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class BannerActivity extends BaseActivity {

	private ImageView back, img;
	private TextView title, go, atitle, author_and_time, content;
	private ImageLoader mImageLoader;
	String s_title, s_path, s_createtime, s_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_banner);
		s_title = getIntent().getStringExtra("title");
		s_createtime = getIntent().getStringExtra("createtime");
		s_path = getIntent().getStringExtra("path");
		s_content = getIntent().getStringExtra("content");
		mImageLoader = VolleySingleton.getVolleySingleton(getApplicationContext()).getImageLoader();
		initview();
		inittitle();
		initdata();
	}

	public void initview() {
		title = (TextView) findViewById(R.id.title);
		go = (TextView) findViewById(R.id.go);
		atitle = (TextView) findViewById(R.id.atitle);
		author_and_time = (TextView) findViewById(R.id.author_and_time);
		content = (TextView) findViewById(R.id.content);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		img = (ImageView) findViewById(R.id.img);
	}

	public void inittitle() {
		title.setText("活动详情");
		go.setText("");
		go.setVisibility(View.GONE);
	}

	public void initdata() {
		if (s_title != null) {
			atitle.setText(s_title);
		}
		if (s_createtime != null) {
			author_and_time.setText(s_createtime);
		}
		if (s_path != null) {
			mImageLoader.get(new CreatUrl().getimg() + s_path,
					ImageLoader.getImageListener(img, R.drawable.loading_img, R.drawable.load_error));
		}
		if (s_content != null) {
			content.setText(s_content);
		}

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

}
