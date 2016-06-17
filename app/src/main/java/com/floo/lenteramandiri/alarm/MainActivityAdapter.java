package com.floo.lenteramandiri.alarm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.floo.lenteramandiri.MainActivity;
import com.floo.lenteramandiri.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Floo on 6/17/2016.
 */
public class MainActivityAdapter extends BaseAdapter {
    private MainActivity ma;
    private List<Call> list = new ArrayList<>();

    public MainActivityAdapter(MainActivity ma){
        this.ma = ma;
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
            view = LayoutInflater.from(ma).inflate(
                    R.layout.list_row_adapter, null);

        Call alarm = (Call) getItem(position);

        TextView alarmTimeView = (TextView) view
                .findViewById(R.id.txt_Title);
        alarmTimeView.setText(alarm.getTitle());


        TextView alarmDaysView = (TextView) view
                .findViewById(R.id.txt_date);
        alarmDaysView.setText(String.valueOf(alarm.getDate()) );


        return view;
    }

    public List<Call> getMathAlarms() {
        return list;
    }

    public void setMathAlarms(List<Call> list) {
        this.list = list;
    }

}
