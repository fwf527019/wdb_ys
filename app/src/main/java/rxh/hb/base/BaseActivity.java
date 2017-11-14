package rxh.hb.base;

import com.xiningmt.wdb.R;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseActivity extends AppCompatActivity implements OnClickListener {

    LoadingFragment loadingFragment;
    LoadingNoPromptFragment loadingNoPromptFragment;
    public TextView r_load;
    public TextView base_title;
    public ImageView base_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initbaseview();
    }

    public void initbaseview() {
        setContentView(R.layout.activity_base);
        r_load = (TextView) findViewById(R.id.r_load);
        r_load.setOnClickListener(this);
        base_title = (TextView) findViewById(R.id.base_title);
        base_back = (ImageView) findViewById(R.id.base_back);
        base_back.setOnClickListener(this);
    }

    // 显示进度条,flag==ture表示点击dialog外部，dialog不会消失。flag==false表示点击dialog外部，dialog会消失
    public void loading(String title, String flag) {
        if (loadingFragment == null) {
            loadingFragment = new LoadingFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("flag", flag);
            loadingFragment.setArguments(bundle);
        }
        //这句代码用于解决频繁点击时发生的崩溃现象，未测试
        getSupportFragmentManager().executePendingTransactions();
        if (!loadingFragment.isAdded()) {
            loadingFragment.show(getSupportFragmentManager(), "loadingFragment");
        }
    }

    public void loading(String flag) {
        if (loadingNoPromptFragment == null) {
            loadingNoPromptFragment = new LoadingNoPromptFragment();
            Bundle bundle = new Bundle();
            bundle.putString("flag", flag);
            loadingNoPromptFragment.setArguments(bundle);
        }
        if (!loadingNoPromptFragment.isAdded()) {
            loadingNoPromptFragment.show(getSupportFragmentManager(), "loadingNoPromptFragment");
        }
    }


    // 隐藏进度条
    public void dismiss() {
        Fragment prev = getSupportFragmentManager().findFragmentByTag("loadingFragment");
        if (prev != null) {
            loadingFragment.dismiss();
            return;
        }
        prev = getSupportFragmentManager().findFragmentByTag("loadingNoPromptFragment");
        if (prev != null) {
            loadingNoPromptFragment.dismiss();
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.r_load:

                break;
            case R.id.base_back:

                break;

            default:
                break;
        }

    }

    // 判断网络是否可用
    public Boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

}
