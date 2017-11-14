package rxh.hb.presenter;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.allinterface.ServiceCentreInterface;
import rxh.hb.jsonbean.ServiceCentreChildBean;
import rxh.hb.jsonbean.ServiceCentreParentBean;
import android.content.Context;

public class AssetDescriptionPresenter {
	
	Context context;
	ServiceCentreInterface serviceCentreInterface;

	public AssetDescriptionPresenter(Context context,
			ServiceCentreInterface serviceCentreInterface) {
		this.context = context;
		this.serviceCentreInterface = serviceCentreInterface;
	}

	public void getlvinformation(String id) {
		List<ServiceCentreParentBean> serviceCentreParentBeans = new ArrayList<ServiceCentreParentBean>();
		for (int i = 0; i < 10; i++) {
			String problem = "银行卡无法绑定？";
			List<ServiceCentreChildBean> serviceCentreChildBeans = new ArrayList<ServiceCentreChildBean>();
			for (int j = 0; j < 1; j++) {
				ServiceCentreChildBean serviceCentreChildBean = new ServiceCentreChildBean();
				serviceCentreChildBean
						.setIntroduces("这是无法绑定的具体原因介绍，如果你想仔细看的话，就请www.baidu.com进行查看");
				serviceCentreChildBeans.add(serviceCentreChildBean);
			}
			ServiceCentreParentBean serviceCentreParentBean = new ServiceCentreParentBean(
					problem, serviceCentreChildBeans);
			serviceCentreParentBeans.add(serviceCentreParentBean);
		}
		serviceCentreInterface.getinformation(serviceCentreParentBeans);

	}

}
