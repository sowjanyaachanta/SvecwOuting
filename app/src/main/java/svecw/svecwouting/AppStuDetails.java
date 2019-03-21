package svecw.svecwouting;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppStuDetails extends AppCompatActivity {
    DatabaseReference databaseReference,databaseReference1,databaseReference2,databaseReference3,databaseReference4, databaseReference5;
    TextView tv,tv1,tv2;
    Button sbt,pbt,abt,rbt;
    long stn,ptn,y;
    String regnum,name,branch,place,res,fd,td,s,num,aprv,self,msg;
    ApproveView ap;
    CircleImageView circleImageView;
    private ListView listView;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    public int hcnt = 0;
    public String a[];
    long diff;
    String diffDays,Name;
    SimpleDateFormat sdf;
    int cnt = 0,diff1;
    Date today;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decision);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        num = getIntent().getStringExtra("Number");
        s = num.substring(0,10);
        aprv = num.substring(10,12);
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
        circleImageView = (CircleImageView)findViewById(R.id.imageView);
        sbt = (Button)findViewById(R.id.stcall);
        pbt = (Button)findViewById(R.id.ptcall) ;
        abt = (Button)findViewById(R.id.approve);
        rbt = (Button)findViewById(R.id.reject);
        tv1 = (TextView)findViewById(R.id.id1);
        tv = (TextView)findViewById(R.id.view);
        final StorageReference storageRef =
                FirebaseStorage.getInstance().getReference("fourth_ita");

        storageRef.child(s + ".JPG").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        Picasso.with(AppStuDetails.this).load(uri).into(circleImageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error in loading image", Toast.LENGTH_LONG).show();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("granted").child(s);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    cnt++;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("student").child(s);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StudentPojo st;
                st = dataSnapshot.getValue(StudentPojo.class);
                regnum = st.getRegnum().toString();
                name = st.getFirst_name().toString() + ' ' + st.getLast_name().toString();
                branch = st.getBranch();
                y = st.getYear();
                stn = st.getStudent_number();
                ptn = st.getParent_number();
                self = st.getSelf();
                tv1.setText(regnum);
                tv.setText("  Name : " + name + "\n" +"  Branch : " + branch
                        + " - " + y + "\n" +"  No of Outings till now : " +  cnt + "\n" );
                sbt.setText("Student");
                pbt.setText("Parent");
                fillData();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        });

        sbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + stn));
                if (ActivityCompat.checkSelfPermission(AppStuDetails.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

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
                if (ActivityCompat.checkSelfPermission(AppStuDetails.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callintent);

            }
        });
        abt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String r = s;
                Toast.makeText(AppStuDetails.this,"Permission Granted",Toast.LENGTH_LONG).show();
                msg = "Your ward " + r + " has been granted for outing to " +  place + " from " + fd + " to " + td +
                        " with a reason " + res + "....If you have any concerns contact vice principal-9490734166";
                save(s);
                Intent i = new Intent(getApplicationContext(),AppRequestList.class);
                startActivity(i);
            }
        });
        rbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String r = s;
                Toast.makeText(getApplicationContext(),"Rejected",Toast.LENGTH_LONG).show();
                msg = "Your ward " + r + "s request for outing has been rejected";
                reject(s);
                Intent i = new Intent(getApplicationContext(),AppRequestList.class);
                startActivity(i);
            }
        });
    }
    @Override
    public void onBackPressed(){
        startActivity(new Intent(this,AppRequestList.class));
    }


    public void fillData() {
        databaseReference1 = FirebaseDatabase.getInstance().getReference("requests").child(s);
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ApplyPojo rt = dataSnapshot.getValue(ApplyPojo.class);
                regnum = rt.getRegnum().toString();
                res = rt.getReason().toString();
                place = rt.getPlace().toString();
                fd = rt.getFrom_date().toString();
                String fd1 = fd.replaceAll("-","/");
                td = rt.getTo_date().toString();
                String td1 = td.replaceAll("-","/");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    diff1 = (int)((sdf.parse(td1).getTime() - sdf.parse(fd1).getTime()) / (1000 * 60 * 60 * 24));
                }
                catch (Exception e){
                    System.out.print("Date exception");
                }
                tv.append("  Place : " + place + "\n" + "  Reason : "
                        + res + "\n" + "  From : " + fd + "\t\t\t" + "To : " + td + "\n\tNo of days : "+diff1 );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();

            }
        });
       }


    public void reject(String str) {
        today = java.util.Calendar.getInstance().getTime();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        ApproveView av1 = new ApproveView(regnum,name,branch,y,res,place,fd,td,stn,ptn,self,Name,diff1,sdf.format(today).toString());
        databaseReference2 = FirebaseDatabase.getInstance().getReference("rejected");
        String Id = databaseReference2.push().getKey();
        databaseReference2.child(s).child(Id).setValue(av1);
        databaseReference3 = FirebaseDatabase.getInstance().getReference("requests").child(s);
        databaseReference3.removeValue();
        notification();
      }

    public void save(String str) {
        today = java.util.Calendar.getInstance().getTime();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        ApproveView av2 = new ApproveView(regnum,name,branch,y,res,place,fd,td,stn,ptn,self,Name,diff1,sdf.format(today).toString());
        databaseReference4 = FirebaseDatabase.getInstance().getReference("approved");
        databaseReference4.child(s).setValue(av2);
        databaseReference3 = FirebaseDatabase.getInstance().getReference("requests").child(s);
        databaseReference3.removeValue();
        notification();
    }

    public void notification() {
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pop_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.history) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AppStuDetails.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.history, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle(s);
                arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
                listView = (ListView) convertView.findViewById(R.id.list_item);
                tv2 = (TextView)findViewById(R.id.cnt);
                listView.setAdapter(arrayAdapter);
                if(hcnt == 0) {
                databaseReference5 = FirebaseDatabase.getInstance().getReference("granted").child(s);
                databaseReference5.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            PermitPojo pp = ds.getValue(PermitPojo.class);
                            arrayList.add(pp.getReason() + "\n " + pp.getPermittedDays());
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
                }
            alertDialog.show();
            alertDialog.setOnDismissListener(null);
            hcnt++;
        }
        return super.onOptionsItemSelected(item);
    }
   }

