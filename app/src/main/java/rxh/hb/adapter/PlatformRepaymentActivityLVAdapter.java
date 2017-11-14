package rxh.hb.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import rxh.hb.adapter.ActivityCenterActivityLVAdapter.ViewHolder;
import rxh.hb.jsonbean.ActivityCenterActivityLVBean;
import rxh.hb.jsonbean.PlatformRepaymentLVBean;

import com.xiningmt.wdb.R;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlatformRepaymentActivityLVAdapter extends BaseAdapter {

	Context context;
	private LayoutInflater mInflater = null;
	List<PlatformRepaymentLVBean> lvBeans = new ArrayList<PlatformRepaymentLVBean>();

	public PlatformRepaymentActivityLVAdapter(
			List<PlatformRepaymentLVBean> lvBeans, Context context) {
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
					R.layout.activity_platform_repayment_listview_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.investment_number = (TextView) convertView
					.findViewById(R.id.investment_number);
			holder.repayment_amount = (TextView) convertView
					.findViewById(R.id.repayment_amount);
			// 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.title.setText(lvBeans.get(position).getTitle());

		if (lvBeans.get(position).getDatetype().equals("1")) {
			holder.content.setText("投资期限" + lvBeans.get(position).getDeadline()
					+ "个月");
		} else {
			holder.content.setText("投资期限" + lvBeans.get(position).getDeadline()
					+ "天");
		}
		holder.investment_number.setText(Html.fromHtml("投资次数:"
				+ "<font color=red>" + lvBeans.get(position).getCount()
				+ "</font>"));

		DecimalFormat df = new DecimalFormat("0.00");
		holder.repayment_amount.setText(Html.fromHtml("还款金额"
				+ "<font color=red>"
				+ df.format(Double.parseDouble(lvBeans.get(position)
						.getMoneys())) + "</font>元"));

		return convertView;
	}

	// ViewHolder静态类
	static class ViewHolder {
		public TextView title;
		public TextView content;
		public TextView investment_number;
		public TextView repayment_amount;
	}

}
