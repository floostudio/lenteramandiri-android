package com.floo.lenteramandiri.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.floo.lenteramandiri.R;

/**
 * Created by Floo on 3/4/2016.
 */
public class CalendarListAdapter extends BaseAdapter{

    Context context;
    ArrayList<Task> listData;
    ArrayList<Task> list;

    public CalendarListAdapter(Context context, ArrayList<Task> listData){
        this.context = context;
        this.listData = listData;
        this.list = new ArrayList<Task>();
        this.list.addAll(listData);
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
        private TextView idTask, subjectTask, ptTask, expireTask;
        private ImageView imageView;


    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_row_task,null);
            viewHolder = new ViewHolder();
            viewHolder.idTask= (TextView) view.findViewById(R.id.txt_list_task_id);
            viewHolder.subjectTask = (TextView) view.findViewById(R.id.txt_list_task_subject);
            viewHolder.ptTask = (TextView) view.findViewById(R.id.txt_list_task_pt);
            viewHolder.expireTask = (TextView) view.findViewById(R.id.txt_list_task_tgl);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.img_list);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        Task task = listData.get(position);
        int id = task.getTask_id();
        viewHolder.idTask.setText(Integer.toString(id));
        String subject = task.getTitle();
        StringBuilder stringBuilder = new StringBuilder(subject);
        String sbtr = (subject.length()>30) ? stringBuilder.substring(0, 30) : subject;
        viewHolder.subjectTask.setText(sbtr+"....");
        String pt = task.getCompany();
        viewHolder.ptTask.setText(pt);
        int expire = task.getExpire();


        Date date = new Date(expire * 1000L);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        String formatDate = format.format(date);
        viewHolder.expireTask.setText(formatDate);

        long today;
        long epoch = 2592000;

        String str = dateNow();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date2 = null;
        try {
            date2 = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        today = date2.getTime()/1000;

        if (expire < today) {
            viewHolder.imageView.setImageResource(R.drawable.point_red);
        }else if (expire >= (today+epoch)){
            viewHolder.imageView.setImageResource(R.drawable.point_green);
        }else {
            viewHolder.imageView.setImageResource(R.drawable.point_orange);
        }
        return view;
    }

    private String dateNow(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm", Locale.getDefault());
        Date date1 = new Date();
        return dateFormat.format(date1);
    }

    public static String epochtodate(int epoch){
        Date date = new Date(epoch * 1000L);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        return format.format(date);
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        listData.clear();
        if (charText.length() == 0) {
            listData.addAll(list);
        } else {
            for (Task wp : list) {
                if (epochtodate(wp.getExpire()).toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    listData.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}