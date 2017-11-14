package com.xiningmt.wdb;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.base.BaseActivity;
import rxh.hb.fragment.AssetDescriptionFragment;
import rxh.hb.fragment.FunctionIntroductionFragment;
import rxh.hb.fragment.ProductIntroductionFragment;
import rxh.hb.fragment.SecurityFeatureFragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class ServiceCentreActivity extends BaseActivity {

    private ImageView back, go;
    private TextView title;

    private ViewPager viewPager;// 页卡内容
    private ImageView imageView;// 动画图片
    private TextView assetdescription, functionintroduction,
            productintroduction, securityfeature;// 选项名称
    private List<Fragment> fragments;// Tab页面列表
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private int selectedColor, unSelectedColor;
    /**
     * 页卡总数
     **/
    private static final int pageSize = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_service_centre);
        initview();
        initdata();
    }

    public void initview() {
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        go = (ImageView) findViewById(R.id.go);
        go.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        selectedColor = getResources().getColor(R.color.base_top);
        unSelectedColor = getResources().getColor(R.color.textcolor);

        InitImageView();
        InitTextView();
        InitViewPager();
    }

    /**
     * 初始化Viewpager页
     */
    private void InitViewPager() {
        viewPager = (ViewPager) findViewById(R.id.vPager);
        fragments = new ArrayList<Fragment>();
        fragments.add(new AssetDescriptionFragment());
        fragments.add(new FunctionIntroductionFragment());
        fragments.add(new ProductIntroductionFragment());
        fragments.add(new SecurityFeatureFragment());
        viewPager.setAdapter(new FragAdapter(getSupportFragmentManager(),
                fragments));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 初始化头标
     */
    private void InitTextView() {
        assetdescription = (TextView) findViewById(R.id.assetdescription);
        functionintroduction = (TextView) findViewById(R.id.functionintroduction);
        productintroduction = (TextView) findViewById(R.id.productintroduction);
        securityfeature = (TextView) findViewById(R.id.securityfeature);

        assetdescription.setTextColor(selectedColor);
        functionintroduction.setTextColor(unSelectedColor);
        productintroduction.setTextColor(unSelectedColor);
        securityfeature.setTextColor(unSelectedColor);

        assetdescription.setText("资产说明");
        functionintroduction.setText("功能介绍");
        productintroduction.setText("产品介绍");
        securityfeature.setText("安全特性");

        assetdescription.setOnClickListener(new MyOnClickListener(0));
        functionintroduction.setOnClickListener(new MyOnClickListener(1));
        productintroduction.setOnClickListener(new MyOnClickListener(2));
        securityfeature.setOnClickListener(new MyOnClickListener(3));
    }

    /**
     * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据
     */

    private void InitImageView() {
        imageView = (ImageView) findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(),
                R.drawable.tab_selected_bg).getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / pageSize - bmpW) / 2;// 计算偏移量--(屏幕宽度/页卡总数-图片实际宽度)/2
        // = 偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        imageView.setImageMatrix(matrix);// 设置动画初始位置
    }

    /**
     * 头标点击监听
     */
    private class MyOnClickListener implements OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {

            switch (index) {
                case 0:
                    assetdescription.setTextColor(selectedColor);
                    functionintroduction.setTextColor(unSelectedColor);
                    productintroduction.setTextColor(unSelectedColor);
                    securityfeature.setTextColor(unSelectedColor);
                    break;
                case 1:
                    assetdescription.setTextColor(unSelectedColor);
                    functionintroduction.setTextColor(selectedColor);
                    productintroduction.setTextColor(unSelectedColor);
                    securityfeature.setTextColor(unSelectedColor);
                    break;
                case 2:
                    assetdescription.setTextColor(unSelectedColor);
                    functionintroduction.setTextColor(unSelectedColor);
                    productintroduction.setTextColor(selectedColor);
                    securityfeature.setTextColor(unSelectedColor);
                    break;
                case 3:
                    assetdescription.setTextColor(unSelectedColor);
                    functionintroduction.setTextColor(unSelectedColor);
                    productintroduction.setTextColor(unSelectedColor);
                    securityfeature.setTextColor(selectedColor);
                    break;
            }
            viewPager.setCurrentItem(index);
        }

    }

    /**
     * 为选项卡绑定监听器
     */
    public class MyOnPageChangeListener implements OnPageChangeListener {

        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量

        // int two = one * 2;// 页卡1 -> 页卡3 偏移量

        public void onPageScrollStateChanged(int index) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int index) {
            Animation animation = new TranslateAnimation(one * currIndex, one
                    * index, 0, 0);// 显然这个比较简洁，只有一行代码。
            currIndex = index;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            imageView.startAnimation(animation);
            switch (index) {
                case 0:
                    assetdescription.setTextColor(selectedColor);
                    functionintroduction.setTextColor(unSelectedColor);
                    productintroduction.setTextColor(unSelectedColor);
                    securityfeature.setTextColor(unSelectedColor);
                    break;
                case 1:
                    assetdescription.setTextColor(unSelectedColor);
                    functionintroduction.setTextColor(selectedColor);
                    productintroduction.setTextColor(unSelectedColor);
                    securityfeature.setTextColor(unSelectedColor);
                    break;
                case 2:
                    assetdescription.setTextColor(unSelectedColor);
                    functionintroduction.setTextColor(unSelectedColor);
                    productintroduction.setTextColor(selectedColor);
                    securityfeature.setTextColor(unSelectedColor);
                    break;
                case 3:
                    assetdescription.setTextColor(unSelectedColor);
                    functionintroduction.setTextColor(unSelectedColor);
                    productintroduction.setTextColor(unSelectedColor);
                    securityfeature.setTextColor(selectedColor);
                    break;
            }
        }
    }

    /**
     * 定义适配器
     */
    // class myPagerAdapter extends FragmentPagerAdapter {
    // private List<Fragment> fragmentList;
    //
    // public myPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
    // super(fm);
    // this.fragmentList = fragmentList;
    // }
    //
    // /**
    // * 得到每个页面
    // */
    // @Override
    // public Fragment getItem(int arg0) {
    // return (fragmentList == null || fragmentList.size() == 0) ? null
    // : fragmentList.get(arg0);
    // }
    //
    // /**
    // * 每个页面的title
    // */
    // @Override
    // public CharSequence getPageTitle(int position) {
    // return null;
    // }
    //
    // /**
    // * 页面的总个数
    // */
    // @Override
    // public int getCount() {
    // return fragmentList == null ? 0 : fragmentList.size();
    // }
    // }
    public class FragAdapter extends FragmentPagerAdapter {
        public FragmentManager fm;
        public List<Fragment> list = new ArrayList<Fragment>();

        public FragAdapter(FragmentManager fm) {
            super(fm);
        }

        public FragAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fm = fm;
            this.list = fragments;
        }

        @Override
        public Fragment getItem(int arg0) {
            Fragment fragment = null;
            fragment = list.get(arg0);
            Bundle bundle = new Bundle();
            bundle.putString("id", "" + arg0);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container,
                    position);
            fm.beginTransaction().show(fragment).commit();
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            Fragment fragment = list.get(position);
            fm.beginTransaction().hide(fragment).commit();
        }

    }

    public void initdata() {
        title.setText("服务中心");
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
                    // TODO: Consider calling
                    startActivity(intent);
                    return;
                }
                break;
            default:
                break;
        }

    }

}
