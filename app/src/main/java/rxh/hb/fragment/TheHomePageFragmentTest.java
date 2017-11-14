package rxh.hb.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.xiningmt.wdb.ActivityCenterActivity;
import com.xiningmt.wdb.FinancialProjectDetailsActivity;
import com.xiningmt.wdb.LoginActivity;
import com.xiningmt.wdb.MyIntegralActivity;
import com.xiningmt.wdb.NewsBulletinActivity;
import com.xiningmt.wdb.PlatformRepaymentActivity;
import com.xiningmt.wdb.R;
import com.xiningmt.wdb.ServiceCentreSecondActivity;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import rxh.hb.allinterface.TheHomePageFragmentInterface;
import rxh.hb.base.BaseFragment;
import rxh.hb.eventbusentity.BannerEntity;
import rxh.hb.jsonbean.TheHomePageFragmentBean;
import rxh.hb.jsonbean.TheHomePageFragmentimgBean;
import rxh.hb.presenter.TheHomePageFragmentPresenter;
import rxh.hb.utils.ChangeUtils;
import rxh.hb.utils.CreatTime;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.view.CircleProgressBar;
import rxh.hb.view.WaterWaveView;
import rxh.hb.volley.util.VolleySingleton;

public class TheHomePageFragmentTest extends BaseFragment implements TheHomePageFragmentInterface, PopupWindow.OnDismissListener {

    TheHomePageFragmentPresenter theHomePageFragmentPresenter;
    private ScrollView sc;
    private RelativeLayout r_load;
    RequestQueue mRequestQueue;
    private View view, popView;
    private PopupWindow popupWindow;
    private LinearLayout activity_center_ll, news_bulletin_ll, service_centre_ll, sign_ll;
    List<TheHomePageFragmentBean> theHomePageFragmentLVBeans = new ArrayList<TheHomePageFragmentBean>();
    private TextView nianhua, shouyi, qitoujine, qixian, sign_num;
    private Button touzi;
    private ImageView sign, close_sign_iv;
    private FrameLayout home_water_fr;
    String ids;

    private WaterWaveView waterWaveView;

