package com.floo.lenteramandiri.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.utils.database.DBNotification;
import com.floo.lenteramandiri.adapter.NotificationAdapter;
import com.floo.lenteramandiri.adapter.Notifi;

import dmax.dialog.SpotsDialog;

/**
 * Created by Floo on 6/3/2016.
 */
public class NotificationActivity extends Fragment {
    ListView listView;
    DBNotification dbNotification;
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

        dbNotification = new DBNotification(getActivity());
        initView(v);

        return v;
    }

    private void initView(View view){
        listView = (ListView)view.findViewById(R.id.lis_notifikasi);
        progress();
        ArrayList<Notifi> arrayList = dbNotification.getAllSurvey();
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
}
