package com.floo.lenteramandiri;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.floo.lenteramandiri.utils.ConnectionDetector;
import com.floo.lenteramandiri.utils.ConnectivityReceiver;
import com.floo.lenteramandiri.utils.DataManager;
import com.floo.lenteramandiri.utils.MyLenteraMandiri;
import com.floo.lenteramandiri.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Floo on 2/22/2016.
 */
public class LoginActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
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

    String regid;
    String msg = "";
    String PROJECTNUMBER = DataManager.PROJECTNUMBER;

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

                if (email.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "NIP empty", Toast.LENGTH_LONG).show();

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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        DataManager.showSnack(getApplicationContext(), isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyLenteraMandiri.getInstance().setConnectivityListener(this);
        DataManager.checkConnection(this);
    }

    class Login extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(LoginActivity.this, "Log In...", "Please wait....!!");
        }

        @Override
        protected Void doInBackground(Void... params) {
            String json1 = "";
            JSONObject object1 = new JSONObject();

            try {
                object1.put("nip",strEmail);
                object1.put("password", strPassword);
                json1 = object1.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject(DataManager.MyHttpPost(urlLogin, json1));
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

            try{
                if (strStatus.trim().equals("200")){
                    idParsing = Integer.toString(user_id);
                        Toast.makeText(getApplicationContext(), strMessage, Toast.LENGTH_LONG).show();
                        session.createLoginSession(idParsing, strFirstname, strLastname, strProfpic);
                        Intent nextMenu = new Intent(LoginActivity.this, MainActivity.class);

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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Keluar!");
        alertDialogBuilder
                .setMessage("Apakah anda mau keluar dari aplikasi?")
                .setCancelable(false)
                .setPositiveButton("Iya",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
