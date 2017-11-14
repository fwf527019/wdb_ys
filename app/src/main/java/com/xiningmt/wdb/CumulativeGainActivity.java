package com.xiningmt.wdb;

import java.text.DecimalFormat;

import rxh.hb.base.BaseActivity;
import rxh.hb.eventbusentity.WalletEntity;

import de.greenrobot.event.EventBus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CumulativeGainActivity extends BaseActivity {

    private ImageView back;
    private TextView title, go, cumulative_gain;
    private Button recharge, withdrawals;
    String cumulative_gain_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cumulative_gain_tv = getIntent().getStringExtra("cumulative_gain");
        initview();
    }

    public void initview() {
        setContentView(R.layout.activity_cumulative_gain);
        title = (TextView) findViewById(R.id.title);
        back = (ImageView) findViewById(R.id.back);
        go = (TextView) findViewById(R.id.go);
        title.setText("账户余额 ");
        go.setText("交易记录");
        back.setOnClickListener(this);
        go.setOnClickListener(this);
        cumulative_gain = (TextView) findViewById(R.id.cumulative_gain);
        cumulative_gain.setText(cumulative_gain_tv);
        recharge = (Button) findViewById(R.id.recharge);
        recharge.setOnClickListener(this);
        withdrawals = (Button) findViewById(R.id.withdrawals);
        withdrawals.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.go:
                //交易记录
                startActivity(new Intent(getApplicationContext(), TransactionRecordActivity.class));
                break;
            case R.id.recharge:
                Intent intent1 = new Intent();
                intent1.setClass(getApplicationContext(), RechargeActivity.class);
                startActivityForResult(intent1, 1);
                break;
            case R.id.withdrawals:
                Intent intent2 = new Intent();
                intent2.setClass(getApplicationContext(), WithdrawalsActivity.class);
                startActivityForResult(intent2, 2);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String recharge = data.getExtras().getString("recharge");
            double end = Double.parseDouble(recharge)
                    + Double.parseDouble(cumulative_gain.getText().toString());
            DecimalFormat df = new DecimalFormat("0.00");
            String db = df.format(end);
            cumulative_gain.setText(db);
            // 通知钱包界面重新请求数据
            EventBus.getDefault().post(new WalletEntity(""));

        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            String withdrawals = data.getExtras().getString("withdrawals");
            DecimalFormat df = new DecimalFormat("0.00");
            String db = df.format(Double.parseDouble(withdrawals));
            cumulative_gain.setText(db);
            // 通知钱包界面重新请求数据
            EventBus.getDefault().post(new WalletEntity(""));
        }
    }
}
