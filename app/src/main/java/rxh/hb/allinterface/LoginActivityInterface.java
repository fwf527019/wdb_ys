package rxh.hb.allinterface;

import com.android.volley.VolleyError;

import rxh.hb.jsonbean.LoginBean;

public interface LoginActivityInterface {

	public void getloginback(LoginBean loginBean);
	public void error(VolleyError error);

}
