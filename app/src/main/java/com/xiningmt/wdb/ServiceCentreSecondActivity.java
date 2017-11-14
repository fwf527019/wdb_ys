package com.xiningmt.wdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import rxh.hb.base.BaseActivity;
import rxh.hb.jsonbean.ServiceCentreSecondActivityBean;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.utils.ShowDialog;
import rxh.hb.volley.util.VolleySingleton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

public class ServiceCentreSecondActivity extends BaseActivity {

    private ImageView mImageView;
    private float mCurrentCheckedRadioLeft;// 当前被选中的RadioButton距离左侧的距离
    private HorizontalScrollView mHorizontalScrollView;// 上面的水平滚动控件
    private ViewPager mViewPager; // 下方的可横向拖动的控件
    private ArrayList<View> mViews;// 用来存放下方滚动的layout(layout_1,layout_2,layout_3)
    LocalActivityManager manager = null;
    private RadioGroup myRadioGroup;
    private int _id = 1000;
    private LinearLayout layout;
    private TextView title;
    private ImageView back, go;

    RequestQueue mRequestQueue;
    List<ServiceCentreSecondActivityBean> tBeans = new ArrayList<ServiceCentreSecondActivityBean>();
    private List<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_service_centre_second);
        manager = new LocalActivityManager(this, true);
        manager.dispatchCreate(savedInstanceState);
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(
                this.getApplicationContext()).getRequestQueue();
        initview();
        gettitle();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("MP");
        }
    }

    public void initview() {
        title = (TextView) findViewById(R.id.title);
        title.setText("服务中心");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        go = (ImageView) findViewById(R.id.go);
        go.setOnClickListener(this);
    }

    private void getTitleInfo() {
        for (int i = 0; i < tBeans.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", tBeans.get(i).getCode());
            map.put("title", tBeans.get(i).getName());
            titleList.add(map);
        }
        initGroup();
        iniListener();
        iniVariable();
        mViewPager.setCurrentItem(0);

    }

    private void initGroup() {
        layout = (LinearLayout) findViewById(R.id.lay);
        mImageView = (ImageView) findViewById(R.id.img1);
        mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        myRadioGroup = new RadioGroup(this);
        myRadioGroup.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        myRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
        layout.addView(myRadioGroup);
        for (int i = 0; i < titleList.size(); i++) {
            Map<String, Object> map = titleList.get(i);
            RadioButton radio = new RadioButton(this);

            radio.setBackgroundResource(R.drawable.radiobtn_selector);
            radio.setButtonDrawable(android.R.color.transparent);
            LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(
                    get() / 4, LayoutParams.MATCH_PARENT, Gravity.CENTER);
            radio.setLayoutParams(l);
            radio.setGravity(Gravity.CENTER);
            radio.setPadding(0,
                    (int) this.getResources().getDimension(R.dimen.jianju), 0,
                    (int) this.getResources().getDimension(R.dimen.jianju));
            // radio.setPadding(left, top, right, bottom)
            radio.setId(_id + i);
            radio.setText(map.get("title") + "");
            radio.setTextColor(Color.BLACK);
            radio.setTag(map);
            if (i == 0) {
                radio.setChecked(true);
                mImageView.setLayoutParams(new LinearLayout.LayoutParams(
                        get() / 4, 4));
            }
            myRadioGroup.addView(radio);
        }
        myRadioGroup
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // Map<String, Object> map = (Map<String, Object>)
                        // group.getChildAt(checkedId).getTag();
                        int radioButtonId = group.getCheckedRadioButtonId();
                        // 根据ID获取RadioButton的实例
                        RadioButton rb = (RadioButton) findViewById(radioButtonId);
                        AnimationSet animationSet = new AnimationSet(true);
                        TranslateAnimation translateAnimation;
                        translateAnimation = new TranslateAnimation(
                                mCurrentCheckedRadioLeft, rb.getLeft(), 0f, 0f);
                        animationSet.addAnimation(translateAnimation);
                        animationSet.setFillBefore(true);
                        animationSet.setFillAfter(true);
                        animationSet.setDuration(300);

                        mImageView.startAnimation(animationSet);// 开始上面蓝色横条图片的动画切换
                        mViewPager.setCurrentItem(radioButtonId - _id);// 让下方ViewPager跟随上面的HorizontalScrollView切换
                        mCurrentCheckedRadioLeft = rb.getLeft();// 更新当前蓝色横条距离左边的距离
                        mHorizontalScrollView.smoothScrollTo(
                                (int) mCurrentCheckedRadioLeft
                                        - (int) getResources().getDimension(
                                        R.dimen.rdo2), 0);

                        mImageView
                                .setLayoutParams(new LinearLayout.LayoutParams(
                                        rb.getRight() - rb.getLeft(), 4));

                    }
                });

    }

    private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }

    private void iniVariable() {
        mViews = new ArrayList<View>();
        for (int i = 0; i < titleList.size(); i++) {
            Intent intent1 = new Intent(this,
                    ServiceCentreSecondNextActivity.class);
            intent1.putExtra("id", titleList.get(i).get("id").toString());
            mViews.add(getView("View" + i, intent1));
        }
        mViewPager.setAdapter(new MyPagerAdapter());// 设置ViewPager的适配器
    }

    private void iniListener() {
        mViewPager.setOnPageChangeListener(new MyPagerOnPageChangeListener());
    }

    /**
     * ViewPager的适配器
     */
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(View v, int position, Object obj) {
            ((ViewPager) v).removeView(mViews.get(position));
        }

        @Override
        public void finishUpdate(View arg0) {

        }

        @Override
        public int getCount() {

            return mViews.size();
        }

        @Override
        public Object instantiateItem(View v, int position) {
            ((ViewPager) v).addView(mViews.get(position));
            return mViews.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {

            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

    }

    /**
     * ViewPager的PageChangeListener(页面改变的监听器)
     */
    private class MyPagerOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        /**
         * 滑动ViewPager的时候,让上方的HorizontalScrollView自动切换
         */
        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) findViewById(_id + position);
            radioButton.performClick();

        }
    }

    public int get() {
        WindowManager wm = getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.go:
                String num = getResources().getString(R.string.phonenum);
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                        + num));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    public void gettitle() {
        loading("加载中", "true");
        JsonObjectRequest mp = new JsonObjectRequest(Method.POST,
                new CreatUrl().creaturl("help", "menu"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismiss();
                        try {
                            String code = response.getString("code");
                            if (new RequestReturnProcessing(
                                    getApplicationContext(), code).processing() == 200) {
                                tBeans = JsonUtils.getservertitle(response
                                        .toString());
                                getTitleInfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismiss();
                Toast.makeText(getApplicationContext(), "网络出错",
                        Toast.LENGTH_LONG).show();
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
        mp.setTag("MP");
        VolleySingleton.getVolleySingleton(getApplicationContext())
                .addToRequestQueue(mp);

    }

}
