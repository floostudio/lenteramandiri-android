package floo.com.mpm_mandiri.utils;

import android.os.AsyncTask;
import android.util.Log;

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

import floo.com.mpm_mandiri.LoginActivity;

/**
 * Created by Floo on 2/29/2016.
 */
public class MyURL extends AsyncTask<Void, Void, String> {

    String http = DataManager.url;
    String output = "";


    @Override
    protected String doInBackground(Void... params) {

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
            Log.e("json", json);
            HttpClient httpclient = new DefaultHttpClient(myParams);


            HttpPost httppost = new HttpPost(http);
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

        String abc="";
        try{
            JSONObject jsonObject2 = new JSONObject(objek);
            abc = jsonObject2.getString("access_key");
        }catch (Exception e){
            e.printStackTrace();
        }

        return objek;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            JSONObject result = new JSONObject(s);
            output = result.getString("access_key");
            //delegate.processFinish(output);
            //Toast.makeText(LoginActivity.this, abc, Toast.LENGTH_LONG).show();


        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(LoginActivity.this,"Failed to post article ",Toast.LENGTH_LONG).show();
        }


    }
}