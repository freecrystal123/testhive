package pojp;

public class failreason {
    public String dateid;
    public String login_fail_reason;
    public Integer number_of_user;

    public String getDateid() {
        return dateid;
    }

    public void setDateid(String dateid) {
        this.dateid = dateid;
    }

    public String getLogin_fail_reason() {
        return login_fail_reason;
    }

    public void setLogin_fail_reason(String login_fail_reason) {
        this.login_fail_reason = login_fail_reason;
    }

    public Integer getNumber_of_user() {
        return number_of_user;
    }

    public void setNumber_of_user(Integer number_of_user) {
        this.number_of_user = number_of_user;
    }
}
