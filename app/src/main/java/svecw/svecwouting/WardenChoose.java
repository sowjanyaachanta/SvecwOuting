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
 * Created by Sowji on 06-07-2018.
 */
public class WardenChoose extends AppCompatActivity{
    Button requests,pending,approved,rejected,update;
    FirebaseAuth firebaseAuth;
    private static final String SHARED_PREF_NAME = "mysharedpref";
    private static final String KEY_NAME = "keyname";
    String id,db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wardenchoose);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  final String em = firebaseAuth.getInstance().getCurrentUser().getEmail();
      //  s = em.substring(0,2).toUpperCase();
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        id= sp.getString(KEY_NAME, null);
        //  s = getIntent().getStringExtra("id");
        requests = (Button)findViewById(R.id.requests);
        pending = (Button)findViewById(R.id.pending);
        approved = (Button)findViewById(R.id.approved);
        rejected = (Button)findViewById(R.id.rejected);
       // update = (Button)findViewById(R.id.update);
        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WardenRequestList.class);
                intent.putExtra("Number",id );
                startActivity(intent);
            }
        });
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WardenPendingList.class);
                intent.putExtra("id",id );
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

    /*  update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),WardenReturnUpdate.class));
            }
        });*/

    }
    @Override
    public void onBackPressed(){
     //   Toast.makeText(getApplicationContext(),"Press logout to move to Login page",Toast.LENGTH_LONG).show();
        finishAffinity();
    }

}
