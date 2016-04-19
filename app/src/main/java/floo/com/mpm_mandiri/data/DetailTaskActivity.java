package floo.com.mpm_mandiri.data;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import floo.com.mpm_mandiri.MainActivity;
import floo.com.mpm_mandiri.R;
import floo.com.mpm_mandiri.fragment.TaskActivity;
import floo.com.mpm_mandiri.utils.DataManager;

/**
 * Created by Floo on 2/25/2016.
 */
public class DetailTaskActivity extends AppCompatActivity {
    String url = DataManager.url;
    String urlDetailTask = DataManager.urltaskDetails;
    String idTaskParsing, strTitle,  strNote, strCompany,strDetail,
            strDetailDesc, formatDate;
    int strExpire, strid, strDetailTaskid;
    TextView txtSubject, txtPt, txtTgl, txtID;
    Toolbar toolbar;
    LinearLayout line;
    TextView titleToolbar, save;
    Button btnNote, btnReport, btnDone;
    ImageView img_list_task;
    private static final String taskid = "taskid";
    private static final String description = "description";
    private static final String title = "title";
    private static final String note = "note";
    private static final String company = "company";
    private static final String detail = "detail";
    private ProgressDialog pDialog;
    HashMap<String, String> hashmapDetailTaskList;
    ArrayList<HashMap<String, String>> arrayDetailTaskList;
    ListView listDetailTaskList;
    SimpleAdapter adapterDetailTaskList;

    public static final String status_code = "status_code";
    public static final String message = "message";
    String urlDone = DataManager.urltaskDone;
    String strStatus, strMessage;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list_task);
        initView();
        arrayDetailTaskList = new ArrayList<HashMap<String, String>>();
        new DataFetcherDetailTask().execute();




    }

    public void initView(){
        Intent i = getIntent();
        idTaskParsing  = i.getStringExtra("task_id");
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        titleToolbar = (TextView)toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("TASK DETAIL");
        save = (TextView)findViewById(R.id.txt_save);
        txtSubject = (TextView) findViewById(R.id.txt_detail_task_subject);
        txtPt = (TextView) findViewById(R.id.txt_detail_task_pt);
        txtTgl = (TextView) findViewById(R.id.txt_detail_task_tgl);
        txtID = (TextView) findViewById(R.id.txt_detail_task_id);
        img_list_task = (ImageView)findViewById(R.id.img_list_task);
        listDetailTaskList = (ListView)findViewById(R.id.list_detail_task);
        btnNote = (Button) findViewById(R.id.btn_detail_task_note);

        btnDone = (Button) findViewById(R.id.btn_detail_task_done);
        save.setVisibility(View.INVISIBLE);
        line = (LinearLayout) findViewById(R.id.linier_toolbar);

        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailTaskActivity.this.finish();
            }
        });

        btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent nextNote = new Intent(DetailTaskActivity.this, AddNoteActivity.class);
                nextNote.putExtra(note, strNote);
                nextNote.putExtra(taskid, txtID.getText().toString());
                startActivity(nextNote);
            }
        });



        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent back=new Intent(DetailTaskActivity.this, MainActivity.class);
                //back.putExtra("fragment", "fragment");
                //startActivity(back);
               new AlertDialog.Builder(DetailTaskActivity.this)
                        .setTitle("Really?")
                        .setMessage("Are you sure you want to done?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        new DoneAsync().execute();
                                        Intent back=new Intent(DetailTaskActivity.this, MainActivity.class);
                                        back.putExtra("fragment", "fragment");
                                        startActivity(back);
                                        //TaskActivity.ta.RefreshList();


                                    }
                                }).create().show();

            }
        });



    }

    class DoneAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String objek = "";

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

            JSONObject object1 = new JSONObject();
            try {


                DefaultHttpClient httpclient= new DefaultHttpClient(myParams);
                HttpPut httpPut = new HttpPut(urlDone+idTaskParsing);
                httpPut.setHeader("Content-Type", "application/json");
                httpPut.setHeader("Accept", "application/json");
                httpPut.setHeader("X-Header_access_key", access_key);
                httpPut.setHeader("Accept-Language","en-us");
                httpPut.setHeader("X-Timezone", "Asia/Jakarta");

                HttpResponse response = httpclient.execute(httpPut);
                serverData = EntityUtils.toString(response.getEntity());



            }  catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject(serverData);
                strStatus = jsonObject.getString(status_code);
                strMessage = jsonObject.getString(message);


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (strStatus.trim().equals("200")){
                Toast.makeText(getApplicationContext(), strMessage, Toast.LENGTH_LONG).show();
                DetailTaskActivity.this.finish();
                //Intent back = new Intent(DetailTaskActivity.this, MainActivity.class);
                //startActivity(back);


            }else {
                Toast.makeText(getApplicationContext(), strMessage, Toast.LENGTH_LONG).show();
            }

        }
    }

    private String dateNow(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm", Locale.getDefault());
        Date date1 = new Date();
        return dateFormat.format(date1);
    }

    public void epochtodate(int epoch){
        Date date = new Date(epoch * 1000L);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        formatDate = format.format(date);
    }

    private class DataFetcherDetailTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailTaskActivity.this);
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
            HttpGet httpGet = new HttpGet(urlDetailTask+idTaskParsing);
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
            int convert=0;
            try {
                JSONObject jsonObject = new JSONObject(serverData);
                strid = jsonObject.getInt("task_id");
                strTitle = jsonObject.getString(title);
                strExpire = jsonObject.getInt("expire");
                strNote = jsonObject.getString(note);
                strCompany = jsonObject.getString(company);

                //convert = Integer.parseInt(strExpire);
                epochtodate(strExpire);

                JSONArray jsonArray=jsonObject.getJSONArray(detail);
                for (int i=0; i<jsonArray.length();i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    strDetailTaskid = jsonObject1.getInt("task_detail_id");
                    strDetailDesc = jsonObject1.getString(description);

                    HashMap<String, String> hashmapDetailTaskList = new HashMap<String, String>();
                    hashmapDetailTaskList.put("task_id", Integer.toString(strid));
                    hashmapDetailTaskList.put("task_detail_id", Integer.toString(strDetailTaskid));
                    hashmapDetailTaskList.put(description, strDetailDesc);


                    arrayDetailTaskList.add(hashmapDetailTaskList);
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
            String id = Integer.toString(strid);
            txtSubject.setText(strTitle);
            txtPt.setText(strCompany);
            txtTgl.setText(formatDate);
            txtID.setText(id);

            long today;
            long epoch = 2592000;

            String str = dateNow();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date2 = null;
            try {
                date2 = df.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            today = date2.getTime()/1000;

            if (strExpire < today) {
                img_list_task.setImageResource(R.drawable.point_red);

            }else if (strExpire >= (today+epoch)){
                img_list_task.setImageResource(R.drawable.point_green);
            }else {
                img_list_task.setImageResource(R.drawable.point_orange);
            }

            //adapterDetailTaskList = new SimpleAdapter(getApplicationContext(), arrayDetailTaskList,
            //        R.layout.list_row_detail_task,new String[]{description},new int[]{R.id.txt_task_list});
            //listDetailTaskList.setAdapter(adapterDetailTaskList);



        }
    }


}
