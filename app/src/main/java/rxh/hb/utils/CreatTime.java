package rxh.hb.utils;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/10/31.
 */
public class CreatTime {


    //获取当前年
    public static int getyear() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        return year;
    }

    //获取当前月
    public static int getmonth() {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        return month;
    }

    //获取当前日
    public static int getday() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        return day;
    }

    //获取当前小时
    public static int gethours() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        return hour;
    }

    //获取当前分钟
    public static int getminutes() {
        Calendar c = Calendar.getInstance();
        int minute = c.get(Calendar.MINUTE);
        return minute;
    }

    //获取当前秒数
    public static int getsecond() {
        Calendar c = Calendar.getInstance();
        int second = c.get(Calendar.SECOND);
        return second;
    }

}
