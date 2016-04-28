package floo.com.mpm_mandiri;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import floo.com.mpm_mandiri.utils.ConnectionDetector;
import floo.com.mpm_mandiri.utils.DataManager;
import floo.com.mpm_mandiri.utils.SessionManager;

/**
 * Created by Floo on 2/22/2016.
 */
public class LoginActivity extends AppCompatActivity {
    TextView register;
    EditText email, password;
    Button login;
    String url = DataManager.url;
    String urlLogin = DataManager.urlLogin;
    String strEmail, strPassword, idParsing, escalatedParsing, strStatus,
            strFirstname, strLastname, strProfpic, strTitle, strMessage;
    ConnectionDetector connection;
    AlertDialog messagee;
    SessionManager session;
    int user_id=0;
    int strEscalated;
    ProgressDialog progressDialog;

    private static final String message = "message";
    public static final String status_code = "status_code";
    public static final String first_name = "first_name";
    public static final String last_name = "last_name";
    public static final String profpic = "profpic";
    public static final String title = "title";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        String str = dateNow();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = null;
        try {
            date = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long today = date.getTime();

    }

    private String dateNow(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm", Locale.getDefault());
        Date date1 = new Date();
        return dateFormat.format(date1);
    }

    public void initView(){
        session = new SessionManager(getApplicationContext());
        register = (TextView) findViewById(R.id.btn_Register);
        email = (EditText) findViewById(R.id.edtUser);
        password = (EditText) findViewById(R.id.edtPwd);
        login = (Button) findViewById(R.id.btn_Login);


        //initializing
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(nextRegister);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //email.setText("qwerty@domain.com", TextView.BufferType.EDITABLE);
                //password.setText("qwerty", TextView.BufferType.EDITABLE);

                if (email.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Email empty", Toast.LENGTH_LONG).show();

                }else if (password.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Password empty", Toast.LENGTH_LONG).show();

                }else {
                    strEmail = email.getText().toString();
                    strPassword = password.getText().toString();
                    new Login().execute();
                }
            }
        });
    }

    class Login extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(LoginActivity.this, "Log In...", "Please wait....!!");
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
                object1.put("email",strEmail);
                object1.put("password", strPassword);
                String json1 = object1.toString();


                DefaultHttpClient httpclient= new DefaultHttpClient(myParams);
                HttpPost httppost = new HttpPost(urlLogin);
                httppost.setHeader("Content-Type", "application/json");
                httppost.setHeader("Accept", "application/json");
                httppost.setHeader("X-Header_access_key", access_key);
                httppost.setHeader("Accept-Language","en-us");
                httppost.setHeader("X-Timezone", "Asia/Jakarta");

                StringEntity se = new StringEntity(json1);
                httppost.setEntity(se);

                HttpResponse response = httpclient.execute(httppost);
                serverData = EntityUtils.toString(response.getEntity());



            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                JSONObject jsonObject = new JSONObject(serverData);
                strStatus = jsonObject.getString(status_code);
                if (strStatus.trim().equals("200")){
                    user_id = jsonObject.getInt("user_id");
                    strFirstname = jsonObject.getString(first_name);
                    strLastname = jsonObject.getString(last_name);
                    strProfpic = jsonObject.getString(profpic);
                    strTitle = jsonObject.getString(title);
                    strMessage = jsonObject.getString(message);
                }else {
                    strMessage = jsonObject.getString(message);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            //progressDialog.dismiss();
            try{
                if (strStatus.trim().equals("200")){
                    idParsing = Integer.toString(user_id);
                        Toast.makeText(getApplicationContext(), strMessage, Toast.LENGTH_LONG).show();
                        session.createLoginSession(idParsing, strFirstname, strLastname, strProfpic);
                        Intent nextMenu = new Intent(LoginActivity.this, MainActivity.class);

                        //nextMenu.putExtra("IDPARSING", idParsing);
                        //nextMenu.putExtra(first_name, strFirstname);
                        //nextMenu.putExtra(last_name, strLastname);
                        //nextMenu.putExtra(profpic, strProfpic);

                        startActivity(nextMenu);
                        finish();

                }else{

                    Toast.makeText(getApplicationContext(), "Access Denied", Toast.LENGTH_LONG).show();

                }

            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "Access Denied", Toast.LENGTH_LONG).show();
            }


        }
    }


}
