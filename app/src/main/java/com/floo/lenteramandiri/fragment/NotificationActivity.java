package com.floo.lenteramandiri.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.adapter.DBHandler;
import com.floo.lenteramandiri.adapter.NotificationAdapter;
import com.floo.lenteramandiri.adapter.Notifi;
import com.floo.lenteramandiri.calendar.CalendarDay;
import com.floo.lenteramandiri.utils.DataManager;

import dmax.dialog.SpotsDialog;

/**
 * Created by Floo on 6/3/2016.
 */
public class NotificationActivity extends Fragment {
    ListView listView;
    DBHandler dbHandler;
    NotificationAdapter adapter;
    private SpotsDialog pDialog;
    private static final int SPLASH_DURATION = 3000;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_notification, container, false);
        pDialog = new SpotsDialog(getActivity(), R.style.CustomProgress);
        pDialog.setMessage("Please wait...!!!");
        pDialog.setCancelable(false);
        pDialog.show();

        dbHandler = new DBHandler(getActivity());
        initView(v);

        //Log.d("today", String.valueOf(DataManager.epochtodate((int) (DataManager.dateToEpoch(DataManager.getDatesNow())-(2678400+86400)))));

        return v;
    }

    private void initView(View view){
        listView = (ListView)view.findViewById(R.id.lis_notifikasi);
        progress();
        ArrayList<Notifi> arrayList = dbHandler.getAllSurvey();
        adapter = new NotificationAdapter(getActivity(), arrayList);
        listView.setAdapter(adapter);

    }

    private void progress(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                pDialog.dismiss();
            }
        }, SPLASH_DURATION);
    }



    /*@Override
    public void onStart() {
        super.onStart();
        ArrayList<Notifi> arrayList = dbHandler.getAllSurvey();
        //Log.d("semuadata", arrayList.toString());
        adapter = new NotificationAdapter(getActivity(), arrayList);
        //int fisrt = listView.getFirstVisiblePosition();
        listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<Notifi> arrayList = dbHandler.getAllSurvey();
        adapter = new NotificationAdapter(getActivity(), arrayList);
        listView.setAdapter(adapter);
    }*/
}
