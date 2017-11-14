package rxh.hb.presenter;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.allinterface.ServiceCentreInterface;
import rxh.hb.jsonbean.ServiceCentreChildBean;
import rxh.hb.jsonbean.ServiceCentreParentBean;
import android.content.Context;

public class ProductIntroductionPresenter {

	Context context;
	ServiceCentreInterface serviceCentreInterface;

	public ProductIntroductionPresenter(Context context,
			ServiceCentreInterface serviceCentreInterface) {
		this.context = context;
		this.serviceCentreInterface = serviceCentreInterface;
	}

	public void getlvinformation(String id) {
		List<ServiceCentreParentBean> serviceCentreParentBeans = new ArrayList<ServiceCentreParentBean>();
		for (int i = 0; i < 1; i++) {
			String problem = "投资收益去向？";
			List<ServiceCentreChildBean> serviceCentreChildBeans = new ArrayList<ServiceCentreChildBean>();
			for (int j = 0; j < 1; j++) {
				ServiceCentreChildBean serviceCentreChildBean = new ServiceCentreChildBean();
				serviceCentreChildBean
						.setIntroduces("投资所获得的收益会打到你所绑定的银行卡上，如果你未绑定银行卡，会打到你支付的那个卡上。");
				serviceCentreChildBeans.add(serviceCentreChildBean);
			}
			ServiceCentreParentBean serviceCentreParentBean = new ServiceCentreParentBean(
					problem, serviceCentreChildBeans);
			serviceCentreParentBeans.add(serviceCentreParentBean);
		}
		serviceCentreInterface.getinformation(serviceCentreParentBeans);

	}

}
