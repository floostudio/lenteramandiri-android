package floo.com.mpm_mandiri.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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

import dmax.dialog.SpotsDialog;
import floo.com.mpm_mandiri.R;
import floo.com.mpm_mandiri.adapter.News;
import floo.com.mpm_mandiri.adapter.NewsAdapter;
import floo.com.mpm_mandiri.data.NewsDetailActivity;
import floo.com.mpm_mandiri.utils.DataManager;
import floo.com.mpm_mandiri.utils.swiperefreshbottom.SwipeRefreshLayoutBottom;

/**
 * Created by Floo on 3/3/2016.
 */
public class NewsActivity extends Fragment implements SwipeRefreshLayoutBottom.OnRefreshListener{
    ListView list_news;

    String urlNews = DataManager.urlNewsList;
    HashMap<String, Object> hashmapNews;

    private SpotsDialog pDialog;
    private static final String title = "title";
    private static final String content = "content";
    private static final String image = "image";
    private static final String url = "url";
    int strnews_id;
    String strTitle, strContent, formatDate, strimage, strUrl;
    int strDate;
    NewsAdapter newsAdapter;
    ArrayList<News> newsArray;
    SwipeRefreshLayoutBottom refreshLayout;
    private int offset = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_news, container, false);
        initView(v);

        //new DataFetcherTask().execute();
        //appearanceAnimate(1);


        return v;
    }
    public void initView(View view) {
        refreshLayout = (SwipeRefreshLayoutBottom) view.findViewById(R.id.refresh);
        list_news = (ListView) view.findViewById(R.id.list_news);

        newsArray = new ArrayList<News>();
        //newsAdapter = new NewsAdapter(getActivity(), newsArray);
        //list_news.setAdapter(newsAdapter);

        list_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView taID = (TextView)view.findViewById(R.id.txt_id_news);
                TextView taURL = (TextView)view.findViewById(R.id.txt_url_news);
                //Log.e("urlLink", taURL.getText().toString());
                //Log.e("idparsing", taID.getText().toString());
                if (taURL.getText().toString().trim().equals("")){
                    Intent detailNews = new Intent(getActivity(), NewsDetailActivity.class);
                    detailNews.putExtra("news_id", taID.getText().toString());
                    startActivity(detailNews);
                }else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(taURL.getText().toString()));
                    startActivity(intent);
                }
            }
        });


        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(R.color.yellow, R.color.cpb_blue);


        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                new DataFetcherTask().execute();
            }
        });
    }

    private void epochtodate(int epoch){
        Date date = new Date(epoch * 1000L);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));

        formatDate = format.format(date);
    }

    @Override
    public void onRefresh() {
        new DataFetcherTask().execute();
    }


    private class DataFetcherTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new SpotsDialog(getActivity(), R.style.CustomProgress);
            pDialog.setMessage("Please wait...!!!");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                //Log.d("urutan", String.valueOf(offset));
                JSONArray jsonArray = new JSONArray(DataManager.MyHttpGet(urlNews+offset+"&limit=10"));
                for (int i=0; i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    strnews_id = jsonObject.getInt("news_id");
                    strTitle = jsonObject.getString(title);
                    strimage = jsonObject.getString(image);
                    strUrl = jsonObject.getString(url);
                    strContent = jsonObject.getString(content);
                    strDate = jsonObject.getInt("date");

                    //epochtodate(strDate);

                    News news = new News();
                    news.setNews_id(strnews_id);
                    news.setTitle(strTitle);
                    news.setImage(strimage);
                    news.setUrl(strUrl);
                    news.setContent(strContent);
                    news.setDate(strDate);

                    newsArray.add(news);

                    offset = offset+1;

                }
            }catch (JSONException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            refreshLayout.setRefreshing(false);
            newsAdapter = new NewsAdapter(getActivity(), newsArray);
            newsAdapter.notifyDataSetChanged();
            list_news.setAdapter(newsAdapter);

        }
    }
}