package com.floo.lenteramandiri.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.floo.lenteramandiri.R;

/**
 * Created by Floo on 5/17/2016.
 */
public class TaskDetailAdapter extends BaseAdapter {
    Context context;
    ArrayList<Escalateds> listData;

    public TaskDetailAdapter(Context context, ArrayList<Escalateds> listData){
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
        Escalateds escalated = listData.get(position);
        String a = escalated.getEscalate();

        if (a.trim().equals("Escalated 1")){
            viewHolder.liner.setPadding(0, 0, 0, 0);
            viewHolder.img.setImageResource(R.drawable.check_fortask_active);
            //viewHolder.escal.setText(escalated.getEscalate());
        }else if (a.trim().equals("Escalated 2")){
            viewHolder.liner.setPadding(0, 0, 0, 0);
            viewHolder.img.setImageResource(R.drawable.check_fortask_active);
        }else if (a.trim().equals("Escalated 3")){
            viewHolder.liner.setPadding(0, 0, 0, 0);
            viewHolder.img.setImageResource(R.drawable.check_fortask_active);
        }else if (a.trim().equals("Escalated 4")){
            viewHolder.liner.setPadding(0, 0, 0, 0);
            viewHolder.img.setImageResource(R.drawable.check_fortask_active);
        }else if (a.trim().equals("Escalated 5")){
            viewHolder.liner.setPadding(0, 0, 0, 0);
            viewHolder.img.setImageResource(R.drawable.check_fortask_active);
        }
        viewHolder.escal.setText(escalated.getEscalate());

        return view;
    }
}
