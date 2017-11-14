package rxh.hb.viewinterface;

import rxh.hb.view.RiseNumberTextView;

public interface RiseNumberBase {

	public void start();

	public RiseNumberTextView withNumber(float number);

	public RiseNumberTextView withNumber(float number, boolean flag);

	public RiseNumberTextView withNumber(int number, boolean flag);

	public RiseNumberTextView withNumber(int number);

	public RiseNumberTextView setDuration(long duration);

	public void setOnEnd(RiseNumberTextView.EndListener callback);

}
