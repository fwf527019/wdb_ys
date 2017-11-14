package com.xiningmt.wdb;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import rxh.hb.base.BaseActivity;

public class LoanDataFillingActivity extends BaseActivity {

	private ImageView back;
	private Button next;
	private TextView title, go;
	private EditText name, id_number, age, phone_number;
	private Spinner gender, marital_status, highest_education, monthly_profit;
	String loan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loan_data_filling);
		loan = getIntent().getStringExtra("loan");
		initview();
		initdata();
	}

	public void initview() {
		title = (TextView) findViewById(R.id.title);
		name = (EditText) findViewById(R.id.name);
		id_number = (EditText) findViewById(R.id.id_number);
		age = (EditText) findViewById(R.id.age);
		phone_number = (EditText) findViewById(R.id.phone_number);
		gender = (Spinner) findViewById(R.id.gender);
		marital_status = (Spinner) findViewById(R.id.marital_status);
		highest_education = (Spinner) findViewById(R.id.highest_education);
		monthly_profit = (Spinner) findViewById(R.id.monthly_profit);
		go = (TextView) findViewById(R.id.go);
		go.setOnClickListener(this);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		next = (Button) findViewById(R.id.next);
		next.setOnClickListener(this);
	}

	public void initdata() {
		title.setText(loan);
		go.setText("贷款记录");
		List<String> genderarray = new ArrayList<String>();
		genderarray.add("其它");
		genderarray.add("男");
		genderarray.add("女");
		// 建立Adapter并且绑定数据源
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, genderarray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 绑定 Adapter到控件
		gender.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.go:
			break;
		case R.id.next:
			break;
		default:
			break;
		}

	}

}
