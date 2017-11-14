package rxh.hb.allinterface;

import java.util.List;

import rxh.hb.jsonbean.InvestmentFragmentActivityLVBean;

public interface InvestmentFragmentActivityInterface {

	public void getlvinformation(
			List<InvestmentFragmentActivityLVBean> investmentFragmentActivityLVBeans);

	public void error();

}
