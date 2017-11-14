package rxh.hb.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.xiningmt.wdb.R;

import org.xutils.BuildConfig;
import org.xutils.x;

public class MyApplication extends Application {

    private ImageLoader imageLoader;
    public static DisplayImageOptions options;

    public static String token = null;
    public static String type = null;//1代表个人，2代表机构

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        MyApplication.token = token;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        init(this);
    }

    public void init(Context context) {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
                context).threadPriority(3)
                .denyCacheImageMultipleSizesInMemory().// //缓存显示不同大小的同一张图片
                diskCacheFileNameGenerator(new Md5FileNameGenerator()). // 文件名字的加密策略
                tasksProcessingOrder(QueueProcessingType.LIFO).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(configuration);
        options = new DisplayImageOptions.Builder().cacheInMemory()
                .cacheOnDisc().showStubImage(R.drawable.loading_img)
                .showImageForEmptyUri(R.drawable.loading_img)
                .showImageOnFail(R.drawable.load_error)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 图片显示方式
                .bitmapConfig(Bitmap.Config.ARGB_4444).build();// 設置圖片配置信息
        //.build();
        // 對圖片進行處理防止內存溢出
    }
}
