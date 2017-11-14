package rxh.hb.fragment;

import java.util.List;

import rxh.hb.adapter.ServiceCentreExpendListViewAdapter;
import rxh.hb.allinterface.ServiceCentreInterface;
import rxh.hb.jsonbean.ServiceCentreParentBean;
import rxh.hb.presenter.FunctionIntroductionPresenter;
import rxh.hb.presenter.SecurityFeaturePresenter;

import com.xiningmt.wdb.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class FunctionIntroductionFragment extends Fragment implements
		ServiceCentreInterface {

	private View view;
	private ExpandableListView lv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_function_introduction, null);
		initview();
		initdata();
		return view;
	}

	public void initview() {
		lv = (ExpandableListView) view.findViewById(R.id.expandablelv);
		lv.setGroupIndicator(null);
	}

	public void initdata() {
		new FunctionIntroductionPresenter(getActivity(), this)
				.getlvinformation("");
	}

	@Override
	public void getinformation(
			List<ServiceCentreParentBean> serviceCentreParentBeans) {
		lv.setAdapter(new ServiceCentreExpendListViewAdapter(
				serviceCentreParentBeans, getActivity()));

	}

	@Override
	public void error() {
		// TODO Auto-generated method stub

	}

}
