package com.xiningmt.wdb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import rxh.hb.base.BaseActivity;

public class SetUpNicknameActivity extends BaseActivity {

    String nickname;
    private TextView title, go;
    private EditText nicknametv;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_nickname);
        nickname = getIntent().getStringExtra("nicknametv");
        initview();
    }

    public void initview() {
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        nicknametv = (EditText) findViewById(R.id.nicknametv);
        nicknametv.setText(nickname);
        title = (TextView) findViewById(R.id.title);
        title.setText("设置昵称");
        go = (TextView) findViewById(R.id.go);
        go.setText("确认");
        go.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (nicknametv.getText().toString().length() < 21) {
                    Intent intent = new Intent();
                    intent.putExtra("nicknametv", nicknametv.getText()
                            .toString());
                    setResult(RESULT_OK, intent); // intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
                    finish();// 此处一定要调用finish()方法
                } else {
                    Toast.makeText(getApplicationContext(), "昵称不能超过20位数",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }

}
