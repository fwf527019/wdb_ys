package com.xiningmt.wdb;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import rxh.hb.base.BaseActivity;
import rxh.hb.eventbusentity.MyAccountActivityEntity;
import rxh.hb.jsonbean.AddBankCardActivityBean;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.utils.ShowDialog;
import rxh.hb.view.HPEditText;
import rxh.hb.volley.util.VolleySingleton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;

import de.greenrobot.event.EventBus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BankCardManagementActivity extends BaseActivity {

    RequestQueue mRequestQueue;
    private ImageView back;
    private Button next;
    private TextView title, go;
    private EditText bank_of_deposit, bank_address, name_of_Bank,
            account_holder;
    private HPEditText bank_card_number;
    private EditText open_an_account;
    AddBankCardActivityBean addBankCardActivityBean = new AddBankCardActivityBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card_management);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
                                    BankCardManagementActivity.this, code)
                                    .processing() == 200) {
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
                dismiss();
                Toast.makeText(getApplicationContext(),
                        error.toString(), Toast.LENGTH_LONG).show();

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
        map.put("name", bank_of_deposit.getText().toString());
        map.put("bname", name_of_Bank.getText().toString());
        map.put("phone", open_an_account.getText().toString());
        map.put("site", bank_address.getText().toString());
        map.put("uname", account_holder.getText().toString());
        map.put("type", "1");
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
                            BankCardManagementActivity.this, code)
                            .processing() == 200) {
                        Intent intent = new Intent();
                        intent.putExtra("bank_card_number",
                                bank_card_number.getText().toString()
                                        .replaceAll(" +", ""));
                        intent.putExtra("bank_of_deposit",
                                bank_of_deposit.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismiss();
                Toast.makeText(getApplicationContext(),
                        error.toString(), Toast.LENGTH_LONG).show();

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
        account_holder = (EditText) findViewById(R.id.account_holder);
        open_an_account = (EditText) findViewById(R.id.open_an_account);
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
        if (addBankCardActivityBean.getUname() != null) {
            account_holder.setText("开户人姓名："
                    + addBankCardActivityBean.getUname());
        }
        if (addBankCardActivityBean.getPhone() != null) {
            open_an_account.setText("开户人电话："
                    + addBankCardActivityBean.getPhone());
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
        if (addBankCardActivityBean.getUname() != null) {
            account_holder.setText(addBankCardActivityBean.getUname());
        }
        if (addBankCardActivityBean.getPhone() != null) {
            open_an_account.setText(addBankCardActivityBean.getPhone());
        }
    }

    public void setenabled(boolean flag) {
        bank_card_number.setEnabled(flag);
        bank_of_deposit.setEnabled(flag);
        bank_address.setEnabled(flag);
        name_of_Bank.setEnabled(flag);
        account_holder.setEnabled(flag);
        open_an_account.setEnabled(flag);
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
                        && !name_of_Bank.getText().toString().equals("")
                        && !account_holder.getText().toString().equals("")
                        && !open_an_account.getText().toString().equals("")) {
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
