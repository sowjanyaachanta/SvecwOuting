package svecw.svecwouting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Sowji on 24-02-2019.
 */
public class GraphReports extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    GraphView graphView;
    LineGraphSeries series;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyymmdd");
    private Calendar mcalendar;
    private DateFormat mDateFormat = new SimpleDateFormat("yyyy:mm:dd");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphreport);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        graphView = (GraphView)findViewById(R.id.gview);
        mcalendar = Calendar.getInstance();
        series = new LineGraphSeries();
        graphView.addSeries(series);
        databaseReference = FirebaseDatabase.getInstance().getReference("graph");
        graphView.getGridLabelRenderer().setNumHorizontalLabels(4);
      /*  graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX) {
                    return sdf.format(new Date((long) value));
                }
                else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });*/
        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
        mcalendar = Calendar.getInstance();
        graphView.getViewport().setScrollable(true); // enables horizontal scrolling
        graphView.getViewport().setScrollableY(true); // enables vertical scrolling
        graphView.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graphView.getViewport().setScalableY(true); // enables vertical zooming and scrolling
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(1.0);
        graphView.getViewport().setMaxY(10.0);
        Date date = new GregorianCalendar(2019, 01, 24).getTime();
        graphView.getViewport().setMinX(date.getTime());
        Date date1 = new GregorianCalendar(2019, 01, 27).getTime();
        graphView.getViewport().setMaxX(date1.getTime());
        //graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getGridLabelRenderer().setHumanRounding(false);
        draw();
    }

    protected void draw() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataPoint[] dp = new DataPoint[(int) dataSnapshot.getChildrenCount()];
                int index = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    PointValue pv = ds.getValue(PointValue.class);
                    dp[index] = new DataPoint(pv.getX(),pv.getY());
                    index++;
                }
                series.resetData(dp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
