package com.floo.lenteramandiri.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.floo.lenteramandiri.utils.ConnectivityReceiver;
import com.floo.lenteramandiri.utils.DataManager;
import com.floo.lenteramandiri.utils.ImageLoader;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;
import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.data.ChangePasswordActivity;
import com.floo.lenteramandiri.utils.MyLenteraMandiri;

/**
 * Created by Floo on 2/23/2016.
 */
public class ProfilActivity extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener{
    String url = DataManager.url;
    String urlProfil = DataManager.urlprofilList;
    HashMap<String, String> hashmapTask;
    private SpotsDialog pDialog;
    String idParsing;
    ImageView imgProfil;
    Bitmap myBitmap;
    LinearLayout linearLayout;

    private static final String USER_ID = "USER_ID";
    private static final String USER_FIRST_NAME = "first_name";
    private static final String USER_LAST_NAME = "last_name";
    private static final String USER_NIP = "nip";
    private static final String USER_DIRECTORATE = "directorate";
    private static final String USER_GROUP = "group";
    private static final String USER_DEPARTMENT = "department";
    private static final String USER_EMAIL = "email";
    private static final String USER_TITLE = "title";
    private static final String profpic = "profpic";


    TextView txtFullname, txtTitle, txtNip, txtEmail, txtDirectorat, txtGroup, txtDepartment;
    String strId, strFirstname, strLastname, strNip, strDirctorate, strGroup,
            strDepartment, strEmail, strTitle, strPass, strImg, strIsactiv;
    Button update;
    ImageLoader imageLoader = new ImageLoader();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_profil, container, false);
        initView(v);
        new DataFetcherProfil().execute();

        return v;
    }

    public void initView(View view){
        idParsing = this.getArguments().getString("IDPARSING");

        txtFullname = (TextView)view.findViewById(R.id.txt_profil_fullname);
        txtTitle = (TextView)view.findViewById(R.id.txt_profil_title);
        txtNip = (TextView)view.findViewById(R.id.txt_profil_nip);
        txtEmail = (TextView) view.findViewById(R.id.txt_profil_email);
        txtDirectorat = (TextView)view.findViewById(R.id.txt_profil_directorat);
        txtGroup = (TextView)view.findViewById(R.id.txt_profil_group);
        txtDepartment = (TextView)view.findViewById(R.id.txt_profil_department);
        imgProfil = (ImageView)view.findViewById(R.id.img_profil);
        linearLayout = (LinearLayout)view.findViewById(R.id.linier_changepassword);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent change = new Intent(getActivity(), ChangePasswordActivity.class);
                change.putExtra("email", txtEmail.getText().toString());
                startActivity(change);

            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        DataManager.showSnack(getActivity(), isConnected);
    }

    @Override
    public void onResume() {
        super.onResume();
        MyLenteraMandiri.getInstance().setConnectivityListener(this);
        DataManager.checkConnection(getActivity());
    }

    private class DataFetcherProfil extends AsyncTask<Void, Void, Void> {

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
            HttpGet httpGet = new HttpGet(urlProfil+idParsing);
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
                JSONObject jsonObject = new JSONObject(serverData);

                strFirstname = jsonObject.getString(USER_FIRST_NAME);
                strLastname = jsonObject.getString(USER_LAST_NAME);
                strNip = jsonObject.getString(USER_NIP);
                strDirctorate = jsonObject.getString(USER_DIRECTORATE);
                strGroup = jsonObject.getString(USER_GROUP);
                strDepartment = jsonObject.getString(USER_DEPARTMENT);
                strEmail = jsonObject.getString(USER_EMAIL);
                strTitle = jsonObject.getString(USER_TITLE);
                strImg = jsonObject.getString(profpic);

                URL urlConnection = new URL(strImg);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();

                if (connection.getResponseCode()==200){

                    InputStream input = connection.getInputStream();
                    myBitmap = BitmapFactory.decodeStream(input);
                }else {
                    myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile);
                }

            }catch (JSONException e){
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            txtFullname.setText(strFirstname+" "+strLastname);
            txtTitle.setText(strTitle);
            txtNip.setText(strNip);
            txtEmail.setText(strEmail);
            txtDirectorat.setText(strDirctorate);
            txtGroup.setText(strGroup);
            txtDepartment.setText(strDepartment);

            imgProfil.setImageBitmap(myBitmap);
        }
    }
}

