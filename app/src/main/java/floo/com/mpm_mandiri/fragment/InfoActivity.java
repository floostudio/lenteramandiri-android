package floo.com.mpm_mandiri.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import dmax.dialog.SpotsDialog;
import floo.com.mpm_mandiri.R;
import floo.com.mpm_mandiri.adapter.Info;
import floo.com.mpm_mandiri.adapter.InfoAdapter;
import floo.com.mpm_mandiri.adapter.News;
import floo.com.mpm_mandiri.adapter.NewsAdapter;
import floo.com.mpm_mandiri.data.NewsDetailActivity;
import floo.com.mpm_mandiri.utils.DataManager;
import floo.com.mpm_mandiri.utils.swiperefreshbottom.SwipeRefreshLayoutBottom;

/**
 * Created by Floo on 3/3/2016.
 */
public class InfoActivity extends Fragment{
    ListView list_info;

    String urlInfo = DataManager.urlInfo;


    private SpotsDialog pDialog;
    private static final String title = "title";
    private static final String image = "image";
    private static final String url = "url";
    int strinfo_id;
    String strTitle, strContent, formatDate, strimage, strUrl;
    int strDate;
    InfoAdapter infoAdapter;
    ArrayList<Info> infoArray;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_info, container, false);
        initView(v);

        new DataFetcherTask().execute();
        //appearanceAnimate(1);


        return v;
    }
    public void initView(View view) {
        list_info = (ListView) view.findViewById(R.id.list_info);

        infoArray = new ArrayList<Info>();
        //newsAdapter = new NewsAdapter(getActivity(), newsArray);
        //list_news.setAdapter(newsAdapter);

        list_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView taID = (TextView)view.findViewById(R.id.txt_id_info);
                TextView taURL = (TextView)view.findViewById(R.id.txt_url_info);
                //Log.e("urlLink", taURL.getText().toString());
                //Log.e("idparsing", taID.getText().toString());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(taURL.getText().toString()));
                    startActivity(intent);

            }
        });


    }

    private void epochtodate(int epoch){
        Date date = new Date(epoch * 1000L);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));

        formatDate = format.format(date);
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
                JSONArray jsonArray = new JSONArray(DataManager.MyHttpGet(urlInfo));
                for (int i=0; i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    strinfo_id = jsonObject.getInt("info_id");
                    strTitle = jsonObject.getString(title);
                    strimage = jsonObject.getString(image);
                    strUrl = jsonObject.getString(url);
                    strDate = jsonObject.getInt("date");

                    //epochtodate(strDate);

                    Info info = new Info();
                    info.setInfo_id(strinfo_id);
                    info.setTitle(strTitle);
                    info.setImage(strimage);
                    info.setUrl(strUrl);
                    info.setDate(strDate);

                    infoArray.add(info);


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
            infoAdapter = new InfoAdapter(getActivity(), infoArray);
            //infoAdapter.notifyDataSetChanged();
            list_info.setAdapter(infoAdapter);

        }
    }
}