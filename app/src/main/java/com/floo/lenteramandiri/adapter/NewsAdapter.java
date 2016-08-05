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
public class NewsAdapter extends BaseAdapter{
    Context context;
    ArrayList<News> listData;

    public NewsAdapter(Context context, ArrayList<News> listData){
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
        private TextView idNews, titleNews, datenews, contentNews, urlNews;
        private ImageView imageView;

    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_row_news,null);
            viewHolder = new ViewHolder();
            viewHolder.idNews= (TextView) view.findViewById(R.id.txt_id_news);
            viewHolder.titleNews = (TextView) view.findViewById(R.id.txt_title_news);
            viewHolder.datenews = (TextView) view.findViewById(R.id.txt_date_news);
            viewHolder.urlNews = (TextView)view.findViewById(R.id.txt_url_news);
            viewHolder.contentNews = (TextView) view.findViewById(R.id.txt_content_news);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.img_news);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        final News news = listData.get(position);
        int id = news.getNews_id();
        viewHolder.idNews.setText(Integer.toString(id));
        String subject = news.getTitle();
        viewHolder.titleNews.setText(subject);
        if (news.getUrl().trim().equals("")){

            viewHolder.titleNews.setText(subject);
        }else {
            String tittle = "[LINK] "+subject;
            viewHolder.titleNews.setText(tittle);
        }

        viewHolder.urlNews.setText(news.getUrl());
        String pt = news.getContent();
        StringBuilder stringBuilder = new StringBuilder(pt);
        String sbtr = (pt.length()>30) ? stringBuilder.substring(0, 30) : pt;
        viewHolder.contentNews.setText(sbtr+"....");
        int expire = news.getDate();

        Date date = new Date(expire * 1000L);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        String formatDate = format.format(date);
        viewHolder.datenews.setText(formatDate);

        String img = news.getImage();
        Picasso.with(context)
                .load(news.getImage())
                .into(viewHolder.imageView);
        //ImageLoader imageLoader = new ImageLoader();
        //imageLoader.DisplayImage(img, viewHolder.imageView);
        //new ImageLoadTask(img, viewHolder.imageView).execute();

        /*viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            String tost = news.getTitle();
            @Override
            public void onClick(View v) {
                //TextView txt = (TextView)v.findViewById(R.id.txt_title_news);
                //Toast.makeText(context, tost, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.pdf995.com/samples/pdf.pdf"));
                context.startActivity(intent);
            }
        });*/

        return view;
    }
}
