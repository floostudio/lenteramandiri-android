package com.floo.lenteramandiri.data;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.alarm.Call;
import com.floo.lenteramandiri.alarm.Database;

/**
 * Created by Floo on 6/17/2016.
 */
public class WakeUpCallActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wakeupcall);

        Database.init(WakeUpCallActivity.this);
        Bundle bundle = getIntent().getExtras();
        long data = bundle.getLong("alarm");

        Call call = new Call();
        call.setDate(data);
        call.setActive(false);
        Database.update(call);
    }
}
