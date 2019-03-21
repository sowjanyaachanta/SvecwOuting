package svecw.svecwouting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.PhoneAuthCredential;

import com.google.firebase.auth.PhoneAuthProvider;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
//import com.onesignal.OSPermissionSubscriptionState;
//import com.onesignal.OneSignal;

import java.util.concurrent.TimeUnit;


/**
 * Created by Sowji on 25-04-2018.
 */
public class Register extends AppCompatActivity  {
    private EditText registerId, password, otp;
    public TextView tv;
    private Button submit, otpBtn;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    public String val, s, dbName, id, reg, ps, userID;
    public long ph;
    private FirebaseAuth mAuth;
    int f = 0, fg  = 0;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId,var;
    private RadioGroup radiogrp;
    private RadioButton radioButton;
    String reg2;
    private int STORAGE_PERMISSION_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (ContextCompat.checkSelfPermission(Register.this,
                Manifest.permission.CALL_PHONE)+ ContextCompat
                .checkSelfPermission(Register.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
          //  Toast.makeText(Register.this, "You have already granted this permission!",
            //        Toast.LENGTH_SHORT).show();
        } else {
            requestStoragePermission();
        }

        registerId = (EditText) findViewById(R.id.registernum2);
        password = (EditText) findViewById(R.id.password2);
        submit = (Button) findViewById(R.id.submit);
        otp = (EditText) findViewById(R.id.otp);
        otpBtn = (Button) findViewById(R.id.otp_btn);
        tv = (TextView)findViewById(R.id.tv);
        radiogrp = (RadioGroup) findViewById(R.id.radio);
        callback_verification();
        otp.setVisibility(View.GONE);
        otpBtn.setVisibility(View.GONE);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
      //  OneSignal.startInit(this).init();
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        userID = status.getSubscriptionStatus().getUserId();
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();///function initialization
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Toast.makeText(getApplication(),"submit", Toast.LENGTH_SHORT).show();
                reg = registerId.getText().toString().toUpperCase();
                int selectedId = radiogrp.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                var = radioButton.getText().toString();
                if(var.contentEquals("Parent")) {
                    var = "parent";
                    reg2 = reg + "P" + "@svecw.in";
                }
                else {
                    var = "student";
                    reg2 = reg + "@svecw.in";
                }
               // Toast.makeText(getApplicationContext(), var , Toast.LENGTH_SHORT).show();
                databaseReference = FirebaseDatabase.getInstance().getReference("OneSignalIds");
             //   Toast.makeText(getApplicationContext(), reg2 , Toast.LENGTH_SHORT).show();
               // Toast.makeText(getApplicationContext(), var , Toast.LENGTH_SHORT).show();
                databaseReference.child(reg).child(var).setValue(userID);
                registerUser();
            }
        });
        otpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otp.getText().toString();
                verifyPhoneNumberWithCode(mVerificationId, code);            //call function for verify code
            }
        });
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)||
                ActivityCompat.shouldShowRequestPermissionRationale
                        (Register.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of phone call and accessing location")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Register.this,
                                    new String[] {Manifest.permission
                                            .CALL_PHONE, Manifest.permission.ACCESS_FINE_LOCATION}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void registerUser() {
       // Toast.makeText(getApplicationContext(),"registeruser" + reg2,Toast.LENGTH_SHORT).show();
       // reg = registerId.getText().toString().toUpperCase();
        ps = password.getText().toString();
        if (TextUtils.isEmpty(reg) || TextUtils.isEmpty(ps)) {
            Toast.makeText(this, "Enter Valid Register Number / Password !", Toast.LENGTH_LONG).show();
            return;
        }
        //addData(reg2,ps);
        id = reg;
        if (reg.substring(0, 1).equals("1"))
            dbName = "student";
        else
            dbName = "faculty";
      //  Toast.makeText(getApplicationContext(),"registeruser2 " + reg,Toast.LENGTH_SHORT).show();
        // reg2 += "@svecw.in";
        progressDialog.setMessage("Sending OTP......");
        progressDialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference(dbName).child(reg);
      //  Toast.makeText(getApplicationContext(),"registeruser3 " + reg,Toast.LENGTH_SHORT).show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
          //      Toast.makeText(getApplicationContext(),"registeruser5 " + reg,Toast.LENGTH_SHORT).show();
                if (dataSnapshot.getChildrenCount() != 0) {
            //        Toast.makeText(getApplicationContext(), " valid number", Toast.LENGTH_LONG).show();
                    otp();
                   // addData(reg2,ps);
                } else {
                   progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Enter valid number", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    //    Toast.makeText(getApplicationContext(),"registeruser4 " + reg,Toast.LENGTH_SHORT).show();
    }


   public void otp() {
        progressDialog.dismiss();
  //      Toast.makeText(getApplicationContext(), "OTP", Toast.LENGTH_LONG).show();
        otp.setVisibility(View.VISIBLE);
        otpBtn.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference(dbName).child(reg);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dbName.contentEquals("student")) {
                    StudentPojo st;
                    st = dataSnapshot.getValue(StudentPojo.class);
                    if (var.contentEquals("student")) {
                        ph = st.getStudent_number();
                 //      Toast.makeText(getApplicationContext(), "stu1" + reg2 + ph, Toast.LENGTH_LONG).show();
                    }
                    else {
                        ph = st.getParent_number();
                  //      Toast.makeText(getApplicationContext(), "stu2" + reg2 + ph, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    ApproverPojo ap;
                    ap = dataSnapshot.getValue(ApproverPojo.class);
                    ph = ap.getPhnm();
                //    Toast.makeText(getApplicationContext(), "fa" + reg2 + ph, Toast.LENGTH_SHORT).show();
                }
                String num = String.valueOf(ph);
                //   Toast.makeText(getApplicationContext(),num,Toast.LENGTH_SHORT).show();
                startPhoneNumberVerification(num);          // call function for receive OTP 6 digit code
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        // Toast.makeText(getApplicationContext(),"Instartphonenumber" + " " + phoneNumber,Toast.LENGTH_LONG).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]
        tv.setText("OTP has been sent to " + phoneNumber);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
    //    Toast.makeText(getApplicationContext(),"in signwithphoneauth",Toast.LENGTH_LONG).show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            addData(reg2,ps);
                                //FirebaseUser user = task.getResult().getUser();
                               // Toast.makeText(getApplicationContext(), "Registration successfull", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(Register.this, LoginAll.class));
                               // fg = 1;

                        } else {
                            // Sign in failed, display a message and update the UI

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid

                            }


                        }
                    }
                });
    }


    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
    //    Toast.makeText(getApplicationContext(),"verifyphonenum",Toast.LENGTH_LONG).show();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
        //addData(reg2,ps);
    }

    private void callback_verification() {
   //     Toast.makeText(getApplicationContext(),"verification",Toast.LENGTH_LONG).show();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.

         //       Toast.makeText(getApplicationContext(),"completed",Toast.LENGTH_LONG).show();

                //signInWithPhoneAuthCredential(credential);
                addData(reg2,ps);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.

                Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_LONG).show();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded

                }

                // Show a message and update the UI

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.


                // Save verification ID and resending token so we can use them later

                mVerificationId = verificationId;
                mResendToken = token;
                progressDialog.dismiss();
              //  Toast.makeText(getApplicationContext(),mVerificationId + " " + mResendToken,Toast.LENGTH_LONG).show();


            }
        };
    }


    public void addData(String r, String p) {
        progressDialog.setMessage("Registering User......");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(r, p).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, "Registered Successfully!!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Register.this,LoginAll.class));
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, "Could not register..Please try again!!", Toast.LENGTH_LONG).show();
                }
            }
        });
        progressDialog.dismiss();
    }
}

