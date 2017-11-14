package rxh.hb.fragment;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import rxh.hb.eventbusentity.MainEntity;
import rxh.hb.eventbusentity.PhotoAndNameEventBus;
import rxh.hb.eventbusentity.WalletEntity;
import rxh.hb.jsonbean.MyAccountActivityBean;
import rxh.hb.jsonbean.ProfitEntity;
import rxh.hb.utils.ChangeUtils;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.utils.ShowDialog;
import rxh.hb.view.CircleImageView;
import rxh.hb.volley.util.VolleySingleton;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.xiningmt.wdb.CumulativeGainActivity;
import com.xiningmt.wdb.LoginActivity;
import com.xiningmt.wdb.MemberCertificationActivity;
import com.xiningmt.wdb.MemberCertificationOneActivity;
import com.xiningmt.wdb.MyAccountActivity;
import com.xiningmt.wdb.MyIntegralActivity;
import com.xiningmt.wdb.MyLoanActivity;
import com.xiningmt.wdb.MyMessageActivity;
import com.xiningmt.wdb.MyMonthlyBillActivity;
import com.xiningmt.wdb.MyProjectActivity;
import com.xiningmt.wdb.MyProjectTwoActivity;
import com.xiningmt.wdb.R;
import com.xiningmt.wdb.RealNameAuthenticationSuccessActivity;
import com.xiningmt.wdb.TransactionRecordActivity;

import de.greenrobot.event.EventBus;

public class WalletFragment extends Fragment implements OnClickListener {


    RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private View view;
    private CircleImageView img;
    private TextView username, historical_income, yesterday_income, state, cumulative_gain_tv, integral_tv;
    private RelativeLayout cumulative_gain, my_project, bank_card_management, transaction_records, my_loan,
            my_account, my_monthly_bill, my_message, integral;

