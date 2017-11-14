package rxh.hb.jsonbean;

/**
 * Created by Administrator on 2016/10/11.
 */
public class LoanApplicationEntity {

    private String createtime;
    private String brolin;
    private String tname;
    private String money;
    private String type;
    private String isno;

    public String getIsno() {
        return isno;
    }

    public void setIsno(String isno) {
        this.isno = isno;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getBrolin() {
        return brolin;
    }

    public void setBrolin(String brolin) {
        this.brolin = brolin;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }
}
