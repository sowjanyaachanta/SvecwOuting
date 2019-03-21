package svecw.svecwouting;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Sowji on 24-02-2019.
 */
public class PointValue{
    long x;
    int y;
    public PointValue() {

    }
    public PointValue(long x, int y) {
        this.x = x;
        this.y = y;
    }

    public long getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
