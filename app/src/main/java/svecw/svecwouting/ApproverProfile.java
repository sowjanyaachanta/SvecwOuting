package svecw.svecwouting;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Sowji on 28-07-2018.
 */
public class ApproverProfile extends AppCompatActivity {
    CircleImageView profile_dp;
    TextView profile_text;
    String name, id;
    long  ptn;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        profile_dp = (CircleImageView)findViewById(R.id.profile_dp);
        profile_text = (TextView)findViewById(R.id.profile_text);
        id = getIntent().getStringExtra("i");
        final StorageReference storageRef =
                FirebaseStorage.getInstance().getReference("fourth_ita");
        String stname = id + ".JPG";
        storageRef.child(stname).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        Picasso.with(ApproverProfile.this).load(uri).into(profile_dp);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error in loading image", Toast.LENGTH_LONG).show();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("faculty").child(id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ApproverPojo ap;
                Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_SHORT).show();
                ap = dataSnapshot.getValue(ApproverPojo.class);
                id = ap.getRegnum();
                name = ap.getName();
                ptn = ap.getPhnm();
                //tv1.setText(regnum);
                profile_text.setText("  ID: "+id + "\n  Name : " + name + "\n" +"  Mobile Number : " + ptn );
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        });
    }
}
