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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back=new Intent(getApplicationContext(), MainActivity.class);
                back.putExtra("fragment", "fragment");
                startActivity(back);
            }
        });

        Database.init(WakeUpCallActivity.this);
        Bundle bundle = getIntent().getExtras();
        long data = bundle.getLong("alarm");
        int view = Integer.parseInt(DataManager.epochtodate((int) data));
        txtView.setText("You have task that will expired" +
                "\nin "+view+" days !");

        Call call = new Call();
        call.setDate(data);
        call.setActive(false);
        Database.update(call);
    }
}
