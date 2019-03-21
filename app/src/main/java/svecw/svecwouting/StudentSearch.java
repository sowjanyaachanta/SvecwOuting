package svecw.svecwouting;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Sowji on 07-01-2019.
 */
public class StudentSearch extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private DatePickerDialog datePickerDialog;
    DatabaseReference databaseReference;
    PermitPojo pp;
    private Date date1;
    FirebaseAuth firebaseAuth;
    private TextView dt1,dt2;
    private EditText et;
    private Calendar c;
    SimpleDateFormat sdf;
    private static final String SHARED_PREF_NAME = "mysharedpref";
    private static final String KEY_NAME = "keyname";
    public String id,key,str,from,to;
    StudentPojo stp;
    Button bt;
    Intent i;
    String dbName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        dbName = bundle.getString("dbName");
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        id = sp.getString(KEY_NAME, null);
        //Toast.makeText(getApplicationContext(),dbName,Toast.LENGTH_SHORT).show();
        dt1 = (TextView) findViewById(R.id.dt1);
        dt2 = (TextView) findViewById(R.id.dt2);
        et = (EditText) findViewById(R.id.et);
        bt = (Button)findViewById(R.id.btn);
        dt1.setVisibility(View.VISIBLE);
        dt2.setVisibility(View.GONE);
        et.setVisibility(View.GONE);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        c = Calendar.getInstance();
        String str = sdf.format(c.getTime());
       // Toast.makeText(getApplicationContext(),"today" +  str, Toast.LENGTH_LONG).show();
        dt1.setText(str);
        //getDate(dt1);
        //dateSearch(str);
        dt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate(dt1);
                from = dt1.getText().toString();
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateSearch();
                arrayList.clear();
            }
        });
    }

    @Override
    public void onBackPressed(){
        if (id.substring(0, 1).equals("F"))
            startActivity(new Intent(this, AppChoose.class));
        else
            startActivity(new Intent(this, WardenChoose.class));
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reports, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home: {
                if (id.substring(0, 1).equals("F")) {
                    startActivity(new Intent(this, AppChoose.class));
                } else
                    startActivity(new Intent(this, WardenChoose.class));
            }
            return  true;
        }
        //return super.onOptionsItemSelected(item);
        int id = item.getItemId();
        if(id == R.id.name) {
            et.setText("");
            dt1.setVisibility(View.GONE);
            dt2.setVisibility(View.GONE);
            et.setVisibility(View.VISIBLE);
            arrayList.clear();
            et = (EditText) findViewById(R.id.et);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrayList.clear();
                    key = et.getText().toString();
                    nameSearch(key);
                }
            });
        }
        if(id == R.id.range) {
            dt1.setVisibility(View.VISIBLE);
            dt1.setText("From Date(Tap here)");
            dt2.setVisibility(View.VISIBLE);
            dt2.setText("To Date(Tap here)");
            et.setVisibility(View.GONE);
          //  arrayList.clear();
            dt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDate(dt1);
                }
            });
            dt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDate(dt2);
                }
            });
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrayList.clear();
                    rangeSearch(from, to);
                }
            });
        }
        if(id == R.id.date) {
            dt1.setVisibility(View.VISIBLE);
            dt2.setVisibility(View.GONE);
            et.setVisibility(View.GONE);
            arrayList.clear();
            dt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDate(dt1);
                }
            });
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrayList.clear();
                    dateSearch();
                }
            });
        }
        if(id == R.id.num) {
            et.setText("");
            dt1.setVisibility(View.GONE);
            dt2.setVisibility(View.GONE);
            et.setVisibility(View.VISIBLE);
            arrayList.clear();
            et = (EditText) findViewById(R.id.et);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // arrayList.clear();
                    key = et.getText().toString();
                    idSearch(key);
                }
            });
        }
      //  if(id == R.id.graph) {
        //    startActivity(new Intent(StudentSearch.this,GraphReports.class));
        //}
        return super.onOptionsItemSelected(item);

    }


    public void nameSearch(final String k) {
        listView = (ListView) findViewById(R.id.list_item);
        listView.setAdapter(arrayAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("student");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String k1;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //stp = ds.getValue(StudentPojo.class);
                   // String re = ds.getKey();
                    k1 = ds.child("first_name").getValue(String.class);
                    for(String s : k1.split(" ")) {
                        if ((s.toUpperCase()).contentEquals(k.toUpperCase())) {
                            arrayList.add(ds.child("Regnum").getValue(String.class) + "\n " + ds.child("branch").getValue(String.class) + " - " + ds.child("year").getValue(Long.class));
                            arrayAdapter.notifyDataSetChanged();
                            //Toast.makeText(getApplicationContext(),"name",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void dateSearch() {
        arrayList.clear();
        listView = (ListView) findViewById(R.id.list_item);
        listView.setAdapter(arrayAdapter);
        from = dt1.getText().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference(dbName);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for(DataSnapshot ds1 : ds.getChildren()) {
                        pp = ds1.getValue(PermitPojo.class);
                        if(pp.getToday().contentEquals(from)) {
                            arrayList.add(pp.getRegnum() + "\n" + pp.getBranch() + "-" + pp.getYear());
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void rangeSearch(final String f,final String t) {
        from = dt1.getText().toString();
        to = dt2.getText().toString();
       // Toast.makeText(getApplicationContext(),"In rangeSearch" + from + " " + to,Toast.LENGTH_LONG).show();
        arrayList.clear();
        listView = (ListView) findViewById(R.id.list_item);
        listView.setAdapter(arrayAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference(dbName);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for(DataSnapshot ds1 : ds.getChildren()) {
                        pp = ds1.getValue(PermitPojo.class);
                        if(pp.getToday().compareTo(from) >= 0 && pp.getToday().compareTo(to) < 0) {
                            arrayList.add(pp.getRegnum() + "\n" + pp.getBranch() + "-" + pp.getYear() + "\n" + pp.getToday());
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

     public void getDate(final TextView dt) {
        int d1 = c.get(Calendar.DAY_OF_MONTH);
        int m1 = c.get(Calendar.MONTH);
        int y1 = c.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(StudentSearch.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                try {
                    date1 = sdf.parse(year + "-" + (monthOfYear + 1) + "-" + (dayOfMonth));
                    str = sdf.format(date1);
                    Date today = java.util.Calendar.getInstance().getTime();
                    if (date1.compareTo(today) <= 0) {
                        dt.setText(str);
                     //   Toast.makeText(getApplicationContext(),"str" + dt.getText(), Toast.LENGTH_SHORT).show();
                        //arrayList.clear();
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
    }

    public void idSearch(final String key) {
        arrayList.clear();
        listView = (ListView) findViewById(R.id.list_item);
        listView.setAdapter(arrayAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("student");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                   // stp = ds.getValue(StudentPojo.class);
                    String re = ds.getKey();
                  //  Toast.makeText(getApplicationContext(),re,Toast.LENGTH_LONG).show();

                   // String k = ds.child(re).child("Regnum").getValue(String.class);
                   // Toast.makeText(getApplicationContext(),k,Toast.LENGTH_LONG).show();

                    String k1 = re.substring(6,10);
//                    Toast.makeText(getApplicationContext(),k1,Toast.LENGTH_LONG).show();
                    if(k1.contentEquals(key)) {
                        arrayList.add(ds.child("Regnum").getValue(String.class) + "\n " + ds.child("branch").getValue(String.class) + " - " + ds.child("year").getValue(Long.class));
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}





