package rxh.hb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiningmt.wdb.R;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import rxh.hb.jsonbean.MyIntegralEntity;
import rxh.hb.utils.AnimateFirstDisplayListener;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.MyApplication;

/**
 * Created by Administrator on 2016/10/11.
 */
public class MyIntegralGVAdapterr extends BaseAdapter {


    Context context;
    private LayoutInflater mInflater = null;
    List<MyIntegralEntity> data = new ArrayList<MyIntegralEntity>();

    public MyIntegralGVAdapterr(List<MyIntegralEntity> data, Context context) {
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
            convertView = mInflater.inflate(R.layout.activity_my_integral_gv_item, null);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.integral = (TextView) convertView.findViewById(R.id.integral);
            // 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        ImageLoader.getInstance().displayImage(data.get(position).getPath(),
//                holder.img, MyApplication.options, new AnimateFirstDisplayListener());
        holder.name.setText(data.get(position).getName());
        holder.integral.setText(data.get(position).getNum());

        Glide.with(context).load(new CreatUrl().getimg()+data.get(position).getPath()).placeholder(R.drawable.loading_img).into(holder.img);
        return convertView;
    }

    // ViewHolder静态类
    static class ViewHolder {
        public ImageView img;
        public TextView name;
        public TextView integral;
    }
}
