package floo.com.mpm_mandiri;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;

import floo.com.mpm_mandiri.utils.DataManager;
import floo.com.mpm_mandiri.adapter.DataSpinner;

/**
 * Created by Floo on 2/22/2016.
 */
public class RegisterActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView titletoolbar, save;
    String url = DataManager.url;
    String urlRegister = DataManager.urlRegister;
    String urlDepartment = DataManager.urlMasterDepartment;
    String urlGroup = DataManager.urlMasterGroup;
    String urlTitle = DataManager.urlMasterTitle;
    EditText edtFirstName, edtLastName, edtNip, edtDirectorate,
            edtGroup, edtDepartment, edtEmail, edtTitle, edtPassword, edtConfirm;
    Button btnRegister;
    Spinner spinDirectorate, spinDepartment, spinGroup, spinTitle;
    TextView idDirectorate, idDepartment, idGroup, idTitle;
    String strFirstname, strLastname, strNip, strDirectorate,
            strGroup, strDepartment, strEmail,strTitle, strPassword, strConfirm;
    String strStatus, strMessage;

    private static final String first_name = "first_name";
    private static final String last_name = "last_name";
    private static final String nip = "nip";
    private static final String directorate = "directorate";
    private static final String group = "group";
    private static final String department = "department";
    private static final String email = "email";
    private static final String title = "title";
    private static final String password = "password";

    private static final String status_code = "status_code";
    private static final String message = "message";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        new DataDirectorate().execute();
        new DataDepartment().execute();
        new DataGroup().execute();
        new DataTitle().execute();

    }

    public void initView(){
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        titletoolbar = (TextView) toolbar.findViewById(R.id.titleToolbar);
        titletoolbar.setText("REGISTER");
        save = (TextView)findViewById(R.id.txt_save);
        save.setVisibility(View.INVISIBLE);
        LinearLayout line = (LinearLayout) findViewById(R.id.linier_toolbar);
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(backLogin);
                finish();
            }
        });

        edtFirstName = (EditText) findViewById(R.id.edt_reg_firstname);
        edtLastName = (EditText) findViewById(R.id.edt_reg_lastname);
        edtNip = (EditText) findViewById(R.id.edt_reg_nip);
        spinDirectorate = (Spinner) findViewById(R.id.spin_Directorate);
        idDirectorate = (TextView) findViewById(R.id.txtid_directorate);
        spinGroup = (Spinner) findViewById(R.id.spin_Group);
        idGroup = (TextView) findViewById(R.id.txtid_group);
        spinDepartment = (Spinner) findViewById(R.id.spin_Department);
        idDepartment = (TextView) findViewById(R.id.txtid_department);
        //edtDirectorate = (EditText) findViewById(R.id.edt_reg_directorate);
        //edtGroup = (EditText) findViewById(R.id.edt_reg_group);
        //edtDepartment = (EditText) findViewById(R.id.edt_reg_department);
        edtEmail = (EditText) findViewById(R.id.edt_reg_email);
        spinTitle = (Spinner)findViewById(R.id.spin_Title);
        idTitle = (TextView) findViewById(R.id.txtid_title);
        //edtTitle = (EditText) findViewById(R.id.edt_reg_title);
        edtPassword = (EditText) findViewById(R.id.edt_reg_password);
        edtConfirm = (EditText) findViewById(R.id.edt_reg_conf_password);
        btnRegister = (Button) findViewById(R.id.btn_reg_submit);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (edtFirstName.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Firstname empty",Toast.LENGTH_LONG).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }else if (edtLastName.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Lastname empty",Toast.LENGTH_LONG).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }else if (edtNip.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "NIP empty",Toast.LENGTH_LONG).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }else if (edtEmail.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Email empty",Toast.LENGTH_LONG).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }else if (edtPassword.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Password empty",Toast.LENGTH_LONG).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }else if (edtConfirm.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Confirm Password empty",Toast.LENGTH_LONG).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }else {
                    if (!edtPassword.getText().toString().trim().equals(edtConfirm.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Password does not match", Toast.LENGTH_LONG)
                                .show();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }else {
                        strFirstname = edtFirstName.getText().toString();
                        strLastname = edtLastName.getText().toString();
                        strNip = edtNip.getText().toString();
                        strDirectorate = idDirectorate.getText().toString();
                        strGroup = idGroup.getText().toString();
                        strDepartment = idDepartment.getText().toString();
                        strEmail = edtEmail.getText().toString();
                        strTitle = idTitle.getText().toString();
                        strPassword = edtPassword.getText().toString();
                        strConfirm = edtConfirm.getText().toString();
                        new RegisterAsync().execute();
                    }
                }
            }
        });

    }

    class RegisterAsync extends AsyncTask<Void, Void, Void> {

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
                Log.e("json", json);
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
                Log.e("hello", objek);

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
                object1.put(first_name,strFirstname);
                object1.put(last_name, strLastname);
                object1.put(nip, strNip);
                object1.put(directorate, strDirectorate);
                object1.put(group,strGroup);
                object1.put(department, strDepartment);
                object1.put(email,strEmail);
                object1.put(title, strTitle);
                object1.put(password,strPassword);

                String json1 = object1.toString();
                Log.e("json", json1);

                DefaultHttpClient httpclient= new DefaultHttpClient(myParams);
                HttpPost httppost = new HttpPost(urlRegister);
                httppost.setHeader("Content-Type", "application/json");
                httppost.setHeader("Accept", "application/json");
                httppost.setHeader("X-Header_access_key", access_key);
                httppost.setHeader("Accept-Language","en-us");
                httppost.setHeader("X-Timezone", "Asia/Jakarta");

                StringEntity se = new StringEntity(json1);
                httppost.setEntity(se);

                HttpResponse response = httpclient.execute(httppost);
                serverData = EntityUtils.toString(response.getEntity());
                Log.e("hello", serverData);


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
                //strStatus= jsonObject.getString(status_code);
                strMessage = jsonObject.getString(message);

                Log.e("hello", strMessage);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //Log.e("Login", strStatus);
            if (strMessage.trim().equals("Registered Success")){
                Toast.makeText(getApplicationContext(), strMessage, Toast.LENGTH_LONG).show();
                Intent nextMenu = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(nextMenu);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), strMessage, Toast.LENGTH_LONG).show();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
    }

    private class DataDirectorate extends AsyncTask<Void, Void, Void> {
        String urlDirectorate = DataManager.urlMasterDirectorate;
        private static final String name = "name";
        int stridDirectorate;
        String strNameDrectorate;
        ArrayList<String> worldListDirectorate;
        ArrayList<DataSpinner> worldDirectorate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            String objek="";
            worldDirectorate = new ArrayList<DataSpinner>();
            worldListDirectorate = new ArrayList<String>();

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
                Log.e("json", json);
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
                Log.e("hello", objek);

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
            HttpGet httpGet = new HttpGet(urlDirectorate);
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("X-Header_access_key", access_key);
            httpGet.setHeader("Accept-Language","en-us");
            httpGet.setHeader("X-Timezone","Asia/Jakarta");

            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                serverData = EntityUtils.toString(httpEntity);
                Log.d("response", serverData);
            }catch (ClientProtocolException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

            String coba="";

            try {
                JSONArray jsonArray=new JSONArray(serverData);
                for (int i=0; i<jsonArray.length();i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    stridDirectorate = jsonObject1.getInt("id");
                    strNameDrectorate = jsonObject1.getString(name);

                    DataSpinner dataSpinner = new DataSpinner();
                    dataSpinner.setId(stridDirectorate);
                    dataSpinner.setName(strNameDrectorate);

                    worldDirectorate.add(dataSpinner);

                    worldListDirectorate.add(strNameDrectorate);


                }

                coba = strNameDrectorate;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("hello", coba);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            spinDirectorate.setAdapter(new ArrayAdapter<String>(RegisterActivity.this,
                    R.layout.list_spinner, worldListDirectorate));

            spinDirectorate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int position, long id) {
                    TextView txtid = (TextView) findViewById(R.id.txtid_directorate);
                    txtid.setText(Integer.toString(worldDirectorate.get(position).getId()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }

    private class DataDepartment extends AsyncTask<Void, Void, Void> {
        String urlDepartment = DataManager.urlMasterDepartment;
        private static final String name = "name";
        int strid;
        String strName;
        ArrayList<String> worldList;
        ArrayList<DataSpinner> world;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            String objek="";
            world = new ArrayList<DataSpinner>();
            worldList = new ArrayList<String>();

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
                Log.e("json", json);
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
                Log.e("hello", objek);

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
            HttpGet httpGet = new HttpGet(urlDepartment);
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("X-Header_access_key", access_key);
            httpGet.setHeader("Accept-Language","en-us");
            httpGet.setHeader("X-Timezone","Asia/Jakarta");

            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                serverData = EntityUtils.toString(httpEntity);
                Log.d("response", serverData);
            }catch (ClientProtocolException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

            String coba="";

            try {
                JSONArray jsonArray=new JSONArray(serverData);
                for (int i=0; i<jsonArray.length();i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    strid = jsonObject1.getInt("id");
                    strName = jsonObject1.getString(name);

                    DataSpinner dataSpinner = new DataSpinner();
                    dataSpinner.setId(strid);
                    dataSpinner.setName(strName);

                    world.add(dataSpinner);

                    worldList.add(strName);


                }

                coba = strName;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("hello", coba);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            spinDepartment.setAdapter(new ArrayAdapter<String>(RegisterActivity.this,
                    R.layout.list_spinner, worldList));

            spinDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int position, long id) {
                    TextView txtid = (TextView) findViewById(R.id.txtid_department);
                    txtid.setText(Integer.toString(world.get(position).getId()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }

    private class DataGroup extends AsyncTask<Void, Void, Void> {
        String urlGroup = DataManager.urlMasterGroup;
        private static final String name = "name";
        int stridDirectorate;
        String strNameDrectorate;
        ArrayList<String> worldListDirectorate;
        ArrayList<DataSpinner> worldDirectorate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            String objek = "";
            worldDirectorate = new ArrayList<DataSpinner>();
            worldListDirectorate = new ArrayList<String>();

            HttpParams myParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(myParams, 5000);
            HttpConnectionParams.setSoTimeout(myParams, 5000);

            JSONObject object = new JSONObject();
            try {

                object.put("device_type", "Samsung Galaxy Note 5");
                object.put("device_os", "android OS 4.4.2");
                object.put("device_uuid", "njadnjlvafjvnjnjasmsodc");
                object.put("vendor_name", "DOT");
                object.put("vendor_pass", "DOTVNDR");
                String json = object.toString();
                Log.e("json", json);
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
                Log.e("hello", objek);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String access_key = "";
            try {
                JSONObject jsonObject2 = new JSONObject(objek);
                access_key = jsonObject2.getString("access_key");
            } catch (Exception e) {
                e.printStackTrace();
            }

            String serverData = "";
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(urlGroup);
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("X-Header_access_key", access_key);
            httpGet.setHeader("Accept-Language", "en-us");
            httpGet.setHeader("X-Timezone", "Asia/Jakarta");

            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                serverData = EntityUtils.toString(httpEntity);
                Log.d("response", serverData);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String coba = "";

            try {
                JSONArray jsonArray = new JSONArray(serverData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    stridDirectorate = jsonObject1.getInt("id");
                    strNameDrectorate = jsonObject1.getString(name);

                    DataSpinner dataSpinner = new DataSpinner();
                    dataSpinner.setId(stridDirectorate);
                    dataSpinner.setName(strNameDrectorate);

                    worldDirectorate.add(dataSpinner);

                    worldListDirectorate.add(strNameDrectorate);


                }

                coba = strNameDrectorate;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("hello", coba);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            spinGroup.setAdapter(new ArrayAdapter<String>(RegisterActivity.this,
                    R.layout.coba, worldListDirectorate));

            spinGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int position, long id) {
                    TextView txtid = (TextView) findViewById(R.id.txtid_group);
                    txtid.setText(Integer.toString(worldDirectorate.get(position).getId()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }

    private class DataTitle extends AsyncTask<Void, Void, Void> {
        String urlTitle = DataManager.urlMasterTitle;
        private static final String name = "name";
        int stridDirectorate;
        String strNameDrectorate;
        ArrayList<String> worldListDirectorate;
        ArrayList<DataSpinner> worldDirectorate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            String objek = "";
            worldDirectorate = new ArrayList<DataSpinner>();
            worldListDirectorate = new ArrayList<String>();

            HttpParams myParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(myParams, 5000);
            HttpConnectionParams.setSoTimeout(myParams, 5000);

            JSONObject object = new JSONObject();
            try {

                object.put("device_type", "Samsung Galaxy Note 5");
                object.put("device_os", "android OS 4.4.2");
                object.put("device_uuid", "njadnjlvafjvnjnjasmsodc");
                object.put("vendor_name", "DOT");
                object.put("vendor_pass", "DOTVNDR");
                String json = object.toString();
                Log.e("json", json);
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
                Log.e("hello", objek);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String access_key = "";
            try {
                JSONObject jsonObject2 = new JSONObject(objek);
                access_key = jsonObject2.getString("access_key");
            } catch (Exception e) {
                e.printStackTrace();
            }

            String serverData = "";
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(urlTitle);
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("X-Header_access_key", access_key);
            httpGet.setHeader("Accept-Language", "en-us");
            httpGet.setHeader("X-Timezone", "Asia/Jakarta");

            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                serverData = EntityUtils.toString(httpEntity);
                Log.d("response", serverData);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String coba = "";

            try {
                JSONArray jsonArray = new JSONArray(serverData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    stridDirectorate = jsonObject1.getInt("id");
                    strNameDrectorate = jsonObject1.getString(name);

                    DataSpinner dataSpinner = new DataSpinner();
                    dataSpinner.setId(stridDirectorate);
                    dataSpinner.setName(strNameDrectorate);

                    worldDirectorate.add(dataSpinner);

                    worldListDirectorate.add(strNameDrectorate);


                }

                coba = strNameDrectorate;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("hello", coba);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            spinTitle.setAdapter(new ArrayAdapter<String>(RegisterActivity.this,
                    R.layout.list_spinner, worldListDirectorate));

            spinTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int position, long id) {
                    TextView txtid = (TextView) findViewById(R.id.txtid_title);
                    txtid.setText(Integer.toString(worldDirectorate.get(position).getId()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }
}

