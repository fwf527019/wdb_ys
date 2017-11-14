package rxh.hb.jsonbean;

/**
 * Created by Administrator on 2016/10/24.
 */
public class ProfitEntity {

    private String moneys;
    private String integral;
    private String zprofit;
    private String examine;//1,已认证，2，未认证，3，审核中，4，认证失败

    public String getMoneys() {
        return moneys;
    }

    public void setMoneys(String moneys) {
        this.moneys = moneys;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getZprofit() {
        return zprofit;
    }

    public void setZprofit(String zprofit) {
        this.zprofit = zprofit;
    }

    public String getExamine() {
        return examine;
    }

    public void setExamine(String examine) {
        this.examine = examine;
    }
}
