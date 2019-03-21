package svecw.svecwouting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Sowji on 14-07-2018.
 */
public class ApplyMessage extends AppCompatActivity {
    TextView msg;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    CircleImageView circleImageView;
    TextView welcome;
    private static final String SHARED_PREF_NAME = "mysharedpref";
    private static final String KEY_NAME = "keyname";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_message);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final String em = firebaseAuth.getInstance().getCurrentUser().getEmail();
        final String email = em.substring(0, 10).toUpperCase();
        TextView msg = (TextView) findViewById(R.id.msg);
        circleImageView = (CircleImageView)findViewById(R.id.imageView);
        welcome = (TextView) findViewById(R.id.welcome);
        final StorageReference storageRef =
                FirebaseStorage.getInstance().getReference("fourth_year");

        storageRef.child(email + ".jpg").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        Picasso.with(ApplyMessage.this).load(uri).into(circleImageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error in loading image", Toast.LENGTH_LONG).show();
            }
        });
        welcome.append(" " + email);
        msg.setText("You have not updated your return time!!!!!.......\nYou cant apply for outing until you finish your updation");
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.apply_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();
            startActivity(new Intent(getApplicationContext(), LoginAll.class));
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed(){
        Toast.makeText(getApplicationContext(),"Press logout to move to Login page",Toast.LENGTH_LONG).show();
        finishAffinity();
    }
}
