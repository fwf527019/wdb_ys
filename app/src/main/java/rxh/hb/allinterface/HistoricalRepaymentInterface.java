package rxh.hb.allinterface;

import java.util.List;

import rxh.hb.jsonbean.HistoricalRepaymentLVBean;

public interface HistoricalRepaymentInterface {

	public void getinformation(
			List<HistoricalRepaymentLVBean> historicalRepaymentLVBeans);

	public void error();

}
