package com.floo.lenteramandiri.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;


import java.util.ArrayList;

import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.utils.DataManager;
import com.floo.lenteramandiri.utils.RobotoTextView;

/**
 * Created by Floo on 5/31/2016.
 */
public class NotificationAdapter extends BaseAdapter {
    Context context;
    ArrayList<Notifi> listData;

    public NotificationAdapter(Context context, ArrayList<Notifi> listData){
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
        private RobotoTextView txtTitle, txtContent, txtDate;
        private LinearLayout row;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_row_notification,null);
            viewHolder = new ViewHolder();
            viewHolder.txtTitle = (RobotoTextView) view.findViewById(R.id.list_item_sticky_header_media_album_name);
            viewHolder.txtContent = (RobotoTextView) view.findViewById(R.id.list_item_sticky_header_media_artist_name);
            viewHolder.txtDate = (RobotoTextView) view.findViewById(R.id.list_row_txt_date);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        Notifi survey = listData.get(position);
        String strTittle = survey.getCONTENT();
        viewHolder.txtTitle.setText(strTittle);
        String strContent = survey.getTITLE();
        viewHolder.txtContent.setText(strContent);
        viewHolder.txtDate.setText(DataManager.epochtodate(survey.getDATE()));

        return view;
    }
}