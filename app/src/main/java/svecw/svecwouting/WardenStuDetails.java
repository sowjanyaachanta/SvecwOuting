package svecw.svecwouting;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
//import com.onesignal.OSPermissionSubscriptionState;
//import com.onesignal.OneSignal;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Sowji on 01-05-2018.
 */
public class WardenStuDetails extends AppCompatActivity {
    DatabaseReference databaseReference,databaseReference1,databaseReference2;
    TextView tv,tv1;
    Button sbt,pbt,abt,rbt;
    long stn,ptn,y,x;
    int cnt = 0, f = 0;
    String regnum,name,branch,place,res,fd,td,data,s[],self,approver,aprv,Name,msg;
    ApproveView av,gt;
    CircleImageView circleImageView;
    SimpleDateFormat sdf;
    int permittedDays;
    Date today;
    PointValue p;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decision);
        sbt = (Button)findViewById(R.id.stcall);
        pbt = (Button)findViewById(R.id.ptcall) ;
        abt = (Button)findViewById(R.id.approve);
        rbt = (Button)findViewById(R.id.reject);
        tv = (TextView)findViewById(R.id.view);
        tv1 = (TextView)findViewById(R.id.id1);
        circleImageView = (CircleImageView)findViewById(R.id.imageView);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();

        //Toast.makeText(getApplicationContext(),"Entered",Toast.LENGTH_LONG).show();*/
      //  OneSignal.startInit(this).init();
        data = getIntent().getStringExtra("Number");
        s = data.split(" ");
      //  Toast.makeText(getApplicationContext(),s[0] +" " + s[1],Toast.LENGTH_LONG).show();
        databaseReference = FirebaseDatabase.getInstance().getReference(s[1]).child(s[0]);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                av = dataSnapshot.getValue(ApproveView.class);
                regnum = av.getRegnum();
                name = av.getName();
                branch = av.getBranch();
                res = av.getRes();
                place = av.getPlace();
                fd = av.getFm();
                td = av.getTd();
                stn = av.getStunum();
                ptn = av.getParnum();
                y = av.getYear();
                self = av.getSelf();
                approver = av.getApproverName();
                permittedDays = av.getPermittedDays();
                tv1.setText(regnum);
                tv.setText("  Name : " + name + "\n" +"  Branch : " + branch
                        + " - "  + y + "\n" + "  Place : " + place + "\n" + "  Reason : " + res + "\n" + "  From : " + fd + "\t\t\t" +
                        "To : " + td + "\n" + "  Self : " + self + "\n"+ "  Approved by : " + approver + "\n  PemittedDays : " + permittedDays);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        });
        final StorageReference storageRef =
                FirebaseStorage.getInstance().getReference("fourth_ita");

        storageRef.child(s[0] + ".JPG").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        Picasso.with(WardenStuDetails.this).load(uri).into(circleImageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error in loading image", Toast.LENGTH_LONG).show();
            }
        });
        sbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + stn));
                if (ActivityCompat.checkSelfPermission(WardenStuDetails.this,
                        android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });
        pbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callintent = new Intent(Intent.ACTION_CALL);
                callintent.setData(Uri.parse("tel:" + ptn));
                if (ActivityCompat.checkSelfPermission(WardenStuDetails.this,
                        android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callintent);
            }
        });
        abt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                today = java.util.Calendar.getInstance().getTime();
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                gt = new ApproveView(regnum,name,branch,y,res,place,fd,td,stn,ptn,self,approver,permittedDays,sdf.format(today).toString());
                databaseReference1 = FirebaseDatabase.getInstance().getReference("granted");
                String Id = databaseReference1.push().getKey();
                databaseReference1.child(s[0]).child(Id).setValue(gt);
                f = 0;
                databaseReference = FirebaseDatabase.getInstance().getReference("graph").child(sdf.format(today).toString());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists() && f == 0) {
                            f = 1;
                            PointValue pvl = dataSnapshot.getValue(PointValue.class);
                            cnt = pvl.getY();
                            Toast.makeText(getApplicationContext(),cnt + "exists ",Toast.LENGTH_SHORT).show();
                            update(cnt);
                        }
                        else if(f == 0){
                            f = 1;
                            update(cnt);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                //Toast.makeText(getApplicationContext(),cnt + " 2" ,Toast.LENGTH_SHORT).show();
                databaseReference = FirebaseDatabase.getInstance().getReference("update");
                databaseReference.child(s[0]).setValue(gt);
                msg = "Your ward " + s[0] + " has been granted for outing to " +  place + " from " + fd + " to " + td +
                        " with a reason " + res + "....If you have any concerns contact vice principal-9490734166 ";
               // sendMessage(msg,"9490734166");
                //notification();
                Toast.makeText(WardenStuDetails.this,"Permission Granted",Toast.LENGTH_SHORT).show();
                reject();
              //  finish();
            }
        });
        rbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(WardenStuDetails.this);
                builder1.setMessage("Reason for Rejecting......");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Parent Rejected",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                             //   Toast.makeText(getApplicationContext(), "Outing Rejected", Toast.LENGTH_SHORT).show();
                                msg = regnum + "s parent rejected for outing......";
                             //   sendMessage(msg, "9490734166");
                                Date today = java.util.Calendar.getInstance().getTime();
                                sdf = new SimpleDateFormat("yyyy-MM-dd");
                                gt = new ApproveView(regnum,name,branch,y,res,place,fd,td,stn,ptn,self,approver,permittedDays,sdf.format(today).toString());
                                databaseReference2 = FirebaseDatabase.getInstance().getReference("rejected");
                                String Id = databaseReference2.push().getKey();
                                databaseReference2.child(s[0]).child(Id).setValue(gt);
                                reject();
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Approval Pending",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Date today = java.util.Calendar.getInstance().getTime();
                                sdf = new SimpleDateFormat("yyyy-MM-dd");
                                ApproveView gt1 = new ApproveView(regnum,name,branch,y,res,place,fd,td,stn,ptn,self,approver,permittedDays,sdf.format(today).toString());
                                databaseReference2 = FirebaseDatabase.getInstance().getReference("pending");
                                //  String Id = databaseReference2.push().getKey();
                                databaseReference2.child(s[0]).setValue(gt1);
                                msg = regnum + " parent approval for outing is pending......";
                                reject();
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder1.create();
                alert.show();
                Toast.makeText(getApplicationContext(), "Outing Rejected", Toast.LENGTH_SHORT).show();
                //sendMessage(msg, "9490734166");
            }
        });
    }

    @Override
    public void onBackPressed(){
        // Toast.makeText(getApplicationContext(),"Press logout to move to Login page",Toast.LENGTH_LONG).show();
        startActivity(new Intent(this,WardenRequestList.class));
    }
    public void sendMessage(String msg, String num) {
        try {
            SmsManager sm = SmsManager.getDefault();
            sm.sendTextMessage(num, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message sent successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }
    public void reject() {
        databaseReference2 = FirebaseDatabase.getInstance().getReference(s[1]).child(s[0]);
        databaseReference2.removeValue();
        notification();
     /*   ApproveView gt = new ApproveView(regnum,name,branch,y,place,res,fd,td,stn,ptn,self,Name);
        databaseReference2 = FirebaseDatabase.getInstance().getReference("rejected");
        String Id = databaseReference1.push().getKey();
        databaseReference1.child(s).child(Id).setValue(gt);*/
       startActivity(new Intent(WardenStuDetails.this, WardenRequestList.class));
    }
    public String aprvName(String id) {
        databaseReference = FirebaseDatabase.getInstance().getReference("faculty").child(aprv);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Name = dataSnapshot.child("name").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Network Issue",Toast.LENGTH_LONG).show();
            }
        });
        return Name;
    }
    public void notification() {
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        //  String userID = status.getSubscriptionStatus().getUserId();
        boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();
        //   Log.i("OneSignalExample", "postNotification Success: " + userID + "    " + isSubscribed);
        // textView.setText("Subscription Status, is subscribed:" + isSubscribed);
        //Toast.makeText(getApplicationContext(),userID + "    " + isSubscribed,Toast.LENGTH_LONG).show();
        if (!isSubscribed)
            return;
        databaseReference = FirebaseDatabase.getInstance().getReference("OneSignalIds").child(regnum);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String val = ds.child("student").getValue(String.class);
                    sendNotification(val,msg);
                    val = dataSnapshot.child("parent").getValue(String.class);
                    sendNotification(val,msg);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void sendNotification(String userID, String msg) {
        try {
            OneSignal.postNotification(new JSONObject("{'contents': {'en': '"+ msg +"'}, 'include_player_ids': ['" + userID + "']}"),
                    new OneSignal.PostNotificationResponseHandler() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Log.i("OneSignalExample", "postNotification Success: " + response);
                        }

                        @Override
                        public void onFailure(JSONObject response) {
                            Log.e("OneSignalExample", "postNotification Failure: " + response);
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void update(int c) {
        Toast.makeText(getApplicationContext(),"update",Toast.LENGTH_SHORT).show();
        x = new Date().getTime();
        c += 1;
        Toast.makeText(getApplicationContext(),c + " ",Toast.LENGTH_SHORT).show();
        SimpleDateFormat s2 = new SimpleDateFormat("ddMMyyyy");
      //  Date d = s1.parse("02/11/2012 23:11");
        String s3 = s2.format(today);
        System.out.println(s3);
        long l = Long.parseLong(s3);
        p = new PointValue(l, c);
        databaseReference = FirebaseDatabase.getInstance().getReference("graph").child(sdf.format(today).toString());
        databaseReference.setValue(p);
        //  databaseReference = FirebaseDatabase.getInstance().getReference("graph").child(sdf.format(today).toString());
     //   Toast.makeText(getApplicationContext(),x + " " + c,Toast.LENGTH_SHORT).show();
    }
}
