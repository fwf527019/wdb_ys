package com.xiningmt.wdb;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import rxh.hb.base.BaseActivity;
import rxh.hb.jsonbean.ActivityCenterDetailsActivityBean;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.utils.MyApplication;
import rxh.hb.utils.RequestReturnProcessing;
import rxh.hb.utils.ShowDialog;
import rxh.hb.volley.util.VolleySingleton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityCenterDetailsActivity extends BaseActivity {

    private ImageView back, img;
    private TextView title, go, atitle, author_and_time, content;
    String ids;
    RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    ActivityCenterDetailsActivityBean cbBean = new ActivityCenterDetailsActivityBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_center_details);
        ids = getIntent().getStringExtra("ids");
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(
                this.getApplicationContext()).getRequestQueue();
        mImageLoader = VolleySingleton.getVolleySingleton(
                getApplicationContext()).getImageLoader();
        initview();
        inittitle();
        getdetails(ids);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("MP");
        }
    }

    public void initview() {
        title = (TextView) findViewById(R.id.title);
        go = (TextView) findViewById(R.id.go);
        atitle = (TextView) findViewById(R.id.atitle);
        author_and_time = (TextView) findViewById(R.id.author_and_time);
        content = (TextView) findViewById(R.id.content);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        img = (ImageView) findViewById(R.id.img);
    }

    public void inittitle() {
        title.setText("活动详情");
        go.setText("");
    }

    public void initdata() {
        if (cbBean.getPath() != null) {
            mImageLoader.get(new CreatUrl().getimg() + cbBean.getPath(),
                    ImageLoader.getImageListener(img, R.drawable.loading_img,
                            R.drawable.load_error));
        }
        if (cbBean.getTitle() != null) {
            atitle.setText(cbBean.getTitle());
        }
        if (cbBean.getBegintime() != null) {
            author_and_time.setText(cbBean.getBegintime());
        }
        if (cbBean.getContent() != null) {
            content.setText(cbBean.getContent());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            default:
                break;
        }

    }

    public void getdetails(String ids) {
        loading("加载中", "true");
        Map<String, String> map = new HashMap<String, String>();
        map.put("ids", ids);
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest mp = new JsonObjectRequest(Method.POST,
                new CreatUrl().creaturl("meeting", "detail"), paramMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismiss();
                        try {
                            String code = response.getString("code");
                            if (new RequestReturnProcessing(
                                    getApplicationContext(), code).processing() == 200) {
                                cbBean = JsonUtils.getacActivityBean(response
                                        .toString());
                                initdata();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismiss();
                Toast.makeText(getApplicationContext(),
                        error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        mp.setTag("MP");
        VolleySingleton.getVolleySingleton(getApplicationContext())
                .addToRequestQueue(mp);

    }

}
