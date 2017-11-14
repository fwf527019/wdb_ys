package rxh.hb.base;

import com.xiningmt.wdb.R;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class BaseFragment extends Fragment implements OnClickListener {

	public View base_view;
	public TextView r_load,base_title;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		base_view = inflater.inflate(R.layout.fragment_base, null);
		initbaseview();
		return base_view;
	}

	public void initbaseview() {
		r_load = (TextView) base_view.findViewById(R.id.r_load);
		r_load.setOnClickListener(this);
		base_title=(TextView) base_view.findViewById(R.id.base_title);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.r_load:

			break;

		default:
			break;
		}

	}

	// 判断网络是否可用
	public Boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}

}
