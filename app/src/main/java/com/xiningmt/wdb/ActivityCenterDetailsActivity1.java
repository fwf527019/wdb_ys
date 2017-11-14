package com.xiningmt.wdb;

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rxh.hb.base.BaseActivity;
import rxh.hb.utils.CreatUrl;

public class ActivityCenterDetailsActivity1 extends BaseActivity {
    String ids;
    WebView webView;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.go)
    TextView go;
    @Bind(R.id.activitycenter_web_ll)
    LinearLayout activitycenterWebLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_center_details_web);
        ButterKnife.bind(this);
        title.setText("活动详情");
        ids = getIntent().getStringExtra("ids");
        webView = new WebView(ActivityCenterDetailsActivity1.this);
        activitycenterWebLl.addView(webView);
        WebSettings settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setSupportZoom(true);// 支持缩放
        webView.getSettings().setJavaScriptEnabled(true);//启用js
        webView.getSettings().setBlockNetworkImage(false);//解决图片不显示
        settings.setLoadsImagesAutomatically(true);  //支持自动加载图片);
        settings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView.loadUrl(new CreatUrl().getimg() + "mobile/meeting/detail/" + ids);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }


    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
