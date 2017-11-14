package rxh.hb.allinterface;

import java.util.List;

import rxh.hb.jsonbean.TheHomePageFragmentBean;
import rxh.hb.jsonbean.TheHomePageFragmentimgBean;

public interface TheHomePageFragmentInterface {

	public void getlvinformation(
			List<TheHomePageFragmentBean> theHomePageFragmentBeans);

	public void error();

	public void getimgurl(
			List<TheHomePageFragmentimgBean> theHomePageFragmentimgBeans);

	public void imgerror();

}
