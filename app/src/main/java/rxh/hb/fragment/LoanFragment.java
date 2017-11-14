package rxh.hb.fragment;

import rxh.hb.eventbusentity.LoanEntity;
import rxh.hb.eventbusentity.MainEntity;
import rxh.hb.eventbusentity.WalletEntity;

import java.lang.annotation.Annotation;

import com.lidroid.xutils.view.annotation.event.OnChildClick;
import com.xiningmt.wdb.FinanceActivity;
import com.xiningmt.wdb.LoanDataFillingActivity;
import com.xiningmt.wdb.R;
import com.xiningmt.wdb.RegisterActivity;

import de.greenrobot.event.EventBus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoanFragment extends Fragment {

    private View view;
    private TextView i_want_to_finance;
    private LinearLayout dianhuaone, dianhuatwo, dianhuathree;
    private Button mortgage, credit_loan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_loan, container, false);
        initview();
        return view;
    }

    public void initview() {
        dianhuaone = (LinearLayout) view.findViewById(R.id.dianhuaone);
        dianhuaone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "09716102069"));
                startActivity(intent);

            }
        });
        dianhuatwo = (LinearLayout) view.findViewById(R.id.dianhuatwo);
        dianhuatwo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "09716102069"));
                startActivity(intent);

            }
        });
        dianhuathree = (LinearLayout) view.findViewById(R.id.dianhuathree);
        dianhuathree.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = getResources().getString(R.string.phonenum);
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
                startActivity(intent);
            }
        });
        mortgage = (Button) view.findViewById(R.id.mortgage);
        mortgage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Intent intent = new Intent();
                // intent.putExtra("loan", "抵押贷款");
                // intent.setClass(getActivity(),
                // LoanDataFillingActivity.class);
                // startActivity(intent);
                String num = getResources().getString(R.string.phonenum);
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
                startActivity(intent);

            }
        });
        credit_loan = (Button) view.findViewById(R.id.credit_loan);
        credit_loan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Intent intent = new Intent();
                // intent.putExtra("loan", "信用贷款");
                // intent.setClass(getActivity(),
                // LoanDataFillingActivity.class);
                // startActivity(intent);
                String num = getResources().getString(R.string.phonenum);
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
                startActivity(intent);

            }
        });
        i_want_to_finance = (TextView) view.findViewById(R.id.i_want_to_finance);
        i_want_to_finance.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FinanceActivity.class));

            }
        });
    }
}
