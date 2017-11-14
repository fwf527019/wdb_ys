package rxh.hb.eventbusentity;

import java.util.List;

import rxh.hb.jsonbean.TheHomePageFragmentimgBean;

public class BannerEntity {

	private List<TheHomePageFragmentimgBean> imgBeans;

	public BannerEntity(List<TheHomePageFragmentimgBean> imgBeans) {
		this.imgBeans = imgBeans;
	}

	public List<TheHomePageFragmentimgBean> getimgBeans() {
		return imgBeans;
	}

}
