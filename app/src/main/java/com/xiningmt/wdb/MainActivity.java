package com.xiningmt.wdb;

import com.umeng.update.UmengUpdateAgent;

import rxh.hb.fragment.ConductFinancialTransactionsFragment;
import rxh.hb.fragment.InvestmentFragment;
import rxh.hb.fragment.LoanFragment;
import rxh.hb.fragment.TheHomePageFragmentTest;
import rxh.hb.fragment.WalletFragment;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.ShowDialog;
import rxh.hb.utils.StatusBarUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

    private LinearLayout the_home_page, investment, loan, wallet;
    private ImageView the_home_page_img, investment_img, loan_img, wallet_img;
    private TextView the_home_page_title, investment_title, loan_title, wallet_title;
    private long mExitTime;
    private SharedPreferences sp;
    // 后续开发加上的
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private TheHomePageFragmentTest thpf;
    private ConductFinancialTransactionsFragment inf;
    private LoanFragment lof;
    private WalletFragment waf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("user", 0);
        MyApplication.setToken(sp.getString("token", null));
        setContentView(R.layout.activity_main);
        UmengUpdateAgent.update(this);
        initview();
        setbg(0);
        selectFragment(0);
    }

    public void selectFragment(int i) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        switch (i) {
            case 0:// 稳当宝
                if (thpf == null) {
                    thpf = new TheHomePageFragmentTest();
                    fragmentTransaction.add(R.id.fragment, thpf);
                } else {
                    fragmentTransaction.show(thpf);
                }
                break;

            case 1:// 投资
                if (inf == null) {
                    inf = new ConductFinancialTransactionsFragment();
                    fragmentTransaction.add(R.id.fragment, inf);
                } else {
                    fragmentTransaction.show(inf);
                }
                break;

            case 2:// 借款
                if (lof == null) {
                    lof = new LoanFragment();
                    fragmentTransaction.add(R.id.fragment, lof);
                } else {
                    fragmentTransaction.show(lof);
                }
                break;

            case 3:// 我的
                if (waf == null) {
                    waf = new WalletFragment();
                    fragmentTransaction.add(R.id.fragment, waf);
                } else {
                    fragmentTransaction.show(waf);
                }
                break;
        }
        fragmentTransaction.commitAllowingStateLoss();

    }

    /**
     * 隐藏所有Fragment
     */
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (thpf != null) {
            fragmentTransaction.hide(thpf);
        }
        if (inf != null) {
            fragmentTransaction.hide(inf);
        }
        if (lof != null) {
            fragmentTransaction.hide(lof);
        }
        if (waf != null) {
            fragmentTransaction.hide(waf);
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public void setbg(int i) {
        if (i == 0) {
            the_home_page_title.setTextColor(this.getResources().getColor(R.color.is_selected));
            investment_title.setTextColor(this.getResources().getColor(R.color.not_selected));
            loan_title.setTextColor(this.getResources().getColor(R.color.not_selected));
            wallet_title.setTextColor(this.getResources().getColor(R.color.not_selected));
            the_home_page_img.setBackground(getResources().getDrawable(R.drawable.logo_down));
            investment_img.setBackground(getResources().getDrawable(R.drawable.touzi_up));
            loan_img.setBackground(getResources().getDrawable(R.drawable.jiekuan_up));
            wallet_img.setBackground(getResources().getDrawable(R.drawable.qianbao_up));
        } else if (i == 1) {
            the_home_page_title.setTextColor(this.getResources().getColor(R.color.not_selected));
            investment_title.setTextColor(this.getResources().getColor(R.color.is_selected));
            loan_title.setTextColor(this.getResources().getColor(R.color.not_selected));
            wallet_title.setTextColor(this.getResources().getColor(R.color.not_selected));
            the_home_page_img.setBackground(getResources().getDrawable(R.drawable.logo_up));
            investment_img.setBackground(getResources().getDrawable(R.drawable.touzi_down));
            loan_img.setBackground(getResources().getDrawable(R.drawable.jiekuan_up));
            wallet_img.setBackground(getResources().getDrawable(R.drawable.qianbao_up));
        } else if (i == 2) {
            the_home_page_title.setTextColor(this.getResources().getColor(R.color.not_selected));
            investment_title.setTextColor(this.getResources().getColor(R.color.not_selected));
            loan_title.setTextColor(this.getResources().getColor(R.color.is_selected));
            wallet_title.setTextColor(this.getResources().getColor(R.color.not_selected));
            the_home_page_img.setBackground(getResources().getDrawable(R.drawable.logo_up));
            investment_img.setBackground(getResources().getDrawable(R.drawable.touzi_up));
            loan_img.setBackground(getResources().getDrawable(R.drawable.jiekuan_down));
            wallet_img.setBackground(getResources().getDrawable(R.drawable.qianbao_up));
        } else if (i == 3) {
            the_home_page_title.setTextColor(this.getResources().getColor(R.color.not_selected));
            investment_title.setTextColor(this.getResources().getColor(R.color.not_selected));
            loan_title.setTextColor(this.getResources().getColor(R.color.not_selected));
            wallet_title.setTextColor(this.getResources().getColor(R.color.is_selected));
            the_home_page_img.setBackground(getResources().getDrawable(R.drawable.logo_up));
            investment_img.setBackground(getResources().getDrawable(R.drawable.touzi_up));
            loan_img.setBackground(getResources().getDrawable(R.drawable.jiekuan_up));
            wallet_img.setBackground(getResources().getDrawable(R.drawable.qianbao_down));
        }
    }

    public void initview() {
        the_home_page_img = (ImageView) findViewById(R.id.the_home_page_img);
        investment_img = (ImageView) findViewById(R.id.investment_img);
        loan_img = (ImageView) findViewById(R.id.loan_img);
        wallet_img = (ImageView) findViewById(R.id.wallet_img);
        the_home_page_title = (TextView) findViewById(R.id.the_home_page_title);
        investment_title = (TextView) findViewById(R.id.investment_title);
        loan_title = (TextView) findViewById(R.id.loan_title);
        wallet_title = (TextView) findViewById(R.id.wallet_title);
        the_home_page = (LinearLayout) findViewById(R.id.the_home_page);
        the_home_page.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setbg(0);
                selectFragment(0);
            }
        });
        investment = (LinearLayout) findViewById(R.id.investment);
        investment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setbg(1);
                selectFragment(1);

            }
        });
        loan = (LinearLayout) findViewById(R.id.loan);
        loan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setbg(2);
                selectFragment(2);

            }
        });
        wallet = (LinearLayout) findViewById(R.id.wallet);
        wallet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setbg(3);
                selectFragment(3);
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
