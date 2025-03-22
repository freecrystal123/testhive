package pojp;

public class failmonitorindetail {

    public String hour;

    public String fail_reason;

    public Integer fail_count;

    public double fail_ratio;


    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getFail_reason() {
        return fail_reason;
    }

    public void setFail_reason(String fail_reason) {
        this.fail_reason = fail_reason;
    }

    public Integer getFail_count() {
        return fail_count;
    }

    public void setFail_count(Integer fail_count) {
        this.fail_count = fail_count;
    }

    public double getFail_ratio() {
        return fail_ratio;
    }

    public void setFail_ratio(double fail_ratio) {
        this.fail_ratio = fail_ratio;
    }
}
