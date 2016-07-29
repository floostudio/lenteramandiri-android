package com.floo.lenteramandiri.data;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.floo.lenteramandiri.utils.DataManager;

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
import java.util.ArrayList;

import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.adapter.DataSpinner;

/**
 * Created by Floo on 4/11/2016.
 */
public class UpdateProfilActivity extends AppCompatActivity {
    private static String first_name = "first_name";
    private static String last_name = "last_name";
    private static String nip = "nip";
    private static String directorate = "directorate";
    private static String group = "group";
    private static String department = "department";
    private static String title = "title";
    private static String email = "email";
    private static String profpic = "profpic";

    private static final String status_code = "status_code";
    private static final String message = "message";

    String url = DataManager.url;
    String urlUpdProfile = DataManager.urlUpdProfile;
    Toolbar toolbar;
    LinearLayout line;
    TextView titleToolbar, save;
    EditText edtFirstName, edtLastName, edtNip, edtEmail;
    Spinner spinDirectorate, spinDepartment, spinGroup, spinTitle;
    TextView idDirectorate, idDepartment, idGroup, idTitle;
    String pProfpic, pId, pEmail, strFirstName, strLastName, strEmail, strNip,
            strProfpic, strStatus, strMessage, strDirectorate, strGroup, strDepartment, strTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        initView();
        new DataDirectorate().execute();
        new DataDepartment().execute();
        new DataGroup().execute();
        new DataTitle().execute();
    }

    public void initView() {
        Intent i = getIntent();
        pId = i.getStringExtra("IDPARSING");
        pProfpic = i.getStringExtra(profpic);
        pEmail = i.getStringExtra(email);

        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        titleToolbar = (TextView) toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("UPDATE PROFILE");
        save = (TextView) findViewById(R.id.txt_save);
        line = (LinearLayout) findViewById(R.id.linier_toolbar);

        edtFirstName = (EditText) findViewById(R.id.edt_update_firstname);
        edtLastName = (EditText) findViewById(R.id.edt_update_lastname);
        edtNip = (EditText) findViewById(R.id.edt_update_nip);
        spinDirectorate = (Spinner) findViewById(R.id.spin_Directorate);
        idDirectorate = (TextView) findViewById(R.id.txtid_directorate);
        spinGroup = (Spinner) findViewById(R.id.spin_Group);
        idGroup = (TextView) findViewById(R.id.txtid_group);
        spinDepartment = (Spinner) findViewById(R.id.spin_Department);
        idDepartment = (TextView) findViewById(R.id.txtid_department);
        spinTitle = (Spinner)findViewById(R.id.spin_Title);
        idTitle = (TextView) findViewById(R.id.txtid_title);


        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfilActivity.this.finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtFirstName.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Firstname Empty", Toast.LENGTH_LONG).show();

                } else if (edtLastName.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Lastname Empty", Toast.LENGTH_LONG).show();

                } else if (edtNip.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Nip Empty", Toast.LENGTH_LONG).show();

                }else {

                    strFirstName = edtFirstName.getText().toString();
                    strLastName = edtLastName.getText().toString();
                    strNip = edtNip.getText().toString();
                    strDirectorate = idDirectorate.getText().toString();
                    strGroup = idGroup.getText().toString();
                    strDepartment = idDepartment.getText().toString();
                    strTitle = idTitle.getText().toString();
                    strEmail = edtEmail.getText().toString();

                    new NoteAsync().execute();
                }
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
                object1.put(first_name, strFirstName);
                object1.put(last_name, strLastName);
                object1.put(nip, strNip);
                object1.put(directorate, strDirectorate);
                object1.put(group, strGroup);
                object1.put(department, strDepartment);
                object1.put(title, strTitle);
                object1.put(email, pEmail);
                object1.put(profpic, pProfpic);
                String json1 = object1.toString();


                DefaultHttpClient httpclient= new DefaultHttpClient(myParams);
                HttpPut httpPut = new HttpPut(urlUpdProfile+pId);
                httpPut.setHeader("Content-Type", "application/json");
                httpPut.setHeader("Accept", "application/json");
                httpPut.setHeader("X-Header_access_key", access_key);
                httpPut.setHeader("Accept-Language","en-us");
                httpPut.setHeader("X-Timezone", "Asia/Jakarta");

                StringEntity se = new StringEntity(json1);
                httpPut.setEntity(se);

                HttpResponse response = httpclient.execute(httpPut);
                serverData = EntityUtils.toString(response.getEntity());
                Log.d("isidata", serverData);



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
                Log.d("status_code", strStatus);
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
                UpdateProfilActivity.this.finish();
            }else {
                Toast.makeText(getApplicationContext(), strMessage, Toast.LENGTH_LONG).show();
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


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            spinDirectorate.setAdapter(new ArrayAdapter<String>(UpdateProfilActivity.this,
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


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            spinDepartment.setAdapter(new ArrayAdapter<String>(UpdateProfilActivity.this,
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


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            spinGroup.setAdapter(new ArrayAdapter<String>(UpdateProfilActivity.this,
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


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            spinTitle.setAdapter(new ArrayAdapter<String>(UpdateProfilActivity.this,
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
