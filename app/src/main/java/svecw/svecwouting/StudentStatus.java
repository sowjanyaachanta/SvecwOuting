package svecw.svecwouting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

/**
 * Created by Sowji on 05-01-2019.
 */
public class StudentStatus extends AppCompatActivity {
    DatabaseReference databaseReference;
    private static final String SHARED_PREF_NAME = "mysharedpref";
    private static final String KEY_NAME = "keyname";
    public String id,msg;
    TextView status;
    public ApproveView av;
    public String gdate,rdate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_status);
        //msg = "Faculty approval pending";
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        id = sp.getString(KEY_NAME, null);
        status = (TextView)findViewById(R.id.status);
        databaseReference = FirebaseDatabase.getInstance().getReference("requests").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    msg = "Faculty approval pending";
                    status.setText(msg.toString());
                }
                else {
                    msg = "Faculty Approved";
                    checkInApproved();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Network Issue",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void checkInApproved() {
        databaseReference = FirebaseDatabase.getInstance().getReference("approved").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    msg = "Faculty Approved\nWarden approval pending";
                    status.setText(msg.toString());
                   // status.
                }
                else {
                    checkInUpdate();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Network Issue",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void checkInUpdate() {
        databaseReference = FirebaseDatabase.getInstance().getReference("update").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    msg = "Faculty Approved\nWarden Approved";
                }
                else
                    checkInPending();
                status.setText(msg.toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Network Issue",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void checkInPending() {
        databaseReference = FirebaseDatabase.getInstance().getReference("pending").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    msg = "Faculty Approved\nParent approval pending ";
                }
                else
                    msg = "Faculty Approved\nWarden Rejected";
                status.setText(msg.toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Network Issue",Toast.LENGTH_LONG).show();
            }
        });
    }

}
