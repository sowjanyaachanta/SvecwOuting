package svecw.svecwouting;


/**
 * Created by Sowji on 26-04-2018.
 */
public class ApplyPojo {
    private String regnum;
    private String reason;
    private String place;
    private String from_date;
    private String to_date;
    private long parent_num;
    private long student_num;
    public ApplyPojo() {

    }

    public ApplyPojo(long parent_num, long student_num, String regnum, String reason, String place, String from_date, String to_date) {
        this.parent_num = parent_num;
        this.student_num = student_num;
        this.regnum = regnum;
        this.reason = reason;
        this.place = place;
        this.from_date = from_date;
        this.to_date = to_date;
    }

    public long getParent_num() {
        return parent_num;
    }

    public void setParent_num(long parent_num) {
        this.parent_num = parent_num;
    }

    public long getStudent_num() {
        return student_num;
    }

    public void setStudent_num(long student_num) {
        this.student_num = student_num;
    }

    public String getRegnum() {
        return regnum;
    }

    public void setRegnum(String regnum) {
        this.regnum = regnum;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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
