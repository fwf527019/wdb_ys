package rxh.hb.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.xiningmt.wdb.R;

public class MyDialog extends Dialog {

	String content;

	public MyDialog(Context context, int theme, String content) {
		super(context, theme);
		this.content = content;
	}

	public MyDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(content);
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
	}
}
