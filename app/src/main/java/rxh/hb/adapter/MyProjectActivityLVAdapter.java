package rxh.hb.adapter;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.adapter.InvestmentFragmentActivityLVAdapter.ViewHolder;
import rxh.hb.jsonbean.InvestmentFragmentActivityLVBean;
import rxh.hb.jsonbean.TransferThePossessionOfFragmentLVBean;
import rxh.hb.utils.ChangeUtils;
import rxh.hb.view.SpringProgressView;

import com.xiningmt.wdb.R;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyProjectActivityLVAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater mInflater = null;
    List<TransferThePossessionOfFragmentLVBean> data = new ArrayList<TransferThePossessionOfFragmentLVBean>();

    public MyProjectActivityLVAdapter(
            List<TransferThePossessionOfFragmentLVBean> data,
            Context context) {
        this.data = data;
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

    // 获取一个在数据集中指定索引的视图来显示数据
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        // 如果缓存convertView为空，则需要创建View
        if (convertView == null) {
            holder = new ViewHolder();
            // 根据自定义的Item布局加载布局
            convertView = mInflater.inflate(
                    R.layout.activity_my_project_listview_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.monely = (TextView) convertView.findViewById(R.id.monely);
            // 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(ChangeUtils.get_receord_list(data.get(position).getType()));
        holder.monely.setText(data.get(position).getMoney());
        holder.time.setText(data.get(position).getUpdatetime());
        return convertView;
    }

    // ViewHolder静态类
    static class ViewHolder {
        public TextView name;
        public TextView time;
        public TextView monely;
    }

}
