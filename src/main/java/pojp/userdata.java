package pojp;

import java.util.Date;

public class userdata {

    String order_id;
    String user_id;
    Date order_time;
    Integer won_entries;
    Integer won_amount;
    String series_number;
    String entries;
    String cost_entries;
    String ns ;

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

    public Date getOrder_time() {
        return order_time;
    }

    public void setOrder_time(Date order_time) {
        this.order_time = order_time;
    }

    public Integer getWon_entries() {
        return won_entries;
    }

    public void setWon_entries(Integer won_entries) {
        this.won_entries = won_entries;
    }

    public String getSeries_number() {
        return series_number;
    }

    public void setSeries_number(String series_number) {
        this.series_number = series_number;
    }

    public Integer getWon_amount() {
        return won_amount;
    }

    public void setWon_amount(Integer won_amount) {
        this.won_amount = won_amount;
    }

    public String getCost_entries() {
        return cost_entries;
    }

    public void setCost_entries(String cost_entries) {
        this.cost_entries = cost_entries;
    }

    public String getNs() {
        return ns;
    }

    public void setNs(String ns) {
        this.ns = ns;
    }

    public String getEntries() {
        return entries;
    }

    public void setEntries(String entries) {
        this.entries = entries;
    }
}
