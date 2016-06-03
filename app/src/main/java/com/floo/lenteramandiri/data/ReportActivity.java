package com.floo.lenteramandiri.data;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.floo.lenteramandiri.utils.DataManager;

import org.json.JSONException;
import org.json.JSONObject;

import com.floo.lenteramandiri.R;

/**
 * Created by Floo on 2/26/2016.
 */
public class ReportActivity extends AppCompatActivity {
    String url = DataManager.url;
    String urlNote = DataManager.urltaskNote;
    Toolbar toolbar;
    TextView titleToolbar, save;
    String isiNote, strNote, taskid, strStatus, strMessage;
    private static final String taskidd = "taskid";
    private static final String note = "note";
    public static final String status_code = "status_code";
    public static final String message = "message";

    EditText edt_add_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initView();
    }

    public void initView(){
        Intent i = getIntent();
        isiNote = i.getStringExtra(note);
        taskid = i.getStringExtra(taskidd);
        toolbar = (Toolbar)findViewById(R.id.id_toolbar);
        titleToolbar = (TextView)toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("REPORT");
        save = (TextView)findViewById(R.id.txt_save);
        edt_add_report = (EditText)findViewById(R.id.edt_add_report);
        edt_add_report.setText(isiNote);
        LinearLayout line = (LinearLayout) findViewById(R.id.linier_toolbar);

        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportActivity.this.finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportActivity.this);

                // set title
                alertDialogBuilder.setTitle("Message");

                // set dialog message
                alertDialogBuilder
                        .setMessage("If you believe this text click save, otherwise click cancel???")
                        .setCancelable(false)
                        .setPositiveButton("Save",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                strNote = edt_add_report.getText().toString();
                                new NoteAsync().execute();

                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });
    }

    class NoteAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            /*String objek = "";

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
            //int id = Integer.parseInt(taskid);
            JSONObject object1 = new JSONObject();
            try {
                object1.put(note, strNote);
                String json1 = object1.toString();


                DefaultHttpClient httpclient= new DefaultHttpClient(myParams);
                HttpPut httpPut = new HttpPut(urlNote+taskid);
                httpPut.setHeader("Content-Type", "application/json");
                httpPut.setHeader("Accept", "application/json");
                httpPut.setHeader("X-Header_access_key", access_key);
                httpPut.setHeader("Accept-Language","en-us");
                httpPut.setHeader("X-Timezone", "Asia/Jakarta");

                StringEntity se = new StringEntity(json1);
                httpPut.setEntity(se);

                HttpResponse response = httpclient.execute(httpPut);
                serverData = EntityUtils.toString(response.getEntity());



            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            String putNote = "";
            JSONObject objNote = new JSONObject();

            try {
                objNote.put(note, strNote);
                putNote = objNote.toString();

                JSONObject jsonObject = new JSONObject(DataManager.MyHttpPut(urlNote+taskid, putNote));
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
                ReportActivity.this.finish();
            }else {
                Toast.makeText(getApplicationContext(), strMessage, Toast.LENGTH_LONG).show();
            }

        }
    }
}
