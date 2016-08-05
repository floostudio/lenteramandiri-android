package com.floo.lenteramandiri.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.floo.lenteramandiri.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Floo on 3/4/2016.
 */
public class DashboardTFDAdapter extends BaseAdapter{
    Context context;
    ArrayList<HashMap<String, String>> listData;

    public DashboardTFDAdapter(Context context, ArrayList<HashMap<String, String>> listData){
        this.context = context;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    class ViewHolder {
        private TextView key1, key2;
        private TextView value1, value2;
        private LinearLayout row1, row2;

    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_row_dashboard_tfd_new,null);
            viewHolder = new ViewHolder();
            viewHolder.row1 = (LinearLayout) view.findViewById(R.id.linier_key_1);
            viewHolder.row2 = (LinearLayout) view.findViewById(R.id.linier_key_2);
            viewHolder.key1= (TextView) view.findViewById(R.id.tfd_title_1);
            viewHolder.key2= (TextView) view.findViewById(R.id.tfd_title_5);
            viewHolder.value1= (TextView) view.findViewById(R.id.tfd_data_1);
            viewHolder.value2= (TextView) view.findViewById(R.id.tfd_data_5);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        HashMap<String, String> item = listData.get(position);
        //Log.d("datadash", item.get("data1").replaceAll(".*_", ""));

        /*if (position%2==0){
            viewHolder.row1.setBackgroundColor(context.getResources().getColor(R.color.grey));
            viewHolder.row2.setBackgroundColor(context.getResources().getColor(R.color.cpb_white));
            viewHolder.value1.setBackground(context.getResources().getDrawable(R.drawable.activity_btn_bluee));
            viewHolder.value2.setBackground(context.getResources().getDrawable(R.drawable.activity_btn_yellow));
        }*/

        viewHolder.key1.setText(beforeString(item.get("data1")));
        viewHolder.value1.setText(item.get("data2"));
        viewHolder.key2.setText(beforeString(item.get("data3")));
        viewHolder.value2.setText(item.get("data4"));

        return view;
    }

    private String beforeString(String string){
        String result = null;

        result = string.replaceAll(".*_", "");

        return result;
    }
}
