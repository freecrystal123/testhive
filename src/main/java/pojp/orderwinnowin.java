package pojp;

import java.util.Date;

public class orderwinnowin {

    public String  uid;
    public String lottery_type;
    public String order_id;
    public Integer investment_amount;
    public Integer lottery_entries;
    public String order_time;
    public String series_number;
    public String winning_flag;
    public Integer winning_amount;
    public String winning_time;

    public Integer getInvestment_amount() {
        return investment_amount;
    }

    public void setInvestment_amount(Integer investment_amount) {
        this.investment_amount = investment_amount;
    }

    public String getSeries_number() {
        return series_number;
    }

    public void setSeries_number(String series_number) {
        this.series_number = series_number;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLottery_type() {
        return lottery_type;
    }

    public void setLottery_type(String lottery_type) {
        this.lottery_type = lottery_type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }


    public Integer getLottery_entries() {
        return lottery_entries;
    }

    public void setLottery_entries(Integer lottery_entries) {
        this.lottery_entries = lottery_entries;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getWinning_flag() {
        return winning_flag;
    }

    public void setWinning_flag(String winning_flag) {
        this.winning_flag = winning_flag;
    }

    public Integer getWinning_amount() {
        return winning_amount;
    }

    public void setWinning_amount(Integer winning_amount) {
        this.winning_amount = winning_amount;
    }

    public String getWinning_time() {
        return winning_time;
    }

    public void setWinning_time(String winning_time) {
        this.winning_time = winning_time;
    }
}
