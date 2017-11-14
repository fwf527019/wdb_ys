package rxh.hb.utils;

import java.util.ArrayList;
import java.util.List;

import rxh.hb.jsonbean.ActivityCenterActivityLVBean;
import rxh.hb.jsonbean.ActivityCenterDetailsActivityBean;
import rxh.hb.jsonbean.AddBankCardActivityBean;
import rxh.hb.jsonbean.AddIdentityAuthenticationActivityBean;
import rxh.hb.jsonbean.AllRegisterActivityBean;
import rxh.hb.jsonbean.ConductFinancialTransactionsFragmentBean;
import rxh.hb.jsonbean.FinancialProjectDetailsActivityBean;
import rxh.hb.jsonbean.FinancialProjectDetailsActivityBeanDetails;
import rxh.hb.jsonbean.ImmediateRepaymentEntity;
import rxh.hb.jsonbean.IntegralDetailsEntity;
import rxh.hb.jsonbean.InvestmentFragmentActivityLVBean;
import rxh.hb.jsonbean.LoanApplicationEntity;
import rxh.hb.jsonbean.LoginBean;
import rxh.hb.jsonbean.MyAccountActivityBean;
import rxh.hb.jsonbean.MyIntegralEntity;
import rxh.hb.jsonbean.MyLoanEntity;
import rxh.hb.jsonbean.MyMessageActivityLVBean;
import rxh.hb.jsonbean.MyMonthlyBillBean;
import rxh.hb.jsonbean.NewsBulletinActivityBean;
import rxh.hb.jsonbean.PlatformRepaymentLVBean;
import rxh.hb.jsonbean.ProfitEntity;
import rxh.hb.jsonbean.ServiceCentreSecondActivityBean;
import rxh.hb.jsonbean.ServiceCentreSecondNextActivitySBBean;
import rxh.hb.jsonbean.TheHomePageFragmentBean;
import rxh.hb.jsonbean.TheHomePageFragmentimgBean;
import rxh.hb.jsonbean.TransactionRecordEntity;
import rxh.hb.jsonbean.TransactionRecordsActivityLVBean;
import rxh.hb.jsonbean.TransactionRecordsActivityLVBean1;
import rxh.hb.jsonbean.TransactionRecordsActivityLVBean2;
import rxh.hb.jsonbean.TransferThePossessionOfFragmentLVBean;
import rxh.hb.jsonbean.WithdrawalsBean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {

    // 解析用户注册时，服务器返回的json数据
    public static AllRegisterActivityBean register(String data) {
        AllRegisterActivityBean reister = new AllRegisterActivityBean();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        reister = gson.fromJson(obj.toString(), AllRegisterActivityBean.class);
        return reister;

    }

    // 解析获取验证码时，服务器返回的json数据
    public static AllRegisterActivityBean getverificationcode(String data) {
        AllRegisterActivityBean reister = new AllRegisterActivityBean();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        reister = gson.fromJson(obj.toString(), AllRegisterActivityBean.class);
        return reister;

    }

    // 解析登陆时，服务器返回的json数据
    public static LoginBean getLoginBean(String data) {
        LoginBean login = new LoginBean();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        login = gson.fromJson(obj.toString(), LoginBean.class);
        return login;

    }

    // 解析投资界面的title时，服务器返回的json数据
    public static List<ConductFinancialTransactionsFragmentBean> gettitle(
            String data) {
        List<ConductFinancialTransactionsFragmentBean> conductFinancialTransactionsFragmentBeans = new ArrayList<ConductFinancialTransactionsFragmentBean>();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONArray jsonArray = obj.getJSONArray("obj");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                ConductFinancialTransactionsFragmentBean cbBean = new ConductFinancialTransactionsFragmentBean();
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                cbBean = gson.fromJson(jsonArray.get(i).toString(),
                        ConductFinancialTransactionsFragmentBean.class);
                conductFinancialTransactionsFragmentBeans.add(cbBean);
            }
        }
        return conductFinancialTransactionsFragmentBeans;

    }

    // 解析投资界面的内容时，服务器返回的json数据
    public static List<InvestmentFragmentActivityLVBean> getcontent(String data) {
        List<InvestmentFragmentActivityLVBean> investmentFragmentActivityLVBeans = new ArrayList<InvestmentFragmentActivityLVBean>();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONObject jsonObject = obj.getJSONObject("obj");
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                InvestmentFragmentActivityLVBean ibBean = new InvestmentFragmentActivityLVBean();
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                ibBean = gson.fromJson(jsonArray.get(i).toString(),
                        InvestmentFragmentActivityLVBean.class);
                investmentFragmentActivityLVBeans.add(ibBean);
            }
        }
        return investmentFragmentActivityLVBeans;

    }

    // 解析收益界面时，服务器返回的json数据
    public static FinancialProjectDetailsActivityBean getBeans(String data) {
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONObject object = obj.getJSONObject("obj");
        FinancialProjectDetailsActivityBean cbBean = new FinancialProjectDetailsActivityBean();
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        cbBean = gson.fromJson(object.toString(),
                FinancialProjectDetailsActivityBean.class);
        return cbBean;

    }

    // 解析投资界面的内容时，服务器返回的json数据
    public static List<TheHomePageFragmentBean> gethome(String data) {
        List<TheHomePageFragmentBean> theHomePageFragmentBeans = new ArrayList<TheHomePageFragmentBean>();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONObject jsonObject = obj.getJSONObject("obj");
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                TheHomePageFragmentBean ibBean = new TheHomePageFragmentBean();
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                ibBean = gson.fromJson(jsonArray.get(i).toString(),
                        TheHomePageFragmentBean.class);
                theHomePageFragmentBeans.add(ibBean);
            }
        }
        return theHomePageFragmentBeans;

    }

    // 解析个人中心时，服务器返回的json数据
    public static MyAccountActivityBean getinMyAccountActivityBean(String data) {
        MyAccountActivityBean myAccountActivityBean = new MyAccountActivityBean();
        JSONObject obj = JSONObject.parseObject(data);
        JSONObject object = obj.getJSONObject("obj");
        // Gson解析Json
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        myAccountActivityBean = gson.fromJson(object.toString(),
                MyAccountActivityBean.class);
        return myAccountActivityBean;

    }

    // 解析身份认证界面时，服务器返回的json数据
    public static AddIdentityAuthenticationActivityBean getAddIdentityAuthenticationActivityBean(
            String data) {
        AddIdentityAuthenticationActivityBean addIdentityAuthenticationActivityBean = new AddIdentityAuthenticationActivityBean();
        JSONObject obj = JSONObject.parseObject(data);
        JSONObject object = obj.getJSONObject("obj");
        if (object != null) {
            // Gson解析Json
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            addIdentityAuthenticationActivityBean = gson.fromJson(
                    object.toString(),
                    AddIdentityAuthenticationActivityBean.class);
        }
        return addIdentityAuthenticationActivityBean;

    }

    // 解析收益图片时，服务器返回的json数据
    public static List<TheHomePageFragmentimgBean> getHomePageFragmentimgBeans(
            String data) {
        List<TheHomePageFragmentimgBean> theHomePageFragmentBeans = new ArrayList<TheHomePageFragmentimgBean>();
        JSONObject obj = JSONObject.parseObject(data);
        JSONArray jsonArray = obj.getJSONArray("obj");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                TheHomePageFragmentimgBean theHomePageFragmentBean = new TheHomePageFragmentimgBean();
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                theHomePageFragmentBean = gson.fromJson(jsonArray.get(i)
                        .toString(), TheHomePageFragmentimgBean.class);
                theHomePageFragmentBeans.add(theHomePageFragmentBean);
            }
        }
        return theHomePageFragmentBeans;

    }

    // 解析银行卡认证界面时，服务器返回的json数据
    public static AddBankCardActivityBean getBankCardActivityBean(String data) {
        AddBankCardActivityBean addBankCardActivityBean = new AddBankCardActivityBean();
        JSONObject obj = JSONObject.parseObject(data);
        JSONObject object = obj.getJSONObject("obj");
        if (object != null) {
            // Gson解析Json
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            addBankCardActivityBean = gson.fromJson(object.toString(),
                    AddBankCardActivityBean.class);
        }
        return addBankCardActivityBean;

    }

    // 解析活动中心界面的内容时，服务器返回的json数据
    public static List<ActivityCenterActivityLVBean> getActivityLVBeans(
            String data) {
        List<ActivityCenterActivityLVBean> activityLVBeans = new ArrayList<ActivityCenterActivityLVBean>();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONObject jsonObject = obj.getJSONObject("obj");
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                ActivityCenterActivityLVBean ibBean = new ActivityCenterActivityLVBean();
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                ibBean = gson.fromJson(jsonArray.get(i).toString(),
                        ActivityCenterActivityLVBean.class);
                activityLVBeans.add(ibBean);
            }
        }
        return activityLVBeans;

    }

    // 解析新闻公告界面的内容时，服务器返回的json数据
    public static List<NewsBulletinActivityBean> getBulletinActivityBeans(
            String data) {
        List<NewsBulletinActivityBean> newsBulletinActivityBeans = new ArrayList<NewsBulletinActivityBean>();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONObject jsonObject = obj.getJSONObject("obj");
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                NewsBulletinActivityBean ibBean = new NewsBulletinActivityBean();
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                ibBean = gson.fromJson(jsonArray.get(i).toString(),
                        NewsBulletinActivityBean.class);
                newsBulletinActivityBeans.add(ibBean);
            }
        }
        return newsBulletinActivityBeans;

    }

    // 解析活动中心详情时，服务器返回的json数据
    public static ActivityCenterDetailsActivityBean getacActivityBean(
            String data) {
        ActivityCenterDetailsActivityBean cbBean = new ActivityCenterDetailsActivityBean();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONObject object = obj.getJSONObject("obj");
        if (object != null) {
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            cbBean = gson.fromJson(object.toString(),
                    ActivityCenterDetailsActivityBean.class);
        }
        return cbBean;

    }

    // 解析我的消息列表的内容时，服务器返回的json数据
    public static List<MyMessageActivityLVBean> getMessageActivityLVBeans(
            String data) {
        List<MyMessageActivityLVBean> myMessageActivityLVBeans = new ArrayList<MyMessageActivityLVBean>();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONObject jsonObject = obj.getJSONObject("obj");
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                MyMessageActivityLVBean ibBean = new MyMessageActivityLVBean();
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                ibBean = gson.fromJson(jsonArray.get(i).toString(),
                        MyMessageActivityLVBean.class);
                myMessageActivityLVBeans.add(ibBean);
            }
        }
        return myMessageActivityLVBeans;

    }

    // 解析用户注册时，服务器返回的json数据
    public static String getcode(String data) {
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        String code = obj.getString("code");
        return code;

    }

    // 解析获得服务中心标题时，服务器返回的json数据
    public static List<ServiceCentreSecondActivityBean> getservertitle(
            String data) {
        List<ServiceCentreSecondActivityBean> serviceCentreSecondActivityBeans = new ArrayList<ServiceCentreSecondActivityBean>();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONArray jsonArray = obj.getJSONArray("obj");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                ServiceCentreSecondActivityBean cbBean = new ServiceCentreSecondActivityBean();
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                cbBean = gson.fromJson(jsonArray.get(i).toString(),
                        ServiceCentreSecondActivityBean.class);
                serviceCentreSecondActivityBeans.add(cbBean);
            }
        }
        return serviceCentreSecondActivityBeans;

    }

    // 解析服务中心列表的内容时，服务器返回的json数据
    public static List<ServiceCentreSecondNextActivitySBBean> getSbBeans(
            String data) {
        List<ServiceCentreSecondNextActivitySBBean> sbBeans = new ArrayList<ServiceCentreSecondNextActivitySBBean>();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONObject jsonObject = obj.getJSONObject("obj");
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                ServiceCentreSecondNextActivitySBBean ibBean = new ServiceCentreSecondNextActivitySBBean();
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                ibBean = gson.fromJson(jsonArray.get(i).toString(),
                        ServiceCentreSecondNextActivitySBBean.class);
                sbBeans.add(ibBean);
            }
        }
        return sbBeans;

    }

    // 解析交易记录的内容时，服务器返回的json数据
    public static List<TransferThePossessionOfFragmentLVBean> getppBeans(
            String data) {
        List<TransferThePossessionOfFragmentLVBean> ppBeans = new ArrayList<TransferThePossessionOfFragmentLVBean>();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONObject jsonObject = obj.getJSONObject("obj");
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                TransferThePossessionOfFragmentLVBean ibBean = new TransferThePossessionOfFragmentLVBean();
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                ibBean = gson.fromJson(jsonArray.get(i).toString(),
                        TransferThePossessionOfFragmentLVBean.class);
                ppBeans.add(ibBean);
            }
        }
        return ppBeans;

    }

    // 解析平台还款的内容时，服务器返回的json数据
    public static List<PlatformRepaymentLVBean> getplvBeans(String data) {
        List<PlatformRepaymentLVBean> ppBeans = new ArrayList<PlatformRepaymentLVBean>();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONObject jsonObject = obj.getJSONObject("obj");
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                PlatformRepaymentLVBean ibBean = new PlatformRepaymentLVBean();
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                ibBean = gson.fromJson(jsonArray.get(i).toString(),
                        PlatformRepaymentLVBean.class);
                ppBeans.add(ibBean);
            }
        }
        return ppBeans;

    }

    // 解析我的月账单的内容时，服务器返回的json数据
    public static List<MyMonthlyBillBean> getmonBean(String data) {
        List<MyMonthlyBillBean> ppBeans = new ArrayList<MyMonthlyBillBean>();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONArray jsonArray = obj.getJSONArray("obj");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                MyMonthlyBillBean cbBean = new MyMonthlyBillBean();
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                cbBean = gson.fromJson(jsonArray.get(i).toString(),
                        MyMonthlyBillBean.class);
                ppBeans.add(cbBean);
            }
        }
        return ppBeans;

    }

    // 解析我的项目的内容时，服务器返回的json数据
    public static List<TransactionRecordsActivityLVBean> getmpBeans(String data) {
        List<TransactionRecordsActivityLVBean> ppBeans = new ArrayList<TransactionRecordsActivityLVBean>();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONObject jsonObject = obj.getJSONObject("obj");
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                TransactionRecordsActivityLVBean ibBean = new TransactionRecordsActivityLVBean();
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                ibBean = gson.fromJson(jsonArray.get(i).toString(),
                        TransactionRecordsActivityLVBean.class);
                ppBeans.add(ibBean);
            }
        }
        return ppBeans;

    }
    // 解析我的项目的内容时，服务器返回的json数据
    public static List<TransactionRecordsActivityLVBean2> getmpsBeans(String data) {
        List<TransactionRecordsActivityLVBean2> ppBeans = new ArrayList<TransactionRecordsActivityLVBean2>();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONObject jsonObject = obj.getJSONObject("obj");
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                TransactionRecordsActivityLVBean2 ibBean = new TransactionRecordsActivityLVBean2();
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                ibBean = gson.fromJson(jsonArray.get(i).toString(),
                        TransactionRecordsActivityLVBean2.class);
                ppBeans.add(ibBean);
            }
        }
        return ppBeans;

    }
    // 解析我的项目的内容时，服务器返回的json数据
    public static List<TransactionRecordsActivityLVBean1> getmpBeans1(String data) {
        List<TransactionRecordsActivityLVBean1> ppBeans = new ArrayList<TransactionRecordsActivityLVBean1>();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONObject jsonObject = obj.getJSONObject("obj");
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                TransactionRecordsActivityLVBean1 ibBean = new TransactionRecordsActivityLVBean1();
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                ibBean = gson.fromJson(jsonArray.get(i).toString(),
                        TransactionRecordsActivityLVBean1.class);
                ppBeans.add(ibBean);
            }
        }
        return ppBeans;

    }

    // 解析用户注册时，服务器返回的json数据
    public static WithdrawalsBean getw(String data) {
        WithdrawalsBean w = new WithdrawalsBean();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONObject jsonObject = obj.getJSONObject("obj");
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        w = gson.fromJson(jsonObject.toString(), WithdrawalsBean.class);
        return w;

    }

    // 解析用户购买产品详情界面时，服务器返回的json数据
    public static FinancialProjectDetailsActivityBeanDetails getu(String data) {
        FinancialProjectDetailsActivityBeanDetails userDetails = new FinancialProjectDetailsActivityBeanDetails();
        JSONObject obj = JSONObject.parseObject(data);
        JSONObject jsonObject = obj.getJSONObject("obj");
        // Gson解析Json
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        userDetails = gson.fromJson(jsonObject.toString(),
                FinancialProjectDetailsActivityBeanDetails.class);
        return userDetails;

    }

    //获取昨日收益
    public static ProfitEntity getprofit(String data) {
        ProfitEntity entity = new ProfitEntity();
        JSONObject obj = JSONObject.parseObject(data);
        JSONObject jsonObject = obj.getJSONObject("obj");
        // Gson解析Json
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        entity = gson.fromJson(jsonObject.toString(),
                ProfitEntity.class);
        return entity;

    }


    // 解析我的借款的内容时，服务器返回的json数据
    public static List<MyLoanEntity> getmyloan(String data) {
        List<MyLoanEntity> ppBeans = new ArrayList<MyLoanEntity>();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONObject jsonObject = obj.getJSONObject("obj");
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                MyLoanEntity ibBean = new MyLoanEntity();
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                ibBean = gson.fromJson(jsonArray.get(i).toString(),
                        MyLoanEntity.class);
                ppBeans.add(ibBean);
            }
        }
        return ppBeans;

    }


    // 解析我的借款申请的内容时，服务器返回的json数据
    public static List<LoanApplicationEntity> getloan(String data) {
        List<LoanApplicationEntity> ppBeans = new ArrayList<LoanApplicationEntity>();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONObject jsonObject = obj.getJSONObject("obj");
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                LoanApplicationEntity ibBean = new LoanApplicationEntity();
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                ibBean = gson.fromJson(jsonArray.get(i).toString(),
                        LoanApplicationEntity.class);
                ppBeans.add(ibBean);
            }
        }
        return ppBeans;

    }

    // 解析我的借款申请还款的内容时，服务器返回的json数据
    public static List<ImmediateRepaymentEntity> getrepay(String data) {
        List<ImmediateRepaymentEntity> ppBeans = new ArrayList<ImmediateRepaymentEntity>();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONObject jsonObject = obj.getJSONObject("obj");
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                ImmediateRepaymentEntity ibBean = new ImmediateRepaymentEntity();
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                ibBean = gson.fromJson(jsonArray.get(i).toString(),
                        ImmediateRepaymentEntity.class);
                ppBeans.add(ibBean);
            }
        }
        return ppBeans;

    }


    // 解析我积分商品的内容时，服务器返回的json数据
    public static List<MyIntegralEntity> getmyintegral(String data) {
        List<MyIntegralEntity> ppBeans = new ArrayList<MyIntegralEntity>();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONObject jsonObject = obj.getJSONObject("obj");
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                MyIntegralEntity ibBean = new MyIntegralEntity();
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                ibBean = gson.fromJson(jsonArray.get(i).toString(),
                        MyIntegralEntity.class);
                ppBeans.add(ibBean);
            }
        }
        return ppBeans;
    }


    // 解析我积分详情的内容时，服务器返回的json数据
    public static List<IntegralDetailsEntity> getmyintegraldetails(String data) {
        List<IntegralDetailsEntity> ppBeans = new ArrayList<IntegralDetailsEntity>();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONObject jsonObject = obj.getJSONObject("obj");
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                IntegralDetailsEntity ibBean = new IntegralDetailsEntity();
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                ibBean = gson.fromJson(jsonArray.get(i).toString(),
                        IntegralDetailsEntity.class);
                ppBeans.add(ibBean);
            }
        }
        return ppBeans;
    }

    // 解析我交易记录的内容时，服务器返回的json数据
    public static List<TransactionRecordEntity> get_transaction_record(String data) {
        List<TransactionRecordEntity> ppBeans = new ArrayList<TransactionRecordEntity>();
        JSONObject obj = JSONObject.parseObject(data);
        // Gson解析Json
        JSONObject jsonObject = obj.getJSONObject("obj");
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                TransactionRecordEntity ibBean = new TransactionRecordEntity();
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                ibBean = gson.fromJson(jsonArray.get(i).toString(),
                        TransactionRecordEntity.class);
                ppBeans.add(ibBean);
            }
        }
        return ppBeans;

    }
}
