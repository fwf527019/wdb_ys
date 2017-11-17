package com.xiningmt.wdb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import rxh.hb.base.BaseActivity;
import rxh.hb.eventbusentity.CIAEntity;
import rxh.hb.jsonbean.FinancialProjectDetailsActivityBean;
import rxh.hb.utils.ChangeUtils;
import rxh.hb.utils.CreatUrl;
import rxh.hb.utils.JsonUtils;
import rxh.hb.volley.util.VolleySingleton;

public class FinancialProjectDetailsActivity extends BaseActivity implements OnDismissListener {


    private ImageView back, calculation;
    private Button immediate_investment;
    private WebView project_overview;
    private LinearLayout showCorporationLl;
    private TextView title, expected_annual, investment_introduction, financing_amount, product_type,
            guarantee_corporation, residual_amount, biaodedetails, xingyongdengji;
    private static TextView expected_return;
    static EditText investment_amount;
    RequestQueue mRequestQueue;
    String ids, shengyujine, url;
    static String nianhuashouyi;
    static int date = 0;
    private static int lintype = 1;
    static FinancialProjectDetailsActivityBean cbBean = new FinancialProjectDetailsActivityBean();

    private PopupWindow popupWindow;
    private View popView, parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ids = getIntent().getStringExtra("ids");
        shengyujine = getIntent().getStringExtra("shengyujine");
        // 获得实例对象
        mRequestQueue = VolleySingleton.getVolleySingleton(this.getApplicationContext()).getRequestQueue();
        EventBus.getDefault().register(this);
        base_title.setText("稳当宝");
        getinformation(ids);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GETIF");
        }
    }

    public void onEventMainThread(CIAEntity entity) {
        getinformation(ids);
    }

    public void initview() {
        setContentView(R.layout.activity_financial_project_details);
        parent = findViewById(R.id.parent);
        title = (TextView) findViewById(R.id.title);
        title.setText("详情");
        expected_annual = (TextView) findViewById(R.id.expected_annual);
        financing_amount = (TextView) findViewById(R.id.financing_amount);
        residual_amount = (TextView) findViewById(R.id.residual_amount);
        product_type = (TextView) findViewById(R.id.product_type);
        guarantee_corporation = (TextView) findViewById(R.id.guarantee_corporation);
        project_overview = (WebView) findViewById(R.id.project_overview);
        investment_introduction = (TextView) findViewById(R.id.investment_introduction);
        biaodedetails = (TextView) findViewById(R.id.biaodedetails);
        xingyongdengji = (TextView) findViewById(R.id.xingyongdengji);
        back = (ImageView) findViewById(R.id.back);
        showCorporationLl= (LinearLayout) findViewById(R.id.show_corporation_ll);
        back.setOnClickListener(this);
        calculation = (ImageView) findViewById(R.id.calculation);
        calculation.setOnClickListener(this);
        immediate_investment = (Button) findViewById(R.id.immediate_investment);
        immediate_investment.setOnClickListener(this);
    }

    public void initdata() {
        initview();
        expected_annual.setText(ChangeUtils.get_money(Double.parseDouble(cbBean.getRate())));
        if (cbBean.getLinetype().equals("2")) {
            biaodedetails.setText(cbBean.getMinimum() + "元起投 | 期限" + cbBean.getDeadline() + "个月 | "
                    + gethuankuan(cbBean.getPaymenttype()));
        } else if (cbBean.getLinetype().equals("1")) {
            biaodedetails.setText(cbBean.getMinimum() + "元起投 | 期限" + cbBean.getDeadline() + "天 | "
                    + gethuankuan(cbBean.getPaymenttype()));
        }

        if (cbBean.getType().equals("3")) {
            showCorporationLl.setVisibility(View.VISIBLE);
        } else {
            showCorporationLl.setVisibility(View.GONE);
        }

        financing_amount.setText("融资金额:" + ChangeUtils.get_money(Double.valueOf(cbBean.getMoneys())) + "元");
        residual_amount.setText(ChangeUtils.get_mark_state(cbBean.getStage()));
        xingyongdengji.setText("信用等级" + ChangeUtils.get_credit_rating(cbBean.getCreditrating()));
        product_type.setText(ChangeUtils.get_mark_type(cbBean.getType()));
        String content = null;
        if (cbBean.getCompany() == null && cbBean.getCompanymoney() == null && cbBean.getCompanycode() == null
                && cbBean.getCompanytype() == null) {
            content = "无";
        }
        // 担保公司
        content = "担保公司名称：" + cbBean.getName();
        content = content + "\n注册资金：" + cbBean.getCompanymoney() + "万元";
        if (cbBean.getCompanycode() == null) {
            content = content + "\n组织机构代码：" + "无";
        } else {
            content = content + "\n组织机构代码：" + cbBean.getCompanycode();
        }

        if (cbBean.getCompanytype() != null) {
            if (cbBean.getCompanytype().equals("1")) {
                content = content + "\n是否是融资公司：" + "是";
            } else {
                content = content + "\n是否是融资公司：" + "否";
            }
        }
        guarantee_corporation.setText(content);
        url = url.substring(1);
        project_overview.loadUrl(new CreatUrl().getimg() + url + "/" + ids);
        //防止直接跳到系统浏览器

        project_overview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                project_overview.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.base_back:
                finish();
                break;
            case R.id.calculation:
                backgroundAlpha(0.5f);
                // 为popWindow添加动画效果
                popupWindow.setAnimationStyle(R.style.popWindow_animation);
                // 点击弹出泡泡窗口
                popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
                break;
            case R.id.immediate_investment:
                Intent intent = new Intent();
                intent.putExtra("id", cbBean.getIds());
                intent.putExtra("title", cbBean.getTitle());
                intent.putExtra("nianhua", cbBean.getRate());
                intent.putExtra("qitoujine", cbBean.getMinimum());
                intent.putExtra("shengyujine", shengyujine);
                if (cbBean.getLinetype() != null) {
                    if (cbBean.getLinetype().equals("1")) {
                        intent.putExtra("licaiqixian", cbBean.getDeadline() + "月");
                    } else {
                        intent.putExtra("licaiqixian", cbBean.getDeadline() + "天");
                    }
                }
                intent.setClass(getApplicationContext(), ConfirmInvestmentAmountActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }


    public void getinformation(String ids) {
        loading("加载中", "true");
        Map<String, String> map = new HashMap<String, String>();
        map.put("ids", ids);
        JSONObject paramMap = new JSONObject(map);
        JsonObjectRequest getif = new JsonObjectRequest(Method.POST, new CreatUrl().creaturl("loan", "getLoan"),
                paramMap, new Response.Listener<JSONObject>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(JSONObject response) {
                Log.d("FinancialProjectDetails", "response:" + response);
                dismiss();
                try {
                    String code = response.getString("code");
                    url = response.getString("url");
                    if (Integer.parseInt(code) == 200) {
                        cbBean = JsonUtils.getBeans(response.toString());

                        nianhuashouyi = cbBean.getRate();
                        lintype = Integer.parseInt(cbBean.getLinetype());
                        date = Integer.parseInt(cbBean.getDeadline());
                        initdata();
                        initPopupWindow();
                        if (cbBean.getStage().equals("2")) {
                            immediate_investment.setEnabled(false);
                            immediate_investment.setAlpha(0.8f);
                            immediate_investment.setText("项目已完成");
                        }

                        if (!cbBean.getStage().equals("5")) {
                            immediate_investment.setBackgroundResource(R.drawable.fragment_the_home_page_text_btn_bg_gry);
                            immediate_investment.setClickable(false);
                        } else {
                            immediate_investment.setBackgroundResource(R.drawable.fragment_the_home_page_text_btn_bg);
                            immediate_investment.setClickable(true);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismiss();
                r_load.setText("服务器或网络出现异常");

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
        getif.setTag("GETIF");
        VolleySingleton.getVolleySingleton(getApplicationContext()).addToRequestQueue(getif);
    }

    /**
     * 初始化popupWindow 计算器
     */
    private void initPopupWindow() {
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        popView = getLayoutInflater().inflate(R.layout.activity_financial_project_details_dialog, null);
        investment_amount = (EditText) popView.findViewById(R.id.investment_amount);
        setPricePoint(investment_amount);
        expected_return = (TextView) popView.findViewById(R.id.expected_return);
        popupWindow = new PopupWindow(popView, width / 3 * 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(this);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        this.getWindow().setAttributes(lp);
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */

    @Override
    public void onDismiss() {
        backgroundAlpha(1f);

    }


    public String gethuankuan(String i) {
        String result = null;
        if (i.equals("1")) {
            result = "等额本息";
        } else if (i.equals("2")) {
            result = "每月付息到期还本";
        } else if (i.equals("3")) {
            result = "到期一次性还本息";
        }
        return result;
    }

    public static void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (s.toString().contains(".")) {
                        if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                            s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                            editText.setText(s);
                            editText.setSelection(s.length());
                        }
                    }
                    if (s.toString().trim().substring(0).equals(".")) {
                        s = "0" + s;
                        editText.setText(s);
                        editText.setSelection(2);
                    }

                    if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                        if (!s.toString().substring(1, 2).equals(".")) {
                            editText.setText(s.subSequence(0, 1));
                            editText.setSelection(1);
                            return;
                        }
                    }
                    float end = 0;
                    if (lintype == 1) {
                        end = Float.parseFloat(investment_amount.getText().toString())
                                * (Float.parseFloat(nianhuashouyi) / 100 * date / 365);
                    } else if (lintype == 2) {
                        end = Float.parseFloat(investment_amount.getText().toString())
                                * (Float.parseFloat(nianhuashouyi) / 100 * date / 12);
                    }

                    expected_return.setText(end + "");
                    expected_return.setText(ChangeUtils.get_money((double) end));
                } else {
                    expected_return.setText("");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

    }

}
