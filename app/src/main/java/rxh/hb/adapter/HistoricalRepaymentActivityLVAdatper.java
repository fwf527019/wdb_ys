package rxh.hb.adapter;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.adapter.PlatformRepaymentActivityLVAdapter.ViewHolder;
import rxh.hb.jsonbean.HistoricalRepaymentLVBean;
import rxh.hb.jsonbean.PlatformRepaymentLVBean;

import com.xiningmt.wdb.R;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistoricalRepaymentActivityLVAdatper extends BaseAdapter {

	Context context;
	private LayoutInflater mInflater = null;
	List<HistoricalRepaymentLVBean> lvBeans = new ArrayList<HistoricalRepaymentLVBean>();

	public HistoricalRepaymentActivityLVAdatper(
			List<HistoricalRepaymentLVBean> lvBeans, Context context) {
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
					R.layout.activity_historical_repayment_listview_item, null);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.repayment_amount = (TextView) convertView
					.findViewById(R.id.repayment_amount);
			holder.repayment_of_principal = (TextView) convertView
					.findViewById(R.id.repayment_of_principal);
			holder.investment_number = (TextView) convertView
					.findViewById(R.id.investment_number);
			// 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.time.setText(lvBeans.get(position).getTime());
		holder.repayment_amount.setText(Html.fromHtml("还款金额"
				+ "<font color=red>"
				+ lvBeans.get(position).getRepayment_amount() + "</font>元"));
		holder.repayment_of_principal.setText(lvBeans.get(position)
				.getRepayment_of_principal());
		holder.investment_number.setText(Html.fromHtml("投资人数:"
				+ "<font color=red>"
				+ lvBeans.get(position).getInvestment_number() + "</font>"));
		return convertView;
	}

	// ViewHolder静态类
	static class ViewHolder {
		public TextView time;
		public TextView repayment_amount;
		public TextView repayment_of_principal;
		public TextView investment_number;
	}
}
