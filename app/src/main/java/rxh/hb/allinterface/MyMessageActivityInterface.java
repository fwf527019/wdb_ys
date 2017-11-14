package rxh.hb.allinterface;

import java.util.List;

import rxh.hb.jsonbean.MyMessageActivityLVBean;

public interface MyMessageActivityInterface {

	public void getinformation(
			List<MyMessageActivityLVBean> myMessageActivityLVBeans);

	public void error();

}
