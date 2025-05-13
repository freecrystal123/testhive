package pojp;

public class lossesmoney {
    String user_id;
    String register_date;
    double withdraw_amount;
    double recharge_amount;
    double net_loss;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRegister_date() {
        return register_date;
    }

    public void setRegister_date(String register_date) {
        this.register_date = register_date;
    }

    public double getWithdraw_amount() {
        return withdraw_amount;
    }

    public void setWithdraw_amount(double withdraw_amount) {
        this.withdraw_amount = withdraw_amount;
    }

    public double getRecharge_amount() {
        return recharge_amount;
    }

    public void setRecharge_amount(double recharge_amount) {
        this.recharge_amount = recharge_amount;
    }

    public double getNet_loss() {
        return net_loss;
    }

    public void setNet_loss(double net_loss) {
        this.net_loss = net_loss;
    }
}
