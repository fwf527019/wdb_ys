package rxh.hb.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xiningmt.wdb.R;
import rxh.hb.jsonbean.TransferThePossessionOfFragmentLVBean;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TransferThePossessionOfFragmentLVAdapter extends BaseAdapter {

	Context context;
	private LayoutInflater mInflater = null;
	List<TransferThePossessionOfFragmentLVBean> thePossessionOfFragmentLVBeans = new ArrayList<TransferThePossessionOfFragmentLVBean>();

	public TransferThePossessionOfFragmentLVAdapter(
			List<TransferThePossessionOfFragmentLVBean> thePossessionOfFragmentLVBeans,
			Context context) {
		this.thePossessionOfFragmentLVBeans = thePossessionOfFragmentLVBeans;
		mInflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		return thePossessionOfFragmentLVBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// 获取一个在数据集中指定索引的视图来显示数据
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		// 如果缓存convertView为空，则需要创建View
		if (convertView == null) {
			holder = new ViewHolder();
			// 根据自定义的Item布局加载布局
			convertView = mInflater.inflate(
					R.layout.fragment_transfer_the_possession_of_listview_item,
					null);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.expected_annual = (TextView) convertView
					.findViewById(R.id.expected_annual);
			holder.residual_term = (TextView) convertView
					.findViewById(R.id.residual_term);
			holder.can_receive_benefits = (TextView) convertView
					.findViewById(R.id.can_receive_benefits);
			holder.transfer = (TextView) convertView
					.findViewById(R.id.transfer);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			// 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

//		holder.title.setText(thePossessionOfFragmentLVBeans.get(position)
//				.getTitle());
//		holder.expected_annual.setText(thePossessionOfFragmentLVBeans.get(
//				position).getExpected_annual());
//		holder.residual_term.setText(thePossessionOfFragmentLVBeans.get(
//				position).getResidual_term());
//		holder.can_receive_benefits.setText(thePossessionOfFragmentLVBeans.get(
//				position).getCan_receive_benefits());
//		holder.transfer.setText(thePossessionOfFragmentLVBeans.get(position)
//				.getTransfer());
//		holder.price.setText(Html.fromHtml("价格：<font color=" + "#FB6006" + ">"
//				+ thePossessionOfFragmentLVBeans.get(position).getPrice()
//				+ "</font>"));
		return convertView;
	}

	// ViewHolder静态类
	static class ViewHolder {
		public TextView title;
		public TextView expected_annual;
		public TextView residual_term;
		public TextView can_receive_benefits;
		public TextView transfer;
		public TextView price;
	}

}
