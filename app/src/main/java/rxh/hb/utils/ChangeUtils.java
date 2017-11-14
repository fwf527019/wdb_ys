package rxh.hb.utils;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/11/5.
 */
public class ChangeUtils {


    //金额校验，取小数点后两位
    public static String get_money(Double money) {
        String resul = null;
        DecimalFormat df = new DecimalFormat("0.00");
        if (money != null) {
            resul = df.format(money);
        } else {
            resul = "0.00";
        }
        return resul;
    }

    //信用等级校验
    public static String get_credit_rating(String credit_rating) {
        String result = null;
        if (credit_rating != null) {
            if (credit_rating.equals("1")) {
                result = "A";
            } else if (credit_rating.equals("2")) {
                result = "B";
            } else if (credit_rating.equals("3")) {
                result = "C";
            } else if (credit_rating.equals("4")) {
                result = "D";
            } else if (credit_rating.equals("5")) {
                result = "E";
            } else if (credit_rating.equals("6")) {
                result = "F";
            }
        } else {
            result = "参数为空";
        }
        return result;
    }


    //还款方式校验
    public static String get_repayment_method(String i) {
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


    //标的状态校验
    public static String get_mark_state(String state) {
        String result = null;
        if (state != null) {
            if (state.equals("1")) {
                result = "撤标";
            } else if (state.equals("2")) {
                result = "流标";
            } else if (state.equals("3")) {
                result = "待审核";
            } else if (state.equals("4")) {
                result = "待发布";
            } else if (state.equals("5")) {
                result = "投资中";
            } else if (state.equals("6")) {
                result = "待放款";
            } else if (state.equals("7")) {
                result = "待还款";
            } else if (state.equals("8")) {
                result = "完成";
            }
        } else {
            result = "参数为空";
        }
        return result;
    }

    //标的类型校验
    public static String get_mark_type(String j) {
        String result = null;
        if (j != null) {
            if (j.equals("1")) {
                result = "信用标";
            } else if (j.equals("2")) {
                result = "质押标";
            } else if (j.equals("3")) {
                result = "担保标";
            }
        } else {
            result = "参数为空";
        }
        return result;
    }

    //用户是否认证的校验
    public static String get_user_state(String state) {
        String result = null;
        if (state != null) {
            if (state.equals("1")) {
                result = "已认证";
            } else if (state.equals("2")) {
                result = "未认证";
            } else if (state.equals("3")) {
                result = "审核中";
            } else if (state.equals("4")) {
                result = "认证失败";
            }
        } else {
            result = "参数为空";
        }
        return result;
    }

    public static String get_receord_list(String type) {
        String result = null;
        if (type != null) {
            if (type.equals("1")) {
                result = "充值";
            } else if (type.equals("2")) {
                result = "提现";
            } else if (type.equals("3")) {
                result = "投资";
            } else if (type.equals("4")) {
                result = "还款";
            } else if (type.equals("5")) {
                result = "退款";
            } else if (type.equals("6")) {
                result = "其他";
            }
        } else {
            result = "其他";
        }
        return result;
    }

}
