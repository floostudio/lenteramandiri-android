package com.floo.lenteramandiri.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.floo.lenteramandiri.MainActivity;
import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.alarm.Call;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Floo on 8/26/2016.
 */
public class CobaAdapter extends BaseAdapter {
    private Context context;
    private List<Call> list = new ArrayList<>();

    public CobaAdapter(Context context, List<Call> alarms){
        this.context = context;
        this.list = alarms;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (null == view)
            view = LayoutInflater.from(context).inflate(
                    R.layout.list_row_adapter, null);

        Call alarm = (Call) getItem(position);

        TextView alarmTimeView = (TextView) view
                .findViewById(R.id.txt_Title);
        //alarmTimeView.setText(alarm.getId());


        TextView alarmDaysView = (TextView) view
                .findViewById(R.id.txt_date);
        alarmDaysView.setText(String.valueOf(alarm.getActive()));


        return view;
    }
}
