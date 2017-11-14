package com.xiningmt.wdb;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rxh.hb.base.BaseActivity;
import rxh.hb.utils.CreatUrl;


public class NewsBulletinDetailsActivity1 extends BaseActivity {
    String ids;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.go)
    TextView go;
    @Bind(R.id.news_web)
    WebView newsWeb;
    @Bind(R.id.back)
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_bulletin_details_web);
        ButterKnife.bind(this);
        title.setText("新闻详情");
        ids = getIntent().getStringExtra("ids");
        Log.d("NewsBulletinDetailsActi", ids);
        WebSettings settings = newsWeb.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setSupportZoom(true);// 支持缩放
        newsWeb.getSettings().setJavaScriptEnabled(true);//启用js
        newsWeb.getSettings().setBlockNetworkImage(false);//解决图片不显示
        settings.setLoadsImagesAutomatically(true);  //支持自动加载图片);
        settings.setJavaScriptEnabled(true);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        newsWeb.loadUrl(new CreatUrl().getimg()+"mobile/news/detail/"+ids);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }


}
