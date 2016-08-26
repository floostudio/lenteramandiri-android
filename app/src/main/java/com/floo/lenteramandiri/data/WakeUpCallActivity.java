package com.floo.lenteramandiri.data;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.floo.lenteramandiri.MainActivity;
import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.adapter.CobaAdapter;
import com.floo.lenteramandiri.alarm.Call;
import com.floo.lenteramandiri.alarm.Database;
import com.floo.lenteramandiri.utils.DataManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Floo on 6/17/2016.
 */
public class WakeUpCallActivity extends AppCompatActivity {
    TextView txtView;
    Button button;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wakeupcall);

        txtView = (TextView)findViewById(R.id.txt_wakeupcall);
        button = (Button)findViewById(R.id.btn_wakeupcall);

        Database.init(WakeUpCallActivity.this);
        Bundle bundle = getIntent().getExtras();
        long data = bundle.getLong("alarm");

        Call call = new Call();
        call.setDate(data);
        call.setActive(false);
        Database.update(call);

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(data);
        String strNow = formatter.format(calendar.getTime());

        //int view = Integer.parseInt(DataManager.epochtodate((int) data));
        txtView.setText("You have task that will expired" +
                "\nat "+strNow+" !");
        List<Call> alarms = Database.getAll();
        CobaAdapter adapter = new CobaAdapter(getApplicationContext(), alarms);
        ListView listView = (ListView)findViewById(R.id.list_wakeup);
        listView.setAdapter(adapter);

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        //mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(10.0f, 10.0f);
        mediaPlayer.start();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back=new Intent(getApplicationContext(), MainActivity.class);
                back.putExtra("fragment", "fragment");
                mediaPlayer.stop();
                finish();
                startActivity(back);
            }
        });
    }
}
