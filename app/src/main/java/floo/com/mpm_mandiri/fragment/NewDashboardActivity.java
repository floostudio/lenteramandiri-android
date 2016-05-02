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
import android.view.Window;
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
    HashMap<String, String> hashMapTFD, hashMapCashio, hashMapCashout, hashMapDpk, hashMapLcf, hashMapMonth;
    ArrayList<HashMap<String, String>> mylistTFD, mylistCashin, mylistCashout, mylistDpk, mylistLcf,
            mylistMonth, myListMonthCashout, myListMonthDPK, myListMonthLCF;
    ExpandableHeightListView listTFD, listCashin, listCashout, listDPK, listLCF,
            listMonth, listMonthCashout, listMonthDpk, listMonthLcf,
            listDLR, listAGF, listAVG, listBakiDebet;
    SimpleAdapter adapterTFD, adapterCashin, adapterCashout, adapterDpk, adapterLcf, adapterMonth, adapterMonthCashout, adapterMonthDPK, adapterMonthLCF;
    Button btntfd, btncash, btncashout, btndpk, btnlcf, btndlr, btnagf, btnavg, btnbakidebet, btndetail;
    Spinner spinner;
    TextView text1, text2, title_blue, title_yellow, title1 ;

    private ProgressDialog pDialog;
    String url = DataManager.url;
    String urlDashboard = DataManager.urlDashboard;
    String urlGetperAccount = DataManager.urlGetperAccountSementara;
    String strAcc_num;
    private static final String acc_num = "acc_num";
    private static final String tfd = "tfd";
    private static final String collection = "collection";
    private static String[] titles = new String[8];
    private static final String payment = "payment";
    private static final String cashin = "cashin" ;
    private static final String cashout = "cashout";
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
        mylistCashin = new ArrayList<HashMap<String, String>>();
        mylistCashout = new ArrayList<HashMap<String, String>>();
        mylistDpk = new ArrayList<HashMap<String, String>>();
        mylistLcf = new ArrayList<HashMap<String, String>>();
        mylistMonth = new ArrayList<HashMap<String, String>>();
        myListMonthCashout = new ArrayList<HashMap<String, String>>();
        myListMonthDPK = new ArrayList<HashMap<String, String>>();
        myListMonthLCF = new ArrayList<HashMap<String, String>>();

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
            btncashout.setBackgroundResource(R.drawable.activity_btn);
            btncashout.setTextColor(Color.parseColor("#0b3a77"));
            btndpk.setBackgroundResource(R.drawable.activity_btn);
            btndpk.setTextColor(Color.parseColor("#0b3a77"));
            btnlcf.setBackgroundResource(R.drawable.activity_btn);
            btnlcf.setTextColor(Color.parseColor("#0b3a77"));
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
            btncashout.setBackgroundResource(R.drawable.activity_btn_blue);
            btncashout.setTextColor(Color.parseColor("#ffffff"));
            btndpk.setBackgroundResource(R.drawable.activity_btn_blue);
            btndpk.setTextColor(Color.parseColor("#ffffff"));
            btnlcf.setBackgroundResource(R.drawable.activity_btn_blue);
            btnlcf.setTextColor(Color.parseColor("#ffffff"));
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
        listCashin.setVisibility(View.INVISIBLE);
        listCashout.setVisibility(View.INVISIBLE);
        listDPK.setVisibility(View.INVISIBLE);
        listLCF.setVisibility(View.INVISIBLE);
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
        title1 = (TextView)v.findViewById(R.id.tfd_title_1);
        spinner = (Spinner)v.findViewById(R.id.spin_array);
        btntfd = (Button)v.findViewById(R.id.btn_tfd);
        btncash = (Button)v.findViewById(R.id.btn_cash);
        btncashout = (Button)v.findViewById(R.id.btn_cashout);
        btndpk = (Button)v.findViewById(R.id.btn_dpk);
        btnlcf = (Button)v.findViewById(R.id.btn_lcf);
        btndlr = (Button)v.findViewById(R.id.btn_dlr);
        btnagf = (Button)v.findViewById(R.id.btn_agf);
        btnavg = (Button)v.findViewById(R.id.btn_avg);
        btnbakidebet = (Button)v.findViewById(R.id.btn_BakiDebet);
        btndetail = (Button)v.findViewById(R.id.btn_detail);
        listTFD = (ExpandableHeightListView)v.findViewById(R.id.list_dasboard);
        listCashin = (ExpandableHeightListView) v.findViewById(R.id.list_cashio);
        listCashout = (ExpandableHeightListView) v.findViewById(R.id.list_cashout);
        listDPK = (ExpandableHeightListView) v.findViewById(R.id.list_dpk);
        listLCF = (ExpandableHeightListView) v.findViewById(R.id.list_lcf );
        listMonth = (ExpandableHeightListView)v.findViewById(R.id.list_month);
        listMonthCashout = (ExpandableHeightListView)v.findViewById(R.id.list_monthcashout);
        listMonthDpk = (ExpandableHeightListView)v.findViewById(R.id.list_month_dpk);
        listMonthLcf = (ExpandableHeightListView)v.findViewById(R.id.list_month_lcf);
        listDLR = (ExpandableHeightListView)v.findViewById(R.id.list_dlr);
        listAGF = (ExpandableHeightListView)v.findViewById(R.id.list_agf);
        listAVG = (ExpandableHeightListView)v.findViewById(R.id.list_avg);
        listBakiDebet = (ExpandableHeightListView)v.findViewById(R.id.list_bakidebet);



        listTFD.setEnabled(false);
        listCashin.setEnabled(false);
        listCashout.setEnabled(false);
        listDPK.setEnabled(false);
        listLCF.setEnabled(false);
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
                listMonthCashout.setVisibility(View.INVISIBLE);
                listMonthDpk.setVisibility(View.INVISIBLE);
                listMonthLcf.setVisibility(View.INVISIBLE);
                //btndetail.setVisibility(View.VISIBLE);

            }
        });

        btncash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_yellow.setText("Cash In");
                title_blue.setText("Cash In");
                text1.setText("Cashin Target | Cashin Actual");
                text2.setText("Percentage");
                toggleButtonActive(false);
                btncash.setBackgroundResource(R.drawable.activity_btn_blue);
                btncash.setTextColor(Color.parseColor("#ffffff"));

                toggleListView(listCashin);
                listMonth.setVisibility(View.VISIBLE);
                listMonthCashout.setVisibility(View.INVISIBLE);
                listMonthDpk.setVisibility(View.INVISIBLE);
                listMonthLcf.setVisibility(View.INVISIBLE);
                btndetail.setVisibility(View.VISIBLE);
            }
        });

        btncashout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    title_yellow.setText("Cash Out");
                    title_blue.setText("Cash Out");
                    text1.setText("Cashout Target | Cashout Actual");
                    text2.setText("Percentage");
                    toggleButtonActive(false);
                    btncashout.setBackgroundResource(R.drawable.activity_btn_blue);
                    btncashout.setTextColor(Color.parseColor("#ffffff"));

                    toggleListView(listCashin);
                    listMonthCashout.setVisibility(View.VISIBLE);
                    listMonth.setVisibility(View.INVISIBLE);
                    listMonthDpk.setVisibility(View.INVISIBLE);
                    listMonthLcf.setVisibility(View.INVISIBLE);
                    btndetail.setVisibility(View.VISIBLE);
                }
            });

        btndpk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_yellow.setText("DPK");
                title_blue.setText("DPK");
                text1.setText("DPK | Credit");
                text2.setText("Percentage");
                toggleButtonActive(false);
                btndpk.setBackgroundResource(R.drawable.activity_btn_blue);
                btndpk.setTextColor(Color.parseColor("#ffffff"));

                toggleListView(listDPK);
                listMonthDpk.setVisibility(View.VISIBLE);
                listMonth.setVisibility(View.INVISIBLE);
                listMonthCashout.setVisibility(View.INVISIBLE);
                listMonthLcf.setVisibility(View.INVISIBLE);
                btndetail.setVisibility(View.VISIBLE);
            }
        });
        btnlcf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_yellow.setText("LCF");
                title_blue.setText("LCF");
                text1.setText("LCF | Credit");
                text2.setText("Percentage");
                toggleButtonActive(false);
                btnlcf.setBackgroundResource(R.drawable.activity_btn_blue);
                btnlcf.setTextColor(Color.parseColor("#ffffff"));

                toggleListView(listLCF);
                listMonthLcf.setVisibility(View.VISIBLE);
                listMonthCashout.setVisibility(View.INVISIBLE);
                listMonthDpk.setVisibility(View.INVISIBLE);
                listMonth.setVisibility(View.INVISIBLE);
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
            HttpConnectionParams.setSoTimeout(myParams, 7000);

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
                    if(jsonObject.has(tfd)) {
                        JSONObject objTfd = jsonObject.getJSONObject(tfd);
                        JSONObject objCollection = objTfd.getJSONObject(collection);
                        titles[0] = objCollection.getString("data_title_1");
                        titles[1] = objCollection.getString("data_title_2");
                        titles[2] = objCollection.getString("data_title_3");
                        titles[3] = objCollection.getString("data_title_4");
                        String data_1 = objCollection.getString("data_1");
                        String data_2 = objCollection.getString("data_2");
                        String data_3 = objCollection.getString("data_3");
                        String data_4 = objCollection.getString("data_4");

                        JSONObject objPayment = objTfd.getJSONObject(payment);

                        titles[4] = objPayment.getString("data_title_5");
                        titles[5] = objPayment.getString("data_title_6");
                        titles[6] = objPayment.getString("data_title_7");
                        titles[7] = objPayment.getString("data_title_8");
                        String data_5 = objPayment.getString("data_5");
                        String data_6 = objPayment.getString("data_6");
                        String data_7 = objPayment.getString("data_7");
                        String data_8 = objPayment.getString("data_8");

                        mylistTFD.clear();
                        hashMapTFD = new HashMap<String, String>();
                        hashMapTFD.put("data_1", data_1);
                        hashMapTFD.put("data_2", data_2);
                        hashMapTFD.put("data_3", data_3);
                        hashMapTFD.put("data_4", data_4);
                        hashMapTFD.put("data_5", data_5);
                        hashMapTFD.put("data_6", data_6);
                        hashMapTFD.put("data_7", data_7);
                        hashMapTFD.put("data_8", data_8);
                        hashMapTFD.put("dt_title_1", titles[0]);
                        hashMapTFD.put("dt_title_2", titles[1]);
                        hashMapTFD.put("dt_title_3", titles[2]);
                        hashMapTFD.put("dt_title_4", titles[3]);
                        hashMapTFD.put("dt_title_5", titles[4]);
                        hashMapTFD.put("dt_title_6", titles[5]);
                        hashMapTFD.put("dt_title_7", titles[6]);
                        hashMapTFD.put("dt_title_8", titles[7]);
                        mylistTFD.add(hashMapTFD);
                    } else {
                        mylistTFD.clear();
                        hashMapTFD  = new HashMap<String, String>();
                        mylistTFD.add(hashMapTFD);
                    }


                    // Mapping Data Cashin
                    if(jsonObject.has("cashin")) {
                        mylistCashin.clear();
                        mylistMonth.clear();
                        JSONArray arrayCashin = jsonObject.getJSONArray("cashin");
                        for (int a = 0; a < arrayCashin.length(); a++){
                            JSONObject objCashin = arrayCashin.getJSONObject(a);

                            hashMapMonth = new HashMap<String, String>();
                            hashMapMonth.put(month, objCashin.getString(month));
                            mylistMonth.add(hashMapMonth);

                            hashMapCashio = new HashMap<String, String>();
                            hashMapCashio.put(cashintarget, objCashin.getString(cashintarget));
                            hashMapCashio.put("targetrevenue", objCashin.getString("targetrevenue"));
                            hashMapCashio.put("percentage", objCashin.getString("percentage"));
                            mylistCashin.add(hashMapCashio);

                        }
                    } else {
                        mylistCashin.clear();
                        mylistMonth.clear();
                        hashMapCashio = new HashMap<String, String>();
                        mylistCashin.add(hashMapCashio);
                        hashMapMonth = new HashMap<String, String>();
                        mylistMonth.add(hashMapMonth);
                    }

                    // Mapping Data Cashout
                    if(jsonObject.has("cashout")) {
                        mylistCashout.clear();
                        myListMonthCashout.clear();
                        JSONArray arrayCashout = jsonObject.getJSONArray("cashout");
                        for (int a = 0; a < arrayCashout.length(); a++){
                            JSONObject objCashout = arrayCashout.getJSONObject(a);

                            hashMapMonth = new HashMap<String, String>();
                            hashMapMonth.put(month, objCashout.getString(month));
                            myListMonthCashout.add(hashMapMonth);

                            hashMapCashout = new HashMap<String, String>();
                            hashMapCashout.put(cashouttarget, objCashout.getString(cashouttarget));
                            hashMapCashout.put("targetrevenue", objCashout.getString("targetrevenue"));
                            hashMapCashout.put("percentage", objCashout.getString("percentage"));
                            mylistCashout.add(hashMapCashout);

                        }
                    } else {
                        mylistCashout.clear();
                        myListMonthCashout.clear();
                        hashMapCashout = new HashMap<String, String>();
                        mylistCashout.add(hashMapCashout);
                        hashMapMonth = new HashMap<String, String>();
                        myListMonthCashout.add(hashMapMonth);
                    }
                    // Mapping Data DPK
                    if(jsonObject.has("dpk")) {
                        mylistDpk.clear();
                        myListMonthDPK.clear();
                        JSONArray arrayDPK = jsonObject.getJSONArray("dpk");
                        for (int a = 0; a < arrayDPK.length(); a++){
                            JSONObject objDPK = arrayDPK.getJSONObject(a);

                            hashMapMonth = new HashMap<String, String>();
                            hashMapMonth.put(month, objDPK.getString(month));
                            myListMonthDPK.add(hashMapMonth);

                            hashMapDpk = new HashMap<String, String>();
                            hashMapDpk.put("dpk", objDPK.getString("dpk"));
                            hashMapDpk.put("credit", objDPK.getString("credit"));
                            hashMapDpk.put("percentage", objDPK.getString("percentage"));
                            mylistDpk.add(hashMapDpk);

                        }
                    } else {
                        mylistDpk.clear();
                        myListMonthDPK.clear();
                        hashMapDpk = new HashMap<String, String>();
                        mylistDpk.add(hashMapDpk);
                        hashMapMonth = new HashMap<String, String>();
                        myListMonthDPK.add(hashMapMonth);
                    }
                    // Mapping Data Lcf
                    if(jsonObject.has("lcf")) {
                        mylistLcf.clear();
                        myListMonthLCF.clear();
                        JSONArray arrayLCF = jsonObject.getJSONArray("lcf");
                        for (int a = 0; a < arrayLCF.length(); a++){
                            JSONObject objLCF = arrayLCF.getJSONObject(a);

                            hashMapMonth = new HashMap<String, String>();
                            hashMapMonth.put(month, objLCF.getString(month));
                            myListMonthLCF.add(hashMapMonth);

                            hashMapLcf = new HashMap<String, String>();
                            hashMapLcf.put("lcf", objLCF.getString("lcf"));
                            hashMapLcf.put("credit", objLCF.getString("credit"));
                            hashMapLcf.put("percentage", objLCF.getString("percentage"));
                            mylistLcf.add(hashMapLcf);

                        }
                    } else {
                        mylistLcf.clear();
                        myListMonthLCF.clear();
                        hashMapLcf = new HashMap<String, String>();
                        mylistLcf.add(hashMapLcf);
                        hashMapMonth = new HashMap<String, String>();
                        myListMonthLCF.add(hashMapMonth);
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
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            String[] tfdColumns = new String[]{"data_1","data_2","data_3","data_4","data_5","data_6","data_7","data_8",
                                            "dt_title_1","dt_title_2","dt_title_3","dt_title_4",
                                            "dt_title_5","dt_title_6","dt_title_7","dt_title_8"};
            int[] tfdTags = new int[] {R.id.tfd_data_1, R.id.tfd_data_2, R.id.tfd_data_3, R.id.tfd_data_4,
                    R.id.tfd_data_5, R.id.tfd_data_6, R.id.tfd_data_7, R.id.tfd_data_8,
                    R.id.tfd_title_1, R.id.tfd_title_2, R.id.tfd_title_3, R.id.tfd_title_4,
                    R.id.tfd_title_5, R.id.tfd_title_6, R.id.tfd_title_7, R.id.tfd_title_8,
            };
            //TFD
            adapterTFD = new SimpleAdapter(getActivity(), mylistTFD, R.layout.list_row_dashboard_tfd, tfdColumns, tfdTags);
            listTFD.setAdapter(adapterTFD);
            listTFD.setExpanded(true);

            //CASHIN
            String[] columnTags = new String[] {cashintarget, "targetrevenue", "percentage"};
            int[] columnIds = new int[] {R.id.cashin_target, R.id.cashin_targetrevenue, R.id.cashin_percentage};
            adapterCashin = new SimpleAdapter(getActivity(), mylistCashin, R.layout.list_row_dashboard_cashio, columnTags , columnIds);
            listCashin.setAdapter(adapterCashin);
            listCashin.setExpanded(true);

            //CASHOut
            String[] cashoutTags = new String[] {cashouttarget, "targetrevenue", "percentage"};
            int[] cashoutIds = new int[] {R.id.cashout_target, R.id.cashout_targetrevenue, R.id.cashout_percentage};
            adapterCashout = new SimpleAdapter(getActivity(), mylistCashout, R.layout.list_row_dashboard_cashout, cashoutTags , cashoutIds);
            listCashout.setAdapter(adapterCashout);
            listCashout.setExpanded(true);

            //DPK
            String[] dpkTags = new String[] {"dpk", "credit", "percentage"};
            int[] dpkIds = new int[] {R.id.dpk_target, R.id.dpk_credit, R.id.dpk_percentage};
            adapterDpk = new SimpleAdapter(getActivity(), mylistDpk, R.layout.list_row_dashboard_dpk, dpkTags , dpkIds);
            listDPK.setAdapter(adapterDpk);
            listDPK.setExpanded(true);

            //LCF
            String[] lcfTags = new String[] {"lcf", "credit", "percentage"};
            int[] lcfIds = new int[] {R.id.lcf_target, R.id.lcf_credit, R.id.lcf_percentage};
            adapterLcf = new SimpleAdapter(getActivity(), mylistLcf, R.layout.list_row_dashboard_lcf, lcfTags , lcfIds);
            listLCF.setAdapter(adapterLcf);
            listLCF.setExpanded(true);

            adapterMonth = new SimpleAdapter(getActivity(), mylistMonth, R.layout.list_row_dashboard_month,
                    new String[]{month}, new int[]{R.id.list_month});
            adapterMonthCashout = new SimpleAdapter(getActivity(), myListMonthCashout, R.layout.list_row_dashboard_month,
                    new String[]{month}, new int[]{R.id.list_month});
            adapterMonthDPK = new SimpleAdapter(getActivity(), myListMonthDPK, R.layout.list_row_dashboard_month,
                    new String[]{month}, new int[]{R.id.list_month});
            adapterMonthLCF = new SimpleAdapter(getActivity(), myListMonthLCF, R.layout.list_row_dashboard_month,
                    new String[]{month}, new int[]{R.id.list_month});
            listMonth.setAdapter(adapterMonth);
            listMonthCashout.setAdapter(adapterMonthCashout);
            listMonthDpk.setAdapter(adapterMonthDPK);
            listMonthLcf.setAdapter(adapterMonthLCF);

            listMonth.setExpanded(true);
            listMonthCashout.setExpanded(true);
            listMonthDpk.setExpanded(true);
            listMonthLcf.setExpanded(true);
            adapterTFD.notifyDataSetChanged();
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
