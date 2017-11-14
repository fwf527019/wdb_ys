package rxh.hb.fragment;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.adapter.MyProjectActivityLVAdapter;
import rxh.hb.adapter.TransactionRecordsActivityLVAdapter;
import rxh.hb.allinterface.MyProjectActivityInterface;
import rxh.hb.allinterface.TransactionRecordsActivityInterface;
import rxh.hb.jsonbean.InvestmentFragmentActivityLVBean;
import rxh.hb.jsonbean.TransactionRecordsActivityLVBean;
import rxh.hb.jsonbean.TransactionRecordsActivityLVBean2;
import rxh.hb.jsonbean.TransferThePossessionOfFragmentLVBean;
import rxh.hb.presenter.MyProjectActivityPresenter;
import rxh.hb.presenter.TransactionRecordsActivityPresenter;
import rxh.hb.utils.ShowDialog;
import rxh.hb.volley.util.VolleySingleton;

import com.android.volley.RequestQueue;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.xiningmt.wdb.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

/***
 *
 * 投资中
 */
public class InvestmentingFragment extends Fragment implements
        TransactionRecordsActivityInterface {

    int page = 1;
    RequestQueue mRequestQueue;
    private View view;
    private PullToRefreshListView lv;
    TransactionRecordsActivityLVAdapter adapter;
    List<TransactionRecordsActivityLVBean2> LVBeans = new ArrayList<TransactionRecordsActivityLVBean2>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transfer, container, false);
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(this.getActivity())
                .getRequestQueue();
        initview();
        initdata();
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GETIF");
        }
    }

    public void initview() {
        lv = (PullToRefreshListView) view.findViewById(R.id.lv);
        lv.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(),
                        System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                page = 1;
                // 这里写下拉刷新的任务
                new TransactionRecordsActivityPresenter(getActivity(),
                        InvestmentingFragment.this).getlvinformation(
                        String.valueOf(page), "1");

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // 这里写上拉加载更多的任务
                new TransactionRecordsActivityPresenter(getActivity(),
                        InvestmentingFragment.this).getlvinformation(
                        String.valueOf(page), "1");

            }
        });

    }

    /**
     * 获取投资中的数据
     */
    public void initdata() {
        new TransactionRecordsActivityPresenter(getActivity(),
                InvestmentingFragment.this).getlvinformation(String.valueOf(page),
                "1");

    }

    @Override
    public void getinformation(
            List<TransactionRecordsActivityLVBean2> transactionRecordsActivityLVBeans) {
        if (page == 1) {
            LVBeans.clear();
            LVBeans.addAll(transactionRecordsActivityLVBeans);
            adapter = new TransactionRecordsActivityLVAdapter(LVBeans,
                    getActivity());
            lv.setAdapter(adapter);
        } else {
            LVBeans.addAll(transactionRecordsActivityLVBeans);
            adapter.notifyDataSetChanged();
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
