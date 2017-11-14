package rxh.hb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xiningmt.wdb.FinancialProjectDetailsActivity;
import com.xiningmt.wdb.R;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.adapter.InvestmentFragmentActivityLVAdapter;
import rxh.hb.allinterface.InvestmentFragmentActivityInterface;
import rxh.hb.jsonbean.InvestmentFragmentActivityLVBean;
import rxh.hb.presenter.InvestmentFragmentActivityPresenter;
import rxh.hb.volley.util.VolleySingleton;

/**
 * Created by Administrator on 2016/10/9.
 */
public class ConductFinancialTransactionsPageFragment extends Fragment implements InvestmentFragmentActivityInterface {

    View view;
    String ids;
    Double sy;
    int page = 1;
    RequestQueue mRequestQueue;
    private PullToRefreshListView lv;
    InvestmentFragmentActivityLVAdapter ia;
    List<InvestmentFragmentActivityLVBean> lvBeans = new ArrayList<InvestmentFragmentActivityLVBean>();

    public static ConductFinancialTransactionsPageFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        ConductFinancialTransactionsPageFragment pageFragment = new ConductFinancialTransactionsPageFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_investment_fragment, null);
        ids = getArguments().getString("id");
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(getActivity().getApplicationContext()).getRequestQueue();
        initview();
        page = 1;
        initdata();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GETIF");
        }
    }

    public void initview() {
        lv = (PullToRefreshListView) view.findViewById(R.id.lv);
        lv.setEmptyView(view.findViewById(R.id.img));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("ids", lvBeans.get(position).getIds());
                sy = Double.valueOf(lvBeans.get(position).getMoneys()) - Double.valueOf(lvBeans.get(position).getPsum());
                intent.putExtra("shengyujine", String.valueOf(sy));
                intent.setClass(getActivity(), FinancialProjectDetailsActivity.class);
                startActivity(intent);

            }
        });
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                page = 1;
                // 这里写下拉刷新的任务
                new InvestmentFragmentActivityPresenter(ConductFinancialTransactionsPageFragment.this,
                        getActivity()).getlvinformation(String.valueOf(page), ids);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 这里写上拉加载更多的任务
                new InvestmentFragmentActivityPresenter(ConductFinancialTransactionsPageFragment.this,
                        getActivity()).getlvinformation(String.valueOf(page), ids);

            }
        });
    }

    public void initdata() {
        new InvestmentFragmentActivityPresenter(this, getActivity())
                .getlvinformation(String.valueOf(page), ids);
    }

    @Override
    public void getlvinformation(List<InvestmentFragmentActivityLVBean> investmentFragmentActivityLVBeans) {
        if (page == 1) {
            lvBeans.clear();
            lvBeans.addAll(investmentFragmentActivityLVBeans);
            ia = new InvestmentFragmentActivityLVAdapter(lvBeans, getActivity());
            lv.setAdapter(ia);
        } else {
            lvBeans.addAll(investmentFragmentActivityLVBeans);
            ia.notifyDataSetChanged();
        }
        lv.onRefreshComplete();
        lv.onRefreshComplete();
        page++;

    }

    @Override
    public void error() {
        lv.onRefreshComplete();
        lv.onRefreshComplete();

    }
}
