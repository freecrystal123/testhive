package pojp;

public class failmonitoring {

    public String hour;

    public Integer success_count;

    public Integer all_count;

    public double success_rate;

    public double avg7days_rate;


    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Integer getSuccess_count() {
        return success_count;
    }

    public void setSuccess_count(Integer success_count) {
        this.success_count = success_count;
    }

    public Integer getAll_count() {
        return all_count;
    }

    public void setAll_count(Integer all_count) {
        this.all_count = all_count;
    }

    public double getSuccess_rate() {
        return success_rate;
    }

    public void setSuccess_rate(double success_rate) {
        this.success_rate = success_rate;
    }

    public double getAvg7days_rate() {
        return avg7days_rate;
    }

    public void setAvg7days_rate(double avg7days_rate) {
        this.avg7days_rate = avg7days_rate;
    }
}
