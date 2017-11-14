package rxh.hb.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import rxh.hb.adapter.ActivityCenterActivityLVAdapter.ViewHolder;
import rxh.hb.jsonbean.ActivityCenterActivityLVBean;
import rxh.hb.jsonbean.TransactionRecordsActivityLVBean;
import rxh.hb.jsonbean.TransactionRecordsActivityLVBean2;
import rxh.hb.utils.ChangeUtils;
import rxh.hb.view.RoundImageView;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiningmt.wdb.R;

public class TransactionRecordsActivityLVAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater mInflater = null;
    List<TransactionRecordsActivityLVBean2> lvBeans = new ArrayList<TransactionRecordsActivityLVBean2>();

    public TransactionRecordsActivityLVAdapter(
            List<TransactionRecordsActivityLVBean2> lvBeans, Context context) {
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
                    R.layout.activity_transaction_records_listview_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.year_rate = (TextView) convertView
                    .findViewById(R.id.year_rate);
            holder.function = (TextView) convertView
                    .findViewById(R.id.function);
            holder.investment = (TextView) convertView
                    .findViewById(R.id.investment);
            holder.data_last = (TextView) convertView
                    .findViewById(R.id.data_last);
            holder.term_of_investment = (TextView) convertView
                    .findViewById(R.id.term_of_investment);
            holder.the_date_of_investment = (TextView) convertView
                    .findViewById(R.id.the_date_of_investment);
            holder.all_profit = (TextView) convertView
                    .findViewById(R.id.all_profit);
            holder.yestady_profit = (TextView) convertView
                    .findViewById(R.id.yestady_profit);
            holder.stage = (TextView) convertView
                    .findViewById(R.id.stage);
            holder.all_profit_rl = (RelativeLayout) convertView
                    .findViewById(R.id.all_profit_rl);
            holder.yestaday_profit_rl = (RelativeLayout) convertView
                    .findViewById(R.id.yestaday_profit_rl);

            // 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(lvBeans.get(position).getTitle());
        //总收益
        if (lvBeans.get(position).getZprofit() != null) {
            holder.all_profit.setText(ChangeUtils.get_money(Double.parseDouble(lvBeans.get(position)
                    .getZprofit())) + "元");
        } else {
            holder.all_profit.setText("0.00元");
        }


        // 昨日收益
        if (lvBeans.get(position).getProfit() != null) {
            holder.yestady_profit.setText(ChangeUtils.get_money(Double
                    .parseDouble(lvBeans.get(position).getProfit())) + "元");
        } else {
            holder.yestady_profit.setText("0.00元");
        }
        // 投资金额
        holder.investment.setText(Double.parseDouble(lvBeans.get(position)
                .getMoney()) + "元");

        //状态
        holder.stage.setText(lvBeans.get(position)
                .getLname());

        if (lvBeans.get(position).getLname().equals("待还款")) {
            holder.yestaday_profit_rl.setVisibility(View.VISIBLE);
            holder.all_profit_rl.setVisibility(View.VISIBLE);
        } else {
            holder.yestaday_profit_rl.setVisibility(View.GONE);
            holder.all_profit_rl.setVisibility(View.GONE);
        }

        // 预期年化
        holder.year_rate.setText(lvBeans.get(position).getRate() + "%");
        //还款方式
        if (lvBeans.get(position).getPaymenttype().equals("3")) {
            holder.function.setText("到期一次性还本付息");
        } else {
            holder.function.setText("等额本息");
        }
        // 投资期限
        if (lvBeans.get(position).getLinetype().equals("2")) {
            holder.term_of_investment.setText(lvBeans.get(position)
                    .getDeadline() + "个月");
        } else if (lvBeans.get(position).getLinetype().equals("1")) {
            holder.term_of_investment.setText(lvBeans.get(position)
                    .getDeadline() + "天");
        }
        // 投资日期
        holder.the_date_of_investment.setText(lvBeans.get(position)
                .getCreatetime());
        // 计划还款时间
        holder.data_last.setText(lvBeans.get(position).getPlaytime());
        return convertView;
    }

    // ViewHolder静态类
    static class ViewHolder {
        public TextView name;
        public TextView investment;
        public TextView term_of_investment;
        public TextView year_rate;
        public TextView function;
        public TextView the_date_of_investment;
        public TextView data_last;
        public TextView all_profit;
        public TextView yestady_profit;
        public TextView stage;
        public RelativeLayout all_profit_rl;
        public RelativeLayout yestaday_profit_rl;


    }

}
