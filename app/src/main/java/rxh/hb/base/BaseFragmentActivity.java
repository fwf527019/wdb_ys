package rxh.hb.base;

import com.xiningmt.wdb.R;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BaseFragmentActivity extends FragmentActivity implements
		OnClickListener {

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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.r_load:

			break;
		case R.id.back:

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
