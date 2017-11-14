package rxh.hb.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xiningmt.wdb.R;

import rxh.hb.jsonbean.TheHomePageFragmentBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TheHomePageLVAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater mInflater = null;
    List<TheHomePageFragmentBean> theHomePageFragmentLVBeans = new ArrayList<TheHomePageFragmentBean>();

    public TheHomePageLVAdapter(
            List<TheHomePageFragmentBean> theHomePageFragmentLVBeans,
            Context context) {
        this.theHomePageFragmentLVBeans = theHomePageFragmentLVBeans;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return theHomePageFragmentLVBeans.size();
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
                    R.layout.fragment_the_home_page_listview_item, null);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            // 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(theHomePageFragmentLVBeans.get(position)
                .getTitle());
        if (theHomePageFragmentLVBeans.get(position).getLinetype().equals("2")) {
            holder.content.setText(theHomePageFragmentLVBeans.get(position)
                    .getDeadline()
                    + "月|"
                    + theHomePageFragmentLVBeans.get(position).getMinimum()
                    + "元起购");
        } else {
            holder.content.setText(theHomePageFragmentLVBeans.get(position)
                    .getDeadline()
                    + "天|"
                    + theHomePageFragmentLVBeans.get(position).getMinimum()
                    + "元起购");
        }
        return convertView;
    }

    // ViewHolder静态类
    static class ViewHolder {
        public TextView title;
        public TextView content;
    }

}
