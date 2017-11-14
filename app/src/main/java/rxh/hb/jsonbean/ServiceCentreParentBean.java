package rxh.hb.jsonbean;

import java.util.List;

public class ServiceCentreParentBean {

	private String problem;
	private List<ServiceCentreChildBean> serviceCentreChildBean;

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public List<ServiceCentreChildBean> getServiceCentreChildBean() {
		return serviceCentreChildBean;
	}

	public void setServiceCentreChildBean(
			List<ServiceCentreChildBean> serviceCentreChildBean) {
		this.serviceCentreChildBean = serviceCentreChildBean;
	}

	public ServiceCentreParentBean(String problem,
			List<ServiceCentreChildBean> serviceCentreChildBean) {
		super();
		this.problem = problem;
		this.serviceCentreChildBean = serviceCentreChildBean;
	}

	public ServiceCentreParentBean() {
		super();
		// TODO Auto-generated constructor stub
	}

}
