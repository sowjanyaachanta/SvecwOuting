package svecw.svecwouting;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Sowji on 12-07-2018.
 */
public class WardenReturnUpdate extends AppCompatActivity {
    EditText reg;
    DatabaseReference databaseReference,databaseReference1;
    PermitPojo pp;
    CircleImageView circleImageView;
    TextView tv;
    Button update;
    SimpleDateFormat sdf;
    Calendar c;
    String id;
    Button bt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warden_update);
        reg = (EditText)findViewById(R.id.reg);
        circleImageView = (CircleImageView)findViewById(R.id.imageView);
        tv = (TextView)findViewById(R.id.view);
        bt = (Button)findViewById(R.id.sr);
        update = (Button)findViewById(R.id.update);
        update.setEnabled(false);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = reg.getText().toString().toUpperCase();
                search();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateReturn();
            }
        });
    }

    public void search() {
         databaseReference = FirebaseDatabase.getInstance().getReference("update").child(id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                if(ds.exists()) {
                    final StorageReference storageRef =
                            FirebaseStorage.getInstance().getReference("fourth_ita");

                    storageRef.child(id + ".JPG").getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Got the download URL for 'users/me/profile.png'
                                    Picasso.with(WardenReturnUpdate.this).load(uri).into(circleImageView);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error in loading image", Toast.LENGTH_LONG).show();
                        }
                    });

                    pp = ds.getValue(PermitPojo.class);
                    String str = pp.getName() + "\n" + pp.getBranch() + "-" + pp.getYear();
                    tv.setText(str);
                    update.setEnabled(true);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Enter valid Number!!!!..",Toast.LENGTH_LONG).show();
                    update.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        });
}


    @Override
    public void onBackPressed(){
        // Toast.makeText(getApplicationContext(),"Press logout to move to Login page",Toast.LENGTH_LONG).show();
        startActivity(new Intent(this,WardenChoose.class));
    }

    public void updateReturn() {
        String reg = pp.getRegnum();
        String from = pp.getFrom_date();
        String to = pp.getTo_date();
        int permittedDays = pp.getPermittedDays();
       // String fd1 = from.replaceAll("-","/");
        String td1 = to.replaceAll("-","/");
        int diff = 0;
       // Toast.makeText(getApplicationContext(),fd1 + " " + td1,Toast.LENGTH_LONG).show();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        c = Calendar.getInstance();
        String str = sdf.format(java.util.Calendar.getInstance().getTime());
        try {
            diff = (int)((sdf.parse(str).getTime() - sdf.parse(td1).getTime()) / (1000 * 60 * 60 * 24));
        }
        catch (Exception e){
            System.out.print("Date exception");
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("return");
        UpdatePojo up = new UpdatePojo(reg,diff,permittedDays);
        databaseReference.child(id).setValue(up);
        databaseReference1 = FirebaseDatabase.getInstance().getReference("update").child(id);
        databaseReference1.removeValue();
        Toast.makeText(getApplicationContext(),"Updated Successfully",Toast.LENGTH_LONG).show();
        Intent i = new Intent(getApplicationContext(),WardenReturnUpdate.class);
        startActivity(i);
    }
}


