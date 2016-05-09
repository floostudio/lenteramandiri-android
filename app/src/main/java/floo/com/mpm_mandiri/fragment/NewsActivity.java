package floo.com.mpm_mandiri.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import floo.com.mpm_mandiri.R;
import floo.com.mpm_mandiri.adapter.News;
import floo.com.mpm_mandiri.adapter.NewsAdapter;
import floo.com.mpm_mandiri.data.NewsDetailActivity;
import floo.com.mpm_mandiri.utils.DataManager;

/**
 * Created by Floo on 3/3/2016.
 */
public class NewsActivity extends Fragment {
    DynamicListView list_news;
    String url = DataManager.url;
    String urlNews = DataManager.urlNewsList;
    HashMap<String, Object> hashmapNews;

    private ProgressDialog pDialog;
    private static final String title = "title";
    private static final String content = "content";
    private static final String image = "image";
    int strnews_id;
    String strTitle, strContent, formatDate, strimage;
    int strDate;
    NewsAdapter newsAdapter;
    ArrayList<News> newsArray;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_news, container, false);
        initView(v);

        new DataFetcherTask().execute();
        //appearanceAnimate(1);


        return v;
    }
    public void initView(View view) {

        list_news = (DynamicListView) view.findViewById(R.id.list_news);

        list_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView taID = (TextView)view.findViewById(R.id.txt_id_news);
                Intent detailNews = new Intent(getActivity(), NewsDetailActivity.class);
                detailNews.putExtra("news_id", taID.getText().toString());
                startActivity(detailNews);
            }
        });
    }

    private void epochtodate(int epoch){
        Date date = new Date(epoch * 1000L);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));

        formatDate = format.format(date);
    }


    private class DataFetcherTask extends AsyncTask<Void, Void, ArrayList<News>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...!!!");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<News> doInBackground(Void... arg0) {

            try {
                newsArray = new ArrayList<News>();
                JSONArray jsonArray = new JSONArray(DataManager.MyHttpGet(urlNews));
                for (int i=0; i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    strnews_id = jsonObject.getInt("news_id");
                    strTitle = jsonObject.getString(title);
                    strimage = jsonObject.getString(image);
                    strContent = jsonObject.getString(content);
                    strDate = jsonObject.getInt("date");

                    //epochtodate(strDate);

                    News news = new News();
                    news.setNews_id(strnews_id);
                    news.setTitle(strTitle);
                    news.setImage(strimage);
                    news.setContent(strContent);
                    news.setDate(strDate);

                    newsArray.add(news);

                }
            }catch (JSONException e){
                e.printStackTrace();
            }

            return newsArray;
        }

        @Override
        protected void onPostExecute(ArrayList<News> result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            newsAdapter = new NewsAdapter(getActivity(), result);
            //AnimationAdapter animasi = new ScaleInAnimationAdapter(newsAdapter);
            //animasi.setAbsListView(list_news);
            //animationAdapter = new AlphaInAnimationAdapter(newsAdapter);
            //animationAdapter.setAbsListView(list_news);
            list_news.setAdapter(newsAdapter);

        }
    }
}