package floo.com.mpm_mandiri.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import floo.com.mpm_mandiri.R;

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
        private TextView idNews, titleNews, datenews, contentNews;
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
            viewHolder.contentNews = (TextView) view.findViewById(R.id.txt_content_news);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.img_news);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        News news = listData.get(position);
        int id = news.getNews_id();
        viewHolder.idNews.setText(Integer.toString(id));
        String subject = news.getTitle();
        viewHolder.titleNews.setText(subject);
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
        new ImageLoadTask(img, viewHolder.imageView).execute();



        return view;
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {

                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                return myBitmap;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }
    }


}
