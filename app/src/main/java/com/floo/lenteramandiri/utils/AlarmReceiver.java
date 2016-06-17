package com.floo.lenteramandiri.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.floo.lenteramandiri.data.WakeUpCallActivity;

/**
 * Created by Floo on 6/17/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    MediaPlayer player;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        //Toast.makeText(context, "Alarm aktif!", Toast.LENGTH_LONG).show();
        //player = MediaPlayer.create(context, R.raw.souljah_takselalu);
        //player.start();

        Intent mathAlarmAlertActivityIntent;

        mathAlarmAlertActivityIntent = new Intent(context, WakeUpCallActivity.class);

        //mathAlarmAlertActivityIntent.putExtra("alarm", "alarm");

        mathAlarmAlertActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(mathAlarmAlertActivityIntent);
        //Log.e("dataqu", "datamu");
    }
}
