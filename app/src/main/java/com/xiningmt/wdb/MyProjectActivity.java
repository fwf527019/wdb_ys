package com.xiningmt.wdb;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.base.BaseFragmentActivity;
import rxh.hb.fragment.InvestmentingFragment;
import rxh.hb.fragment.IsOverFragment;
import rxh.hb.fragment.TransferableFragment;
import rxh.hb.utils.ShowDialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyProjectActivity extends BaseFragmentActivity {

    private ImageView back;
    private TextView title;
    private TextView investmenting, is_over;
    List<Fragment> fragments = new ArrayList<Fragment>();
    ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_project);
        initview();
        showmenubg(1);

    }

    public void initview() {
        title = (TextView) findViewById(R.id.title);
        title.setText("我的投资");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        vp = (ViewPager) findViewById(R.id.viewpager);
        fragments.add(new InvestmentingFragment());
        //fragments.add(new TransferableFragment());
        fragments.add(new IsOverFragment());
        FragAdapter adapter = new FragAdapter(getSupportFragmentManager(), fragments);
        // 设定适配器
        vp.setAdapter(adapter);
        vp.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                showmenubg(arg0 + 1);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        investmenting = (TextView) findViewById(R.id.investmenting);
        investmenting.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showmenubg(1);
                vp.setCurrentItem(0);

            }
        });

        is_over = (TextView) findViewById(R.id.is_over);
        is_over.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showmenubg(3);
                vp.setCurrentItem(2);

            }
        });
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public void showmenubg(int i) {
        if (i == 1) {
            investmenting.setBackground(getResources().getDrawable(R.drawable.activity_my_project_leftbutton_down));
            investmenting.setTextColor(this.getResources().getColor(R.color.white));
//            transferable.setBackgroundColor(this.getResources().getColor(R.color.white));
//            transferable.setTextColor(this.getResources().getColor(R.color.base_top));
            is_over.setBackground(getResources().getDrawable(R.drawable.activity_my_project_rightbutton_up));
            is_over.setTextColor(this.getResources().getColor(R.color.base_top));
        } else if (i == 2) {
            investmenting.setBackground(getResources().getDrawable(R.drawable.activity_my_project_leftbutton_up));
            investmenting.setTextColor(this.getResources().getColor(R.color.base_top));
//            transferable.setBackgroundColor(this.getResources().getColor(R.color.white));
//            transferable.setTextColor(this.getResources().getColor(R.color.base_top));
            is_over.setBackground(getResources().getDrawable(R.drawable.activity_my_project_rightbutton_down));
            is_over.setTextColor(this.getResources().getColor(R.color.white));
        } else if (i == 3) {
            investmenting.setBackground(getResources().getDrawable(R.drawable.activity_my_project_leftbutton_up));
            investmenting.setTextColor(this.getResources().getColor(R.color.base_top));
//            transferable.setBackgroundColor(this.getResources().getColor(R.color.white));
//            transferable.setTextColor(this.getResources().getColor(R.color.base_top));
            is_over.setBackground(getResources().getDrawable(R.drawable.activity_my_project_rightbutton_down));
            is_over.setTextColor(this.getResources().getColor(R.color.white));
        }
    }

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
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            fm.beginTransaction().show(fragment).commit();
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            Fragment fragment = list.get(position);
            fm.beginTransaction().hide(fragment).commit();
        }

    }

}
