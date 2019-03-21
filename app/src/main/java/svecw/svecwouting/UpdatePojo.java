package svecw.svecwouting;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Sowji on 12-07-2018.
 */
public class UpdatePojo {
    String reg;
    int delay;
    int permittedDays;
    public UpdatePojo(){

    }
    public UpdatePojo(String reg,int delay,int permittedDays) {
        this.reg = reg;
        this.delay = delay;
        this.permittedDays = permittedDays;
    }
    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public int getPermittedDays() {
        return permittedDays;
    }

    public void setPermittedDays(int permittedDays) {
        this.permittedDays = permittedDays;
    }


    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
