package svecw.svecwouting;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sowji on 07-05-2018.
 */
public class Pop extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    DatabaseReference databaseReference;
    PermitPojo pp;
    String cnt;
    public String a[];
    long diff;
    String diffDays;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cnt  = getIntent().getStringExtra("Number");
        a = cnt.split(" ");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Pop.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.approver, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle(a[0]);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        listView = (ListView) convertView.findViewById(R.id.list_item);
        listView.setAdapter(arrayAdapter);
        listView.setBackgroundColor(getResources().getColor(R.color.lightRed));
        databaseReference = FirebaseDatabase.getInstance().getReference("granted").child(a[0]);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    pp = ds.getValue(PermitPojo.class);
                    try {
                        String fmd = pp.getFrom_date().toString();
                        Date fm = new SimpleDateFormat("dd-mm-yyyy").parse(fmd);
                        String tod = pp.getTo_date().toString();
                        Date to = new SimpleDateFormat("dd-mm-yyyy").parse(tod);
                        long difference = Math.abs(fm.getTime() - to.getTime());
                        diff = difference / (24 * 60 * 60 * 1000);
                        //Convert long to String
                        diffDays = Long.toString(diff);
                    }
                    catch (Exception e) {
                        System.out.print("Error");
                    }
                    arrayList.add(pp.getReason() + "\n " + diffDays);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        alertDialog.show();
    }
}
