package rxh.hb.allinterface;

import java.util.List;
import rxh.hb.jsonbean.NewsBulletinActivityBean;

public interface NewsBulletinActivityInterface {
	
	public void getinformation(
			List<NewsBulletinActivityBean> newsBulletinActivityBeans);

	public void error();

}