    Double sy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        theHomePageFragmentPresenter = new TheHomePageFragmentPresenter(this, getActivity());
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(this.getActivity().getApplicationContext())
                .getRequestQueue();
        view = inflater.inflate(R.layout.fragment_the_home_page_test, null);
        initview();
        if (isNetworkConnected()) {
            r_load.setClickable(false);
            r_load.setVisibility(View.GONE);
            initdata();
        } else {
            r_load.setClickable(true);
            sc.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GETIF");
            mRequestQueue.cancelAll("GETIMG");
            mRequestQueue.cancelAll("QD");
        }
    }

    public void initview() {
        r_load = (RelativeLayout) view.findViewById(R.id.r_load);
        r_load.setOnClickListener(this);
        sc = (ScrollView) view.findViewById(R.id.sc);
        sc.scrollTo(0, 0);
        activity_center_ll = (LinearLayout) view.findViewById(R.id.activity_center_ll);
        activity_center_ll.setOnClickListener(this);
        news_bulletin_ll = (LinearLayout) view.findViewById(R.id.news_bulletin_ll);
        news_bulletin_ll.setOnClickListener(this);
        sign_ll = (LinearLayout) view.findViewById(R.id.sign_ll);
        sign_ll.setOnClickListener(this);
        service_centre_ll = (LinearLayout) view.findViewById(R.id.service_centre_ll);
        home_water_fr = (FrameLayout) view.findViewById(R.id.home_water_img_fr);
        home_water_fr.setOnClickListener(this);
        service_centre_ll.setOnClickListener(this);
        nianhua = (TextView) view.findViewById(R.id.nianhua);
        qitoujine = (TextView) view.findViewById(R.id.qitoujine);
        shouyi = (TextView) view.findViewById(R.id.shouyi);
        qixian = (TextView) view.findViewById(R.id.qixian);
        touzi = (Button) view.findViewById(R.id.touzi);
        touzi.setOnClickListener(this);
        sign = (ImageView) view.findViewById(R.id.sign);
        waterWaveView = (WaterWaveView) view.findViewById(R.id.waterWaveView);
      waterWaveView.setProgress(0f);
    }

    public void initdata() {
        theHomePageFragmentPresenter.getlvinformation(String.valueOf(1));
        theHomePageFragmentPresenter.getimg();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_center_ll:
                startActivity(new Intent(getActivity(), ActivityCenterActivity.class));
                break;
            case R.id.news_bulletin_ll:
                startActivity(new Intent(getActivity(), NewsBulletinActivity.class));
                break;
            case R.id.home_water_img_fr:
                Intent intent1 = new Intent();
                intent1.putExtra("ids", ids);
                intent1.putExtra("shengyujine", String.valueOf(sy));
                intent1.setClass(getActivity(), FinancialProjectDetailsActivity.class);
                startActivity(intent1);
                break;
            case R.id.sign_ll:
                if (MyApplication.getToken() != null) {
                    sign();
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            //服务中心
            case R.id.service_centre_ll:
                startActivity(new Intent(getActivity(), ServiceCentreSecondActivity.class));
                break;
            case R.id.touzi:
                Intent intent = new Intent();
                intent.putExtra("ids", ids);
                intent.putExtra("shengyujine", String.valueOf(sy));
                intent.setClass(getActivity(), FinancialProjectDetailsActivity.class);
                startActivity(intent);
                break;
            case R.id.close_sign_iv:
                popupWindow.dismiss();
                break;
            case R.id.r_load:
                if (isNetworkConnected()) {
                    sc.setVisibility(View.VISIBLE);
                    initdata();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void getlvinformation(List<TheHomePageFragmentBean>theHomePageFragmentBeans) {
        if (theHomePageFragmentBeans.size() > 0) {
            ids = theHomePageFragmentBeans.get(0).getIds();
            sy = Double.valueOf(theHomePageFragmentBeans.get(0).getMoneys()) - Double.valueOf(theHomePageFragmentBeans.get(0).getPsum());
            nianhua.setText(ChangeUtils.get_money(Double.parseDouble(theHomePageFragmentBeans.get(0).getRate())));
            shouyi.setText(ChangeUtils.get_repayment_method(theHomePageFragmentBeans.get(0).getPaymenttype()));
            qitoujine.setText(theHomePageFragmentBeans.get(0).getMinimum() + "元起投");
            if(!theHomePageFragmentBeans.get(0).getStage().equals("5")){
                //TODO:图片投资能否的判断
            //    home_water_fr.setClickable(false);
                touzi.setBackgroundResource(R.drawable.fragment_the_home_page_text_btn_bg_gry);
                touzi.setClickable(false);
            }else {
                touzi.setBackgroundResource(R.drawable.fragment_the_home_page_text_btn_bg);
                touzi.setClickable(true);
       //         home_water_fr.setClickable(true);
            }
            if (theHomePageFragmentBeans.get(0).getLinetype() != null) {
                if (theHomePageFragmentBeans.get(0).getLinetype().equals("2")) {
                    qixian.setText(theHomePageFragmentBeans.get(0).getDeadline() + "个月  ");
                } else if (theHomePageFragmentBeans.get(0).getLinetype().equals("1")) {
                    qixian.setText(theHomePageFragmentBeans.get(0).getDeadline() + "天  ");
                }
            }
          waterWaveView.setProgress(Float.parseFloat(theHomePageFragmentBeans.get(0).getBaifeibi()));
          waterWaveView.startWave();
        }
//        touzi.setBackgroundResource(R.drawable.fragment_the_home_page_text_btn_bg_gry);
//        touzi.setClickable(false);
    }

    @Override
    public void error() {
    }

    @Override
    public void imgerror() {

    }

    @Override
    public void getimgurl(List<TheHomePageFragmentimgBean> theHomePageFragmentBeans) {
        EventBus.getDefault().post(new BannerEntity(theHomePageFragmentBeans));

    }

    /**
     * 初始化popupWindow
     */
    private void initPopupWindow(String signnum) {
        popView = getActivity().getLayoutInflater().inflate(R.layout.fragment_the_home_page_test_sign_pop_windon,
                null);
        sign_num = (TextView) popView.findViewById(R.id.sign_num);
        close_sign_iv = (ImageView) popView.findViewById(R.id.close_sign_iv);
        sign_num.setText(signnum);
        close_sign_iv.setOnClickListener(this);
        popupWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(this);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(lp);
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */

    @Override
    public void onDismiss() {
        backgroundAlpha(1f);

    }

    //签到
    public void sign() {//new CreatUrl().creaturl("authorization/sign", "signIndex")
        JsonObjectRequest qiandao = new JsonObjectRequest(Request.Method.POST,
                new CreatUrl().creaturl("authorization/sign", "signIndex"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String code = response.getString("code");
                    if (new RequestReturnProcessing(getActivity(), code).processing() == 200) {
                        //profitEntity = JsonUtils.getprofit(response.toString());
                        String integralSign = response.getString("obj");
                        initPopupWindow(integralSign);
                        backgroundAlpha(0.5f);
                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                        EventBus.getDefault().post("刷新");
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
        qiandao.setTag("QD");
        VolleySingleton.getVolleySingleton(getActivity()).addToRequestQueue(qiandao);
    }

}
