package rxh.hb.utils;

import com.xiningmt.wdb.LoginActivity;
import com.xiningmt.wdb.MemberCertificationOneActivity;
import com.xiningmt.wdb.RealNameAuthenticationSuccessActivity;
import com.xiningmt.wdb.RegisterActivity;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class RequestReturnProcessing {
    Context context;
    String code = null;

    public RequestReturnProcessing(Context context, String code) {
        this.context = context;
        this.code = code;
    }

    public int processing() {
        if (code.equals("200")) {
            return 200;
        } else if (code.equals("300006") || code.equals("300008")) {
            context.startActivity(new Intent(context, MemberCertificationOneActivity.class));
            return 0;
        } else if (code.equals("300007")) {
            Intent intent = new Intent();
            intent.putExtra("name", "审核中...");
            intent.putExtra("card", "");
            intent.setClass(context, RealNameAuthenticationSuccessActivity.class);
            context.startActivity(intent);
            return 0;
        } else if (code.equals("400003")) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return 0;
        } else {
            Toast.makeText(context, new StatusCode().getstatus(code),
                    Toast.LENGTH_LONG).show();
            return 0;
        }
    }
}
