package svecw.svecwouting;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Sowji on 15-07-2018.
 */
public class LoginAll extends AppCompatActivity implements View.OnClickListener {
    private EditText registernum1, password1;
    private Button loginobj;
    private TextView signupobj;
    public FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    Intent intent;
    private String mRecentAddress, reg, pas;
    private static final String SHARED_PREF_NAME = "mysharedpref";
    private static final String KEY_NAME = "keyname";
    DatabaseReference databaseReference;
    int f  = 0;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        name = sp.getString(KEY_NAME, null);
        if (name != null && name.substring(0,1).equals("1")) {
            checkArea(name);
            Intent i = new Intent(LoginAll.this,StudentLaunch.class);
            i.putExtra("id",name);
            startActivity(i);
            }
            else if(name != null && name.substring(0,1).equals("F") ) {
                Intent i = new Intent(LoginAll.this, ApproverLaunch.class);
                i.putExtra("id",name);
                startActivity(i);
            }
            else if(name != null && name.substring(0,1).equals("W") ) {
                Intent i = new Intent(LoginAll.this, ApproverLaunch.class);
                i.putExtra("id",name);
                startActivity(i);
            }
        else {
            firebaseAuth = FirebaseAuth.getInstance();
            setContentView(R.layout.activity_main);
            registernum1 = (EditText) findViewById(R.id.registernum);
            password1 = (EditText) findViewById(R.id.password);
            loginobj = (Button) findViewById(R.id.login);
            signupobj = (TextView) findViewById(R.id.signup);
            firebaseAuth = FirebaseAuth.getInstance();
            progressDialog = new ProgressDialog(this);
            loginobj.setOnClickListener(this);
            signupobj.setOnClickListener(this);
        }
        }
   @Override
    public void onBackPressed(){
        finishAffinity();
   }

    private void validate(final String regnum, String pass) {
        progressDialog.setMessage("Logging User......");
        progressDialog.show();
      //  Toast.makeText(LoginAll.this, regnum + pass, Toast.LENGTH_SHORT).show();
        firebaseAuth.signInWithEmailAndPassword(regnum, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                progressDialog.dismiss();
                                                                                                SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                                                                                                SharedPreferences.Editor editor = sp.edit();
                                                                                                editor.putString(KEY_NAME, reg);
                                                                                                editor.apply();
                                                                                                if(reg.substring(0,1).equals("1") ) {
                                                                                                    checkArea(reg);
                                                                                                    Intent i = new Intent(LoginAll.this,StudentLaunch.class);
                                                                                                  //  Toast.makeText(getApplicationContext(),reg + " in login All",Toast.LENGTH_LONG).show();
                                                                                                    i.putExtra("id",reg);
                                                                                                    startActivity(i);

                                                                                                    }
                                                                                                else if(reg.substring(0,1).equals("F") ) {
                                                                                                    Intent i = new Intent(LoginAll.this, ApproverLaunch.class);
                                                                                                    i.putExtra("id",reg);
                                                                                                    startActivity(i);
                                                                                                }
                                                                                                else if(reg.substring(0,1).equals("W") ) {
                                                                                                    Intent i = new Intent(LoginAll.this, ApproverLaunch.class);
                                                                                                    i.putExtra("id",reg);
                                                                                                    startActivity(i);
                                                                                                }
                                                                                            } else {
                                                                                                progressDialog.dismiss();
                                                                                                Toast.makeText(LoginAll.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                                                                                             //   finish();
                                                                                            }
                                                                                        }
                                                                                    }
        );
    }

    public void onClick(View v) {
        if (v == signupobj) {
            intent = new Intent(getApplicationContext(), Register.class);
            startActivity(intent);
        }
        if (v == loginobj) {
            reg = registernum1.getText().toString().toUpperCase();
            pas = password1.getText().toString();
            if (!(reg.isEmpty()) || !(pas.isEmpty())) {
                validate(reg + "@svecw.in", pas);
            }
            else
                Toast.makeText(getApplicationContext(), "Invalid Username/Password", Toast.LENGTH_LONG).show();
        }
    }

    public void checkArea(String r) {
       // Toast.makeText(getApplicationContext(),"ca",Toast.LENGTH_SHORT).show();
        databaseReference = FirebaseDatabase.getInstance().getReference("update").child(r);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    startActivity(new Intent(LoginAll.this, MainFencing.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

