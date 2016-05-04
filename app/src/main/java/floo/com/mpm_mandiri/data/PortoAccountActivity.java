package floo.com.mpm_mandiri.data;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;

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
import java.util.HashMap;
import java.util.StringTokenizer;

import floo.com.mpm_mandiri.R;
import floo.com.mpm_mandiri.utils.DataManager;

/**
 * Created by Floo on 3/3/2016.
 */
public class PortoAccountActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout line;
    TextView titleToolbar, save;
    DynamicListView portoAccount;
    String[] subject, pt, idr;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> mylist;
    SimpleAdapter adapter;

    String url = DataManager.url;
    String urlPortAccount = DataManager.urlPortAccount;
    private ProgressDialog pDialog;
    private static final String id = "id";
    private static final String acc_number = "acc_number";
    private static final String valuta = "valuta";
    private static final String saldo = "saldo";
    private static final String limit = "limit";
    private static final String tunggakan = "tunggakan";
    private static final String kolektibilitas = "kolektibilitas";
    private static final String jumlah_tempo = "jumlah_tempo";
    private static final String trans_debet = "trans_debet";
    private static final String trans_kredit = "trans_kredit";
    private static final String saldo_rata = "saldo_rata";
    private static final String company_name = "company_name";
    private static final String covenant = "covenant";

    String strId, strAccNumber, strValuta, strSaldo, strLimit,
            strTunggakan, strKolektibilitas, strJmlhTempo, strTransDebet,
            strTransKredit, strSaldoRata, strCompanyName, strConvenant, idParsing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portofolio_account);
        initView();
        mylist = new ArrayList<HashMap<String, String>>();
        new DataAccount().execute();

        /*subject = new String[]{"012-3912-9312-23910", "012-3912-9312-23910", "012-3912-9312-23910",
                "012-3912-9312-23910", "012-3912-9312-23910"};

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
        portoAccount.setAdapter(adapter);*/

        portoAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtAccNumber = (TextView) view.findViewById(R.id.txt_porto_account_acc);
                TextView txtValuta = (TextView) view.findViewById(R.id.txt_porto_account_valuta);
                TextView txtSaldo = (TextView) view.findViewById(R.id.txt_porto_account_saldo);
                TextView txtLimit = (TextView) view.findViewById(R.id.txt_porto_account_limit);
                TextView txtTunggakan = (TextView) view.findViewById(R.id.txt_porto_account_tunggakan);
                TextView txtKolektib = (TextView) view.findViewById(R.id.txt_porto_account_kolektibilitas);
                TextView txtJmlhTempo = (TextView) view.findViewById(R.id.txt_porto_account_tempo);
                TextView txtDebet = (TextView) view.findViewById(R.id.txt_porto_account_debet);
                TextView txtKredit = (TextView) view.findViewById(R.id.txt_porto_account_kredit);
                TextView txtRata = (TextView) view.findViewById(R.id.txt_porto_account_rata);
                TextView txtCompany = (TextView) view.findViewById(R.id.txt_porto_account_company);
                TextView txtConvenant = (TextView) view.findViewById(R.id.txt_porto_account_id);
                Intent GroupDetail = new Intent(PortoAccountActivity.this, PortoAccountDetailActivity.class);
                GroupDetail.putExtra(acc_number, txtAccNumber.getText().toString());
                GroupDetail.putExtra(valuta, txtValuta.getText().toString());
                GroupDetail.putExtra(saldo, txtSaldo.getText().toString());
                GroupDetail.putExtra(limit, txtLimit.getText().toString());
                GroupDetail.putExtra(tunggakan, txtTunggakan.getText().toString());
                GroupDetail.putExtra(kolektibilitas, txtKolektib.getText().toString());
                GroupDetail.putExtra(jumlah_tempo, txtJmlhTempo.getText().toString());
                GroupDetail.putExtra(trans_debet, txtDebet.getText().toString());
                GroupDetail.putExtra(trans_kredit, txtKredit.getText().toString());
                GroupDetail.putExtra(saldo_rata, txtRata.getText().toString());
                GroupDetail.putExtra(company_name, txtCompany.getText().toString());
                GroupDetail.putExtra("id", txtConvenant.getText().toString());
                startActivity(GroupDetail);

            }
        });

    }
    public void initView(){
        Intent i = getIntent();
        idParsing  = i.getStringExtra("IDPARSING");
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        titleToolbar = (TextView)toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("PORTFOLIO ACCOUNT");
        save = (TextView)findViewById(R.id.txt_save);
        line = (LinearLayout) findViewById(R.id.linier_toolbar);
        portoAccount = (DynamicListView) findViewById(R.id.list_porto_account);

        save.setVisibility(View.INVISIBLE);
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PortoAccountActivity.this.finish();
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

    private class DataAccount extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PortoAccountActivity.this);
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
            HttpGet httpGet = new HttpGet(urlPortAccount+idParsing);
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("X-Header_access_key", access_key);
            httpGet.setHeader("Accept-Language","en-us");
            httpGet.setHeader("X-Timezone","Asia/Jakarta");

            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                serverData = EntityUtils.toString(httpEntity);
                Log.d("serverData", serverData);

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
                    strId = jsonObject.getString(id);
                    strAccNumber= jsonObject.getString(acc_number);
                    strValuta= jsonObject.getString(valuta);
                    strSaldo = jsonObject.getString(saldo);
                    strLimit= jsonObject.getString(limit);
                    strTunggakan = jsonObject.getString(tunggakan);
                    strKolektibilitas = jsonObject.getString(kolektibilitas);
                    strJmlhTempo = jsonObject.getString(jumlah_tempo);
                    strTransDebet = jsonObject.getString(trans_debet);
                    strTransKredit = jsonObject.getString(trans_kredit);
                    strSaldoRata= jsonObject.getString(saldo_rata);
                    strCompanyName = jsonObject.getString(company_name);
                    //strConvenant = jsonObject.getString(covenant);

                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put(id, strId);
                    hashMap.put(acc_number, strAccNumber);
                    hashMap.put(valuta, strValuta);
                    hashMap.put(saldo, getDecimalFormat(strSaldo));
                    hashMap.put(limit, getDecimalFormat(strLimit));
                    hashMap.put(tunggakan, getDecimalFormat(strTunggakan));
                    hashMap.put(kolektibilitas, strKolektibilitas);
                    hashMap.put(jumlah_tempo, strJmlhTempo);
                    hashMap.put(trans_debet, getDecimalFormat(strTransDebet));
                    hashMap.put(trans_kredit, getDecimalFormat(strTransKredit));
                    hashMap.put(saldo_rata, getDecimalFormat(strSaldoRata));
                    hashMap.put(company_name, strCompanyName);
                    //hashMap.put(covenant, strConvenant);


                    mylist.add(hashMap);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            adapter = new SimpleAdapter(getApplicationContext(), mylist, R.layout.list_row_porto_account,
                    new String[]{acc_number, valuta, saldo, limit, tunggakan, kolektibilitas,
                            jumlah_tempo, trans_debet, trans_kredit, saldo_rata, company_name, id},
                    new int[]{R.id.txt_porto_account_acc, R.id.txt_porto_account_valuta, R.id.txt_porto_account_saldo, R.id.txt_porto_account_limit,
                            R.id.txt_porto_account_tunggakan, R.id.txt_porto_account_kolektibilitas, R.id.txt_porto_account_tempo,
                            R.id.txt_porto_account_debet, R.id.txt_porto_account_kredit, R.id.txt_porto_account_rata,
                            R.id.txt_porto_account_company, R.id.txt_porto_account_id});

            AlphaInAnimationAdapter animasi = new AlphaInAnimationAdapter(adapter);
            //animasi.setAbsListView(portoAccount);

            portoAccount.setAdapter(adapter);


        }
    }
}
