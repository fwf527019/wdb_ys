package rxh.hb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiningmt.wdb.R;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.jsonbean.LoanApplicationEntity;
import rxh.hb.view.SmoothCheckBox;

/**
 * Created by Administrator on 2016/10/11.
 */
public class LoanApplicationLVAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater mInflater = null;
    List<LoanApplicationEntity> data = new ArrayList<LoanApplicationEntity>();

    public LoanApplicationLVAdapter(List<LoanApplicationEntity> data, Context context) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        // 如果缓存convertView为空，则需要创建View
        if (convertView == null) {
            holder = new ViewHolder();
            // 根据自定义的Item布局加载布局
            convertView = mInflater.inflate(R.layout.fragment_loan_application_lv_item, null);
            holder.application_time = (TextView) convertView.findViewById(R.id.application_time);
            holder.loan_amount = (TextView) convertView.findViewById(R.id.loan_amount);
            holder.loan_type = (TextView) convertView.findViewById(R.id.loan_type);
            holder.term_of_loan = (TextView) convertView.findViewById(R.id.term_of_loan);
            holder.stage = (TextView) convertView.findViewById(R.id.stage);
            // 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.application_time.setText(data.get(position).getCreatetime());
        holder.loan_amount.setText(data.get(position).getMoney() + "元");
        //   holder.stage.setText(data.get(position).getStage());
        if (data.get(position).getType().equals("1")) {
            holder.loan_type.setText("信用贷款");
        } else {
            holder.loan_type.setText("抵押贷款");
        }
        holder.term_of_loan.setText(data.get(position).getBrolin() + "个月");

        if(data.get(position).getIsno().equals("1")){
            holder.stage.setText("已处理");
        }else {
            holder.stage.setText("未处理");
        }
        return convertView;
    }



    // ViewHolder静态类
    static class ViewHolder {
        public TextView application_time;
        public TextView loan_amount;
        public TextView loan_type;
        public TextView term_of_loan;
        public TextView stage;

    }


}
