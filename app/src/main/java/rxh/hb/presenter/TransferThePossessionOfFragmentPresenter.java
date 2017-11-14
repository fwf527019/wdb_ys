package rxh.hb.presenter;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.allinterface.TransferThePossessionOfFragmentInterface;
import rxh.hb.jsonbean.TransferThePossessionOfFragmentLVBean;

public class TransferThePossessionOfFragmentPresenter {

	TransferThePossessionOfFragmentInterface ti;

	public TransferThePossessionOfFragmentPresenter(
			TransferThePossessionOfFragmentInterface ti) {
		this.ti = ti;
	}

	public void getlvinformation(String id) {
		List<TransferThePossessionOfFragmentLVBean> transferThePossessionOfFragmentLVBeans = new ArrayList<TransferThePossessionOfFragmentLVBean>();
		for (int i = 0; i < 20; i++) {
			TransferThePossessionOfFragmentLVBean tv = new TransferThePossessionOfFragmentLVBean();
			transferThePossessionOfFragmentLVBeans.add(tv);
		}
		ti.getlvinformation(transferThePossessionOfFragmentLVBeans);
	}

}
