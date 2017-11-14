package rxh.hb.adapter;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.adapter.ActivityCenterActivityLVAdapter.ViewHolder;
import rxh.hb.jsonbean.ActivityCenterActivityLVBean;
import rxh.hb.jsonbean.MyMessageActivityLVBean;
import rxh.hb.view.RoundImageView;

import com.xiningmt.wdb.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyMessageActivityLVAdapter extends BaseAdapter {

	Context context;
	private LayoutInflater mInflater = null;
	List<MyMessageActivityLVBean> lvBeans = new ArrayList<MyMessageActivityLVBean>();

	public MyMessageActivityLVAdapter(List<MyMessageActivityLVBean> lvBeans,
			Context context) {
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
	public View getView( int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		// 如果缓存convertView为空，则需要创建View
		if (convertView == null) {
			holder = new ViewHolder();
			// 根据自定义的Item布局加载布局
			convertView = mInflater.inflate(
					R.layout.activity_my_message_listview_item, null);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			// 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.time.setText(lvBeans.get(position).getCreatetime());
		holder.title.setText(lvBeans.get(position).getTitle());
		holder.content.setText(lvBeans.get(position).getContent());
		return convertView;
	}

	// ViewHolder静态类
	static class ViewHolder {
		public TextView time;
		public TextView title;
		public TextView content;
	}
}
