package rxh.hb.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiningmt.wdb.R;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.jsonbean.TransactionRecordEntity;
import rxh.hb.utils.ChangeUtils;

/**
 * Created by Administrator on 2016/11/30.
 */
public class TransactionRecordLVAdapter extends BaseAdapter {


    Context context;
    private LayoutInflater mInflater = null;
    List<TransactionRecordEntity> data = new ArrayList<TransactionRecordEntity>();

    public TransactionRecordLVAdapter(List<TransactionRecordEntity> data, Context context) {
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
            convertView = mInflater.inflate(R.layout.activity_transaction_record_lv_item, null);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.state = (TextView) convertView.findViewById(R.id.state);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.money = (TextView) convertView.findViewById(R.id.money);
            holder.mark = (TextView) convertView.findViewById(R.id.mark);
            // 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (data.get(position).getRecharge() != null) {
            holder.state.setVisibility(View.VISIBLE);
            if (data.get(position).getRecharge().equals("1")) {
                holder.state.setText("充值成功");
            } else if (data.get(position).getRecharge().equals("2")) {
                holder.state.setText("充值失败");
            } else if (data.get(position).getRecharge().equals("3")) {
                holder.state.setText("审核中");
            }
        } else if (data.get(position).getDraw() != null) {
            holder.state.setVisibility(View.VISIBLE);
            if (data.get(position).getDraw().equals("1")) {
                holder.state.setText("未提现");
            } else if (data.get(position).getDraw().equals("2")) {
                holder.state.setText("已提现");
            }
        } else {
            holder.state.setVisibility(View.GONE);
        }
        if (data.get(position).getWay() != null) {
            if (data.get(position).getWay().equals("1")) {
                holder.title.setText("线下" + data.get(position).getTname());
            } else {
                holder.title.setText("线上" + data.get(position).getTname());
            }
        } else {
            holder.title.setText(data.get(position).getTname());
        }
        holder.time.setText(data.get(position).getUpdatetime());
        holder.money.setText(Html.fromHtml("<font color='#FB6006'>" + "" + "</font>" + ChangeUtils.get_money(Double.valueOf(data.get(position).getMoney()))));
        if (data.get(position).getContent() != null) {
            holder.mark.setVisibility(View.VISIBLE);
            holder.mark.setText("备注:" + data.get(position).getContent());
        } else {
            holder.mark.setVisibility(View.GONE);
        }
        return convertView;
    }

    // ViewHolder静态类
    static class ViewHolder {
        public TextView title;
        public TextView state;
        public TextView time;
        public TextView money;
        public TextView mark;
    }

}
