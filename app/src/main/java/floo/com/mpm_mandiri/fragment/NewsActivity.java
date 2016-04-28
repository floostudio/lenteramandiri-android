package floo.com.mpm_mandiri.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
    ListView list_news;
    String url = DataManager.url;
    String urlNews = DataManager.urlNewsList;
    HashMap<String, String > hashmapNews;

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


        return v;
    }
    public void initView(View view) {

        list_news = (ListView) view.findViewById(R.id.list_news);
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

    public void epochtodate(int epoch){
        Date date = new Date(epoch * 1000L);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        formatDate = format.format(date);
    }

    private class DataFetcherTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...!!!");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            String objek="";

            HttpParams myParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(myParams, 5000);
            HttpConnectionParams.setSoTimeout(myParams, 5000);

            JSONObject object = new JSONObject();
            try {

                object.put("device_type","Samsung Galaxy Note 5");
                object.put("device_os","android OS 4.4.2");
                object.put("device_uuid","njadnjlvafjvnjnjasmsodc");
                object.put("vendor_name","DOT");
                object.put("vendor_pass","DOTVNDR");


                String json = object.toString();

                HttpClient httpclient = new DefaultHttpClient(myParams);


                HttpPost httppost = new HttpPost(url);
                httppost.setHeader("Content-Type", "application/json");
                httppost.setHeader("Accept", "application/json");
                httppost.setHeader("Accept-Language", "en-us");
                httppost.setHeader("X-Timezone", "Asia/Jakarta");

                StringEntity se = new StringEntity(json);
                httppost.setEntity(se);

                HttpResponse response = httpclient.execute(httppost);
                objek = EntityUtils.toString(response.getEntity());


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String access_key="";
            try{
                JSONObject jsonObject2 = new JSONObject(objek);
                access_key = jsonObject2.getString("access_key");
            }catch (Exception e){
                e.printStackTrace();
            }

            String serverData="";
            DefaultHttpClient httpClient= new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(urlNews);
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("X-Header_access_key", access_key);
            httpGet.setHeader("Accept-Language","en-us");
            httpGet.setHeader("X-Timezone","Asia/Jakarta");

            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                serverData = EntityUtils.toString(httpEntity);

            }catch (ClientProtocolException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            Log.e("fuck", "fuck this shit");
            String coba="";
            try {
                newsArray = new ArrayList<News>();
                JSONArray jsonArray = new JSONArray(serverData);
                for (int i=0; i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    strnews_id = jsonObject.getInt("news_id");
                    strTitle = jsonObject.getString(title);
                    strimage = jsonObject.getString(image);
                    strContent = jsonObject.getString(content);
                    strDate = jsonObject.getInt("date");
                    Log.d("strlen", strContent);
                    //epochtodate(strDate);

                    News news = new News();
                    news.setNews_id(strnews_id);
                    news.setTitle(strTitle);
                    news.setImage(strimage);
                    news.setContent(strContent);
                    news.setDate(strDate);

                    newsArray.add(news);

                    coba = title;
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

            newsAdapter = new NewsAdapter(getActivity(), newsArray);
            list_news.setAdapter(newsAdapter);

        }
    }
}