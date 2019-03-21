package svecw.svecwouting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Sowji on 28-07-2018.
 */
public class ContactInfo extends AppCompatActivity {
    TextView tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_info);
        tv = (TextView)findViewById(R.id.contact);
        tv.setText("Contact Info : 9999999999\nReport Bug\nemail us at : xyz@gmail.com");
    }
}
