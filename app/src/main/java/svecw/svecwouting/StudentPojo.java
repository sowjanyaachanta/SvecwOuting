package svecw.svecwouting;

/**
 * Created by Sowji on 25-04-2018.
 */
public class StudentPojo {
    private String branch;
    private String first_name;
    private String hostel_name;
    private String last_name;
    private long parent_number;
    private String place;
    private String Regnum;
    private long room_number;
    private String section;
    private String self;
    private long student_number;
    private long year;
    public StudentPojo() {

    }
    public StudentPojo(String branch, String first_name, String hostel_name, String last_name, long parent_number,
                       String place, String Regnum, long room_number, String section, String self, long student_number, long year)
    {
        this.branch = branch;
        this.first_name = first_name;
        this.hostel_name = hostel_name;
        this.last_name = last_name;
        this.parent_number = parent_number;
        this.place = place;
        this.Regnum  = Regnum;
        this.room_number = room_number;
        this.section = section;
        this.self = self;
        this.student_number = student_number;
        this.year = year;
    }
    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getHostel_name() {
        return hostel_name;
    }

    public void setHostel_name(String hostel_name) {
        this.hostel_name = hostel_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public long getParent_number() {
        return parent_number;
    }

    public void setParent_number(long parent_number) {
        this.parent_number = parent_number;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getRegnum() {
        return Regnum;
    }

    public void setRegnum(String regnum) {
        this.Regnum = regnum;
    }

    public long getRoom_number() {
        return room_number;
    }

    public void setRoom_number(long room_number) {
        this.room_number = room_number;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public long getStudent_number() {
        return student_number;
    }

    public void setStudent_number(long student_number) {
        this.student_number = student_number;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }
   }
