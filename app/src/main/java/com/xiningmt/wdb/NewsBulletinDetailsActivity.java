package com.xiningmt.wdb;

import rxh.hb.base.BaseActivity;
import rxh.hb.utils.CreatUrl;
import rxh.hb.volley.util.VolleySingleton;

import com.android.volley.toolbox.ImageLoader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsBulletinDetailsActivity extends BaseActivity  {

	private ImageView back, img;
	private TextView title, go, atitle, author_and_time, content;
	String titString, auString, imString, cString,ids;
	private ImageLoader mImageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_news_bulletin_details);
		titString = getIntent().getStringExtra("title");
		auString = getIntent().getStringExtra("author_and_time");
		imString = getIntent().getStringExtra("imgurl");
		cString = getIntent().getStringExtra("content");
		ids = getIntent().getStringExtra("ids");

		mImageLoader = VolleySingleton.getVolleySingleton(
				getApplicationContext()).getImageLoader();
		initview();
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

	public void initdata() {
		title.setText("公告详情");
		go.setText("");
		mImageLoader.get(new CreatUrl().getimg() + imString, ImageLoader
				.getImageListener(img, R.drawable.loading_img, R.drawable.load_error));
		atitle.setText(titString);
		author_and_time.setText(auString);
		content.setText(cString);
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
