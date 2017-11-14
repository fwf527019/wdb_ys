package rxh.hb.fragment;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.banner.ConvenientBanner;
import rxh.hb.banner.holder.CBViewHolderCreator;
import rxh.hb.banner.holder.Holder;
import rxh.hb.banner.listener.OnItemClickListener;
import rxh.hb.eventbusentity.BannerEntity;
import rxh.hb.jsonbean.TheHomePageFragmentimgBean;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.MyApplication;
import rxh.hb.volley.util.VolleySingleton;

import com.android.volley.toolbox.ImageLoader;
import com.xiningmt.wdb.ActivityCenterDetailsActivity;
import com.xiningmt.wdb.BannerActivity;
import com.xiningmt.wdb.R;

import de.greenrobot.event.EventBus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BannerFragment extends Fragment {

    View view1;
    ImageLoader mImageLoader;
    ConvenientBanner convenientBanner;
    List<TheHomePageFragmentimgBean> imgBeans = new ArrayList<TheHomePageFragmentimgBean>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view1 = inflater.inflate(R.layout.fragment_banner, container, false);
        convenientBanner = (ConvenientBanner) view1.findViewById(R.id.convenient_banner);
        // 注册EventBus
        EventBus.getDefault().register(this);
        return view1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);// 反注册EventBus
    }

    public void onEventMainThread(BannerEntity bannerEntity) {
        imgBeans = bannerEntity.getimgBeans();
        initview();
    }

    public void initview() {
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, imgBeans)
                //设置翻页指示器
                .setPageIndicator(new int[]{R.drawable.lunbozhishiqi, R.drawable.lunbozhishiqi1})
                //设置翻页指示器位置
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
    }

    //开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        convenientBanner.startTurning(5000);
    }

    //停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        convenientBanner.stopTurning();
    }

    public class NetworkImageHolderView implements Holder<TheHomePageFragmentimgBean> {

        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(final Context context, final int position, final TheHomePageFragmentimgBean data) {
            mImageLoader.get(new CreatUrl().getimg()
                    + imgBeans.get(position).getPath(), ImageLoader
                    .getImageListener(imageView, R.drawable.loading_img,
                            R.drawable.load_error));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("content", imgBeans.get(position).getContent());
                    intent.putExtra("createtime", imgBeans.get(position).getCreatetime());
                    intent.putExtra("path", imgBeans.get(position).getPath());
                    intent.putExtra("title", imgBeans.get(position).getTitle());
                    intent.setClass(getActivity(),
                            BannerActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

//    private class TestLoopAdapter extends LoopPagerAdapter {
//
//        public TestLoopAdapter(RollPagerView viewPager) {
//            super(viewPager);
//        }
//
//        @Override
//        public View getView(ViewGroup container, final int position) {
//            ImageView view = new ImageView(container.getContext());
//            mImageLoader.get(new CreatUrl().getimg()
//                    + imgBeans.get(position).getPath(), ImageLoader
//                    .getImageListener(view, R.drawable.loading_img,
//                            R.drawable.load_error));
//            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            view.setLayoutParams(new ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT));
//            view.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    intent.putExtra("content", imgBeans.get(position).getContent());
//                    intent.putExtra("createtime", imgBeans.get(position).getCreatetime());
//                    intent.putExtra("path", imgBeans.get(position).getPath());
//                    intent.putExtra("title", imgBeans.get(position).getTitle());
//                    intent.setClass(getActivity(),
//                            BannerActivity.class);
//                    startActivity(intent);
//
//                }
//            });
//            return view;
//        }
//
//        @Override
//        public int getRealCount() {
//            return imgBeans.size();
//        }
//    }

}
