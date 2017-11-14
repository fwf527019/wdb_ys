package rxh.hb.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.xiningmt.wdb.R;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.bean.Image;
import rxh.hb.jsonbean.ImmediateRepaymentEntity;
import rxh.hb.utils.ChangeUtils;
import rxh.hb.view.SmoothCheckBox;

/**
 * Created by Administrator on 2016/10/11.
 */
public class ImmediateRepaymentLVAdapter extends BaseAdapter {


    Context context;

    private LayoutInflater mInflater = null;
    List<ImmediateRepaymentEntity> data = new ArrayList<ImmediateRepaymentEntity>();
    OnCheckClickLitener mOnCheckClickLitener;
    boolean isCheacked[];
    int sectorflag = -1;

    public interface OnCheckClickLitener {
        void onCheckClick(int position, boolean ischecked);
    }


    public void setOnCheckClickLitener(OnCheckClickLitener mOnCheckClickLitener) {
        this.mOnCheckClickLitener = mOnCheckClickLitener;
    }


    public ImmediateRepaymentLVAdapter(List<ImmediateRepaymentEntity> data, Context context) {
        this.data = data;
        this.context = context;
        isCheacked = new boolean[data.size()];
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
            convertView = mInflater.inflate(R.layout.activity_immediate_repayment_lv_item, null);
            holder.selecte = (ImageView) convertView.findViewById(R.id.selecte);
            holder.repayment_amount = (TextView) convertView.findViewById(R.id.repayment_amount);
            holder.number_of_periods = (TextView) convertView.findViewById(R.id.number_of_periods);
            holder.principal = (TextView) convertView.findViewById(R.id.principal);
            holder.interest = (TextView) convertView.findViewById(R.id.interest);
            holder.liquidated_damages = (TextView) convertView.findViewById(R.id.liquidated_damages);
            holder.late_days = (TextView) convertView.findViewById(R.id.late_days);
            // 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.repayment_amount.setText(Html.fromHtml("还款金额"
                + "<font color='#ff8740'>"
                + ChangeUtils.get_money(Double.parseDouble(data.get(position).getZmoney())) + "</font>元"));
        if (!data.get(0).getPaymenttype().equals("1")) {
            // 1 等额本息
            holder.number_of_periods.setText("[" + 1 + "/" + 1 + "]期");
        } else {
            // 3到期一次性
            holder.number_of_periods.setText("[" + data.get(position).getStaturs() + "/" + data.get(position).getDeadline() + "]期");
        }
        holder.principal.setText("本金:" + ChangeUtils.get_money(Double.parseDouble(data.get(position).getMoney())));
        holder.interest.setText("利息:" + ChangeUtils.get_money(Double.parseDouble(data.get(position).getRatemoney())));
        holder.liquidated_damages.setText(Html.fromHtml("违约金"
                + "<font color='#ff8740'>"
                + ChangeUtils.get_money(Double.parseDouble(data.get(position).getZnmoney())) + "</font>元"));
        holder.late_days.setText("逾期天数:" + data.get(position).getDay() + "天");
        if (data.get(position).getDay().contains("-")) {
            holder.late_days.setText("剩余:" + data.get(position).getDay().replace("-", "") + "天");
        } else {
            holder.late_days.setText("逾期天数:" + data.get(position).getDay() + "天");
        }


        final ViewHolder finalHolder = holder;

//单选
//        if (sectorflag == position) {
//            Picasso.with(context).load(R.drawable.item_sec).into(holder.selecte);
//        } else {
//            Picasso.with(context).load(R.drawable.item_not_sec).into(holder.selecte);
//        }
        //多选
        if (data.get(position).isCheacked()) {
            Picasso.with(context).load(R.drawable.item_sec).into(holder.selecte);

        } else {

            Picasso.with(context).load(R.drawable.item_not_sec).into(holder.selecte);
        }

        final ViewHolder finalHolder1 = holder;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               单选
//                if (sectorflag == position) {
//                    sectorflag=-1;
//                    Picasso.with(context).load(R.drawable.item_not_sec).into(finalHolder1.selecte);
//                    mOnCheckClickLitener.onCheckClick(position,false);
//                    notifyDataSetChanged();
//                } else {
//                    sectorflag=position;
//                    Picasso.with(context).load(R.drawable.item_sec).into(finalHolder1.selecte);
//                    mOnCheckClickLitener.onCheckClick(position,true);
//                    notifyDataSetChanged();
//                }
                //多选

                if (data.get(position).isCheacked()) {
                    Picasso.with(context).load(R.drawable.item_not_sec).into(finalHolder1.selecte);
                    mOnCheckClickLitener.onCheckClick(position, false);
                    notifyDataSetChanged();
                } else {
                    Picasso.with(context).load(R.drawable.item_sec).into(finalHolder1.selecte);
                    mOnCheckClickLitener.onCheckClick(position, true);
                    notifyDataSetChanged();
                }

            }
        });


        return convertView;
    }

    // ViewHolder静态类
    static class ViewHolder {
        public ImageView selecte;
        public TextView repayment_amount;
        public TextView number_of_periods;
        public TextView principal;
        public TextView interest;
        public TextView liquidated_damages;
        public TextView late_days;
    }


    private ClicListener clicListener;

    public void setClicListener(ClicListener clicListener) {
        this.clicListener = clicListener;
    }

    public interface ClicListener {
        void onClick(int postion, boolean isCheackd, View v);
    }


}
