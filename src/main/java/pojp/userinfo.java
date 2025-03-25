package pojp;

import java.sql.Date;

public class userinfo {

    public String  uid;
    public String first_visit_source;
    public String register_time;
    public String country;
    public String city;
    public String birthday;
    public String update_date;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirst_visit_source() {
        return first_visit_source;
    }

    public void setFirst_visit_source(String first_visit_source) {
        this.first_visit_source = first_visit_source;
    }

    public String getRegister_time() {
        return register_time;
    }

    public void setRegister_time(String register_time) {
        this.register_time = register_time;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }
}
