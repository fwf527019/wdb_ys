package rxh.hb.utils;

import rxh.hb.view.MyDialog;

import com.xiningmt.wdb.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class ShowDialog {

	private Dialog mDialog;
	private MyDialog mydialog, paydialog;

	public void showdialog(Context mContext) {
		OnKeyListener keyListener = new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_HOME
						|| keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				}
				return false;
			}
		};

		mDialog = new AlertDialog.Builder(mContext).create();
		mDialog.setOnKeyListener(keyListener);
		mDialog.show();
		// 注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.loading_process_dialog_anim);
	}

	public void showpaydialog(Context context, String content) {
		paydialog = new MyDialog(context, R.style.dialog_style, content);// 创建Dialog并设置样式主题
		paydialog.setCanceledOnTouchOutside(false);// 设置点击Dialog外部任意区域关闭Dialog
		paydialog.show();
		Window dialogWindow = paydialog.getWindow();
		WindowManager.LayoutParams params = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager m = dialogWindow.getWindowManager();
		Display d = m.getDefaultDisplay();
		params.width = d.getWidth() / 5 * 3;
		dialogWindow.setAttributes(params);

	}

	public void showmydialog(Context context, String content) {
		// 取得自定义的view
		// LayoutInflater layoutInflater = LayoutInflater.from(context);
		// View myview = layoutInflater.inflate(R.layout.loading, null);
		// TextView title = (TextView) myview.findViewById(R.id.title);
		// title.setText(content);
		// mydialog = new AlertDialog.Builder(context).setView(myview).create();
		// mydialog.setCanceledOnTouchOutside(true);
		// mydialog.show();
		// Window dialogWindow = mydialog.getWindow();
		// WindowManager.LayoutParams params = dialogWindow.getAttributes();
		// dialogWindow.setGravity(Gravity.CENTER);
		// WindowManager m = dialogWindow.getWindowManager();
		// Display d = m.getDefaultDisplay();
		// params.width = d.getWidth() / 5 * 3;
		// dialogWindow.setAttributes(params);
		mydialog = new MyDialog(context, R.style.dialog_style, content);// 创建Dialog并设置样式主题
		mydialog.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
		mydialog.show();
		Window dialogWindow = mydialog.getWindow();
		WindowManager.LayoutParams params = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager m = dialogWindow.getWindowManager();
		Display d = m.getDefaultDisplay();
		params.width = d.getWidth() / 5 * 3;
		dialogWindow.setAttributes(params);

	}

	public void dismissmydialog() {
		mydialog.dismiss();
	}

	public void dismisspaydialog() {
		paydialog.dismiss();
	}

}
