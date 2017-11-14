package com.xiningmt.wdb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import rxh.hb.base.BaseActivity;
import rxh.hb.eventbusentity.MyAccountActivityEntity;
import rxh.hb.eventbusentity.PhotoAndNameEventBus;
import rxh.hb.eventbusentity.WalletEntity;
import rxh.hb.jsonbean.MyAccountActivityBean;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.utils.ShowDialog;
import rxh.hb.view.CircleImageView;
import rxh.hb.volley.util.VolleySingleton;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import de.greenrobot.event.EventBus;

public class MyAccountActivity extends BaseActivity {

    String url_path = null;
    RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private ImageView back;
    private CircleImageView img;
    private Button exit_sign;
    private TextView title, go, nicknametv, accounttv, status_authentication, bank_card_status_authentication;
    private RelativeLayout head_portrait, nickname, account, identity_authentication, bank_card_management,
            modify_the_login_password, modify_the_payment_password;
    private SharedPreferences sp;
    MyAccountActivityBean myAccountActivityBean = new MyAccountActivityBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(this.getApplicationContext()).getRequestQueue();
        mImageLoader = VolleySingleton.getVolleySingleton(getApplicationContext()).getImageLoader();
        sp = getSharedPreferences("user", 0);
        base_title.setText("设置");
        EventBus.getDefault().register(this);
        getinformation();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GETIF");
            mRequestQueue.cancelAll("ADDNAME");
        }
    }

    public void onEventMainThread(MyAccountActivityEntity event) {
        if (event.getMsg().equals("AddIdentityAuthenticationActivity")) {
            status_authentication.setText("已认证");
        } else if (event.getMsg().equals("AddBankCardActivity")) {
            bank_card_status_authentication.setText("已认证");
        } else if (event.getMsg().equals("BoundPhoneActivity")) {
            getinformation();
        }
    }

    public void initview() {
        setContentView(R.layout.activity_my_account);
        title = (TextView) findViewById(R.id.title);
        title.setText("设置");
        go = (TextView) findViewById(R.id.go);
        go.setText("");
        go.setOnClickListener(this);
        status_authentication = (TextView) findViewById(R.id.status_authentication);
        bank_card_status_authentication = (TextView) findViewById(R.id.bank_card_status_authentication);
        nicknametv = (TextView) findViewById(R.id.nicknametv);
        accounttv = (TextView) findViewById(R.id.accounttv);
        if (sp.getString("user_name", null) != null) {
            accounttv.setText(sp.getString("user_name", null));
        }
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        img = (CircleImageView) findViewById(R.id.img);
        head_portrait = (RelativeLayout) findViewById(R.id.head_portrait);
        head_portrait.setOnClickListener(this);
        nickname = (RelativeLayout) findViewById(R.id.nickname);
        nickname.setOnClickListener(this);
        account = (RelativeLayout) findViewById(R.id.account);
        account.setOnClickListener(this);
        identity_authentication = (RelativeLayout) findViewById(R.id.identity_authentication);
        identity_authentication.setOnClickListener(this);
        bank_card_management = (RelativeLayout) findViewById(R.id.bank_card_management);
        bank_card_management.setOnClickListener(this);
        modify_the_login_password = (RelativeLayout) findViewById(R.id.modify_the_login_password);
        modify_the_login_password.setOnClickListener(this);
        modify_the_payment_password = (RelativeLayout) findViewById(R.id.modify_the_payment_password);
        modify_the_payment_password.setOnClickListener(this);
        exit_sign = (Button) findViewById(R.id.exit_sign);
        exit_sign.setOnClickListener(this);

    }

    public void initdata() {
        if (myAccountActivityBean.getExamine() != null) {
            if (myAccountActivityBean.getExamine().equals("1")) {
                status_authentication.setText("已认证");
                bank_card_status_authentication.setText("已认证");
            } else if (myAccountActivityBean.getExamine().equals("2")) {
                status_authentication.setText("未认证");
                bank_card_status_authentication.setText("未认证");
            } else if (myAccountActivityBean.getExamine().equals("3")) {
                status_authentication.setText("审核中");
                bank_card_status_authentication.setText("审核中");
            } else if (myAccountActivityBean.getExamine().equals("4")) {
                status_authentication.setText("认证失败");
                bank_card_status_authentication.setText("认证失败");
            }
        }
        if (myAccountActivityBean.getNames() != null) {
            nicknametv.setText(myAccountActivityBean.getNames());
        } else {
            nicknametv.setText("未设置");
        }
        accounttv.setText(myAccountActivityBean.getLoginname());
        mImageLoader.get(new CreatUrl().getimg() + myAccountActivityBean.getHeadpath(),
                ImageLoader.getImageListener(img, R.drawable.preson, R.drawable.preson));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            String nicknametextview = data.getStringExtra("nicknametv");
            if (nicknametextview != null) {
                nicknametv.setText(nicknametextview);
                addusername(nicknametextview);
                EventBus.getDefault().post(new PhotoAndNameEventBus(nicknametextview, null, null));
            }

        } else if (requestCode == 0 && resultCode == RESULT_OK) {
            // 获取返回的图片列表
            List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            url_path = path.get(0);
            post(new File(url_path));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go:
                break;
            case R.id.back:
                finish();
                break;
            case R.id.base_back:
                finish();
                break;
            case R.id.account:
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), BoundPhoneActivity.class);
                intent.putExtra("phone_num", myAccountActivityBean.getLoginname());
                startActivity(intent);
                break;
            case R.id.head_portrait:
                Intent intent0 = new Intent(getApplicationContext(), MultiImageSelectorActivity.class);
                // 是否显示调用相机拍照
                intent0.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // 最大图片选择数量
                //intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
                intent0.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
                startActivityForResult(intent0, 0);
                break;
            case R.id.nickname:
                Intent intent2 = new Intent();
                intent2.putExtra("nicknametv", nicknametv.getText().toString());
                intent2.setClass(getApplicationContext(), SetUpNicknameActivity.class);
                startActivityForResult(intent2, 1);
                break;
            case R.id.identity_authentication:
                Intent intent1 = null;
                //实名认证
                if (MyApplication.getToken() != null) {
                    if (myAccountActivityBean.getExamine().equals("4")) {
                        intent1 = new Intent();
                        intent1.setClass(getApplicationContext(), MemberCertificationActivity.class);
                        startActivity(intent1);
                    } else if (myAccountActivityBean.getExamine().equals("3")) {

                    } else if (myAccountActivityBean.getExamine().equals("2")) {
                        startActivity(new Intent(getApplicationContext(), MemberCertificationOneActivity.class));

                    } else if (myAccountActivityBean.getExamine().equals("1")) {
                        intent1 = new Intent();
                        intent1.putExtra("name", myAccountActivityBean.getNames());
                        intent1.putExtra("card", myAccountActivityBean.getCard());
                        intent1.setClass(getApplicationContext(), RealNameAuthenticationSuccessActivity.class);
                        startActivity(intent1);
                    }
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
                break;
            case R.id.bank_card_management:
                if (MyApplication.getToken() != null) {
                    Intent intent3 = new Intent();
                    intent3.putExtra("flag", bank_card_status_authentication.getText().toString());
                    intent3.setClass(getApplicationContext(), AddBankCardActivity.class);
                    startActivity(intent3);
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
                break;
            case R.id.modify_the_login_password:
                if (MyApplication.getToken() != null) {
                    startActivity(new Intent(getApplicationContext(), ModifyTheLoginPasswordActivity.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
                break;
            case R.id.modify_the_payment_password:
                if (MyApplication.getToken() != null) {
                    Intent intent3 = new Intent();
                    intent3.putExtra("phone_num", myAccountActivityBean.getLoginname());
                    intent3.setClass(getApplicationContext(), SetThePaymentPasswordActivity.class);
                    startActivity(intent3);
                } else {
                    startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                }
                break;
            case R.id.exit_sign:
                MyApplication.setToken(null);
                Editor editor = sp.edit();
                editor.putString("user_name", null);
                editor.putString("password", null);
                editor.putString("token", null);
                editor.commit();
                EventBus.getDefault().post(new WalletEntity("exit"));
                finish();
                break;
            case R.id.r_load:
                if (isNetworkConnected()) {
                    initview();
                    getinformation();
                }
                break;
            default:
                break;
        }

    }

    public void getinformation() {
        loading("加载中", "true");
        JsonObjectRequest getif = new JsonObjectRequest(Method.POST,
                new CreatUrl().creaturl("authorization/info", "getInfo"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismiss();
                try {
                    String code = response.getString("code");
                    if (new RequestReturnProcessing(MyAccountActivity.this, code).processing() == 200) {
                        myAccountActivityBean = JsonUtils.getinMyAccountActivityBean(response.toString());
                        initview();
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
                r_load.setText("服务器或网络出现异常");

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
        getif.setTag("GETIF");
        VolleySingleton.getVolleySingleton(getApplicationContext()).addToRequestQueue(getif);
    }

    public void addusername(String name) {
        loading("上传中", "true");
        Map<String, String> map = new HashMap<String, String>();
        map.put("realName", name);
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest addname = new JsonObjectRequest(Method.POST,
                new CreatUrl().creaturl("authorization/info", "updateInfo"), paramMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismiss();
                        try {
                            String code = response.getString("code");
                            if (new RequestReturnProcessing(getApplicationContext(), code).processing() == 200) {
                                Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismiss();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

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
        addname.setTag("ADDNAME");
        VolleySingleton.getVolleySingleton(getApplicationContext()).addToRequestQueue(addname);
    }

    public void post(File file) {
        RequestParams params = new RequestParams(new CreatUrl().creaturl("authorization/info", "updateHead"));
        params.setMultipart(true);
        params.addHeader("Authorization", MyApplication.getToken());
        params.addHeader("flag", "1");
        params.addBodyParameter("headPath", file);
        loading("上传中", "true");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                dismiss();
                String code = JsonUtils.getcode(result);
                if (new RequestReturnProcessing(getApplicationContext(), code).processing() == 200) {
                    Glide.with(getApplicationContext())
                            .load(url_path)
                            .centerCrop()
                            .into(img);
                    EventBus.getDefault().post(new PhotoAndNameEventBus(null, url_path, null));
                    Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dismiss();
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {

            }
        });

    }

    



}
