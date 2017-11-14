package rxh.hb.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.fragment.ConductFinancialTransactionsPageFragment;
import rxh.hb.jsonbean.ConductFinancialTransactionsFragmentBean;

/**
 * Created by Administrator on 2016/10/9.
 */
public class ConductFinancialTransactionsPageAdapter extends FragmentPagerAdapter {

    List<String> titles = new ArrayList<>();

    public ConductFinancialTransactionsPageAdapter(FragmentManager fm) {
        super(fm);
        titles.add("担保标");
        titles.add("质押标");
        titles.add("信用标");
    }

    @Override
    public Fragment getItem(int position) {
        return ConductFinancialTransactionsPageFragment.newInstance(String.valueOf(3-position));
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position % titles.size());
    }

}
