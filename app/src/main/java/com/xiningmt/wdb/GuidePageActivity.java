package com.xiningmt.wdb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class GuidePageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_page);
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(GuidePageActivity.this, MainActivity.class);
				startActivity(intent);
				GuidePageActivity.this.finish();
				finish();
				overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
			}

		}, 2000);
	}

}
