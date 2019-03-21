package svecw.svecwouting;

/**
 * Created by Sowji on 03-05-2018.
 */
public class ApproveView  {
    private String Regnum;
    private String Name;
    private String branch;
    private long year;
    private String res;
    private String place;
    private String fm;
    private String td;
    private long stunum;
    private long parnum;
    private String self;
    private String ApproverName;
    private int PermittedDays;
    private String today;

    public ApproveView() {}

    public ApproveView(String regnum, String name, String branch, long year, String res, String place, String fm, String td, long stunum, long parnum, String self, String approverName, int permittedDays, String today) {
        this.Regnum = regnum;
        this.Name = name;
        this.branch = branch;
        this.year = year;
        this.res = res;
        this.place = place;
        this.fm = fm;
        this.td = td;
        this.stunum = stunum;
        this.parnum = parnum;
        this.self = self;
        this.ApproverName = approverName;
        this.PermittedDays = permittedDays;
        this.today = today;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public int getPermittedDays() {
        return PermittedDays;
    }

    public void setPermittedDays(int permittedDays) {
        this.PermittedDays = permittedDays;
    }
    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }
    public String getApproverName() {
        return ApproverName;
    }

    public void setApproverName(String approverName) {
        this.ApproverName = approverName;
    }

    public long getStunum() {
        return stunum;
    }

    public void setStunum(long stunum) {
        this.stunum = stunum;
    }

    public long getParnum() {
        return parnum;
    }

    public void setParnum(long parnum) {
        this.parnum = parnum;
    }


    public String getRegnum() {
        return Regnum;
    }

    public void setRegnum(String regnum) {
        this.Regnum = regnum;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
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

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getFm() {
        return fm;
    }

    public void setFm(String fm) {
        this.fm = fm;
    }

    public String getTd() {
        return td;
    }

    public void setTd(String td) {
        this.td = td;
    }
}
