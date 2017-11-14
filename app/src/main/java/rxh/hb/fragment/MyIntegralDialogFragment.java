package rxh.hb.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.xiningmt.wdb.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/26.
 */
public class MyIntegralDialogFragment extends DialogFragment {


    View view;
    String tisi;
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.sure)
    Button sure;

    public interface MyIntegralDialogListener {
        void exchange();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle bundle = getArguments();
        tisi = bundle.getString("tisi");
        getDialog().setCanceledOnTouchOutside(false);
        view = inflater.inflate(R.layout.fragment_my_integral_dialog, null);
        ButterKnife.bind(this, view);
        initview();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels / 4 * 3, getDialog().getWindow().getAttributes().height);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void initview() {
        content.setText(tisi);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyIntegralDialogListener listener = (MyIntegralDialogListener) getActivity();
                if (listener != null) {
                    listener.exchange();
                    dismiss();
                }
            }
        });
    }
}
