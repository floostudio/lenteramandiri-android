package com.floo.lenteramandiri.data;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.floo.lenteramandiri.utils.DataManager;
import com.floo.lenteramandiri.utils.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import dmax.dialog.SpotsDialog;
import com.floo.lenteramandiri.R;

/**
 * Created by Floo on 3/6/2016.
 */
public class NewsDetailActivity extends AppCompatActivity{
    String url = DataManager.url;
    String urlDetailNews = DataManager.urlFetchNews;
    Toolbar toolbar;
    LinearLayout line;
    TextView titleToolbar, save;
    String idNewsParsing, strTitle, strImage, strContent, formatDate;
    int strid, strDate;
    private static final String title = "title";
    private static final String content = "content";
    private static final String image = "image";
    HashMap<String, String> hashmapDetailNewsList;
    ArrayList<HashMap<String, String>> arrayDetailNewsList;
    SimpleAdapter adapterDetailNewsList;
    private SpotsDialog pDialog;
    Bitmap myBitmap;
    TextView txtTitle, txtDate, txtContent;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        findbyid();
        new DataFetcherDetailTask().execute();
    }
    public void findbyid(){
        Intent i = getIntent();
        idNewsParsing  = i.getStringExtra("news_id");
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        titleToolbar = (TextView)toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("DETIL INFORMASI");
        save = (TextView)findViewById(R.id.txt_save);
        save.setVisibility(View.INVISIBLE);
        line = (LinearLayout) findViewById(R.id.linier_toolbar);

        txtTitle = (TextView) findViewById(R.id.txt_news_detail_subject);
        txtContent = (TextView) findViewById(R.id.txt_news_detail_content);
        txtDate = (TextView) findViewById(R.id.txt_news_detail_date);
        imageView = (ImageView) findViewById(R.id.img_news_detail);

        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsDetailActivity.this.finish();
            }
        });
    }

    public void epochtodate(int epoch){
        Date date = new Date(epoch * 1000L);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        formatDate = format.format(date);
    }

    private class DataFetcherDetailTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new SpotsDialog(NewsDetailActivity.this, R.style.CustomProgress);
            pDialog.setMessage("Please wait...!!!");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            String serverData = DataManager.MyHttpGet(urlDetailNews+idNewsParsing);

            try {
                JSONObject jsonObject = new JSONObject(serverData);
                strid = jsonObject.getInt("news_id");
                strTitle = jsonObject.getString(title);
                strImage = jsonObject.getString(image);
                strContent = jsonObject.getString(content);
                strDate = jsonObject.getInt("date");

                epochtodate(strDate);

                myBitmap = ImageLoader.getBitmap(strImage);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            txtTitle.setText(strTitle);
            txtDate.setText(formatDate);
            txtContent.setText(strContent);
            imageView.setImageBitmap(myBitmap);

        }
    }
}
