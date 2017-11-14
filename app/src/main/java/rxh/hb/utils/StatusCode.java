package rxh.hb.utils;

public class StatusCode {

    public String getstatus(String num) {
        String result = null;
        if (num.equals("0")) {
            result = "操作已成功";
        } else if (num.equals("1")) {
            result = "请求失败";
        } else if (num.equals("200")) {
            result = "请求成功,请稍后";
        } else if (num.equals("500")) {
            result = "系统异常";
        } else if (num.equals("501")) {
            result = "参数为空";
        } else if (num.equals("502")) {
            result = "参数格式错误";
        } else if (num.equals("503")) {
            result = "参数长度错误";
        } else if (num.equals("100001")) {
            result = "需要注册的手机号码已存在";
        } else if (num.equals("100002")) {
            result = "需要找回密码的手机号码不存在";
        } else if (num.equals("100003")) {
            result = "发送验证码重新发送需要间隔一分钟";
        } else if (num.equals("100004")) {
            result = "该ＩＰ地址超出发送短信上限";
        } else if (num.equals("100005")) {
            result = "发送验证码失败";
        } else if (num.equals("200001")) {
            result = "用户注册账号未校验";
        } else if (num.equals("200002")) {
            result = "用户注册验证码已过期（30分钟过期）";
        } else if (num.equals("200003")) {
            result = "用户注册验证码错误";
        } else if (num.equals("200004")) {
            result = "用户注册账号已存在";
        } else if (num.equals("200005")) {
            result = "被推广的用户不存在";
        } else if (num.equals("300001")) {
            result = "用户登录账号或者密码错误";
        } else if (num.equals("300002")) {
            result = "用户登录账号未激活";
        } else if (num.equals("300003")) {
            result = "用户登录账号禁止登录";
        } else if (num.equals("300004")) {
            result = "密码错误次数上限（默认10次）";
        } else if (num.equals("400001")) {
            result = "权限校验TONKEN为空";
        } else if (num.equals("400002")) {
            result = "权限校验TONKEN格式化异常（格式错误）";
        } else if (num.equals("400003")) {
            result = "权限校验TONKEN已过期（过期时间2小时），请重新登录";
        } else if (num.equals("500001")) {
            result = "重置密码-手机格式错误";
        } else if (num.equals("500002")) {
            result = "重置密码-账号为校验";
        } else if (num.equals("500003")) {
            result = "重置密码-验证码已过期";
        } else if (num.equals("500004")) {
            result = "重置密码-验证码错误";
        } else if (num.equals("500005")) {
            result = "重置密码-密码长度错误";
        } else if (num.equals("500006")) {
            result = "重置密码-账号不存在";
        } else if (num.equals("500007")) {
            result = "原密码错误";
        } else if (num.equals("500008")) {
            result = "支付密码不存在";
        } else if (num.equals("500009")) {
            result = "支付密码错误";
        } else if (num.equals("600001")) {
            result = "资金对象不存在";
        } else if (num.equals("600002")) {
            result = "订单金额小于最小借出单位";
        } else if (num.equals("600003")) {
            result = "支付签名失败";
        } else if (num.equals("600004")) {
            result = "已超过投资日期";
        } else if (num.equals("600005")) {
            result = "投资金额大于剩余值";
        } else if (num.equals("600006")) {
            result = "请勿重复投资";
        } else if (num.equals("700001")) {
            result = "身份证号码错误";
        } else if (num.equals("700002")) {
            result = "银行卡错误";
        } else if (num.equals("700003")) {
            result = "用户已存在";
        } else if (num.equals("700004")) {
            result = "用户原密码错误";
        } else if (num.equals("700005")) {
            result = "上传文件为空";
        } else if (num.equals("800001")) {
            result = "充值对象不存在";
        } else if (num.equals("800002")) {
            result = "充值金额小于最小值";
        } else if (num.equals("900001")) {
            result = "余额不足";
        } else if (num.equals("900002")) {
            result = "未绑定银行卡";
        } else if (num.equals("800003")) {
            result = "积分不足";
        } else if (num.equals("800004")) {
            result = "库存不足，无法兑换";
        } else if (num.equals("700008")) {
            result = "此用户今日已签到";
        }else if(num.equals("600013")){
            result = "超过剩余金额";
        }else if(num.equals("600012")){
            result = "不能投资自己的标";
        }
        return result;
    }
}
