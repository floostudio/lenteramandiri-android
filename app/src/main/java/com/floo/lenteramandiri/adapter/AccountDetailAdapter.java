package com.floo.lenteramandiri.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.utils.DataManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Floo on 3/4/2016.
 */
public class AccountDetailAdapter extends BaseAdapter{
    Context context;
    ArrayList<HashMap<String, String>> listData;

    public AccountDetailAdapter(Context context, ArrayList<HashMap<String, String>> listData){
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
        private TextView variable, data;
        private LinearLayout row;

    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_row_porto_group_detail,null);
            viewHolder = new ViewHolder();
            viewHolder.row = (LinearLayout) view.findViewById(R.id.row_group_detail);
            viewHolder.variable= (TextView) view.findViewById(R.id.txt_group_detail_variable);
            viewHolder.data= (TextView) view.findViewById(R.id.txt_group_detail_cif);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        HashMap<String, String> item = listData.get(position);

        if (position%2==0){
            viewHolder.row.setBackgroundColor(context.getResources().getColor(R.color.cpb_white));
        }
        viewHolder.variable.setText(item.get("key"));

        if (item.get("key").toString().trim().equals("cif")){
            viewHolder.data.setText(item.get("variable"));
        }else {
            String values = item.get("variable");
            if (values.matches("\\d+(?:\\.\\d+)?")){
                if (values.length()>3){
                    viewHolder.data.setText(DataManager.getDecimalFormat(values));
                }else {
                    viewHolder.data.setText(values);
                }
            }else {
                viewHolder.data.setText(values);
            }
        }
        return view;
    }
}
