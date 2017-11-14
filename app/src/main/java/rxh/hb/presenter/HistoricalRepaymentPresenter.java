package rxh.hb.presenter;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.allinterface.ActivityCenterActivityInterface;
import rxh.hb.allinterface.HistoricalRepaymentInterface;
import rxh.hb.jsonbean.ActivityCenterActivityLVBean;
import rxh.hb.jsonbean.HistoricalRepaymentLVBean;
import android.content.Context;



public class HistoricalRepaymentPresenter {

	Context context;
	HistoricalRepaymentInterface historicalRepaymentInterface;

	public HistoricalRepaymentPresenter(Context context,
			HistoricalRepaymentInterface historicalRepaymentInterface) {
		this.context = context;
		this.historicalRepaymentInterface = historicalRepaymentInterface;
	}

	public void getlvinformation(String id) {
		List<HistoricalRepaymentLVBean> historicalRepaymentLVBeans = new ArrayList<HistoricalRepaymentLVBean>();
		for (int i = 0; i < 20; i++) {
			HistoricalRepaymentLVBean historicalRepaymentLVBean = new HistoricalRepaymentLVBean();
			historicalRepaymentLVBean.setTime("2015-12-24");
			historicalRepaymentLVBean.setRepayment_amount("23556");
			historicalRepaymentLVBean
					.setRepayment_of_principal("还款本金12344556677元");
			historicalRepaymentLVBean.setInvestment_number("234");
			historicalRepaymentLVBeans.add(historicalRepaymentLVBean);
		}
		historicalRepaymentInterface.getinformation(historicalRepaymentLVBeans);
	}

}
