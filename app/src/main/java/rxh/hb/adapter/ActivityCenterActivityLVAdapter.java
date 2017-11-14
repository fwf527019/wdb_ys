package rxh.hb.adapter;

import java.util.ArrayList;
import java.util.List;
import com.android.volley.toolbox.ImageLoader;
import com.xiningmt.wdb.R;
import rxh.hb.jsonbean.ActivityCenterActivityLVBean;
import rxh.hb.utils.CreatUrl;
import rxh.hb.volley.util.VolleySingleton;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityCenterActivityLVAdapter extends BaseAdapter {

	Context context;
	private ImageLoader mImageLoader;
	private LayoutInflater mInflater = null;
	List<ActivityCenterActivityLVBean> lvBeans = new ArrayList<ActivityCenterActivityLVBean>();

	public ActivityCenterActivityLVAdapter(
			List<ActivityCenterActivityLVBean> activityCenterActivityLVBeans,
			Context context) {
		this.lvBeans = activityCenterActivityLVBeans;
		this.context = context;
		mInflater = LayoutInflater.from(context);
		mImageLoader = VolleySingleton.getVolleySingleton(context)
				.getImageLoader();

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
					R.layout.activity_activity_center_listview_item, null);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			// 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		mImageLoader.get(new CreatUrl().getimg()
				+ lvBeans.get(position).getPath(), ImageLoader
				.getImageListener(holder.img, R.drawable.loading_img,
						R.drawable.load_error));
		holder.title.setText(lvBeans.get(position).getTitle());
		holder.content.setText(lvBeans.get(position).getIntro());
		return convertView;
	}

	// ViewHolder静态类
	static class ViewHolder {
		public ImageView img;
		public TextView title;
		public TextView content;
	}

}
