package rxh.hb.allinterface;

import java.util.List;

import rxh.hb.jsonbean.ServiceCentreParentBean;

public interface ServiceCentreInterface {

	public void getinformation(List<ServiceCentreParentBean> serviceCentreParentBeans);

	public void error();

}
