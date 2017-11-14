package rxh.hb.allinterface;

import java.util.List;

import rxh.hb.jsonbean.TransactionRecordsActivityLVBean;
import rxh.hb.jsonbean.TransactionRecordsActivityLVBean1;

public interface BorrowingFragmentInterface1 {
	
	public void getinformation(
			List<TransactionRecordsActivityLVBean1> transactionRecordsActivityLVBeans);

	public void error();

}
