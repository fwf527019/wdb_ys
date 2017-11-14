package rxh.hb.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xiningmt.wdb.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyMonthlyBillActivityLVAdapter extends BaseAdapter {

	Context context;
	private LayoutInflater mInflater = null;
	List<String> lvBeans = new ArrayList<String>();

	public MyMonthlyBillActivityLVAdapter(List<String> lvBeans, Context context) {
		this.lvBeans = lvBeans;
		this.context = context;
		mInflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		return lvBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		// 如果缓存convertView为空，则需要创建View
		if (convertView == null) {
			holder = new ViewHolder();
			// 根据自定义的Item布局加载布局
			convertView = mInflater.inflate(
					R.layout.activity_my_monthly_bill_listview_item, null);
			holder.item = (TextView) convertView.findViewById(R.id.item);
			// 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.item.setText(lvBeans.get(position));
		return convertView;
	}

	// ViewHolder静态类
	static class ViewHolder {
		public TextView item;
	}

}
