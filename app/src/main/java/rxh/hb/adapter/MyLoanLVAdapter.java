package rxh.hb.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiningmt.wdb.ImmediateRepaymentActivity;
import com.xiningmt.wdb.R;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.jsonbean.MyLoanEntity;

/**
 * Created by Administrator on 2016/10/11.
 */
public class MyLoanLVAdapter extends BaseAdapter {


    Context context;
    private LayoutInflater mInflater = null;
    List<MyLoanEntity> data = new ArrayList<MyLoanEntity>();

    public MyLoanLVAdapter(List<MyLoanEntity> data, Context context) {
        this.data = data;
        this.context = context;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return data.size();
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
            convertView = mInflater.inflate(R.layout.fragment_my_loan_lv_item, null);
            holder.project_name = (TextView) convertView.findViewById(R.id.project_name);
            holder.state = (TextView) convertView.findViewById(R.id.state);
            holder.loan_amount = (TextView) convertView.findViewById(R.id.loan_amount);
            holder.borrowing_rate = (TextView) convertView.findViewById(R.id.borrowing_rate);
            holder.term_of_loan = (TextView) convertView.findViewById(R.id.term_of_loan);
            holder.repayment_method = (TextView) convertView.findViewById(R.id.repayment_method);

            holder.repayment_time = (TextView) convertView.findViewById(R.id.repayment_time);
            holder.immediate_repayment = (TextView) convertView.findViewById(R.id.immediate_repayment);
            // 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //标名
        holder.project_name.setText("标的名称:"+data.get(position).getLoancode());
        //借款金额
        holder.loan_amount.setText(data.get(position).getMoneys() + "元");
        //利率
        holder.borrowing_rate.setText(data.get(position).getRate()+"%");
        //期限
        if (data.get(position).getLinetype().equals("1")) {
            holder.term_of_loan.setText(data.get(position).getDeadline() + "天");
        } else {
            holder.term_of_loan.setText(data.get(position).getDeadline() + "个月");
        }
        //状态
        if(data.get(position).getStage().equals("8")){
            holder.immediate_repayment.setVisibility(View.GONE);
            holder.state.setText("已完成");
        }else if(data.get(position).getStage().equals("7")){
            holder.state.setText("还款中");
            holder.immediate_repayment.setVisibility(View.VISIBLE);
        }else if(data.get(position).getStage().equals("6")){
            holder.immediate_repayment.setVisibility(View.GONE);
            holder.state.setText("待放款");
        }else if(data.get(position).getStage().equals("5")){
            holder.immediate_repayment.setVisibility(View.GONE);
            holder.state.setText("投资中");
        }else if(data.get(position).getStage().equals("4")){
            holder.immediate_repayment.setVisibility(View.GONE);
            holder.state.setText("待发布");
        }else if(data.get(position).getStage().equals("3")){
            holder.immediate_repayment.setVisibility(View.GONE);
            holder.state.setText("待审核");
        }else if(data.get(position).getStage().equals("2")){
            holder.immediate_repayment.setVisibility(View.GONE);
            holder.state.setText("流标");
        }else if(data.get(position).getStage().equals("1")){
            holder.immediate_repayment.setVisibility(View.GONE);
            holder.state.setText("已撤标");
        }
        holder.repayment_method.setText(data.get(position).getPname());
        holder.repayment_time.setText(data.get(position).getBacketime());
//        holder.state.setText(data.get(position).getTname());
        holder.immediate_repayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
            intent.putExtra("loanids", data.get(position).getIds());
                intent.setClass(context, ImmediateRepaymentActivity.class);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    // ViewHolder静态类
    static class ViewHolder {
        public TextView project_name;
        public TextView state;
        public TextView loan_amount;
        public TextView borrowing_rate;
        public TextView term_of_loan;
        public TextView repayment_method;
        public TextView lending_time;
        public TextView repayment_time;
        public TextView immediate_repayment;
    }

}
