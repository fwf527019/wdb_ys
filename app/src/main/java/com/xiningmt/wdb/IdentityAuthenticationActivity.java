package com.xiningmt.wdb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import rxh.hb.base.BaseActivity;

public class IdentityAuthenticationActivity extends BaseActivity {

	private ImageView back;
	private TextView title, go, bank_card_number, bank_of_deposit,
			bank_address, name_of_Bank, account_holder, open_an_account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_identity_authentication);
		initview();
		initdata();
	}

	public void initview() {
		title = (TextView) findViewById(R.id.title);
		bank_card_number = (TextView) findViewById(R.id.bank_card_number);
		bank_of_deposit = (TextView) findViewById(R.id.bank_of_deposit);
		bank_address = (TextView) findViewById(R.id.bank_address);
		name_of_Bank = (TextView) findViewById(R.id.name_of_Bank);
		account_holder = (TextView) findViewById(R.id.account_holder);
		open_an_account = (TextView) findViewById(R.id.open_an_account);
		go = (TextView) findViewById(R.id.go);
		go.setOnClickListener(this);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
	}

	public void initdata() {
		title.setText("身份认证");
		go.setText("编辑");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.go:
			startActivity(new Intent(getApplicationContext(),
					AddIdentityAuthenticationActivity.class));
			break;

		default:
			break;
		}

	}

}
