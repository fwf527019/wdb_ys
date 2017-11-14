package rxh.hb.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xiningmt.wdb.R;
import rxh.hb.jsonbean.ServiceCentreParentBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ServiceCentreExpendListViewAdapter extends
		BaseExpandableListAdapter {

	Context context;
	private LayoutInflater mInflater = null;
	List<ServiceCentreParentBean> lvBeans = new ArrayList<ServiceCentreParentBean>();

	public ServiceCentreExpendListViewAdapter(
			List<ServiceCentreParentBean> lvBeans, Context context) {
		this.lvBeans = lvBeans;
		this.context = context;
		mInflater = LayoutInflater.from(context);

	}

	// 得到子item需要关联的数据
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return lvBeans.get(groupPosition).getServiceCentreChildBean()
				.get(childPosition);
	}

	// 得到子item的ID
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	// 设置子item的组件
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ItemHolder ih;
		if (convertView == null) {
			ih = new ItemHolder();
			convertView = mInflater.inflate(
					R.layout.activity_service_centre_expendlistview_child_item,
					null);
			ih.introduces = (TextView) convertView
					.findViewById(R.id.introduces);
			convertView.setTag(ih);
		} else {
			ih = (ItemHolder) convertView.getTag();
		}
		ih.introduces
				.setText(lvBeans.get(groupPosition).getServiceCentreChildBean()
						.get(childPosition).getIntroduces());
		return convertView;
	}

	// 获取当前父item下的子item的个数
	@Override
	public int getChildrenCount(int groupPosition) {
		return lvBeans.get(groupPosition).getServiceCentreChildBean().size();
	}

	// 获取当前父item的数据
	@Override
	public Object getGroup(int groupPosition) {
		return lvBeans.get(groupPosition).getProblem();
	}

	@Override
	public int getGroupCount() {
		return lvBeans.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// 设置父item组件
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupHolder gh;
		if (convertView == null) {
			gh = new GroupHolder();
			convertView = mInflater
					.inflate(
							R.layout.activity_service_centre_expendlistview_parent_item,
							null);
			gh.img = (ImageView) convertView.findViewById(R.id.img);
			gh.problem = (TextView) convertView.findViewById(R.id.problem);
			convertView.setTag(gh);
		} else {
			gh = (GroupHolder) convertView.getTag();
		}
		gh.problem.setText(lvBeans.get(groupPosition).getProblem());
		if (isExpanded) {
			gh.img.setImageResource(R.drawable.shang);
		} else {
			gh.img.setImageResource(R.drawable.xia);
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public class GroupHolder {
		ImageView img;
		TextView problem;
	}

	public class ItemHolder {
		TextView introduces;
	}

}
