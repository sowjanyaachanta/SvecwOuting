package svecw.svecwouting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.style.TextAppearanceSpan;
import android.widget.TextView;

/**
 * Created by Sowji on 07-01-2019.
 */
public class ApproverStatus extends AppCompatActivity {
    TextView tv;
    String status;
    @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_status);
        tv = (TextView)findViewById(R.id.status);
        Intent intent = getIntent();
        status = intent.getStringExtra("msg");
        tv.setText(status);
    }
}
