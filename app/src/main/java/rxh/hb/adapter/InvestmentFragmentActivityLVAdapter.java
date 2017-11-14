package rxh.hb.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.xiningmt.wdb.R;

import rxh.hb.jsonbean.InvestmentFragmentActivityLVBean;
import rxh.hb.utils.ChangeUtils;
import rxh.hb.view.CircleProgressBar;
import rxh.hb.view.RiseNumberTextView;
import rxh.hb.view.SpringProgressView;

import android.R.raw;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 *  产品标的列表展示
 */
public class InvestmentFragmentActivityLVAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater mInflater = null;
    DecimalFormat df = new DecimalFormat("0.00");
    List<InvestmentFragmentActivityLVBean> investmentFragmentActivityLVBeans = new ArrayList<InvestmentFragmentActivityLVBean>();

    public InvestmentFragmentActivityLVAdapter(List<InvestmentFragmentActivityLVBean> investmentFragmentActivityLVBeans,
                                               Context context) {
        this.investmentFragmentActivityLVBeans = investmentFragmentActivityLVBeans;
        this.context = context;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return investmentFragmentActivityLVBeans.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        // 如果缓存convertView为空，则需要创建View
        if (convertView == null) {
            holder = new ViewHolder();
            // 根据自定义的Item布局加载布局
            convertView = mInflater.inflate(R.layout.activity_investment_fragment_listview_item_test, null);
            holder.shouyi = (TextView) convertView.findViewById(R.id.shouyi);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.zhuangtai = (TextView) convertView.findViewById(R.id.zhuangtai);
            holder.dengji = (TextView) convertView.findViewById(R.id.dengji);
            holder.huankuanfangshi = (TextView) convertView.findViewById(R.id.huankuanfangshi);
            holder.jiekuanqixian = (TextView) convertView.findViewById(R.id.jiekuanqixian);
            holder.sp = (CircleProgressBar) convertView.findViewById(R.id.sp);
            holder.spjindu = (RiseNumberTextView) convertView.findViewById(R.id.spjindu);
            holder.qitoujine = (TextView) convertView.findViewById(R.id.qitoujine);
            // 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.shouyi.setText(df.format(Double.parseDouble(investmentFragmentActivityLVBeans.get(position).getRate())));
        holder.name.setText(investmentFragmentActivityLVBeans.get(position).getTitle());
        holder.zhuangtai.setText(ChangeUtils.get_mark_state(investmentFragmentActivityLVBeans.get(position).getStage()));
        holder.dengji.setText("信用等级" + ChangeUtils.get_credit_rating(investmentFragmentActivityLVBeans.get(position).getCreditrating()));
        holder.huankuanfangshi
                .setText("还款方式：" + ChangeUtils.get_repayment_method(investmentFragmentActivityLVBeans.get(position).getPaymenttype()));
        if (investmentFragmentActivityLVBeans.get(position).getLinetype() != null) {
            if (investmentFragmentActivityLVBeans.get(position).getLinetype().equals("2")) {
                holder.jiekuanqixian.setText(investmentFragmentActivityLVBeans.get(position).getDeadline() + "个月");
            } else {
                holder.jiekuanqixian.setText(investmentFragmentActivityLVBeans.get(position).getDeadline() + "天");
            }
        }
        holder.sp.setProgress(Float.parseFloat(investmentFragmentActivityLVBeans.get(position).getBaifeibi()), 0);
        float jindu = Float.parseFloat(investmentFragmentActivityLVBeans.get(position).getBaifeibi()) * 100;
        holder.spjindu.withNumber(jindu).setDuration(0).start();
        // holder.spjindu.setText(df.format(jindu));
        holder.qitoujine.setText(investmentFragmentActivityLVBeans.get(position).getMinimum() + "元起投");
        return convertView;
    }

    // ViewHolder静态类
    static class ViewHolder {
        public TextView shouyi;
        public TextView name;
        public TextView zhuangtai;
        public TextView dengji;
        public TextView huankuanfangshi;
        public TextView jiekuanqixian;
        public CircleProgressBar sp;
        public RiseNumberTextView spjindu;
        public TextView qitoujine;

    }

}
