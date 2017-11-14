package rxh.hb.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiningmt.wdb.R;

/**
 * Created by Administrator on 2016/9/21.
 */
public class HPDialogFragment extends DialogFragment {

    View view;
    String end;
    private TextView end_tv;
    private ImageView know;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle bundle = getArguments();
        end = bundle.getString("end");
        getDialog().setCanceledOnTouchOutside(true);
        view = inflater.inflate(R.layout.fragment_dialog_hp, container);
        end_tv = (TextView) view.findViewById(R.id.end);
        know = (ImageView) view.findViewById(R.id.know);
        end_tv.setText(end);
        know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }


}
