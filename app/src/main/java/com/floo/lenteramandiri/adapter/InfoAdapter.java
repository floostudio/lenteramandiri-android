package com.floo.lenteramandiri.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.utils.ImageLoader;
import com.squareup.picasso.Picasso;

/**
 * Created by Floo on 3/4/2016.
 */
public class InfoAdapter extends BaseAdapter{
    Context context;
    ArrayList<Info> listData;

    public InfoAdapter(Context context, ArrayList<Info> listData){
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
        private TextView idInfo, titleInfo, dateInfo, contentNews, urlInfo;
        private ImageView imageView;

    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_row_info,null);
            viewHolder = new ViewHolder();
            viewHolder.idInfo= (TextView) view.findViewById(R.id.txt_id_info);
            viewHolder.titleInfo= (TextView) view.findViewById(R.id.txt_title_info);
            viewHolder.dateInfo= (TextView) view.findViewById(R.id.txt_date_info);
            viewHolder.urlInfo= (TextView)view.findViewById(R.id.txt_url_info);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.img_info);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        final Info info = listData.get(position);
        int id = info.getInfo_id();
        viewHolder.idInfo.setText(Integer.toString(id));
        String tittle = info.getTitle();
        viewHolder.titleInfo.setText("[PDF] "+tittle);

        viewHolder.urlInfo.setText(info.getUrl());

        int expire = info.getDate();

        Date date = new Date(expire * 1000L);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        String formatDate = format.format(date);
        viewHolder.dateInfo.setText(formatDate);

        String img = info.getImage();
        Picasso.with(context)
                .load(info.getImage())
                .into(viewHolder.imageView);

        return view;
    }
}
