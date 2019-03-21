package svecw.svecwouting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sowji on 27-07-2018.
 */
public class StudentHistory extends AppCompatActivity {
    int hcnt = 0;
    String id;
    private ListView listView;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
TextView textView;
    DatabaseReference databaseReference;
    ApproveView av;
    String res,place;
    String fm,to;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requestlist);
        id = getIntent().getStringExtra("i");
        id = id.substring(0,10);
        listView = (ListView) findViewById(R.id.list_item);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
        textView = (TextView)findViewById(R.id.cnt);
        Toast.makeText(getApplicationContext(),id,Toast.LENGTH_LONG).show();
        databaseReference = FirebaseDatabase.getInstance().getReference("granted").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    av = ds.getValue(ApproveView.class);
                    place = ds.child("place").getValue(String.class);
                    res = ds.child("res").getValue(String.class);
                    // place = av.getPlace();
                    fm = ds.child("fm").getValue(String.class);
                    to = ds.child("td").getValue(String.class);
                    Toast.makeText(getApplicationContext(),av.getTd() + " ",Toast.LENGTH_LONG).show();
                    arrayList.add(res + " - " + av.getPermittedDays() + " days" + "\n " + place + "\n" + fm
                            + "  to  " + to);
                    arrayAdapter.notifyDataSetChanged();
                }
                hcnt = arrayList.size();
                textView.setText("     No of outings till now : " + hcnt);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
