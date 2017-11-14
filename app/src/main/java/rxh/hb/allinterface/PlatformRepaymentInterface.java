package rxh.hb.allinterface;

import java.util.List;

import rxh.hb.jsonbean.PlatformRepaymentLVBean;

public interface PlatformRepaymentInterface {
	
	public void getinformation(
			List<PlatformRepaymentLVBean> platformRepaymentLVBeans);

	public void error();

}
