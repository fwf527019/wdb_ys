package rxh.hb.fragment;

import java.util.List;
import rxh.hb.adapter.TransferThePossessionOfFragmentLVAdapter;
import rxh.hb.allinterface.TransferThePossessionOfFragmentInterface;
import rxh.hb.jsonbean.TransferThePossessionOfFragmentLVBean;
import rxh.hb.presenter.TransferThePossessionOfFragmentPresenter;

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

public class TransferThePossessionOfFragment extends Fragment implements
		TransferThePossessionOfFragmentInterface {

	private View view;
	private PullToRefreshListView lv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_transfer_the_possession_of,
				container, false);
//		initview();
//		initdata();
		return view;
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
				// 这里写下拉刷新的任务
				Toast.makeText(getActivity(), "下拉刷新", Toast.LENGTH_LONG).show();
				lv.onRefreshComplete();

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 这里写上拉加载更多的任务
				Toast.makeText(getActivity(), "上拉加载", Toast.LENGTH_LONG).show();
				lv.onRefreshComplete();

			}
		});

	}

	public void initdata() {
		new TransferThePossessionOfFragmentPresenter(this)
				.getlvinformation("2");
	}

	@Override
	public void getlvinformation(
			List<TransferThePossessionOfFragmentLVBean> thePossessionOfFragmentLVBeans) {
		TransferThePossessionOfFragmentLVAdapter lvAdapter = new TransferThePossessionOfFragmentLVAdapter(
				thePossessionOfFragmentLVBeans, getActivity());
		lv.setAdapter(lvAdapter);

	}

}
