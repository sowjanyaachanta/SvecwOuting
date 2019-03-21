package svecw.svecwouting;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Sowji on 28-04-2018.
 */
public class PermitPojo {
    private String  regnum;
    private String name;
    private String branch;
    private long year;
    private String place;
    private String reason;
    private String from_date;
    private String to_date;
    private String today;
    private String approver;
    private int permittedDays;


    public PermitPojo() {

    }

    public PermitPojo(String regnum, String name, String branch, long year, String place, String reason, String from_date, String to_date, String today, String approver, int permittedDays) {
        this.regnum = regnum;
        this.name = name;
        this.branch = branch;
        this.year = year;
        this.place = place;
        this.reason = reason;
        this.from_date = from_date;
        this.to_date = to_date;
        this.today = today;
        this.approver = approver;
        this.permittedDays = permittedDays;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public int getPermittedDays() {
        return permittedDays;
    }

    public void setPermittedDays(int permittedDays) {
        this.permittedDays = permittedDays;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getRegnum() {
        return regnum;
    }

    public void setRegnum(String regnum) {
        this.regnum = regnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }
}
