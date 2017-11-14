package rxh.hb.fragment;

import rxh.hb.base.BaseFragment;

import com.xiningmt.wdb.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

//已弃用

public class InvestmentFragment extends BaseFragment {

    // 注意，在一个fragment中嵌套另外的fragment，程序很容易出现问题。这种操作需要谨慎
    private View view;
    public static Fragment[] mFragments;
    private TextView conduct_financial_transactions, transfer_the_possession_of;
    private RelativeLayout load;
    private LinearLayout fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_investment, container, false);
        initview();
        if (isNetworkConnected()) {
            setFragmentIndicator(0);
            buttonbackground(1);
            load.setVisibility(View.GONE);
        } else {
            fragment.setVisibility(View.GONE);
        }
        return view;
    }

    public void initview() {
        load = (RelativeLayout) view.findViewById(R.id.load);
        load.setOnClickListener(this);
        fragment = (LinearLayout) view.findViewById(R.id.fragment);
        conduct_financial_transactions = (TextView) view.findViewById(R.id.conduct_financial_transactions);
        conduct_financial_transactions.setOnClickListener(this);
        transfer_the_possession_of = (TextView) view.findViewById(R.id.transfer_the_possession_of);
        transfer_the_possession_of.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.conduct_financial_transactions:
                setFragmentIndicator(0);
                buttonbackground(1);
                break;
            case R.id.transfer_the_possession_of:
                setFragmentIndicator(1);
                buttonbackground(2);
                break;
            case R.id.load:
                if (isNetworkConnected()) {
                    setFragmentIndicator(0);
                    initview();
                    buttonbackground(1);
                    load.setVisibility(View.GONE);
                    fragment.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }

    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public void buttonbackground(int i) {
        if (i == 1) {
            conduct_financial_transactions
                    .setBackground(getResources().getDrawable(R.drawable.fragment_investment_leftbutton_down));
            conduct_financial_transactions.setTextColor(this.getResources().getColor(R.color.base_top));
            transfer_the_possession_of
                    .setBackground(getResources().getDrawable(R.drawable.fragment_investment_rightbutton_up));
            transfer_the_possession_of.setTextColor(this.getResources().getColor(R.color.white));
        } else if (i == 2) {
            conduct_financial_transactions
                    .setBackground(getResources().getDrawable(R.drawable.fragment_investment_leftbutton_up));
            conduct_financial_transactions.setTextColor(this.getResources().getColor(R.color.white));
            transfer_the_possession_of
                    .setBackground(getResources().getDrawable(R.drawable.fragment_investment_rightbutton_down));
            transfer_the_possession_of.setTextColor(this.getResources().getColor(R.color.base_top));
        }
    }

    /**
     * 初始化fragment
     */
    private void setFragmentIndicator(int whichIsDefault) {
        mFragments = new Fragment[2];
        mFragments[0] = getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.conduct_financial_transactions_fragment);
        mFragments[1] = getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.transfer_the_possession_of_fragment);
        getActivity().getSupportFragmentManager().beginTransaction().hide(mFragments[0]).hide(mFragments[1])
                .show(mFragments[whichIsDefault]).commit();

    }

}
