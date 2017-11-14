package com.xiningmt.wdb;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import rxh.hb.base.BaseActivity;
import rxh.hb.eventbusentity.WalletEntity;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.volley.util.VolleySingleton;

/**
 * Created by Administrator on 2016/10/13.
 */
public class MemberCertificationThreeActivity extends BaseActivity {


    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.card_no)
    EditText card_no;
    @Bind(R.id.bank_account)
    EditText bank_account;
    @Bind(R.id.branch_address)
    EditText branch_address;
    @Bind(R.id.name_of_Bank)
    EditText name_of_bank;
    @Bind(R.id.next)
    Button next;

    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(
                this.getApplicationContext()).getRequestQueue();
        initview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("ADDBANKIF");
        }
    }

    public void initview() {
        setContentView(R.layout.activity_member_certification_three);
        ButterKnife.bind(this);
        title.setText("会员认证");
        back.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.next:
                if (card_no.getText().toString() == null || card_no.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "银行卡号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (bank_account.getText().toString() == null || bank_account.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "开户银行不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (branch_address.getText().toString() == null || branch_address.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "开户支行地址不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (name_of_bank.getText().toString() == null || name_of_bank.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "name_of_Bank不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                addbankinformation();
                break;
            default:
                break;

        }
    }

    public void addbankinformation() {
        loading("上传中", "true");
        Map<String, String> map = new HashMap<String, String>();
        map.put("nums", card_no.getText().toString());
        map.put("num", card_no.getText().toString());
        map.put("name", bank_account.getText().toString());
        map.put("site", branch_address.getText().toString());
        map.put("bname", name_of_bank.getText().toString());
        map.put("type", "2");
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest addbankif = new JsonObjectRequest(Request.Method.POST,
                new CreatUrl().creaturl("authorization/info", "saveBank"),
                paramMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismiss();
                try {
                    String code = response.getString("code");
                    if (new RequestReturnProcessing(
                            MemberCertificationThreeActivity.this, code)
                            .processing() == 200) {
                        EventBus.getDefault().post(new WalletEntity("mct"));
                        new AlertDialog.Builder(MemberCertificationThreeActivity.this)
                                .setTitle("提示")
                                .setMessage("认证资料上传成功，等待工作人员审核中。")
                                .setCancelable(false)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        finish();
                                    }
                                }).show();
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
}