    MyAccountActivityBean myAccountActivityBean = new MyAccountActivityBean();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wallet, container, false);
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(this.getActivity()).getRequestQueue();
        mImageLoader = VolleySingleton.getVolleySingleton(getActivity()).getImageLoader();
        // 注册EventBus
        EventBus.getDefault().register(this);
        initview();
        if (MyApplication.getToken() != null) {
            getinformation();
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GETIF");
        }
        EventBus.getDefault().unregister(this);// 反注册EventBus
    }

    public void onEventMainThread(WalletEntity event) {
        if (event.getMsg().equals("login")) {
            getinformation();
            //退出登录后，设置页面数据
        } else if (event.getMsg().equals("exit")) {
            username.setText("稳当宝");
            cumulative_gain_tv.setText("0.00");
            integral_tv.setText("0");
            yesterday_income.setText("0.00");
            state.setVisibility(View.GONE);
            mImageLoader.get("", ImageLoader.getImageListener(img, R.drawable.preson, R.drawable.preson));
        } else {
            getinformation();
        }
    }

    public void onEventMainThread(PhotoAndNameEventBus event) {
        String path = event.getPath();
        String name = event.getName();
        if (path != null) {
            Glide.with(getActivity())
                    .load(path)
                    .centerCrop()
                    .into(img);
        }
        if (name != null) {
            username.setText(name);
        }
    }

    public void onEventMainThread(String event) {
        if (event.equals("刷新")) {
            getinformation();
        } else {

        }

    }


    public void initview() {
        state = (TextView) view.findViewById(R.id.state);
        state.setOnClickListener(this);
        yesterday_income = (TextView) view.findViewById(R.id.yesterday_income);
        cumulative_gain_tv = (TextView) view.findViewById(R.id.cumulative_gain_tv);
        cumulative_gain = (RelativeLayout) view.findViewById(R.id.cumulative_gain);
        cumulative_gain.setOnClickListener(this);
        img = (CircleImageView) view.findViewById(R.id.img);
        img.setOnClickListener(this);
        username = (TextView) view.findViewById(R.id.name);
        my_project = (RelativeLayout) view.findViewById(R.id.my_project);
        my_project.setOnClickListener(this);
        bank_card_management = (RelativeLayout) view.findViewById(R.id.bank_card_management);
        bank_card_management.setOnClickListener(this);
        transaction_records = (RelativeLayout) view.findViewById(R.id.transaction_records);
        transaction_records.setOnClickListener(this);
        my_loan = (RelativeLayout) view.findViewById(R.id.my_loan);
        my_loan.setOnClickListener(this);
        my_account = (RelativeLayout) view.findViewById(R.id.my_account);
        my_account.setOnClickListener(this);
        my_monthly_bill = (RelativeLayout) view.findViewById(R.id.my_monthly_bill);
        my_monthly_bill.setOnClickListener(this);
        my_message = (RelativeLayout) view.findViewById(R.id.my_message);
        my_message.setOnClickListener(this);
        integral_tv = (TextView) view.findViewById(R.id.integral_tv);
        integral = (RelativeLayout) view.findViewById(R.id.integral);
        integral.setOnClickListener(this);
    }

    public void initdata() {
        MyApplication.type = myAccountActivityBean.getType();
        if (myAccountActivityBean.getNames() != null) {
            username.setText(myAccountActivityBean.getNames());
        } else {
            username.setText("稳当宝");
        }
        mImageLoader.get(new CreatUrl().getimg() + myAccountActivityBean.getHeadpath(),
                ImageLoader.getImageListener(img, R.drawable.preson, R.drawable.preson));
        cumulative_gain_tv.setText(ChangeUtils.get_money(Double.parseDouble(myAccountActivityBean.getMoneys())));
        state.setText(ChangeUtils.get_user_state(myAccountActivityBean.getExamine()) + ">");
        integral_tv.setText(myAccountActivityBean.getIntegral());
        if (myAccountActivityBean.getZprofit() != null) {
            yesterday_income.setText(ChangeUtils.get_money(Double.parseDouble(myAccountActivityBean.getZprofit())));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.state:
                Intent intent = null;
                //实名认证
                if (MyApplication.getToken() != null) {
                    if (myAccountActivityBean.getExamine() != null) {
                        //未通过
                        if (myAccountActivityBean.getExamine().equals("4")) {
                            intent = new Intent();
                            intent.setClass(getActivity(), MemberCertificationActivity.class);
                            startActivity(intent);
                            //审核中
                        } else if (myAccountActivityBean.getExamine().equals("3")) {
                            intent = new Intent();
                            intent.putExtra("name", "审核中...");
                            intent.putExtra("card", "");
                            intent.setClass(getActivity(), RealNameAuthenticationSuccessActivity.class);
                            startActivity(intent);
                            //实名认证1
                        } else if (myAccountActivityBean.getExamine().equals("2")) {
                            startActivity(new Intent(getActivity(), MemberCertificationOneActivity.class));
                            //实名认证通过，显示名字和身份证信息
                        } else if (myAccountActivityBean.getExamine().equals("1")) {
                            intent = new Intent();
                            intent.putExtra("name", myAccountActivityBean.getNames());
                            intent.putExtra("card", myAccountActivityBean.getCard());
                            intent.setClass(getActivity(), RealNameAuthenticationSuccessActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        intent = new Intent();
                        intent.setClass(getActivity(), MemberCertificationActivity.class);
                        startActivity(intent);
                    }
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.cumulative_gain:
                //账户余额
                if (MyApplication.getToken() != null) {
                    intent = new Intent();
                    intent.putExtra("cumulative_gain", cumulative_gain_tv.getText().toString());
                    intent.setClass(getActivity(), CumulativeGainActivity.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.img:
                if (MyApplication.getToken() != null) {
                   startActivity(new Intent(getActivity(), MyAccountActivity.class));
                  //  startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.my_project:
                // 我的投资
                if (MyApplication.getToken() != null) {
                    startActivity(new Intent(getActivity(), MyProjectActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.bank_card_management:
                break;
            case R.id.my_loan:
                if (MyApplication.getToken() != null) {
                    intent = new Intent();
                    intent.setClass(getActivity(), MyLoanActivity.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.integral:
                if (MyApplication.getToken() != null) {
                    intent = new Intent();
                    intent.setClass(getActivity(), MyIntegralActivity.class);
                    intent.putExtra("integral", integral_tv.getText().toString());
                    startActivity(intent);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.transaction_records:
                // 交易记录
                if (MyApplication.getToken() != null) {
                    startActivity(new Intent(getActivity(), TransactionRecordActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            //设置
            case R.id.my_account:
                if (MyApplication.getToken() != null) {
                    startActivity(new Intent(getActivity(), MyAccountActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.my_monthly_bill:
                if (MyApplication.getToken() != null) {
                    startActivity(new Intent(getActivity(), MyMonthlyBillActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.my_message:
                if (MyApplication.getToken() != null) {
                    startActivity(new Intent(getActivity(), MyMessageActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            default:
                break;
        }

    }

    //获取用户基本信息
    public void getinformation() {
        JsonObjectRequest getif = new JsonObjectRequest(Method.POST,
                new CreatUrl().creaturl("authorization/info", "getInfo"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String code = response.getString("code");
                    if (new RequestReturnProcessing(getActivity(), code).processing() == 200) {
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
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();

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
        VolleySingleton.getVolleySingleton(getActivity()).addToRequestQueue(getif);
    }

    protected boolean isCreate = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("WalletFragment", "isCreate:" + isCreate);
        Log.d("WalletFragment", "isVisibleToUser:" + isVisibleToUser);

        if (isVisibleToUser && isCreate) {
            //相当于Fragment的onResume
            getinformation();

        } else {
            //相当于Fragment的onPause
        }
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }


}
