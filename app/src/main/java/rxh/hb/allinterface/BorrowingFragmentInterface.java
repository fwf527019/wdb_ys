package rxh.hb.allinterface;

import java.util.List;

import rxh.hb.jsonbean.TransactionRecordsActivityLVBean;

public interface BorrowingFragmentInterface {
	
	public void getinformation(
			List<TransactionRecordsActivityLVBean> transactionRecordsActivityLVBeans);

	public void error();

}
