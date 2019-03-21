package svecw.svecwouting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sowji on 27-04-2018.
 */
public class AppRequestList extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    DatabaseReference databaseReference;
    ApplyPojo req;
    FirebaseAuth firebaseAuth;
    private static final String SHARED_PREF_NAME = "mysharedpref";
    private static final String KEY_NAME = "keyname";
    TextView tv;
    String s;
    int cnt = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requestlist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        s = sp.getString(KEY_NAME, null);
        tv = (TextView)findViewById(R.id.cnt);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        databaseReference = FirebaseDatabase.getInstance().getReference("requests");
        listView = (ListView) findViewById(R.id.list_item);
        listView.setAdapter(arrayAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    req = ds.getValue(ApplyPojo.class);
                    arrayList.add(req.getRegnum().toString());
                    arrayAdapter.notifyDataSetChanged();
                }
                cnt = arrayList.size();
                tv.setText("     No of requests : " + cnt);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFromList = (String) (listView.getItemAtPosition(position));
                Intent intent = new Intent(getApplicationContext(), AppStuDetails.class);
                intent.putExtra("Number", selectedFromList + s);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(this,AppChoose.class));
    }
}