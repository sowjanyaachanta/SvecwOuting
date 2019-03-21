package svecw.svecwouting;

/**
 * Created by Sowji on 28-07-2018.
 */
public class ApproverPojo {
    private String Regnum;
    private String name;
    private long phnm;
    public ApproverPojo(){}
    public ApproverPojo(String Regnum, String name, long phnm) {
        this.Regnum = Regnum;
        this.name = name;
        this.phnm = phnm;
    }

    public String getRegnum() {
        return Regnum;
    }

    public void setRegnum(String regnum) {
        this.Regnum = regnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPhnm() {
        return phnm;
    }

    public void setPhnm(long phnm) {
        this.phnm = phnm;
    }
}
