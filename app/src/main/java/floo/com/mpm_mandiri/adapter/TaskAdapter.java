package floo.com.mpm_mandiri.adapter;

import android.content.Context;
import android.util.Log;
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

import floo.com.mpm_mandiri.R;

/**
 * Created by Floo on 3/4/2016.
 */
public class TaskAdapter extends BaseAdapter{
    Context context;
    ArrayList<Task> listData;
    ArrayList<Task> list;

    public TaskAdapter(Context context, ArrayList<Task> listData){
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
        viewHolder.subjectTask.setText(subject);
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
    private long tode() {
        String str = dateNow();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date2 = null;
        try {
            date2 = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date2.getTime()/1000;
    }

    private String dateNow(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm", Locale.getDefault());
        Date date1 = new Date();
        return dateFormat.format(date1);
    }


    public void filterRed(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        listData.clear();
        if (charText.length() == 0) {
            listData.addAll(list);
        } else {
            for (Task wp : list) {
                long h = Long.parseLong(charText);
                if (wp.getExpire()<h) {
                    listData.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filterGreen(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        listData.clear();
        if (charText.length() == 0) {
            listData.addAll(list);
        } else {
            for (Task wp : list) {
                long h = Long.parseLong(charText);
                if ( wp.getExpire()>= h) {
                    listData.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filterOrange(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        listData.clear();
        if (charText.length() == 0) {
            listData.addAll(list);
        } else {
            for (Task wp : list) {
                long h = Long.parseLong(charText);
                if ( wp.getExpire()>=h && wp.getExpire()< (h+2592000)) {
                    listData.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
