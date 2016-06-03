package com.floo.lenteramandiri.data;

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
 * Created by Floo on 3/14/2016.
 */
public class ChangePasswordActivity extends AppCompatActivity{
    String url = DataManager.url;
    String urlChangePass = DataManager.urlChangePass;
    Toolbar toolbar;
    LinearLayout line;
    TextView titleToolbar, save;
    String emailParsing, strCurrent, strNew, strStatus, strMessage;
    EditText edtCurrent, edtNew, edtReenter;
    private static final String email = "email";
    private static final String old_password = "old_password";
    private static final String new_password = "new_password";
    public static final String status_code = "status_code";
    public static final String message = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initView();
    }
    public void initView(){
        Intent i = getIntent();
        emailParsing  = i.getStringExtra("email");
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        titleToolbar = (TextView)toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("CHANGE PASSWORD");
        save = (TextView)findViewById(R.id.txt_save);
        line = (LinearLayout) findViewById(R.id.linier_toolbar);
        edtCurrent = (EditText) findViewById(R.id.edt_change_currentpassword);
        edtNew = (EditText)findViewById(R.id.edt_change_newpassword);
        edtReenter = (EditText)findViewById(R.id.edt_change_reenterpassword);



        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordActivity.this.finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCurrent.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Current Password Empty",Toast.LENGTH_LONG).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }else if (edtNew.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "New Password Empty",Toast.LENGTH_LONG).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }else if (edtReenter.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Re-Enter Password Empty",Toast.LENGTH_LONG).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }else {
                    if (!edtReenter.getText().toString().trim().equals(edtNew.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Password don't match", Toast.LENGTH_LONG)
                                .show();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }else {
                        strCurrent = edtCurrent.getText().toString();
                        strNew = edtNew.getText().toString();
                        new changePasswordAsync().execute();
                    }
                }

            }
        });

    }

    class changePasswordAsync extends AsyncTask<Void, Void, Void> {

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

            JSONObject object1 = new JSONObject();
            try {
                object1.put(email, emailParsing);
                object1.put(old_password, strCurrent);
                object1.put(new_password, strNew);
                String json1 = object1.toString();



                DefaultHttpClient httpclient= new DefaultHttpClient(myParams);
                HttpPut httpPut = new HttpPut(urlChangePass);
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
            String objPut = "";
            JSONObject object1 = new JSONObject();
            try {
                object1.put(email, emailParsing);
                object1.put(old_password, strCurrent);
                object1.put(new_password, strNew);
                objPut = object1.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject(DataManager.MyHttpPut(urlChangePass, objPut));
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
                ChangePasswordActivity.this.finish();
            }else {
                Toast.makeText(getApplicationContext(), strMessage, Toast.LENGTH_LONG).show();
            }

        }
    }
}
