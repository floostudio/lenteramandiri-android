package floo.com.mpm_mandiri.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;

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

import floo.com.mpm_mandiri.R;
import floo.com.mpm_mandiri.data.ImageActivity;
import floo.com.mpm_mandiri.utils.DataManager;

/**
 * Created by Floo on 4/21/2016.
 */
public class NewDashboardActivity extends Fragment {
    HashMap<String, String> hashMapTFD, hashMapCashio, hashMapMonth;
    ArrayList<HashMap<String, String>> mylistTFD, mylistCashio, mylistMonth;
    ExpandableHeightListView listTFD, listCashio, listMonth, listDLR, listAGF, listAVG, listBakiDebet;
    SimpleAdapter adapterTFD, adapterCashio, adapterMonth;
    Button btntfd, btncash, btndlr, btnagf, btnavg, btnbakidebet, btndetail;
    Spinner spinner;
    TextView text1, text2, title_blue, title_yellow;

    private ProgressDialog pDialog;
    String url = DataManager.url;
    String urlDashboard = DataManager.urlDashboard;
    String urlGetperAccount = DataManager.urlGetperAccountSementara;
    String strAcc_num, strtfd, strcollection, strfy, strbmri, strytd, strbmri2, strpayment;
    private static final String acc_num = "acc_num";
    private static final String tfd = "tfd";
    private static final String cashio = "cashio";
    private static final String collection = "collection";
    private static final String fy_2014 = "fy_2014";
    private static final String bmri_share = "bmri_share";
    private static final String ytd_jul = "ytd_jul";
    private static final String bmri_share2 = "bmri_share2";
    private static final String payment = "payment";
    private static final String cashinout = "cashinout";
    private static final String month = "month";
    private static final String cashintarget = "cashintarget";
    private static final String cashinactual = "cashinactual";
    private static final String cashouttarget = "cashouttarget";
    private static final String cashoutactual = "cashoutactual";





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_new_dashboard, container, false);
        initView(v);
        mylistTFD = new ArrayList<HashMap<String, String>>();
        mylistCashio = new ArrayList<HashMap<String, String>>();
        mylistMonth = new ArrayList<HashMap<String, String>>();

        new DataSpinner().execute();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView txt = (TextView)view.findViewById(R.id.text1);

                //Toast.makeText(getActivity(), txt.getText().toString(),Toast.LENGTH_LONG).show();

                new DataFetcherTask(txt.getText().toString()).execute();
                title_yellow.setText("Transaction Flow Diagram");
                title_blue.setText("Transaction Flow Diagram");
                text1.setText("Collection");
                text2.setText("Payment");
                toggleButtonActive(false);
                btntfd.setBackgroundResource(R.drawable.activity_btn_blue);
                btntfd.setTextColor(Color.parseColor("#ffffff"));


                toggleListView(listTFD);
                listMonth.setVisibility(View.INVISIBLE);
                btndetail.setVisibility(View.VISIBLE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return v;
    }

    private void toggleButtonActive(Boolean active){
        if(!active) {
            btntfd.setBackgroundResource(R.drawable.activity_btn);
            btntfd.setTextColor(Color.parseColor("#0b3a77"));
            btncash.setBackgroundResource(R.drawable.activity_btn);
            btncash.setTextColor(Color.parseColor("#0b3a77"));
            btndlr.setBackgroundResource(R.drawable.activity_btn);
            btndlr.setTextColor(Color.parseColor("#0b3a77"));
            btnagf.setBackgroundResource(R.drawable.activity_btn);
            btnagf.setTextColor(Color.parseColor("#0b3a77"));
            btnavg.setBackgroundResource(R.drawable.activity_btn);
            btnavg.setTextColor(Color.parseColor("#0b3a77"));
            btnbakidebet.setBackgroundResource(R.drawable.activity_btn);
            btnbakidebet.setTextColor(Color.parseColor("#0b3a77"));
        } else {
            btntfd.setBackgroundResource(R.drawable.activity_btn_blue);
            btntfd.setTextColor(Color.parseColor("#ffffff"));
            btncash.setBackgroundResource(R.drawable.activity_btn_blue);
            btncash.setTextColor(Color.parseColor("#ffffff"));
            btndlr.setBackgroundResource(R.drawable.activity_btn_blue);
            btndlr.setTextColor(Color.parseColor("#ffffff"));
            btnagf.setBackgroundResource(R.drawable.activity_btn_blue);
            btnagf.setTextColor(Color.parseColor("#ffffff"));
            btnavg.setBackgroundResource(R.drawable.activity_btn_blue);
            btnavg.setTextColor(Color.parseColor("#ffffff"));
            btnbakidebet.setBackgroundResource(R.drawable.activity_btn_blue);
            btnbakidebet.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    private void toggleListView(ListView lv){
        listTFD.setVisibility(View.INVISIBLE);
        listCashio.setVisibility(View.INVISIBLE);
        listDLR.setVisibility(View.INVISIBLE);
        listAGF.setVisibility(View.INVISIBLE);
        listAVG.setVisibility(View.INVISIBLE);
        listBakiDebet.setVisibility(View.INVISIBLE);


        lv.setVisibility(View.VISIBLE);


    }

    private void initView(View v){
        text1 = (TextView)v.findViewById(R.id.txt_dasboard_1);
        text2 = (TextView)v.findViewById(R.id.txt_dasboard_2);
        title_blue = (TextView)v.findViewById(R.id.txt_title_blue);
        title_yellow = (TextView)v.findViewById(R.id.txt_title_yellow);
        spinner = (Spinner)v.findViewById(R.id.spin_array);
        btntfd = (Button)v.findViewById(R.id.btn_tfd);
        btncash = (Button)v.findViewById(R.id.btn_cash);
        btndlr = (Button)v.findViewById(R.id.btn_dlr);
        btnagf = (Button)v.findViewById(R.id.btn_agf);
        btnavg = (Button)v.findViewById(R.id.btn_avg);
        btnbakidebet = (Button)v.findViewById(R.id.btn_BakiDebet);
        btndetail = (Button)v.findViewById(R.id.btn_detail);
        listTFD = (ExpandableHeightListView)v.findViewById(R.id.list_dasboard);
        listCashio = (ExpandableHeightListView) v.findViewById(R.id.list_cashio);
        listMonth = (ExpandableHeightListView)v.findViewById(R.id.list_month);
        listDLR = (ExpandableHeightListView)v.findViewById(R.id.list_dlr);
        listAGF = (ExpandableHeightListView)v.findViewById(R.id.list_agf);
        listAVG = (ExpandableHeightListView)v.findViewById(R.id.list_avg);
        listBakiDebet = (ExpandableHeightListView)v.findViewById(R.id.list_bakidebet);

        listTFD.setEnabled(false);
        listCashio.setEnabled(false);
        listMonth.setEnabled(false);
        listDLR.setEnabled(false);
        listAGF.setEnabled(false);
        listAVG.setEnabled(false);
        listBakiDebet.setEnabled(false);
        //spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_new_spinner, array);
        //spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        //spinner.setAdapter(spinnerArrayAdapter);




        btntfd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                title_yellow.setText("Transaction Flow Diagram");
                title_blue.setText("Transaction Flow Diagram");
                text1.setText("Collection");
                text2.setText("Payment");
                toggleButtonActive(false);
                btntfd.setBackgroundResource(R.drawable.activity_btn_blue);
                btntfd.setTextColor(Color.parseColor("#ffffff"));


                toggleListView(listTFD);
                listMonth.setVisibility(View.INVISIBLE);
                //btndetail.setVisibility(View.VISIBLE);

            }
        });

        btncash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_yellow.setText("CashInOut");
                title_blue.setText("CashInOut");
                text1.setText("Cashin Target | Cashin Actual");
                text2.setText("Cashout Target | Cashout Actual");
                toggleButtonActive(false);
                btncash.setBackgroundResource(R.drawable.activity_btn_blue);
                btncash.setTextColor(Color.parseColor("#ffffff"));

                toggleListView(listCashio);
                listMonth.setVisibility(View.VISIBLE);
                btndetail.setVisibility(View.VISIBLE);
            }
        });

        btndlr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_yellow.setText("Deposito Loan Ratio");
                title_blue.setText("Deposito Loan Ratio");

                toggleButtonActive(false);
                btndlr.setBackgroundResource(R.drawable.activity_btn_blue);
                btndlr.setTextColor(Color.parseColor("#ffffff"));

                toggleListView(listDLR);
                listMonth.setVisibility(View.INVISIBLE);

            }
        });

        btnagf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtonActive(false);
                btnagf.setBackgroundResource(R.drawable.activity_btn_blue);
                btnagf.setTextColor(Color.parseColor("#ffffff"));

                toggleListView(listAGF);
                listMonth.setVisibility(View.INVISIBLE);
            }
        });

        btnavg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toggleButtonActive(false);
                btnavg.setBackgroundResource(R.drawable.activity_btn_blue);
                btnavg.setTextColor(Color.parseColor("#ffffff"));

                toggleListView(listAVG);
                listMonth.setVisibility(View.INVISIBLE);
            }
        });

        btnbakidebet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toggleButtonActive(false);
                btnbakidebet.setBackgroundResource(R.drawable.activity_btn_blue);
                btnbakidebet.setTextColor(Color.parseColor("#ffffff"));

                toggleListView(listBakiDebet);
                listMonth.setVisibility(View.INVISIBLE);
            }
        });

        btndetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent im=new Intent(getActivity(), ImageActivity.class);
                startActivity(im);
            }
        });

    }

    public class DataFetcherTask extends AsyncTask<Void, Void, Void> {
        private String parsing;


        public DataFetcherTask(String parsing) {
            this.parsing = parsing;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...!!!");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            String objek = "";

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

            String access_key = "";
            try {
                JSONObject jsonObject2 = new JSONObject(objek);
                access_key = jsonObject2.getString("access_key");
            } catch (Exception e) {
                e.printStackTrace();
            }

            String serverData = "";
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(urlGetperAccount+parsing);
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

            try {
                JSONArray jsonArray = new JSONArray(serverData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    strAcc_num = jsonObject.getString(acc_num);
                    // Mapping Data TFD
                    JSONObject objTfd=jsonObject.getJSONObject(tfd);

                    JSONObject objCollection=objTfd.getJSONObject(collection);
                    strfy = objCollection.getString(fy_2014);
                    strbmri = objCollection.getString(bmri_share);
                    strytd = objCollection.getString(ytd_jul);
                    strbmri2 = objCollection.getString(bmri_share2);

                    JSONObject objPayment = objTfd.getJSONObject(payment);

                    String strfy1 = objPayment.getString(fy_2014);
                    String strbmri1 = objPayment.getString(bmri_share);
                    String strytd1 = objPayment.getString(ytd_jul);
                    String strbmri21 = objPayment.getString(bmri_share2);

                    mylistTFD.clear();
                    hashMapTFD = new HashMap<String, String>();
                    hashMapTFD.put(fy_2014, strfy);
                    hashMapTFD.put(bmri_share, strbmri);
                    hashMapTFD.put(ytd_jul, strytd);
                    hashMapTFD.put(bmri_share2, strbmri2);
                    hashMapTFD.put("strfy1", strfy1);
                    hashMapTFD.put("strbmri1", strbmri1);
                    hashMapTFD.put("strytd1", strytd1);
                    hashMapTFD.put("strbmri21", strbmri21);
                    mylistTFD.add(hashMapTFD);

                    mylistCashio.clear();
                    mylistMonth.clear();
                    // Mapping Data Cashio
                    JSONArray arrayCashio = jsonObject.getJSONArray(cashinout);
                    for (int a = 0; a < arrayCashio.length(); a++){
                       JSONObject objCashio = arrayCashio.getJSONObject(a);

                        hashMapMonth = new HashMap<String, String>();
                        hashMapMonth.put(month, objCashio.getString(month));
                        mylistMonth.add(hashMapMonth);

                        hashMapCashio = new HashMap<String, String>();
                        hashMapCashio.put(cashintarget, objCashio.getString(cashintarget));
                        hashMapCashio.put(cashinactual, objCashio.getString(cashinactual));
                        hashMapCashio.put(cashouttarget, objCashio.getString(cashouttarget));
                        hashMapCashio.put(cashoutactual, objCashio.getString(cashoutactual));
                        mylistCashio.add(hashMapCashio);

                    }
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

            //TFD
            adapterTFD = new SimpleAdapter(getActivity(), mylistTFD, R.layout.list_row_dashboard_tfd,
                    new String[]{fy_2014, "strfy1", bmri_share, "strbmri1", ytd_jul, "strytd1", bmri_share2, "strbmri21"}, new int[]{R.id.btn_background,
                    R.id.btn_light, R.id.btn_blue, R.id.btn_yellow, R.id.btn_background1,
                    R.id.btn_light1, R.id.btn_blue1, R.id.btn_yellow1});
            listTFD.setAdapter(adapterTFD);
            listTFD.setExpanded(true);

            //CASHIO
            String[] columnTags = new String[] {cashintarget, cashinactual, cashouttarget, cashoutactual};
            int[] columnIds = new int[] {R.id.txt_background, R.id.btn_background, R.id.txt_light, R.id.btn_light};
            adapterCashio = new SimpleAdapter(getActivity(), mylistCashio, R.layout.list_row_dashboard_cashio, columnTags , columnIds);
            listCashio.setAdapter(adapterCashio);
            listCashio.setExpanded(true);

            adapterMonth = new SimpleAdapter(getActivity(), mylistMonth, R.layout.list_row_dashboard_month,
                    new String[]{month}, new int[]{R.id.txt_background});
            listMonth.setAdapter(adapterMonth);
            listMonth.setExpanded(true);

        }
    }

    private class DataSpinner extends AsyncTask<Void, Void, Void> {
        String urlList = DataManager.urlListperAccount;
        private static final String acc_num = "acc_num";
        String strspinnum;
        ArrayList<String> worldListDirectorate;
        ArrayAdapter<String> world;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            String objek = "";
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
            HttpGet httpGet = new HttpGet(urlList);
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
                    strspinnum = jsonObject1.getString(acc_num);

                    worldListDirectorate.add(strspinnum);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            world = new ArrayAdapter<String>(getActivity(), R.layout.list_new_spinner, worldListDirectorate);
            world.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(world);
            //wo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //spinner.setAdapter(new ArrayAdapter<String>(getActivity(),
            //        R.layout.list_new_spinner, worldListDirectorate));
        }
    }
}
