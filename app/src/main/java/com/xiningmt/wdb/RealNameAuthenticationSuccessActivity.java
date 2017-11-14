package com.xiningmt.wdb;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rxh.hb.base.BaseActivity;

/**
 * Created by Administrator on 2016/10/28.
 */
public class RealNameAuthenticationSuccessActivity extends BaseActivity {


    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.card_no)
    TextView cardNo;

    String username, card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getIntent().getStringExtra("name");
        card = getIntent().getStringExtra("card");
        initview();
    }

    public void initview() {
        setContentView(R.layout.activity_real_name_authentication_success);
        ButterKnife.bind(this);
        title.setText("实名认证");
        back.setOnClickListener(this);
        name.setText(username);
        cardNo.setText(card);

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
