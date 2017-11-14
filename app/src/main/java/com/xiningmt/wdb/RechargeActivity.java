package com.xiningmt.wdb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import rxh.hb.base.BaseActivity;
import rxh.hb.eventbusentity.PhotoAndNameEventBus;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.volley.util.VolleySingleton;

public class RechargeActivity extends BaseActivity {


    RequestQueue mRequestQueue;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.online_recharge)
    TextView online_recharge;
    @Bind(R.id.line_recharge)
    TextView line_recharge;
    @Bind(R.id.amount_of_money)
    EditText amount_of_money;
    @Bind(R.id.the_receipt_number)
    EditText the_receipt_number;
    @Bind(R.id.prompt)
    TextView prompt;
    @Bind(R.id.img)
    ImageView img;
    @Bind(R.id.line_recharge_ll)
    LinearLayout line_recharge_ll;
    @Bind(R.id.next)
    Button next;

    int flag = 0;//0表示线上充值，1表示线下充值
    String url_path = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(
                this.getApplicationContext()).getRequestQueue();
        if (isNetworkConnected()) {
            initview();
        } else {
            initbaseview();
            base_title.setText("充值");

        }
        setPoint(amount_of_money);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GETIF");
        }
    }

    public void initview() {
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        title.setText("充值");
        setPricePoint(amount_of_money);
        online_recharge.setOnClickListener(this);
        line_recharge.setOnClickListener(this);
        prompt.setOnClickListener(this);
        img.setOnClickListener(this);
        next.setOnClickListener(this);
        showmenubg(1);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.base_back:
                finish();
                break;
            case R.id.online_recharge:
                flag = 0;
                showmenubg(1);
                break;
            case R.id.line_recharge:
                flag = 1;
                showmenubg(2);
                break;
            case R.id.prompt:
                Intent intent0 = new Intent(getApplicationContext(), MultiImageSelectorActivity.class);
                // 是否显示调用相机拍照
                intent0.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // 最大图片选择数量
                //intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
                intent0.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
                startActivityForResult(intent0, 0);
                break;
            case R.id.img:
                Intent intent = new Intent(getApplicationContext(), MultiImageSelectorActivity.class);
                // 是否显示调用相机拍照
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // 最大图片选择数量
                //intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
                startActivityForResult(intent, 0);
                break;
            case R.id.next:
                // 数据是使用Intent返回
                if (flag == 0) {
                    if (amount_of_money.getText().toString().length() > 0) {
                        getinformation();
                    } else {
                        Toast.makeText(getApplicationContext(), "请输入充值金额",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (amount_of_money.getText().toString().length() == 0) {
                        Toast.makeText(getApplicationContext(), "请输入充值金额", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (the_receipt_number.getText().toString().length() == 0) {
                        Toast.makeText(getApplicationContext(), "请输入回执单号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (url_path == null) {
                        Toast.makeText(getApplicationContext(), "请选择回执单照片", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    post(url_path);
                }
                break;
            case R.id.r_load:
                if (isNetworkConnected()) {
                    initview();
                }
                break;
            default:
                break;
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (requestCode == 0 && resultCode == RESULT_OK) {
            // 获取返回的图片列表
            List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            url_path = path.get(0);
            Glide.with(getApplicationContext())
                    .load(url_path)
                    .centerCrop()
                    .into(img);
        } else {
            String str = data.getExtras().getString("pay_result");
            if (str.equals("cancel")) {
                Toast.makeText(getApplicationContext(), "你取消了本次支付",
                        Toast.LENGTH_LONG).show();
            } else if (str.equals("success")) {
                Intent intent = new Intent();
                // 把返回数据存入Intent
                intent.putExtra("recharge", amount_of_money.getText().toString());
                // 设置返回数据
                RechargeActivity.this.setResult(RESULT_OK, intent);
                // 关闭Activity
                RechargeActivity.this.finish();
                Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_LONG)
                        .show();
                finish();
            } else if (str.equals("fail")) {
                Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_LONG)
                        .show();
            }
        }

    }

    public void getinformation() {
        loading("加载中", "true");
        Map<String, String> map = new HashMap<String, String>();
        map.put("money", amount_of_money.getText().toString());
        map.put("type", "1");
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest getif = new JsonObjectRequest(Method.POST,
                new CreatUrl()
                        .creaturl("authorization/voucherMoney", "voucher"),
                paramMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismiss();
                try {
                    String code = response.getString("code");
                    if (new RequestReturnProcessing(
                            RechargeActivity.this, code).processing() == 200) {
                        String tn = response.getString("obj");
                        String serverMode = "00";
                        UPPayAssistEx.startPayByJAR(
                                RechargeActivity.this,
                                PayActivity.class, null, null, tn,
                                serverMode);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismiss();
                Toast.makeText(RechargeActivity.this, error.toString(),
                        Toast.LENGTH_LONG).show();

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
        VolleySingleton.getVolleySingleton(getApplicationContext())
                .addToRequestQueue(getif);
    }

    public void post(String path) {
        RequestParams params = new RequestParams(new CreatUrl().creaturl("authorization", "info/rechargeLine"));
        params.setMultipart(true);
        params.addHeader("Authorization", MyApplication.getToken());
        params.addHeader("flag", "1");
        params.addBodyParameter("tps", the_receipt_number.getText().toString());
        params.addBodyParameter("money", amount_of_money.getText().toString());
        params.addBodyParameter("path", new File(path));
        loading("上传中", "true");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                dismiss();
                String code = JsonUtils.getcode(result);
                if (new RequestReturnProcessing(getApplicationContext(), code).processing() == 200) {
                    Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
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


    // 截取字符串，限制输入
    public static void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0) {
                    if (s.toString().contains(".")) {
                        if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                            s = s.toString().subSequence(0,
                                    s.toString().indexOf(".") + 3);
                            editText.setText(s);
                            editText.setSelection(s.length());
                        }
                    }
                    if (s.toString().trim().substring(0).equals(".")) {
                        s = "0" + s;
                        editText.setText(s);
                        editText.setSelection(2);
                    }

                    if (s.toString().startsWith("0")
                            && s.toString().trim().length() > 1) {
                        if (!s.toString().substring(1, 2).equals(".")) {
                            editText.setText(s.subSequence(0, 1));
                            editText.setSelection(1);
                            return;
                        }
                    }

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

    }


    @SuppressLint("NewApi")
    public void showmenubg(int i) {
        if (i == 1) {
            line_recharge_ll.setVisibility(View.GONE);
            online_recharge.setBackground(getResources().getDrawable(R.drawable.activity_my_project_leftbutton_down));
            online_recharge.setTextColor(this.getResources().getColor(R.color.white));
            line_recharge.setBackground(getResources().getDrawable(R.drawable.activity_my_project_rightbutton_up));
            line_recharge.setTextColor(this.getResources().getColor(R.color.base_top));
        } else if (i == 2) {
            line_recharge_ll.setVisibility(View.VISIBLE);
            online_recharge.setBackground(getResources().getDrawable(R.drawable.activity_my_project_leftbutton_up));
            online_recharge.setTextColor(this.getResources().getColor(R.color.base_top));
            line_recharge.setBackground(getResources().getDrawable(R.drawable.activity_my_project_rightbutton_down));
            line_recharge.setTextColor(this.getResources().getColor(R.color.white));

        }
    }
    private static final int DECIMAL_DIGITS = 2;//小数的位数F
    public static void setPoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > DECIMAL_DIGITS) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + DECIMAL_DIGITS+1);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

}
