package pojp;

public class failmonitorindetail2 {

    public String hour;

    public String city;
    public String province;
    public String country;
    public String fail_reason;
    public Integer fail_total_num;
    public Integer fail_num;


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

    public Integer getFail_total_num() {
        return fail_total_num;
    }

    public void setFail_total_num(Integer fail_total_num) {
        this.fail_total_num = fail_total_num;
    }

    public Integer getFail_num() {
        return fail_num;
    }

    public void setFail_num(Integer fail_num) {
        this.fail_num = fail_num;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
