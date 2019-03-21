package svecw.svecwouting;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

/**
 * Created by Sowji on 24-01-2019.
 */
public class OtpVerify extends AppCompatActivity {
    EditText et;
    Button bt;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    public Intent intent;
    public String arr[],id,ps;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp);
        et = (EditText) findViewById(R.id.otp);
        bt = (Button) findViewById(R.id.otp_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        intent = new Intent();
        arr = intent.getStringExtra("id").split(" ");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = et.getText().toString();
                verifyPhoneNumberWithCode(mVerificationId, code);            //call function for verify code

            }
        });
    }
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        Toast.makeText(getApplicationContext(),"verifyphonenum",Toast.LENGTH_LONG).show();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
     }

    private void callback_verification() {
        Toast.makeText(getApplicationContext(),"verification",Toast.LENGTH_LONG).show();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.

                Toast.makeText(getApplicationContext(),"completed",Toast.LENGTH_LONG).show();

                signInWithPhoneAuthCredential(credential);
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
             //   Toast.makeText(getApplicationContext(),mVerificationId + " " + mResendToken,Toast.LENGTH_LONG).show();


            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Toast.makeText(getApplicationContext(),"in signwithphoneauth",Toast.LENGTH_LONG).show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            addData(arr[0],arr[1]);
                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(getApplicationContext(), "Registration successfull", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(OtpVerify.this, LoginAll.class));
                        } else {
                            // Sign in failed, display a message and update the UI

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid

                            }


                        }
                    }
                });
    }

    public void addData(String reg, String ps) {
        progressDialog.setMessage("Registering User......");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(reg, ps).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseAuth.signOut();
                    Toast.makeText(OtpVerify.this, "Registered Successfully!!", Toast.LENGTH_LONG).show();
                    //finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(OtpVerify.this, "Could not register..Please try again!!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }



}
