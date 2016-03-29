package floo.com.mpm_mandiri.data;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.EntityTemplate;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;

import floo.com.mpm_mandiri.R;
import floo.com.mpm_mandiri.utils.DataManager;

/**
 * Created by Floo on 3/3/2016.
 */
public class PortoGroupActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout line;
    TextView titleToolbar, save;
    ListView listportoGroup;
    String[] subject, pt, idr;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> mylist;

    String url = DataManager.url;
    String urlPortGroup = DataManager.urlPortGroup;
    private ProgressDialog pDialog;
    private static final String no = "no";
    private static final String group_id = "group_id";
    private static final String group_limit = "group_limit";
    private static final String group_balance = "group_balance";
    private static final String fee = "fee";
    private static final String bunga = "bunga";
    private static final String utilisasi = "utilisasi";
    private static final String company_name = "company_name";
    private static final String acc_amount = "acc_amount";
    private static final String facility_amount = "facility_amount";

    String strNo, strGroupId, strGroupLimit, strGroupBalance, strFee,
            strBunga, strUtilisasi, strCompanyName, strAcc_amount,
            strFacility_amount;

    SimpleAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portofolio_group);
        initView();
        mylist = new ArrayList<HashMap<String, String>>();
        new DataGroup().execute();

        /*subject = new String[]{"G9741123123", "G9741123123", "G9741123123",
                "G9741123123", "G9741123123", "G9741123123", "G9741123123"};

        pt = new String[]{"PT. MASPINA", "PT. MASPINA", "PT. MASPINA", "PT. MASPINA",
                "PT. MASPINA", "PT. MASPINA", "PT. MASPINA"};

        idr = new String[]{"580.000.000.000d","580.000.000.000d","580.000.000.000d",
                "580.000.000.000d","580.000.000.000d","580.000.000.000d","580.000.000.000d"};

        mylist = new ArrayList<HashMap<String, String>>();
        for (int i=0; i<subject.length;i++){
            hashMap = new HashMap<String, String>();
            hashMap.put("subject", subject[i]);
            hashMap.put("pt", pt[i]);
            hashMap.put("idr", idr[i]);
            mylist.add(hashMap);
        }

        adapter = new SimpleAdapter(getApplicationContext(), mylist, R.layout.list_row_porto_group,
                new String[]{"subject", "pt", "idr"}, new int[]{R.id.txt_porto_group_subject,
                R.id.txt_porto_group_pt, R.id.txt_porto_group_idr});
        portoGroup.setAdapter(adapter);*/

        listportoGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtid = (TextView) view.findViewById(R.id.txt_porto_group_id);
                TextView txtlimit = (TextView) view.findViewById(R.id.txt_porto_group_limit);
                TextView txtbalance = (TextView) view.findViewById(R.id.txt_porto_group_balance);
                TextView txtacc = (TextView) view.findViewById(R.id.txt_porto_group_acc_amount);
                TextView txtfacility = (TextView) view.findViewById(R.id.txt_porto_group_facility_amount);
                TextView txtfee = (TextView) view.findViewById(R.id.txt_porto_group_fee);
                TextView txtbunga = (TextView) view.findViewById(R.id.txt_porto_group_bunga);
                TextView txtutilisasi = (TextView) view.findViewById(R.id.txt_porto_group_utilisasi);
                TextView txtcompany = (TextView) view.findViewById(R.id.txt_porto_group_company);
                Intent GroupDetail = new Intent(PortoGroupActivity.this, PortoGroupDetailActivity.class);
                GroupDetail.putExtra(group_id, txtid.getText().toString());
                GroupDetail.putExtra(group_limit, txtlimit.getText().toString());
                GroupDetail.putExtra(group_balance, txtbalance.getText().toString());
                GroupDetail.putExtra(acc_amount, txtacc.getText().toString());
                GroupDetail.putExtra(facility_amount, txtfacility.getText().toString());
                GroupDetail.putExtra(fee, txtfee.getText().toString());
                GroupDetail.putExtra(bunga, txtbunga.getText().toString());
                GroupDetail.putExtra(utilisasi, txtutilisasi.getText().toString());
                GroupDetail.putExtra(company_name, txtcompany.getText().toString());
                startActivity(GroupDetail);

            }
        });

    }
    public void initView(){
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        titleToolbar = (TextView)toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("PORTFOLIO GROUP");
        save = (TextView)findViewById(R.id.txt_save);
        line = (LinearLayout) findViewById(R.id.linier_toolbar);
        listportoGroup = (ListView) findViewById(R.id.list_porto_group);

        save.setVisibility(View.INVISIBLE);
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PortoGroupActivity.this.finish();
            }
        });

    }

    private String getDecimalFormat(String value) {
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
    

    private class DataGroup extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PortoGroupActivity.this);
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
            HttpGet httpGet = new HttpGet(urlPortGroup);
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

                JSONArray jsonArray = new JSONArray(serverData);
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    strGroupId = jsonObject.getString(group_id);
                    strGroupLimit = jsonObject.getString(group_limit);
                    strGroupBalance = jsonObject.getString(group_balance);
                    strAcc_amount = jsonObject.getString(acc_amount);
                    strFacility_amount = jsonObject.getString(facility_amount);
                    strFee = jsonObject.getString(fee);
                    strBunga = jsonObject.getString(bunga);
                    strUtilisasi = jsonObject.getString(utilisasi);
                    strCompanyName = jsonObject.getString(company_name);


                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put(group_id, strGroupId);
                    hashMap.put(group_limit, getDecimalFormat(strGroupLimit));
                    hashMap.put(group_balance, getDecimalFormat(strGroupBalance));
                    hashMap.put(acc_amount, strAcc_amount);
                    hashMap.put(facility_amount, strFacility_amount);
                    hashMap.put(fee, getDecimalFormat(strFee));
                    hashMap.put(bunga, getDecimalFormat(strBunga));
                    hashMap.put(utilisasi, strUtilisasi);
                    hashMap.put(company_name, strCompanyName);

                    mylist.add(hashMap);
                    }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("hello", coba);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            adapter = new SimpleAdapter(getApplicationContext(), mylist, R.layout.list_row_porto_group,
                    new String[]{group_id, group_limit, group_balance, acc_amount, facility_amount, fee, bunga, utilisasi, company_name},
                    new int[]{R.id.txt_porto_group_id, R.id.txt_porto_group_limit, R.id.txt_porto_group_balance, R.id.txt_porto_group_acc_amount,
                    R.id.txt_porto_group_facility_amount, R.id.txt_porto_group_fee, R.id.txt_porto_group_bunga, R.id.txt_porto_group_utilisasi, R.id.txt_porto_group_company});

            listportoGroup.setAdapter(adapter);



        }
    }

}
