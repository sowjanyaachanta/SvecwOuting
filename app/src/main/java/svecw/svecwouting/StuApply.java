package svecw.svecwouting;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import java.text.ParseException;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import java.text.SimpleDateFormat;import java.util.Date;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import de.hdodenhof.circleimageview.CircleImageView;

public class StuApply extends AppCompatActivity{
    Button save;
    EditText reason,place;
    TextView welcome,from,to;
    DatePickerDialog datePickerDialog;
    int d1,m1,y1,d2,m2,y2;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public String pph,sph;
    public long ptn,stn;
    CircleImageView circleImageView;
    Date date1,date2,today;
    String res,p,fd,td,num;
    ApplyPojo req;
    private static final String SHARED_PREF_NAME = "mysharedpref";
    private static final String KEY_NAME = "keyname";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String em = firebaseAuth.getInstance().getCurrentUser().getEmail();
        num = em.substring(0,10).toUpperCase();
        setContentView(R.layout.apply);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        circleImageView = (CircleImageView)findViewById(R.id.imageView);
        reason = (EditText) findViewById(R.id.res);
        place = (EditText) findViewById(R.id.plc);
        from = (TextView) findViewById(R.id.frm);
        to = (TextView) findViewById(R.id.t);
        save = (Button) findViewById(R.id.save);
        welcome = (TextView) findViewById(R.id.welcome);
        final StorageReference storageRef =
                FirebaseStorage.getInstance().getReference("fourth_ita");

        storageRef.child(num + ".JPG").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        Picasso.with(StuApply.this).load(uri).into(circleImageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error in loading image", Toast.LENGTH_LONG).show();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("student").child(num);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String val = dataSnapshot.child("Regnum").getValue(String.class);
                ptn = dataSnapshot.child("parent_number").getValue(Long.class);
                pph = dataSnapshot.child("parent_number").getValue(Long.class).toString();
                stn = dataSnapshot.child("student_number").getValue(Long.class);
                sph = dataSnapshot.child("student_number").getValue(Long.class).toString();
                welcome.setText("Welcome  " + val);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Cancelled",Toast.LENGTH_LONG).show();
            }
        });
    from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                d1 = c.get(Calendar.DAY_OF_MONTH);
                m1 = c.get(Calendar.MONTH);
                y1 = c.get(Calendar.YEAR);
                datePickerDialog = new DatePickerDialog(StuApply.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            date1 = sdf.parse(year + "-" + (monthOfYear+1) + "-" + (dayOfMonth+1) );
                            today = java.util.Calendar.getInstance().getTime();
                            if (date1.compareTo(today) >= 0) {
                                from.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                }
                            else {
                                Toast.makeText(getApplicationContext(), "Choose after current date", Toast.LENGTH_SHORT).show();
                                from.setText("Date (Tap here)");
                            }
                        } catch (ParseException p) {
                            System.out.print("Date error");
                        }

                    }
                }, y1, m1, d1);

                    datePickerDialog.show();

            }
        } );

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    d2 = c.get(Calendar.DAY_OF_MONTH);
                    m2 = c.get(Calendar.MONTH);
                    y2 = c.get(Calendar.YEAR);
                   datePickerDialog = new DatePickerDialog(StuApply.this, new DatePickerDialog.OnDateSetListener() {


                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            date2 = sdf1.parse(year + "-" + (monthOfYear+1) + "-" + (dayOfMonth+1) );
                            if (date2.compareTo(today) < 0 || date2.compareTo(date1) < 0) {
                                Toast.makeText(getApplicationContext(), "Choose after current date", Toast.LENGTH_SHORT).show();
                                to.setText("Date (Tap here");
                            }
                            else {
                                to.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }
                        catch (ParseException p) {
                            System.out.print("Date error");
                        }

                    }
                }, y2, m2, d2);
                datePickerDialog.show();
        } });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                res = reason.getText().toString().trim();
                p = place.getText().toString().trim();
                fd = from.getText().toString().trim();
                td = to.getText().toString().trim();
                if (res.isEmpty() || p.isEmpty() || fd.isEmpty() || fd.equals("From Date (Tap here)") || td.isEmpty() || td.equals("To Date (Tap here)")) {
                    Toast.makeText(getApplicationContext(), "Please fill in the details completely", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(StuApply.this);
                    builder1.setTitle("CONFIRMATION");
                    builder1.setMessage(res + "\n" + p + "\n" + fd +  "\n" + td);
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Apply",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    confirm();
                                }
                            });

                    builder1.setNegativeButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Toast.makeText(getApplicationContext(), "You have cancelled your request", Toast.LENGTH_LONG).show();
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder1.create();
                    alert.show();
                }
            }
        });
    }

    public void confirm() {
        databaseReference = FirebaseDatabase.getInstance().getReference("requests");
        req = new ApplyPojo(ptn, stn, num, res, p, fd, td);
        databaseReference.child(num).setValue(req);
        Toast.makeText(getApplicationContext(), "Applied Successfully", Toast.LENGTH_LONG).show();
        String msg = "Your ward " + num + " has applied for outing to " + p + " from " + fd + " to " + td +
                " with a reason " + res + "....If you have any concerns contact vice principal-9490734166";
        reason.setText("");
        place.setText("");
        from.setText("");
        to.setText("");
    }
 @Override
   public void onBackPressed()
   {
       startActivity(new Intent(this,StudentLaunch.class));
   }
}