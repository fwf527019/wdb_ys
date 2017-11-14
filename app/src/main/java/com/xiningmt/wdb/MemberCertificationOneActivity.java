package com.xiningmt.wdb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import rxh.hb.base.BaseActivity;
import rxh.hb.eventbusentity.PhotoAndNameEventBus;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.volley.util.VolleySingleton;

/**
 * Created by Administrator on 2016/10/13.
 */
public class MemberCertificationOneActivity extends BaseActivity {


    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.card_number)
    EditText card_number;
    @Bind(R.id.positive_identity_card)
    ImageView positive_ddentity_card;
    @Bind(R.id.positive_identity_card_prompt)
    TextView positive_identity_card_prompt;
    @Bind(R.id.back_of_ID_card)
    ImageView back_of_ID_card;
    @Bind(R.id.back_of_ID_card_prompt)
    TextView back_of_ID_card_prompt;
    @Bind(R.id.business_license_view)
    View business_license_view;
    @Bind(R.id.business_license)
    ImageView business_license;
    @Bind(R.id.business_license_prompt)
    TextView business_license_prompt;
    @Bind(R.id.next)
    Button next;


    String idcard_path = null, back_of_ID_card_path = null, business_license_path = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initview();
    }

    public void initview() {
        setContentView(R.layout.activity_member_certification_one);
        ButterKnife.bind(this);
        title.setText("会员认证");
        back.setOnClickListener(this);
        next.setOnClickListener(this);
        positive_ddentity_card.setOnClickListener(this);
        positive_identity_card_prompt.setOnClickListener(this);
        back_of_ID_card.setOnClickListener(this);
        back_of_ID_card_prompt.setOnClickListener(this);
        //1代表个人，2代表机构
        if (MyApplication.type.equals("1")) {
            business_license_prompt.setVisibility(View.GONE);
            business_license_view.setVisibility(View.GONE);
        } else {
            business_license.setOnClickListener(this);
            business_license_prompt.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.positive_identity_card:
                intent = new Intent(getApplicationContext(), MultiImageSelectorActivity.class);
                // 是否显示调用相机拍照
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // 最大图片选择数量
                //intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
                startActivityForResult(intent, 0);
                break;
            case R.id.positive_identity_card_prompt:
                intent = new Intent(getApplicationContext(), MultiImageSelectorActivity.class);
                // 是否显示调用相机拍照
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // 最大图片选择数量
                //intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
                startActivityForResult(intent, 0);
                break;
            case R.id.back_of_ID_card:
                intent = new Intent(getApplicationContext(), MultiImageSelectorActivity.class);
                // 是否显示调用相机拍照
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // 最大图片选择数量
                //intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
                startActivityForResult(intent, 1);
                break;
            case R.id.back_of_ID_card_prompt:
                intent = new Intent(getApplicationContext(), MultiImageSelectorActivity.class);
                // 是否显示调用相机拍照
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // 最大图片选择数量
                //intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
                startActivityForResult(intent, 1);
                break;
            case R.id.business_license:
                intent = new Intent(getApplicationContext(), MultiImageSelectorActivity.class);
                // 是否显示调用相机拍照
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // 最大图片选择数量
                //intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
                startActivityForResult(intent, 2);
                break;
            case R.id.business_license_prompt:
                intent = new Intent(getApplicationContext(), MultiImageSelectorActivity.class);
                // 是否显示调用相机拍照
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // 最大图片选择数量
                //intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
                startActivityForResult(intent, 2);
                break;
            case R.id.next:
                if (name.getText().toString() == null || name.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "你的真实姓名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (card_number.getText().toString() == null || card_number.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "你的身份证号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (idcard_path == null) {
                    Toast.makeText(getApplicationContext(), "你的身份证正面照不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (back_of_ID_card_path == null) {
                    Toast.makeText(getApplicationContext(), "你的身份证背面照不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (MyApplication.type.equals("2")) {
                    if (business_license_path == null) {
                        Toast.makeText(getApplicationContext(), "你的营业执照不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                post();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            // 获取返回的图片列表
            List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            idcard_path = path.get(0);
            Glide.with(getApplicationContext())
                    .load(idcard_path)
                    .centerCrop()
                    .into(positive_ddentity_card);
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            back_of_ID_card_path = path.get(0);
            Glide.with(getApplicationContext())
                    .load(back_of_ID_card_path)
                    .centerCrop()
                    .into(back_of_ID_card);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            business_license_path = path.get(0);
            Glide.with(getApplicationContext())
                    .load(business_license_path)
                    .centerCrop()
                    .into(business_license);
        }
    }


    public void post() {
        RequestParams params = new RequestParams(new CreatUrl().creaturl("authorization", "info/idcard"));
        params.setMultipart(true);
        params.addHeader("Authorization", MyApplication.getToken());
        params.addHeader("flag", "1");
        params.addBodyParameter("name", name.getText().toString());
        params.addBodyParameter("card", card_number.getText().toString());
        params.addBodyParameter("path", new File(idcard_path));
        params.addBodyParameter("pathone", new File(back_of_ID_card_path));
        if (business_license_path != null) {
            params.addBodyParameter("pathtwo", new File(business_license_path));
        }
        loading("上传中", "true");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                dismiss();
                String code = JsonUtils.getcode(result);
                if (new RequestReturnProcessing(getApplicationContext(), code).processing() == 200) {
                    startActivity(new Intent(getApplicationContext(), MemberCertificationTwoActivity.class));
                    finish();
                    Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dismiss();
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {

            }
        });

    }
}
