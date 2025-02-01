package pojp;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.sql.Date;

public class orderwin0122 {

    public BigInteger uid;
    public String lottery_type;
    public BigInteger order_id;
    public Double investment_amount;
    public Integer lottery_entries;
    public String ordertime;
    public Integer series_number;
    public String winning_flag;
    public Double winning_amount;
    public Integer draw_period;
    public String dateid;

    public BigInteger getUid() {
        return uid;
    }

    public void setUid(BigInteger uid) {
        this.uid = uid;
    }

    public String getLottery_type() {
        return lottery_type;
    }

    public void setLottery_type(String lottery_type) {
        this.lottery_type = lottery_type;
    }

    public BigInteger getOrder_id() {
        return order_id;
    }

    public void setOrder_id(BigInteger order_id) {
        this.order_id = order_id;
    }

    public Double getInvestment_amount() {
        return investment_amount;
    }

    public void setInvestment_amount(Double investment_amount) {
        this.investment_amount = investment_amount;
    }

    public Integer getLottery_entries() {
        return lottery_entries;
    }

    public void setLottery_entries(Integer lottery_entries) {
        this.lottery_entries = lottery_entries;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public Integer getSeries_number() {
        return series_number;
    }

    public void setSeries_number(Integer series_number) {
        this.series_number = series_number;
    }

    public String getWinning_flag() {
        return winning_flag;
    }

    public void setWinning_flag(String winning_flag) {
        this.winning_flag = winning_flag;
    }

    public Double getWinning_amount() {
        return winning_amount;
    }

    public void setWinning_amount(Double winning_amount) {
        this.winning_amount = winning_amount;
    }

    public Integer getDraw_period() {
        return draw_period;
    }

    public void setDraw_period(Integer draw_period) {
        this.draw_period = draw_period;
    }

    public String getDateid() {
        return dateid;
    }

    public void setDateid(String dateid) {
        this.dateid = dateid;
    }
}
