package rxh.hb.allinterface;

import java.util.List;

import rxh.hb.jsonbean.TransactionRecordsActivityLVBean;
import rxh.hb.jsonbean.TransactionRecordsActivityLVBean2;

public interface TransactionRecordsActivityInterface {

	public void getinformation(
			List<TransactionRecordsActivityLVBean2> transactionRecordsActivityLVBeans);

	public void error();

}
