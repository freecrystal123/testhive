package pojp;

import java.util.Date;

public class bussinfo {

    public java.util.Date Time;
    public Integer register_num;
    public Integer recharger_num;
    public Integer lottery_order_num;
    public Integer login_result_num;
    public Integer pageview_num;

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }

    public Integer getRegister_num() {
        return register_num;
    }

    public void setRegister_num(Integer register_num) {
        this.register_num = register_num;
    }

    public Integer getRecharger_num() {
        return recharger_num;
    }

    public void setRecharger_num(Integer recharger_num) {
        this.recharger_num = recharger_num;
    }

    public Integer getLottery_order_num() {
        return lottery_order_num;
    }

    public void setLottery_order_num(Integer lottery_order_num) {
        this.lottery_order_num = lottery_order_num;
    }

    public Integer getLogin_result_num() {
        return login_result_num;
    }

    public void setLogin_result_num(Integer login_result_num) {
        this.login_result_num = login_result_num;
    }

    public Integer getPageview_num() {
        return pageview_num;
    }

    public void setPageview_num(Integer pageview_num) {
        this.pageview_num = pageview_num;
    }
}
