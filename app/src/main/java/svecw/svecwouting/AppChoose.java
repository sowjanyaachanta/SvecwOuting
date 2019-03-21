package svecw.svecwouting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Sowji on 26-06-2018.
 */
public class AppChoose extends AppCompatActivity {
    Button pending,approved,rejected;
    String id,db;
    FirebaseAuth firebaseAuth;
    private static final String SHARED_PREF_NAME = "mysharedpref";
    private static final String KEY_NAME = "keyname";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        id = sp.getString(KEY_NAME, null);
        pending = (Button)findViewById(R.id.pending);
        approved = (Button)findViewById(R.id.approved);
        rejected = (Button)findViewById(R.id.rejected);
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = "pending";
                Intent intent = new Intent(getApplicationContext(), AppRequestList.class);
                intent.putExtra("dbName",db);
                startActivity(intent);
            }
        });
        approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = "granted";
                Intent intent = new Intent(getApplicationContext(), StudentSearch.class);
                intent.putExtra("dbName",db);
                startActivity(intent);
            }
        });
        rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = "rejected";
                Intent intent = new Intent(getApplicationContext(), StudentSearch.class);
                intent.putExtra("dbName",db);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed(){
        startActivity(new Intent(this,ApproverLaunch.class));
    }

}
