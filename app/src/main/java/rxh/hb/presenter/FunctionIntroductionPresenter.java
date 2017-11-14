package rxh.hb.presenter;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.allinterface.ServiceCentreInterface;
import rxh.hb.jsonbean.ServiceCentreChildBean;
import rxh.hb.jsonbean.ServiceCentreParentBean;
import android.content.Context;

public class FunctionIntroductionPresenter {

	Context context;
	ServiceCentreInterface serviceCentreInterface;

	public FunctionIntroductionPresenter(Context context,
			ServiceCentreInterface serviceCentreInterface) {
		this.context = context;
		this.serviceCentreInterface = serviceCentreInterface;
	}

	public void getlvinformation(String id) {
		List<ServiceCentreParentBean> serviceCentreParentBeans = new ArrayList<ServiceCentreParentBean>();
		for (int i = 0; i < 1; i++) {
			String problem = "投资流程？";
			List<ServiceCentreChildBean> serviceCentreChildBeans = new ArrayList<ServiceCentreChildBean>();
			for (int j = 0; j < 1; j++) {
				ServiceCentreChildBean serviceCentreChildBean = new ServiceCentreChildBean();
				serviceCentreChildBean
						.setIntroduces("详情请拨打右上角稳当宝客服电话");
				serviceCentreChildBeans.add(serviceCentreChildBean);
			}
			ServiceCentreParentBean serviceCentreParentBean = new ServiceCentreParentBean(
					problem, serviceCentreChildBeans);
			serviceCentreParentBeans.add(serviceCentreParentBean);
		}
		serviceCentreInterface.getinformation(serviceCentreParentBeans);

	}

}
