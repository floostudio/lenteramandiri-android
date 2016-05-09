package floo.com.mpm_mandiri.utils;

import android.util.Log;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Floo on 2/26/2016.
 */
public class DataManager {

    //POST
    public static String url = "http://sandbox.floostudio.com/lenteramandiri/api/v1/instance";
    public static String urlLogin = "http://sandbox.floostudio.com/lenteramandiri/api/v1/users/login";
    public static String urlRegister = "http://sandbox.floostudio.com/lenteramandiri/api/v1/users/register";
    public static String urlLoginSementara = "http://sandbox.floostudio.com/lenteramandiri/index.php/api/v1/users/login";
    String name = "DOT";
    String password = "DOTVNDR";

    //GET
    public static String urltaskList = "http://sandbox.floostudio.com/lenteramandiri/api/v1/tasks/user/";
    public static String urltaskDetails = "http://sandbox.floostudio.com/lenteramandiri/api/v1/tasks/detail/";
    public static String urlprofilList = "http://sandbox.floostudio.com/lenteramandiri/api/v1/users/detail/";
    public static String urlNewsList = "http://sandbox.floostudio.com/lenteramandiri/api/v1/news";
    public static String urlFetchNews = "http://sandbox.floostudio.com/lenteramandiri/api/v1/news/detail/";
    public static String urlMasterDirectorate = "http://sandbox.floostudio.com/lenteramandiri/api/v1/master/directorate";
    public static String urlMasterDepartment = "http://sandbox.floostudio.com/lenteramandiri/api/v1/master/department";
    public static String urlMasterGroup = "http://sandbox.floostudio.com/lenteramandiri/api/v1/master/group";
    public static String urlMasterTitle = "http://sandbox.floostudio.com/lenteramandiri/api/v1/master/title";
    public static String urlPortGroup = "http://sandbox.floostudio.com/lenteramandiri/api/v1/portfolio_group";
    public static String urlPortGroupDetail = "";
    public static String urlPortAccount = "http://sandbox.floostudio.com/lenteramandiri/api/v1/portfolio_acc/user/";
    public static String urlPortAccountDetail = "";
    public static String urlCovenant = "http://sandbox.floostudio.com/lenteramandiri/api/v1/portfolio_acc/covenant/";
    public static String urlDashboard = "http://sandbox.floostudio.com/lenteramandiri/api/v1/dashboard";
    public static String urlListperAccount = "http://sandbox.floostudio.com/lenteramandiri/api/v1/dashboard/listAccount/";
    public static String urlGetperAccount = "http://sandbox.floostudio.com/lenteramandiri/api/v1/dashboard/account/";
    public static String urlGetperAccountSementara = "http://sandbox.floostudio.com/lenteramandiri/api/v1/dashboard/account/";

    //PUT
    public static String urltaskNote = "http://sandbox.floostudio.com/lenteramandiri/api/v1/tasks/note/";
    public static String urlChangePass = "http://sandbox.floostudio.com/lenteramandiri/api/v1/users/change_password";
    public static String urlUpdProfile = "http://sandbox.floostudio.com/lenteramandiri/api/v1/users/detail/";
    public static String urltaskDone = "http://sandbox.floostudio.com/lenteramandiri/api/v1/tasks/done/";

    public static String getHeaderKey(){
        String objek="";
        String headerKey="";

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


        try{
            JSONObject jsonObject2 = new JSONObject(objek);
            headerKey = jsonObject2.getString("access_key");
        }catch (Exception e){
            e.printStackTrace();
        }

        return headerKey;
    }

    public static String MyHttpGet(String urlGet){
        String serverData="";

        DefaultHttpClient httpClient= new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(urlGet);
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("X-Header_access_key", getHeaderKey());
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

        return serverData;
    }

    public static String MyHttpPost(String urlPost, String urlObject){
        String serverData="";

        try {

            DefaultHttpClient httpclient= new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urlPost);
            httppost.setHeader("Content-Type", "application/json");
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("X-Header_access_key", getHeaderKey());
            httppost.setHeader("Accept-Language","en-us");
            httppost.setHeader("X-Timezone", "Asia/Jakarta");

            StringEntity se = new StringEntity(urlObject);
            httppost.setEntity(se);

            HttpResponse response = httpclient.execute(httppost);
            serverData = EntityUtils.toString(response.getEntity());



        }  catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return serverData;
    }

    public static String MyHttpPut(String urlPut, String urlObject){
        String serverData="";

        try {

            DefaultHttpClient httpclient= new DefaultHttpClient();
            HttpPut httpPut = new HttpPut(urlPut);
            httpPut.setHeader("Content-Type", "application/json");
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader("X-Header_access_key", getHeaderKey());
            httpPut.setHeader("Accept-Language","en-us");
            httpPut.setHeader("X-Timezone", "Asia/Jakarta");

            StringEntity se = new StringEntity(urlObject);
            httpPut.setEntity(se);

            HttpResponse response = httpclient.execute(httpPut);
            serverData = EntityUtils.toString(response.getEntity());



        }catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return serverData;

    }
}
