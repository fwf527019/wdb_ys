package com.xiningmt.wdb;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rxh.hb.base.BaseActivity;
import rxh.hb.fragment.LoanApplicationFragment;
import rxh.hb.fragment.MyLoanFrament;

/**
 * Created by Administrator on 2016/10/11.
 */
public class MyLoanActivity extends BaseActivity {


    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.my_loan)
    TextView my_loan;
    @Bind(R.id.loan_application)
    TextView loan_application;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    List<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initview();
    }

    public void initview() {
        setContentView(R.layout.activity_my_loan);
        ButterKnife.bind(this);
        title.setText("我的借款");
        showmenubg(1);
        back.setOnClickListener(this);
        my_loan.setOnClickListener(this);
        loan_application.setOnClickListener(this);
        fragments.add(new MyLoanFrament());

        fragments.add(new LoanApplicationFragment());
        FragAdapter adapter = new FragAdapter(getSupportFragmentManager(), fragments);
        // 设定适配器
        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.my_loan:
                showmenubg(1);
                viewpager.setCurrentItem(0);
                break;
            case R.id.loan_application:
                showmenubg(2);
                viewpager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }

    @SuppressLint("NewApi")
    public void showmenubg(int i) {
        if (i == 1) {
            my_loan.setBackground(getResources().getDrawable(R.drawable.activity_my_project_leftbutton_down));
            my_loan.setTextColor(this.getResources().getColor(R.color.white));
            loan_application.setBackground(getResources().getDrawable(R.drawable.activity_my_project_rightbutton_up));
            loan_application.setTextColor(this.getResources().getColor(R.color.base_top));
        } else if (i == 2) {
            my_loan.setBackground(getResources().getDrawable(R.drawable.activity_my_project_leftbutton_up));
            my_loan.setTextColor(this.getResources().getColor(R.color.base_top));
            loan_application.setBackground(getResources().getDrawable(R.drawable.activity_my_project_rightbutton_down));
            loan_application.setTextColor(this.getResources().getColor(R.color.white));

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
