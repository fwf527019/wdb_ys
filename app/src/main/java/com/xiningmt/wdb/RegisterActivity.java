package com.xiningmt.wdb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import butterknife.OnClick;
import rxh.hb.base.BaseActivity;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.StatusCode;
import rxh.hb.volley.util.VolleySingleton;

public class RegisterActivity extends BaseActivity {


    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.individual_member)
    TextView individual_member;
    @Bind(R.id.institutional_member)
    TextView institutional_member;
    @Bind(R.id.gr_set_membership_name)
    EditText gr_set_membership_name;
    @Bind(R.id.gr_set_password)
    EditText gr_set_password;
    @Bind(R.id.gr_repat_set_password)
    EditText gr_repat_set_password;
    @Bind(R.id.gr_phone_number)
    EditText gr_phone_number;
    @Bind(R.id.gr_verification_code)
    EditText gr_verification_code;
    @Bind(R.id.gr_verification_code_btn)
    Button gr_verification_code_btn;
    @Bind(R.id.gr_extension_code)
    EditText gr_extension_code;
    @Bind(R.id.gerenhuiyuan)
    LinearLayout gerenhuiyuan;
    @Bind(R.id.jg_set_membership_name)
    EditText jg_set_membership_name;
    @Bind(R.id.jg_set_password)
    EditText jg_set_password;
    @Bind(R.id.jg_repat_set_password)
    EditText jg_repat_set_password;
    @Bind(R.id.jg_phone_number)
    EditText jg_phone_number;
    @Bind(R.id.jg_verification_code)
    EditText jg_verification_code;
    @Bind(R.id.jg_verification_code_btn)
    Button jg_verification_code_btn;
    @Bind(R.id.jg_extension_code)
    EditText jg_extension_code;
    @Bind(R.id.jigouhuiyuan)
    LinearLayout jigouhuiyuan;
    @Bind(R.id.next)
    Button next;
    @Bind(R.id.jg_institutional_name)
    EditText jg_institutional_name;
    @Bind(R.id.zhucexieyi_checkbox)
    CheckBox zhucexieyiCheckbox;
    @Bind(R.id.zhucexieyi)
    TextView zhucexieyi;

    private TimeCount time;
    RequestQueue mRequestQueue;
    int flag = 0;//0表示个人会员，1表示机构会员

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
            mRequestQueue.cancelAll("REGISTER");
            mRequestQueue.cancelAll("GETCODE");
        }
    }

    public void initview() {
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        title.setText("注册");
        time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
        showmenubg(1);
        flag = 0;
        back.setOnClickListener(this);
        individual_member.setOnClickListener(this);
        institutional_member.setOnClickListener(this);
        gr_verification_code_btn.setOnClickListener(this);
        jg_verification_code_btn.setOnClickListener(this);
        next.setOnClickListener(this);
        zhucexieyi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.zhucexieyi:
                showPopuwind();
                break;
            case R.id.individual_member:
                showmenubg(1);
                break;
            case R.id.institutional_member:
                showmenubg(2);
                break;
            case R.id.gr_verification_code_btn:
                if (gr_phone_number.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (isMobileNO(gr_phone_number.getText().toString())) {
                        getverificationcode(gr_phone_number.getText().toString());
                    } else {
                        Toast.makeText(getApplicationContext(), "手机号码格式错误", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.jg_verification_code_btn:
                if (jg_phone_number.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (isMobileNO(jg_phone_number.getText().toString())) {
                        getverificationcode(jg_phone_number.getText().toString());
                    } else {
                        Toast.makeText(getApplicationContext(), "手机号码格式错误", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.next:
                if (zhucexieyiCheckbox.isChecked()) {
                    check();
                } else {
                    Toast.makeText(this, "确认注册前请认真阅读注册协议", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private PopupWindow mPopuwidow;

    private void showPopuwind() {
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.popupwindow_zcxy, null);
        mPopuwidow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopuwidow.setContentView(contentView);
        contentView.findViewById(R.id.popuw_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopuwidow.dismiss();
            }
        });
        View rootview = LayoutInflater.from(this).inflate(R.layout.activity_register, null);
        mPopuwidow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);


    }

    public void getverificationcode(String num) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", num);
        map.put("type", "1");
        JSONObject paramMap = new JSONObject(map);
        loading("获取验证码中...", "true");
        JsonObjectRequest getcode = new JsonObjectRequest(Request.Method.POST,
                new CreatUrl().creaturl("account", "sendCode"), paramMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismiss();
                        try {
                            String status = response.getString("status");
                            String code = response.getString("code");
                            if (code.equals("200")) {
                                time.start();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        new StatusCode().getstatus(code),
                                        Toast.LENGTH_LONG).show();
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
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        getcode.setTag("GETCODE");
        VolleySingleton.getVolleySingleton(getApplicationContext())
                .addToRequestQueue(getcode);

    }

    public void register(String loginName, String name, String password1, String password2, String extensioncode, String code, String phcode, String linkman) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("loginName", loginName);
        map.put("name", name); //昵称
        map.put("password1", password1);//密码
        map.put("password2", password2);//密码
        if (flag == 0) {
            map.put("type", "1");//注册类型。1代表个人，2代表企业
        } else {
            map.put("type", "2");
        }
        map.put("extensioncode", extensioncode);//推广码
        map.put("code", code);//验证码
        map.put("phcode", phcode);//手机验证码
        map.put("linkman", linkman);//企业联系人姓名
        map.put("accfrom", "3");//1来源于网页，3来源于Android
        JSONObject paramMap = new JSONObject(map);
        loading("注册中", "true");
        JsonObjectRequest register = new JsonObjectRequest(Request.Method.POST,
                new CreatUrl().creaturl("account", "register"), paramMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismiss();
                        try {
                            String status = response.getString("status");
                            String code = response.getString("code");
                            if (code.equals("200")) {
                                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        new StatusCode().getstatus(code),
                                        Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        register.setTag("REGISTER");
        VolleySingleton.getVolleySingleton(getApplicationContext()).addToRequestQueue(register);

    }

    public void check() {
        if (flag == 0) {
            if (gr_set_membership_name.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "会员名不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (gr_set_password.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "设置密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (gr_repat_set_password.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "确认密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (gr_phone_number.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "手机号码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (gr_verification_code.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!gr_set_password.getText().toString().equals(gr_repat_set_password.getText().toString())){
                Toast.makeText(getApplicationContext(), "两次密码不同,请重新输入", Toast.LENGTH_SHORT).show();
                return;
            }
            //个人登录
            register(gr_phone_number.getText().toString(), gr_set_membership_name.getText().toString(), gr_set_password.getText().toString(), gr_repat_set_password.getText().toString(),
                    gr_extension_code.getText().toString(), gr_verification_code.getText().toString(), gr_verification_code.getText().toString(), null);
        } else {
            if (jg_set_membership_name.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "会员名不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (jg_institutional_name.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "机构名不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (jg_set_password.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (jg_repat_set_password.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "确认密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (jg_phone_number.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "联系人手机号码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (jg_verification_code.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!jg_repat_set_password.getText().toString().equals(jg_set_password.getText().toString())){
                Toast.makeText(getApplicationContext(), "两次密码不同,请重新输入", Toast.LENGTH_SHORT).show();
                return;
            }
            //机构登录
            register(jg_phone_number.getText().toString(), jg_set_membership_name.getText().toString(), jg_set_password.getText().toString(), jg_repat_set_password.getText().toString(),
                    jg_extension_code.getText().toString(), jg_verification_code.getText().toString(), jg_verification_code.getText().toString(), jg_institutional_name.getText().toString());
        }
    }


    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {


        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            if (flag == 0) {
                gr_verification_code_btn.setText("重新验证");
                gr_verification_code_btn.setClickable(true);
            } else {
                jg_verification_code_btn.setText("重新验证");
                jg_verification_code_btn.setClickable(true);
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            if (flag == 0) {
                gr_verification_code_btn.setClickable(false);
                gr_verification_code_btn.setText(millisUntilFinished / 1000 + "秒");
            } else {
                jg_verification_code_btn.setClickable(false);
                jg_verification_code_btn.setText(millisUntilFinished / 1000 + "秒");
            }
        }

    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    @SuppressLint("NewApi")
    public void showmenubg(int i) {
        if (i == 1) {
            flag = 0;
            gerenhuiyuan.setVisibility(View.VISIBLE);
            jigouhuiyuan.setVisibility(View.GONE);
            individual_member.setBackground(getResources().getDrawable(R.drawable.activity_my_project_leftbutton_down));
            individual_member.setTextColor(this.getResources().getColor(R.color.white));
            institutional_member.setBackground(getResources().getDrawable(R.drawable.activity_my_project_rightbutton_up));
            institutional_member.setTextColor(this.getResources().getColor(R.color.base_top));
        } else if (i == 2) {
            flag = 1;
            gerenhuiyuan.setVisibility(View.GONE);
            jigouhuiyuan.setVisibility(View.VISIBLE);
            individual_member.setBackground(getResources().getDrawable(R.drawable.activity_my_project_leftbutton_up));
            individual_member.setTextColor(this.getResources().getColor(R.color.base_top));
            institutional_member.setBackground(getResources().getDrawable(R.drawable.activity_my_project_rightbutton_down));
            institutional_member.setTextColor(this.getResources().getColor(R.color.white));

        }
    }

}
