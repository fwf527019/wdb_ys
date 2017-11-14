package rxh.hb.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xiningmt.wdb.R;

import rxh.hb.adapter.ActivityCenterActivityLVAdapter.ViewHolder;
import rxh.hb.jsonbean.ActivityCenterActivityLVBean;
import rxh.hb.view.RoundImageView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class AssetDescriptionFragmentHeaderViewAdapter extends BaseAdapter {

	Context context;
	private LayoutInflater mInflater = null;
	List<Integer> url = new ArrayList<Integer>();

	public AssetDescriptionFragmentHeaderViewAdapter(List<Integer> url,
			Context context) {
		this.url = url;
		this.context = context;
		mInflater = LayoutInflater.from(context);

	}

	public int getwidth() {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		return width;
	}

	@Override
	public int getCount() {
		return url.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		// 如果缓存convertView为空，则需要创建View
		if (convertView == null) {
			holder = new ViewHolder();
			// 根据自定义的Item布局加载布局
			convertView = mInflater.inflate(
					R.layout.fragment_asset_description_header_view_item, null);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			LayoutParams lp = (LayoutParams) holder.img.getLayoutParams();
			lp.width = getwidth() / 2;
			lp.height = getwidth() / 2;
			holder.img.setLayoutParams(lp);
			// 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.img.setBackgroundResource(url.get(position));
		return convertView;
	}

	// ViewHolder静态类
	static class ViewHolder {
		public ImageView img;
	}

}
