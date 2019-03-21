package svecw.svecwouting;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sowji on 06-07-2018.
 */
public class WardenPendingList extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    DatabaseReference databaseReference;
    ApproveView av;
    FirebaseAuth firebaseAuth;
    String s;
    private DatePickerDialog datePickerDialog;
    private Date date1;
    private TextView dt;
    private Calendar c;
    SimpleDateFormat sdf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.approver);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        s = getIntent().getStringExtra("id");
        dt = (TextView)findViewById(R.id.dt);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        c = Calendar.getInstance();
        String str = sdf.format(c.getTime());
        //Toast.makeText(getApplicationContext(),"today" +  str, Toast.LENGTH_LONG).show();
        dt.setText(str);
        display();
        // Toast.makeText(getApplicationContext(),date1.toString(),Toast.LENGTH_LONG).show();
        dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int d1 = c.get(Calendar.DAY_OF_MONTH);
                int m1 = c.get(Calendar.MONTH);
                int y1 = c.get(Calendar.YEAR);
                datePickerDialog = new DatePickerDialog(WardenPendingList.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            // sdf = new SimpleDateFormat("yyyy-MM-dd");
                            //  String str2 = sdf.format(c.getTime());
                            //   Toast.makeText(getApplicationContext(),"str2 " + str2,Toast.LENGTH_LONG).show();
                            date1 = sdf.parse(year + "-" + (monthOfYear + 1) + "-" + (dayOfMonth));
                            String str2 = sdf.format(date1);
                            // Toast.makeText(getApplicationContext(),"Date " + str2,Toast.LENGTH_LONG).show();
                            Date today = java.util.Calendar.getInstance().getTime();
                            if (date1.compareTo(today) <= 0) {
                                dt.setText(str2);
                                arrayList.clear();
                                display();
                            } else {
                                Toast.makeText(getApplicationContext(), "Choose before current date", Toast.LENGTH_SHORT).show();
                                dt.setText("Date (Tap here)");
                            }
                        } catch (ParseException p) {
                            System.out.print("Date error");
                        }
                    }
                }, y1, m1, d1);
                datePickerDialog.show();
                arrayList.clear();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFromList = (String) (listView.getItemAtPosition(position));
                Intent intent = new Intent(getApplicationContext(), WardenStuDetails.class);
                intent.putExtra("Number", selectedFromList + " " + "pending");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed(){
        // Toast.makeText(getApplicationContext(),"Press logout to move to Login page",Toast.LENGTH_LONG).show();
        startActivity(new Intent(this,WardenChoose.class));
    }
    void display() {
        databaseReference = FirebaseDatabase.getInstance().getReference("pending");
        listView = (ListView) findViewById(R.id.list_item);
        listView.setAdapter(arrayAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             //   listView.setBackgroundColor(Color.rgb(255, 255, 255));
                arrayList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    av = ds.getValue(ApproveView.class);
                    arrayList.add(av.getRegnum().toString());
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        });

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFromList = (String) (listView.getItemAtPosition(position));
                Intent intent = new Intent(getApplicationContext(), AppStuDetails.class);
                intent.putExtra("Number", selectedFromList + s);
                startActivity(intent);

            }
        });*/
    }
    }
