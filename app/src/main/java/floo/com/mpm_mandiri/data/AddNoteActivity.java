package floo.com.mpm_mandiri.data;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import floo.com.mpm_mandiri.MainActivity;
import floo.com.mpm_mandiri.R;
import floo.com.mpm_mandiri.utils.DataManager;

/**
 * Created by Floo on 2/26/2016.
 */
public class AddNoteActivity extends AppCompatActivity {
    String url = DataManager.url;
    String urlNote = DataManager.urltaskNote;
    Toolbar toolbar;
    TextView titleToolbar, save;
    String isiNote, strNote, taskid, strStatus, strMessage;
    private static final String taskidd = "taskid";
    private static final String note = "note";
    public static final String status_code = "status_code";
    public static final String message = "message";

    EditText edt_add_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note_activity);
        initView();
    }

    public void initView(){
        Intent i = getIntent();
        isiNote = i.getStringExtra(note);
        taskid = i.getStringExtra(taskidd);
        toolbar = (Toolbar)findViewById(R.id.id_toolbar);
        titleToolbar = (TextView)toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("ADD NOTE");
        save = (TextView)findViewById(R.id.txt_save);
        edt_add_note = (EditText)findViewById(R.id.edt_add_note);
        edt_add_note.setText(isiNote);
        LinearLayout line = (LinearLayout) findViewById(R.id.linier_toolbar);

        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNoteActivity.this.finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddNoteActivity.this);

                // set title
                alertDialogBuilder.setTitle("Message");

                // set dialog message
                alertDialogBuilder
                        .setMessage("If you believe this text click save, otherwise click cancel???")
                        .setCancelable(false)
                        .setPositiveButton("Save",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                strNote = edt_add_note.getText().toString();
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
                AddNoteActivity.this.finish();
            }else {
                Toast.makeText(getApplicationContext(), strMessage, Toast.LENGTH_LONG).show();
            }

        }
    }
}
