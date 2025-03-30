package pojp;

import java.sql.Date;
import java.sql.Timestamp;

public class orderwintosqlserver {

    public String  order_id;
    public String user_id;
    public String lottery;
    public String series_no;
    public Integer entries;
    public float turnover;
    public String creation_date;
    public float prize;
    public String winning_status;
    public String dateid;
    public String modified_time;


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLottery() {
        return lottery;
    }

    public void setLottery(String lottery) {
        this.lottery = lottery;
    }

    public String getSeries_no() {
        return series_no;
    }

    public void setSeries_no(String series_no) {
        this.series_no = series_no;
    }

    public Integer getEntries() {
        return entries;
    }

    public void setEntries(Integer entries) {
        this.entries = entries;
    }

    public float getTurnover() {
        return turnover;
    }

    public void setTurnover(float turnover) {
        this.turnover = turnover;
    }

    public float getPrize() {
        return prize;
    }

    public void setPrize(float prize) {
        this.prize = prize;
    }

    public String getWinning_status() {
        return winning_status;
    }

    public void setWinning_status(String winning_status) {
        this.winning_status = winning_status;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getDateid() {
        return dateid;
    }

    public void setDateid(String dateid) {
        this.dateid = dateid;
    }

    public String getModified_time() {
        return modified_time;
    }

    public void setModified_time(String modified_time) {
        this.modified_time = modified_time;
    }
}
