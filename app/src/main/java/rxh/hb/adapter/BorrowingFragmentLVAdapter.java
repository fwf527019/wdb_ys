package rxh.hb.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.xiningmt.wdb.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import rxh.hb.adapter.TransactionRecordsActivityLVAdapter.ViewHolder;
import rxh.hb.base.BaseActivity;
import rxh.hb.jsonbean.TransactionRecordsActivityLVBean1;
import rxh.hb.utils.ChangeUtils;

public class BorrowingFragmentLVAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater mInflater = null;
    List<TransactionRecordsActivityLVBean1> lvBeans = new ArrayList<TransactionRecordsActivityLVBean1>();

    public BorrowingFragmentLVAdapter(
            List<TransactionRecordsActivityLVBean1> lvBeans, Context context) {
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
                    R.layout.activity_transaction_records_listview_item1, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);

            holder.total_money = (TextView) convertView
                    .findViewById(R.id.total_money);
            holder.old_money = (TextView) convertView
                    .findViewById(R.id.old_money);
            holder.interest = (TextView) convertView
                    .findViewById(R.id.interest);
            holder.late_money = (TextView) convertView
                    .findViewById(R.id.late_money);
            holder.advance_money = (TextView) convertView
                    .findViewById(R.id.advance_money);
            holder.date = (TextView) convertView
                    .findViewById(R.id.date);
            holder.date_creat = (TextView) convertView.findViewById(R.id.date_creat);
            holder.year_rate = (TextView) convertView
                    .findViewById(R.id.year_rate);
            holder.date_over = (TextView) convertView
                    .findViewById(R.id.date_over);
            holder.dayline = (TextView) convertView
                    .findViewById(R.id.dayline);
            holder.functionintroduction = (TextView) convertView
                    .findViewById(R.id.functionintroduction);
            // 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(lvBeans.get(position).getTitle());

        double v = Double.parseDouble(lvBeans.get(position).getBjmaney()) + Double.parseDouble(lvBeans.get(position).getRatemoney())
                + Double.parseDouble(lvBeans.get(position).getBmaney()) + Double.parseDouble(lvBeans.get(position).getDmaney());


     holder.total_money.setText(ChangeUtils.get_money(v) + "元");
        holder.old_money.setText(ChangeUtils.get_money(Double.parseDouble(lvBeans.get(position).getBjmaney())) + "元");
        holder.interest.setText(ChangeUtils.get_money(Double.parseDouble(lvBeans.get(position).getRatemoney())) + "元");
        holder.late_money.setText(ChangeUtils.get_money(Double.parseDouble(lvBeans.get(position).getBmaney())) + "元");
        holder.advance_money.setText(ChangeUtils.get_money(Double.parseDouble(lvBeans.get(position).getDmaney())) + "元");
        holder.date_creat.setText(lvBeans.get(position).getCreatetime());
        holder.date_over.setText(lvBeans.get(position).getPaytime());
        holder.date.setText(lvBeans.get(position).getDeadline() + lvBeans.get(position).getPname());
        holder.year_rate.setText(lvBeans.get(position).getRate()+"%");


        if (lvBeans.get(position).getPaymenttype().equals("1")) {
            holder.functionintroduction.setText("等额分期");
            holder.dayline.setText("("+lvBeans.get(position).getStaturs()+"/"+lvBeans.get(position).getDeadline()+")");
        } else if (lvBeans.get(position).getPaymenttype().equals("3")) {
            holder.functionintroduction.setText("到期一次性还本付息");
            holder.dayline.setText("1/1");
        }


//        // 未结算收益
//        if (lvBeans.get(position).getInter() != null) {
//            holder.no_settlement_proceeds.setText(ChangeUtils.get_money(Double.parseDouble(lvBeans.get(position).getInter())) + "元");
//        }
//        // 投资金额
//        if (lvBeans.get(position).getMoney() != null) {
//            holder.investment.setText(Double.parseDouble(lvBeans.get(position).getMoney()) + "元");
//        }
//        // 预期年化
//        holder.annual_revenue.setText(lvBeans.get(position).getRate() + "%");
//        // 投资期限
//        if (lvBeans.get(position).getLinetype().equals("2")) {
//            holder.term_of_investment.setText(lvBeans.get(position)
//                    .getDeadline() + "个月");
//        } else if (lvBeans.get(position).getLinetype().equals("1")) {
//            holder.term_of_investment.setText(lvBeans.get(position)
//                    .getDeadline() + "天");
//        }
        // 状态
//        if (lvBeans.get(position).getStatus().equals("3")) {
//            holder.state.setText("已完结");
//        } else if (lvBeans.get(position).getStatus().equals("2")) {
//            holder.state.setText("计息中");
//        } else if (lvBeans.get(position).getStatus().equals("1")) {
//            holder.state.setText("还款中");
//        }
        // 投资日期
//        holder.the_date_of_investment.setText(lvBeans.get(position)
//                .getCreatetime());
//        // 起息日期
//        holder.date_of_value.setText(lvBeans.get(position).getCreatetime());
//        // 还款日期
//        holder.the_date_of_repayment
//                .setText(lvBeans.get(position).getEndtime());

        return convertView;
    }

    // ViewHolder静态类
    static class ViewHolder {
        public TextView name;
        public TextView total_money;
        public TextView old_money;
        public TextView interest;
        public TextView late_money;
        public TextView advance_money;
        public TextView date;
        public TextView date_creat;
        public TextView year_rate;
        public TextView date_over;
        public TextView functionintroduction;
        public TextView dayline;

    }

}
