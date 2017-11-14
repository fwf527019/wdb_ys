package com.xiningmt.wdb;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;

import de.greenrobot.event.EventBus;

import rxh.hb.base.BaseActivity;
import rxh.hb.eventbusentity.MyAccountActivityEntity;
import rxh.hb.eventbusentity.WalletEntity;
import rxh.hb.jsonbean.AddBankCardActivityBean;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.utils.ShowDialog;
import rxh.hb.utils.StatusCode;
import rxh.hb.view.HPEditText;
import rxh.hb.view.XEditText;
import rxh.hb.volley.util.VolleySingleton;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddBankCardActivity extends BaseActivity {

    RequestQueue mRequestQueue;
    private ImageView back;
    private Button next;
    private TextView title, go;
    private EditText bank_of_deposit, bank_address, name_of_Bank;
    private HPEditText bank_card_number;
    String flag;
    AddBankCardActivityBean addBankCardActivityBean = new AddBankCardActivityBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_card);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        flag = getIntent().getStringExtra("flag");
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(
                this.getApplicationContext()).getRequestQueue();
        initview();
        inittitle();
        getbankinformation();
        setenabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GETBANKIF");
            mRequestQueue.cancelAll("ADDBANKIF");
        }
    }

    public void getbankinformation() {
        loading("加载中", "true");
        JsonObjectRequest getbankif = new JsonObjectRequest(Method.POST,
                new CreatUrl().creaturl("authorization/info", "getBank"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismiss();
                        try {
                            String code = response.getString("code");
                            if (new RequestReturnProcessing(
                                    getApplicationContext(), code).processing() == 200) {
                                addBankCardActivityBean = JsonUtils
                                        .getBankCardActivityBean(response
                                                .toString());
                                initdata();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.toString(), Toast.LENGTH_LONG).show();
                dismiss();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", MyApplication.getToken());
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        getbankif.setTag("GETBANKIF");
        VolleySingleton.getVolleySingleton(getApplicationContext())
                .addToRequestQueue(getbankif);
    }

    public void addbankinformation() {
        loading("上传中", "true");
        Map<String, String> map = new HashMap<String, String>();
        map.put("nums",
                bank_card_number.getText().toString().replaceAll(" +", ""));
        map.put("num", bank_card_number.getText().toString().replaceAll(" +", ""));
        map.put("name", bank_of_deposit.getText().toString());
        map.put("type", "1");
        map.put("bname", name_of_Bank.getText().toString());
        map.put("site", bank_address.getText().toString());
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest addbankif = new JsonObjectRequest(Method.POST,
                new CreatUrl().creaturl("authorization/info", "saveBank"),
                paramMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismiss();
                try {
                    String code = response.getString("code");
                    if (new RequestReturnProcessing(
                            getApplicationContext(), code).processing() == 200) {
                        setenabled(false);
                        next.setVisibility(View.GONE);
                        EventBus.getDefault().post(
                                new MyAccountActivityEntity("AddBankCardActivity"));
                        Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.toString(), Toast.LENGTH_LONG).show();
                dismiss();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", MyApplication.getToken());
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        addbankif.setTag("ADDBANKIF");
        VolleySingleton.getVolleySingleton(getApplicationContext())
                .addToRequestQueue(addbankif);
    }

    public void initview() {
        title = (TextView) findViewById(R.id.title);
        bank_card_number = (HPEditText) findViewById(R.id.bank_card_number);
        bank_of_deposit = (EditText) findViewById(R.id.bank_of_deposit);
        bank_address = (EditText) findViewById(R.id.bank_address);
        name_of_Bank = (EditText) findViewById(R.id.name_of_Bank);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        go = (TextView) findViewById(R.id.go);
        go.setOnClickListener(this);
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(this);
        next.setVisibility(View.GONE);
    }

    public void inittitle() {
        title.setText("银行卡管理");
        go.setText("编辑");
    }

    public void initdata() {
        if (addBankCardActivityBean.getNums() != null) {
            bank_card_number.setText("银行卡号："
                    + addBankCardActivityBean.getNums());
        }
        if (addBankCardActivityBean.getName() != null) {
            bank_of_deposit.setText("开户银行类型："
                    + addBankCardActivityBean.getName());

        }
        if (addBankCardActivityBean.getSite() != null) {
            bank_address.setText("开户支行地址：" + addBankCardActivityBean.getSite());
        }
        if (addBankCardActivityBean.getBname() != null) {
            name_of_Bank
                    .setText("开户支行名称：" + addBankCardActivityBean.getBname());
        }
    }

    public void bg() {
        if (addBankCardActivityBean.getNums() != null) {
            bank_card_number.setText(addBankCardActivityBean.getNums());
        }
        if (addBankCardActivityBean.getName() != null) {
            bank_of_deposit.setText(addBankCardActivityBean.getName());

        }
        if (addBankCardActivityBean.getSite() != null) {
            bank_address.setText(addBankCardActivityBean.getSite());
        }
        if (addBankCardActivityBean.getBname() != null) {
            name_of_Bank.setText(addBankCardActivityBean.getBname());
        }

    }

    public void setenabled(boolean flag) {
        bank_card_number.setEnabled(flag);
        bank_of_deposit.setEnabled(flag);
        bank_address.setEnabled(flag);
        name_of_Bank.setEnabled(flag);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.go:
                if (go.getText().toString().equals("编辑")) {
                    go.setText("取消");
                    setenabled(true);
                    next.setVisibility(View.VISIBLE);
                    bg();
                } else {
                    go.setText("编辑");
                    setenabled(false);
                    next.setVisibility(View.GONE);
                }
                break;
            case R.id.next:
                if (!bank_card_number.getText().toString().equals("")
                        && !bank_of_deposit.getText().toString().equals("")
                        && !bank_address.getText().toString().equals("")
                        && !name_of_Bank.getText().toString().equals("")) {
                    addbankinformation();
                } else {
                    Toast.makeText(getApplicationContext(), "所有输入框不能为空",
                            Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }

    }
}
