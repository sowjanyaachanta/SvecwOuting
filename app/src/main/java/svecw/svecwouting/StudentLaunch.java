package svecw.svecwouting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Sowji on 26-07-2018.
 */
public class StudentLaunch extends AppCompatActivity{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    DatabaseReference databaseReference;
    NavigationView navigationView;
    public TextView welcome;
    private static final String SHARED_PREF_NAME = "mysharedpref";
    private static final String KEY_NAME = "keyname";
    CircleImageView enter, launch_dp, header_dp;
    public boolean doublePress = false;
    TextView nav_text;
    View hview;
    String name;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_launch);
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        name = sp.getString(KEY_NAME, null);
        welcome = (TextView)findViewById(R.id.welcome);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        hview = navigationView.getHeaderView(0);
        nav_text = (TextView)hview.findViewById(R.id.welcome);
        header_dp = (CircleImageView)hview.findViewById(R.id.header_dp);
        enter = (CircleImageView)findViewById(R.id.enter);
        launch_dp = (CircleImageView)findViewById(R.id.launch_dp);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        welcome.append(" " + name);
        nav_text.append(" " + name);
        final StorageReference storageRef =
                FirebaseStorage.getInstance().getReference("fourth_ita");
        String stname = name + ".JPG";
        storageRef.child(stname).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        Picasso.with(StudentLaunch.this).load(uri).into(launch_dp);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error in loading image", Toast.LENGTH_LONG).show();
            }
        });

        final StorageReference storageRef1 =
                FirebaseStorage.getInstance().getReference("fourth_ita");
        String stname1 = name + ".JPG";
        storageRef.child(stname1).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        Picasso.with(StudentLaunch.this).load(uri).into(header_dp);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              //  Toast.makeText(getApplicationContext(), "Error in loading image", Toast.LENGTH_LONG).show();
            }
        });

        enter.setImageResource(R.drawable.nxt);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("update").child(name);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        startActivity(new Intent(StudentLaunch.this, StuApply.class));
                    }
                    else
                        startActivity(new Intent(StudentLaunch.this, MainFencing.class));
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
         startActivity(new Intent(StudentLaunch.this,LoginAll.class));
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_logout){
                    //Toast.makeText(getApplicationContext(), "Logout view", Toast.LENGTH_LONG).show();
                    SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.commit();
                    //firebaseAuth.signOut();
                    startActivity(new Intent(getApplicationContext(), LoginAll.class));
                }
                if(item.getItemId() == R.id.nav_profile){
                    //Toast.makeText(getApplicationContext(), "Profile view", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(StudentLaunch.this, StudentProfile.class);
                    i.putExtra("i", name);
                    startActivity(i);
                }
                if(item.getItemId() == R.id.nav_status){
                    Intent i = new Intent(StudentLaunch.this, StudentStatus.class);
                    i.putExtra("i", name);
                    startActivity(i);
                }
                if(item.getItemId() == R.id.nav_history){
                    Intent i = new Intent(StudentLaunch.this, StudentHistory.class);
                    i.putExtra("i", name);
                    startActivity(i);
                }
                if(item.getItemId() == R.id.nav_contact){
                    //Toast.makeText(getApplicationContext(), "History view", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(StudentLaunch.this,ContactInfo.class));
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(doublePress){
            finishAffinity();
        }
        this.doublePress = true;
        Toast.makeText(getApplicationContext(), "Click again to exit", Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doublePress = false;
            }
        }, 2000);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)) {
        //    Toast.makeText(getApplicationContext(), item.toString(), Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
