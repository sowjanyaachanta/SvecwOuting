package svecw.svecwouting;

import android.content.Intent;
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

import java.util.ArrayList;

/**
 * Created by Sowji on 02-05-2018.
 */
public class WardenRequestList extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    DatabaseReference databaseReference;
    ApproveView av;
    FirebaseAuth firebaseAuth;
    String s;
    int cnt = 0;
    TextView tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requestlist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tv = (TextView)findViewById(R.id.cnt);
        s = getIntent().getStringExtra("id");
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        databaseReference = FirebaseDatabase.getInstance().getReference("approved");
        listView = (ListView) findViewById(R.id.list_item);
        listView.setAdapter(arrayAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // listView.setBackgroundColor(Color.rgb(255, 255, 255));
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    av = ds.getValue(ApproveView.class);
                    arrayList.add(av.getRegnum().toString());
                    arrayAdapter.notifyDataSetChanged();
                }
                cnt = arrayList.size();
                tv.setText("     No of requests : " + cnt);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Cancelled",Toast.LENGTH_LONG).show();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFromList = (String) (listView.getItemAtPosition(position));
                Intent intent = new Intent(WardenRequestList.this, WardenStuDetails.class);
                intent.putExtra("Number", selectedFromList + " " + "approved");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed(){
        // Toast.makeText(getApplicationContext(),"Press logout to move to Login page",Toast.LENGTH_LONG).show();
        startActivity(new Intent(this,WardenChoose.class));
    }
}
