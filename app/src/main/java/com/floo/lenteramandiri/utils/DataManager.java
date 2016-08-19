package com.floo.lenteramandiri.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.floo.lenteramandiri.R;
import com.onesignal.OneSignal;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

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

    public static String PROJECTNUMBER = "924946499768";


    //GET
    public static String urltaskList = "http://sandbox.floostudio.com/lenteramandiri/api/v1/tasks/user/";
    public static String urltaskDetails = "http://sandbox.floostudio.com/lenteramandiri/api/v1/tasks/detail/";
    public static String urlprofilList = "http://sandbox.floostudio.com/lenteramandiri/api/v1/users/detail/";
    public static String urlNewsList = "http://sandbox.floostudio.com/lenteramandiri/api/v1/news?offset=";
    public static String urlFetchNews = "http://sandbox.floostudio.com/lenteramandiri/api/v1/news/detail/";
    public static String urlInfo = "http://sandbox.floostudio.com/lenteramandiri/api/v1/info";
    public static String urlFetchInfo = "http://sandbox.floostudio.com/lenteramandiri/api/v1/info/detail/";
    public static String urlMasterDirectorate = "http://sandbox.floostudio.com/lenteramandiri/api/v1/master/directorate";
    public static String urlMasterDepartment = "http://sandbox.floostudio.com/lenteramandiri/api/v1/master/department";
    public static String urlMasterGroup = "http://sandbox.floostudio.com/lenteramandiri/api/v1/master/group";
    public static String urlMasterTitle = "http://sandbox.floostudio.com/lenteramandiri/api/v1/master/title";
    public static String urlPortGroup = "http://sandbox.floostudio.com/lenteramandiri/api/v1/portfolio_group/user/";
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

    public static String oneSignal(){
        final String[] user = {""};
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                /*String text = "OneSignal UserID:\n" + userId + "\n\n";

                if (registrationId != null)
                    text += "Google Registration Id:\n" + registrationId;
                else
                    text += "Google Registration Id:\nCould not subscribe for push";

                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();*/

                user[0] = userId;

                Log.d("userid", userId);
                Log.d("register", registrationId);

            }
        });

        //Log.d("idsignal", user[0]);
        return user[0];
    }

    public static String getHeaderKey(){
        String objek="";
        String headerKey="";

        String model = android.os.Build.MANUFACTURER +" "+ Build.DEVICE;
        String os = Build.VERSION.RELEASE;

        HttpParams myParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(myParams, 15000);
        HttpConnectionParams.setSoTimeout(myParams, 15000);

        JSONObject object = new JSONObject();
        try {
            object.put("device_type", 1);
            object.put("device_model",model);
            object.put("device_os",os);
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

    public static String epochtodate(int epoch){
        Date date = new Date(epoch * 1000L);
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        format.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        return format.format(date);
    }



    public static String getDatesNow() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strDate = sdf.format(c.getTime());

        return strDate;
    }

    public static long dateToEpoch(String date){
        long today;
        String str = date;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date2 = null;
        try {
            date2 = df.parse(str);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        today = date2.getTime()/1000;

        return today;
    }

    public static String epochtodateTime(int epoch){
        Date date = new Date(epoch * 1000L);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        return format.format(date);
    }

    public static long dateTomiliSecond(String parsing){
        long milis;

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); // I assume d-M, you may refer to M-d for month-day instead.
        Date date = null; // You will need try/catch around this
        try {
            date = formatter.parse(parsing);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        milis = date.getTime();

        return milis;
    }

    public static String dateNow(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm", Locale.getDefault());
        Date date1 = new Date();
        return dateFormat.format(date1);
    }

    public static Boolean isValidInteger(String value) {
        try {
            Integer val = Integer.valueOf(value);
            if (val != null)
                return true;
            else
                return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String getDecimalFormat(String value) {
        StringTokenizer lst = new StringTokenizer(value, ".");
        String str1 = value;
        String str2 = "";
        if (lst.countTokens() > 1) {
            str1 = lst.nextToken();
            str2 = lst.nextToken();
        }
        String str3 = "";
        int i = 0;
        int j = -1 + str1.length();
        if (str1.charAt(-1 + str1.length()) == '.') {
            j--;
            str3 = ".";
        }
        for (int k = j; ; k--) {
            if (k < 0) {
                if (str2.length() > 0)
                    str3 = str3 + "." + str2;
                return str3;
            }
            if (i == 3) {
                str3 = "." + str3;
                i = 0;
            }
            str3 = str1.charAt(k) + str3;
            i++;
        }
    }

    public static String getDateTimeStr(String p_time_in_millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd, HH:mm:ss");
        Date l_time = new Date(Long.parseLong(p_time_in_millis));
        return sdf.format(l_time);
    }

    public static void checkConnection(Context context) {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(context, isConnected);
    }

    public static void showSnack(Context context, boolean isConnected) {
        String message = null;
        int color;
        if (!isConnected) {
            message = "Gagal terhubung internet";
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);

            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            v.setTextColor(Color.RED);
            toast.show();
        }


    }


}
