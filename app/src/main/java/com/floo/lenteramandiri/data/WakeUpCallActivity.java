package com.floo.lenteramandiri.data;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.floo.lenteramandiri.MainActivity;
import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.alarm.Call;
import com.floo.lenteramandiri.alarm.Database;
import com.floo.lenteramandiri.utils.DataManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Floo on 6/17/2016.
 */
public class WakeUpCallActivity extends AppCompatActivity {
    TextView txtView;
    Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wakeupcall);

        txtView = (TextView)findViewById(R.id.txt_wakeupcall);
        button = (Button)findViewById(R.id.btn_wakeupcall);

        Database.init(WakeUpCallActivity.this);
        Bundle bundle = getIntent().getExtras();
        long data = bundle.getLong("alarm");

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(data);
        String strNow = formatter.format(calendar.getTime());

        //int view = Integer.parseInt(DataManager.epochtodate((int) data));
        txtView.setText("You have task that will expired" +
                "\nat "+strNow+" !");

        Call call = new Call();
        call.setDate(data);
        call.setActive(false);
        Database.update(call);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back=new Intent(getApplicationContext(), MainActivity.class);
                back.putExtra("fragment", "fragment");
                finish();
                startActivity(back);
            }
        });
    }
}
