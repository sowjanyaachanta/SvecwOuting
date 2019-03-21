package svecw.svecwouting;

/**
 * Created by Sowji on 11-02-2019.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReturnUpdate extends AppCompatActivity {
    Button button;
    DatabaseReference databaseReference;
    private static final String SHARED_PREF_NAME = "mysharedpref";
    private static final String KEY_NAME = "keyname";
    TextView tv;
    String id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        id = sp.getString(KEY_NAME, null);
        button = (Button) findViewById(R.id.update1);
        tv = (TextView)findViewById(R.id.tv);
        tv.setText(id + "\nUpdate return status..." );
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), id + "Your Return status has been updated....!!!", Toast.LENGTH_LONG).show();
                databaseReference = FirebaseDatabase.getInstance().getReference("update").child(id);
                databaseReference.removeValue();
                //Toast.makeText(getApplicationContext(), "Your Return status has been updated....2!!!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ReturnUpdate.this, LoginAll.class);
                startActivity(intent);
            }
        });
    }

}

