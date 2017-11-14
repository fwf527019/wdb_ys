package rxh.hb.allinterface;

import java.util.List;

import rxh.hb.jsonbean.ActivityCenterActivityLVBean;

public interface ActivityCenterActivityInterface {

	public void getinformation(
			List<ActivityCenterActivityLVBean> activityCenterActivityLVBeans);

	public void error();

}
