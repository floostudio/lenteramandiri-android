package com.floo.lenteramandiri.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.floo.lenteramandiri.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Floo on 5/17/2016.
 */
public class TaskDetailFromAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, String>> listData;

    public TaskDetailFromAdapter(Context context, ArrayList<HashMap<String, String>> listData){
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
        private TextView escal;
        private LinearLayout liner;
        private ImageView img;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_row_detail_task,null);
            viewHolder = new ViewHolder();
            viewHolder.liner = (LinearLayout)view.findViewById(R.id.line_detail);
            viewHolder.escal= (TextView) view.findViewById(R.id.txt_task_list);
            viewHolder.img = (ImageView)view.findViewById(R.id.img_detail);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        HashMap<String, String> item = listData.get(position);
        if (item.get("note").toString().trim().equals("1")){
            viewHolder.img.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.img.setVisibility(View.VISIBLE);
        }
        viewHolder.escal.setText(item.get("escalated_from"));
        return view;
    }
}
