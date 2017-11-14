package com.xiningmt.wdb;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import rxh.hb.base.BaseActivity;

public class MyMessageDetailsActivity extends BaseActivity  {

	private ImageView back;
	private TextView title, go, atitle, author_and_time, content;
	String timeString, titleString, contentString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_my_message_details);
		timeString = getIntent().getStringExtra("time");
		titleString = getIntent().getStringExtra("title");
		contentString = getIntent().getStringExtra("content");
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
	}

	public void initdata() {
		title.setText("消息详情");
		go.setText("");
		atitle.setText(titleString);
		author_and_time.setText("发送时间：" + timeString);
		content.setText(contentString);
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
