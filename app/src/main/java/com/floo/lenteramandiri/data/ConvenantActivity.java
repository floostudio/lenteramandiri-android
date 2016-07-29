package com.floo.lenteramandiri.data;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

import dmax.dialog.SpotsDialog;
import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.utils.DataManager;

/**
 * Created by Floo on 3/11/2016.
 */
public class ConvenantActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout line;
    TextView titleToolbar, save, txtConvenant, txtCompany;
    String pCompany, pAcc_number;
    String strCovTitle, strCovNote, strCompany;
    int strCovId, strCovExpire;

    String url = DataManager.url;
    String urlCovenant = DataManager.urlCovenant;
    private SpotsDialog pDialog;
    SimpleAdapter adapter;
    ListView listCovenant;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> mylist;

    private static final String cov_id = "cov_id";
    private static final String cov_title = "cov_title";
    private static final String cov_expire = "cov_expire";
    private static final String cov_note = "cov_note";
    private static final String company_name = "company_name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convenant);
        initView();
        mylist = new ArrayList<HashMap<String, String>>();
        new DataCovenant().execute();
    }
    public void initView(){
        Intent i = getIntent();
        pAcc_number = i.getStringExtra("acc_number");
        pCompany = i.getStringExtra("company_name");

        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        titleToolbar = (TextView)toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("COVENANT");
        save = (TextView)findViewById(R.id.txt_save);
        line = (LinearLayout) findViewById(R.id.linier_toolbar);

        txtCompany = (TextView)findViewById(R.id.txt_convenant_company);
        listCovenant = (ListView)findViewById(R.id.list_covenant);
        txtCompany.setText(pCompany);

        save.setVisibility(View.INVISIBLE);
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConvenantActivity.this.finish();
            }
        });

    }

    private String epochtodate(int epoch){
        Date date = new Date(epoch * 1000L);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        return  format.format(date);
    }

    private class DataCovenant extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new SpotsDialog(ConvenantActivity.this, R.style.CustomProgress);
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
            HttpGet httpGet = new HttpGet(urlCovenant+pAcc_number);

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

            String coba="";

            try {

                JSONArray jsonArray = new JSONArray(serverData);
                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    strCovId = jsonObject.getInt("cov_id");
                    strCovTitle = jsonObject.getString(cov_title);
                    strCovExpire = jsonObject.getInt("cov_expire");
                    strCovNote = jsonObject.getString(cov_note);


                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put(cov_id, Integer.toString(i+1));
                    hashMap.put(cov_title, strCovTitle);

                    mylist.add(hashMap);
                }

                coba = strCompany;
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

            adapter = new SimpleAdapter(getApplicationContext(), mylist,
                    R.layout.list_row_covenant,new String[]{cov_id, cov_title},
                    new int[]{R.id.txt_list_cov_no, R.id.txt_list_cov_title});
            listCovenant.setAdapter(adapter);

        }
    }
}
