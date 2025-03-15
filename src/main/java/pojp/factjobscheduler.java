package pojp;

import java.sql.Date;

public class factjobscheduler {
    Integer job_id;
    String job_name;
    Date call_start_time;
    Date last_call_time;
    Integer job_frequency;
    Integer num_of_calls;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getJob_id() {
        return job_id;
    }

    public void setJob_id(Integer job_id) {
        this.job_id = job_id;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public Date getCall_start_time() {
        return call_start_time;
    }

    public void setCall_start_time(Date call_start_time) {
        this.call_start_time = call_start_time;
    }

    public Date getLast_call_time() {
        return last_call_time;
    }

    public void setLast_call_time(Date last_call_time) {
        this.last_call_time = last_call_time;
    }

    public Integer getJob_frequency() {
        return job_frequency;
    }

    public void setJob_frequency(Integer job_frequency) {
        this.job_frequency = job_frequency;
    }

    public Integer getNum_of_calls() {
        return num_of_calls;
    }

    public void setNum_of_calls(Integer num_of_calls) {
        this.num_of_calls = num_of_calls;
    }
}
