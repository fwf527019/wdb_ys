package rxh.hb.fragment;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.adapter.AssetDescriptionFragmentHeaderViewAdapter;
import rxh.hb.adapter.ServiceCentreExpendListViewAdapter;
import rxh.hb.allinterface.ServiceCentreInterface;
import rxh.hb.jsonbean.ServiceCentreParentBean;
import rxh.hb.presenter.SecurityFeaturePresenter;
import rxh.hb.view.MyGridView;

import com.xiningmt.wdb.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class AssetDescriptionFragment extends Fragment implements
		ServiceCentreInterface {

	private View view, headerview;
	private MyGridView gv;
	private ExpandableListView lv;
	private LayoutInflater mInflater = null;
	List<Integer> url=new ArrayList<Integer>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_asset_description, null);
		mInflater = LayoutInflater.from(getActivity());
		initview();
		initdata();
		return view;
	}

	public void initview() {
		lv = (ExpandableListView) view.findViewById(R.id.expandablelv);
		headerview = mInflater.inflate(
				R.layout.fragment_asset_description_header_view, null);
		lv.addHeaderView(headerview);
		gv=(MyGridView) headerview.findViewById(R.id.gv);
		lv.setGroupIndicator(null);
	}

	public void initdata() {
		url.add(R.drawable.one);
		url.add(R.drawable.two);
		url.add(R.drawable.three);
		//url.add(R.drawable.foue);
		gv.setAdapter(new AssetDescriptionFragmentHeaderViewAdapter(url, getActivity()));
		new SecurityFeaturePresenter(getActivity(), this).getlvinformation("");
		
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
