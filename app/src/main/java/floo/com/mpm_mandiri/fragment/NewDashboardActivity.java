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
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
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
import floo.com.mpm_mandiri.utils.MyValueFormatter;
import floo.com.mpm_mandiri.utils.MyYAxisValueFormatter;

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
    String strAcc_num, idParsing;
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

    BarChart chart_CashIn, chat_CashOut, chart_Lcf, chart_Dpk;

    ImageView imgCashIN, imgcashOut, imgDpk, imgLcf;

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

                chart_CashIn.setVisibility(View.INVISIBLE);
                chat_CashOut.setVisibility(View.INVISIBLE);
                chart_Lcf.setVisibility(View.INVISIBLE);
                chart_Dpk.setVisibility(View.INVISIBLE);
                toggleImage();
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

    private void toggleListView(ExpandableHeightListView lv){
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

    private void toggleImage(){
        imgCashIN.setVisibility(View.INVISIBLE);
        imgcashOut.setVisibility(View.INVISIBLE);
        imgDpk.setVisibility(View.INVISIBLE);
        imgLcf.setVisibility(View.INVISIBLE);

        //img.setVisibility(View.VISIBLE);
    }

    private void initView(View v){
        idParsing = this.getArguments().getString("IDPARSING");
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
        chart_CashIn = (BarChart) v.findViewById(R.id.chart_cashIn);
        chat_CashOut = (BarChart)v.findViewById(R.id.chart_cashOut);
        chart_Lcf = (BarChart) v.findViewById(R.id.chart_lcf);
        chart_Dpk = (BarChart)v.findViewById(R.id.chart_dpk);


        //chart.setDescription("");
        //chart.fitScreen();



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

        imgCashIN = (ImageView)v.findViewById(R.id.img_cashIn);
        imgcashOut = (ImageView)v.findViewById(R.id.img_cashOut);
        imgDpk = (ImageView)v.findViewById(R.id.img_dpk);
        imgLcf = (ImageView)v.findViewById(R.id.img_lcf);


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

                chart_CashIn.setVisibility(View.INVISIBLE);
                chat_CashOut.setVisibility(View.INVISIBLE);
                chart_Lcf.setVisibility(View.INVISIBLE);
                chart_Dpk.setVisibility(View.INVISIBLE);
                toggleListView(listTFD);
                listMonth.setVisibility(View.INVISIBLE);
                //btndetail.setVisibility(View.VISIBLE);
                toggleImage();
                btndetail.setVisibility(View.VISIBLE);

            }
        });

        btncash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_yellow.setText("Cash In");
                title_blue.setText("Cash In");
                text1.setText("");
                text2.setText("");
                toggleButtonActive(false);
                btncash.setBackgroundResource(R.drawable.activity_btn_blue);
                btncash.setTextColor(Color.parseColor("#ffffff"));

                toggleChart(chart_CashIn);
                toggleImage();
                //toggleListView(listCashin);
                listTFD.setVisibility(View.INVISIBLE);
                listMonth.setVisibility(View.INVISIBLE);
                listMonthCashout.setVisibility(View.INVISIBLE);
                listMonthDpk.setVisibility(View.INVISIBLE);
                listMonthLcf.setVisibility(View.INVISIBLE);
                btndetail.setVisibility(View.INVISIBLE);
            }
        });

        btncashout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    title_yellow.setText("Cash Out");
                    title_blue.setText("Cash Out");
                    text1.setText("");
                    text2.setText("");
                    toggleButtonActive(false);
                    btncashout.setBackgroundResource(R.drawable.activity_btn_blue);
                    btncashout.setTextColor(Color.parseColor("#ffffff"));

                    toggleChart(chat_CashOut);
                    toggleImage();
                    listTFD.setVisibility(View.INVISIBLE);
                    //toggleListView(listCashin);
                    listMonthCashout.setVisibility(View.INVISIBLE);
                    listMonth.setVisibility(View.INVISIBLE);
                    listMonthDpk.setVisibility(View.INVISIBLE);
                    listMonthLcf.setVisibility(View.INVISIBLE);
                    btndetail.setVisibility(View.INVISIBLE);
                }
            });

        btndpk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_yellow.setText("DPK");
                title_blue.setText("DPK");
                text1.setText("");
                text2.setText("");
                toggleButtonActive(false);
                btndpk.setBackgroundResource(R.drawable.activity_btn_blue);
                btndpk.setTextColor(Color.parseColor("#ffffff"));

                toggleChart(chart_Dpk);
                toggleImage();
                listTFD.setVisibility(View.INVISIBLE);
                //toggleListView(listDPK);
                listMonthDpk.setVisibility(View.INVISIBLE);
                listMonth.setVisibility(View.INVISIBLE);
                listMonthCashout.setVisibility(View.INVISIBLE);
                listMonthLcf.setVisibility(View.INVISIBLE);
                btndetail.setVisibility(View.INVISIBLE);
            }
        });
        btnlcf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_yellow.setText("LCF");
                title_blue.setText("LCF");
                text1.setText("");
                text2.setText("");
                toggleButtonActive(false);
                btnlcf.setBackgroundResource(R.drawable.activity_btn_blue);
                btnlcf.setTextColor(Color.parseColor("#ffffff"));

                toggleChart(chart_Lcf);
                toggleImage();
                listTFD.setVisibility(View.INVISIBLE);
                //toggleListView(listLCF);
                listMonthLcf.setVisibility(View.INVISIBLE);
                listMonthCashout.setVisibility(View.INVISIBLE);
                listMonthDpk.setVisibility(View.INVISIBLE);
                listMonth.setVisibility(View.INVISIBLE);
                btndetail.setVisibility(View.INVISIBLE);
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

    private void toggleChart(BarChart bc){
        chart_CashIn.setVisibility(View.INVISIBLE);
        chat_CashOut.setVisibility(View.INVISIBLE);
        chart_Lcf.setVisibility(View.INVISIBLE);
        chart_Dpk.setVisibility(View.INVISIBLE);

        bc.setVisibility(View.VISIBLE);
    }
    public class DataFetcherTask extends AsyncTask<Void, Void, Void> {
        private String parsing;
        ArrayList<BarEntry> yCashIn = new ArrayList<BarEntry>();
        ArrayList<String> xCashIn = new ArrayList<String>();

        ArrayList<BarEntry> yCashOut = new ArrayList<BarEntry>();
        ArrayList<String> xCashOut = new ArrayList<String>();

        ArrayList<BarEntry> yLcf = new ArrayList<BarEntry>();
        ArrayList<String> xLcf = new ArrayList<String>();

        ArrayList<BarEntry> yDpk = new ArrayList<BarEntry>();
        ArrayList<String> xDpk = new ArrayList<String>();

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

                            String strBlnCashIn = objCashin.getString(month);
                            String strtargettt = objCashin.getString(cashintarget);
                            String stractualll = objCashin.getString("targetrevenue");

                            int intCashIn = Integer.parseInt(strtargettt);
                            int intRevenue = Integer.parseInt(stractualll);

                            float fltCashIn = (float) intCashIn;
                            float fltRevenue = (float) intRevenue;


                            xCashIn.add(strBlnCashIn);

                            yCashIn.add(new BarEntry(new float[]{fltCashIn, fltRevenue}, a));



                            /*hashMapMonth = new HashMap<String, String>();
                            hashMapMonth.put(month, objCashin.getString(month));
                            mylistMonth.add(hashMapMonth);

                            hashMapCashio = new HashMap<String, String>();
                            hashMapCashio.put(cashintarget, objCashin.getString(cashintarget));
                            hashMapCashio.put("targetrevenue", objCashin.getString("targetrevenue"));
                            hashMapCashio.put("percentage", objCashin.getString("percentage"));
                            mylistCashin.add(hashMapCashio);*/

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

                            String strBlnCashOut = objCashout.getString(month);
                            String strtargettt = objCashout.getString(cashouttarget);
                            String strrevenue = objCashout.getString("targetrevenue");

                            int intCashOut = Integer.parseInt(strtargettt);
                            int intRevenue = Integer.parseInt(strrevenue);

                            float fltCashOut = (float) intCashOut;
                            float fltRevenue = (float) intRevenue;


                            xCashOut.add(strBlnCashOut);

                            yCashOut.add(new BarEntry(new float[]{fltCashOut, fltRevenue}, a));

                            /*hashMapMonth = new HashMap<String, String>();
                            hashMapMonth.put(month, objCashout.getString(month));
                            myListMonthCashout.add(hashMapMonth);

                            hashMapCashout = new HashMap<String, String>();
                            hashMapCashout.put(cashouttarget, objCashout.getString(cashouttarget));
                            hashMapCashout.put("targetrevenue", objCashout.getString("targetrevenue"));
                            hashMapCashout.put("percentage", objCashout.getString("percentage"));
                            mylistCashout.add(hashMapCashout);*/

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

                            String strBlnDPK = objDPK.getString(month);
                            String strdpkkk = objDPK.getString("dpk");
                            String strcredittt = objDPK.getString("credit");

                            int intdpk = Integer.parseInt(strdpkkk);
                            int intcredit = Integer.parseInt(strcredittt);

                            float fltDPK = (float) intdpk;
                            float fltCredit = (float) intcredit;


                            xDpk.add(strBlnDPK);

                            yDpk.add(new BarEntry(new float[]{fltDPK, fltCredit}, a));

                            /*hashMapMonth = new HashMap<String, String>();
                            hashMapMonth.put(month, objDPK.getString(month));
                            myListMonthDPK.add(hashMapMonth);

                            hashMapDpk = new HashMap<String, String>();
                            hashMapDpk.put("dpk", objDPK.getString("dpk"));
                            hashMapDpk.put("credit", objDPK.getString("credit"));
                            hashMapDpk.put("percentage", objDPK.getString("percentage"));
                            mylistDpk.add(hashMapDpk);*/

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

                            String strBlnLCF = objLCF.getString(month);
                            String strlcfff = objLCF.getString("lcf");
                            String strcredittt = objLCF.getString("credit");

                            int intlcf = Integer.parseInt(strlcfff);
                            int intcredit = Integer.parseInt(strcredittt);

                            float fltLCF = (float) intlcf;
                            float fltCredit = (float) intcredit;


                            xLcf.add(strBlnLCF);

                            yLcf.add(new BarEntry(new float[]{fltLCF, fltCredit}, a));

                            /*hashMapMonth = new HashMap<String, String>();
                            hashMapMonth.put(month, objLCF.getString(month));
                            myListMonthLCF.add(hashMapMonth);

                            hashMapLcf = new HashMap<String, String>();
                            hashMapLcf.put("lcf", objLCF.getString("lcf"));
                            hashMapLcf.put("credit", objLCF.getString("credit"));
                            hashMapLcf.put("percentage", objLCF.getString("percentage"));
                            mylistLcf.add(hashMapLcf);*/

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
            BarDataSet setCashIn = new BarDataSet(yCashIn, "");
            setCashIn.setColors(getColors());
            setCashIn.setStackLabels(new String[] { "Cash In", "Target Revenue" });

            ArrayList<BarDataSet> dataSetCashIn = new ArrayList<BarDataSet>();
            dataSetCashIn.add(setCashIn);

            BarData dataCashIn = new BarData(xCashIn, dataSetCashIn);
            dataCashIn.setValueFormatter(new MyValueFormatter());

            chart_CashIn.setDescription("");

            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            chart_CashIn.setMaxVisibleValueCount(60);

            // scaling can now only be done on x- and y-axis separately
            chart_CashIn.setPinchZoom(false);

            chart_CashIn.setDrawGridBackground(false);
            chart_CashIn.setDrawBarShadow(false);

            chart_CashIn.setDrawValueAboveBar(false);

            // change the position of the y-labels
            YAxis leftAxis = chart_CashIn.getAxisLeft();
            leftAxis.setValueFormatter(new MyYAxisValueFormatter());
            leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)
            chart_CashIn.getAxisRight().setEnabled(false);

            XAxis xLabels = chart_CashIn.getXAxis();
            xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);

            // mChart.setDrawXLabels(false);
            // mChart.setDrawYLabels(false);

            Legend l = chart_CashIn.getLegend();
            l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
            l.setFormSize(8f);
            l.setFormToTextSpace(4f);
            l.setXEntrySpace(6f);


            chart_CashIn.setData(dataCashIn);
            chart_CashIn.invalidate();

            //String[] columnTags = new String[] {cashintarget, "targetrevenue", "percentage"};
            //int[] columnIds = new int[] {R.id.cashin_target, R.id.cashin_targetrevenue, R.id.cashin_percentage};
            //adapterCashin = new SimpleAdapter(getActivity(), mylistCashin, R.layout.list_row_dashboard_cashio, columnTags , columnIds);
            //listCashin.setAdapter(adapterCashin);
            //listCashin.setExpanded(true);

            //CASHOut
            BarDataSet setCashOut = new BarDataSet(yCashOut, "");
            setCashOut.setColors(getColors());
            setCashOut.setStackLabels(new String[] { "Cash Out", "Target Revenue" });

            ArrayList<BarDataSet> dataSetCashOut = new ArrayList<BarDataSet>();
            dataSetCashOut.add(setCashOut);

            BarData dataCashOut = new BarData(xCashOut, dataSetCashOut);
            dataCashOut.setValueFormatter(new MyValueFormatter());

            chat_CashOut.setDescription("");

            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            chat_CashOut.setMaxVisibleValueCount(60);

            // scaling can now only be done on x- and y-axis separately
            chat_CashOut.setPinchZoom(false);

            chat_CashOut.setDrawGridBackground(false);
            chat_CashOut.setDrawBarShadow(false);

            chat_CashOut.setDrawValueAboveBar(false);

            // change the position of the y-labels
            YAxis leftAxis_CashOin = chat_CashOut.getAxisLeft();
            leftAxis_CashOin.setValueFormatter(new MyYAxisValueFormatter());
            leftAxis_CashOin.setAxisMinValue(0f); // this replaces setStartAtZero(true)
            chat_CashOut.getAxisRight().setEnabled(false);

            XAxis xLabels_CashOut = chat_CashOut.getXAxis();
            xLabels_CashOut.setPosition(XAxis.XAxisPosition.BOTTOM);

            // mChart.setDrawXLabels(false);
            // mChart.setDrawYLabels(false);

            Legend l_CashOut = chat_CashOut.getLegend();
            l_CashOut.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
            l_CashOut.setFormSize(8f);
            l_CashOut.setFormToTextSpace(4f);
            l_CashOut.setXEntrySpace(6f);


            chat_CashOut.setData(dataCashOut);
            chat_CashOut.invalidate();
            /*String[] cashoutTags = new String[] {cashouttarget, "targetrevenue", "percentage"};
            int[] cashoutIds = new int[] {R.id.cashout_target, R.id.cashout_targetrevenue, R.id.cashout_percentage};
            adapterCashout = new SimpleAdapter(getActivity(), mylistCashout, R.layout.list_row_dashboard_cashout, cashoutTags , cashoutIds);
            listCashout.setAdapter(adapterCashout);
            listCashout.setExpanded(true);*/

            //DPK
            BarDataSet setDPK = new BarDataSet(yDpk, "");
            setDPK.setColors(getColors());
            setDPK.setStackLabels(new String[] { "DPK", "Credit" });

            ArrayList<BarDataSet> dataSetDPK = new ArrayList<BarDataSet>();
            dataSetDPK.add(setDPK);

            BarData dataDPK = new BarData(xDpk, dataSetDPK);
            dataDPK.setValueFormatter(new MyValueFormatter());

            chart_Dpk.setDescription("");

            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            chart_Dpk.setMaxVisibleValueCount(60);

            // scaling can now only be done on x- and y-axis separately
            chart_Dpk.setPinchZoom(false);

            chart_Dpk.setDrawGridBackground(false);
            chart_Dpk.setDrawBarShadow(false);

            chart_Dpk.setDrawValueAboveBar(false);

            // change the position of the y-labels
            YAxis leftAxis_DPK = chat_CashOut.getAxisLeft();
            leftAxis_DPK.setValueFormatter(new MyYAxisValueFormatter());
            leftAxis_DPK.setAxisMinValue(0f); // this replaces setStartAtZero(true)
            chart_Dpk.getAxisRight().setEnabled(false);

            XAxis xLabels_DPK = chart_Dpk.getXAxis();
            xLabels_DPK.setPosition(XAxis.XAxisPosition.BOTTOM);

            // mChart.setDrawXLabels(false);
            // mChart.setDrawYLabels(false);

            Legend l_DPK = chart_Dpk.getLegend();
            l_DPK.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
            l_DPK.setFormSize(8f);
            l_DPK.setFormToTextSpace(4f);
            l_DPK.setXEntrySpace(6f);


            chart_Dpk.setData(dataDPK);
            chart_Dpk.invalidate();
            /*String[] dpkTags = new String[] {"dpk", "credit", "percentage"};
            int[] dpkIds = new int[] {R.id.dpk_target, R.id.dpk_credit, R.id.dpk_percentage};
            adapterDpk = new SimpleAdapter(getActivity(), mylistDpk, R.layout.list_row_dashboard_dpk, dpkTags , dpkIds);
            listDPK.setAdapter(adapterDpk);
            listDPK.setExpanded(true);*/

            //LCF
            BarDataSet setLCF = new BarDataSet(yLcf, "");
            setLCF.setColors(getColors());
            setLCF.setStackLabels(new String[] { "LCF", "Credit" });

            ArrayList<BarDataSet> dataSetLCF = new ArrayList<BarDataSet>();
            dataSetLCF.add(setLCF);

            BarData dataLCF = new BarData(xLcf, dataSetLCF);
            dataLCF.setValueFormatter(new MyValueFormatter());

            chart_Lcf.setDescription("");

            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            chart_Lcf.setMaxVisibleValueCount(60);

            // scaling can now only be done on x- and y-axis separately
            chart_Lcf.setPinchZoom(false);

            chart_Lcf.setDrawGridBackground(false);
            chart_Lcf.setDrawBarShadow(false);

            chart_Lcf.setDrawValueAboveBar(false);

            // change the position of the y-labels
            YAxis leftAxis_LCF = chart_Lcf.getAxisLeft();
            leftAxis_LCF.setValueFormatter(new MyYAxisValueFormatter());
            leftAxis_LCF.setAxisMinValue(0f); // this replaces setStartAtZero(true)
            chart_Lcf.getAxisRight().setEnabled(false);

            XAxis xLabels_LCF = chart_Lcf.getXAxis();
            xLabels_LCF.setPosition(XAxis.XAxisPosition.BOTTOM);

            // mChart.setDrawXLabels(false);
            // mChart.setDrawYLabels(false);

            Legend l_LCF = chart_Lcf.getLegend();
            l_LCF.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
            l_LCF.setFormSize(8f);
            l_LCF.setFormToTextSpace(4f);
            l_LCF.setXEntrySpace(6f);


            chart_Lcf.setData(dataLCF);
            chart_Lcf.invalidate();
            /*String[] lcfTags = new String[] {"lcf", "credit", "percentage"};
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
            adapterTFD.notifyDataSetChanged();*/
        }
    }

    private int[] getColors() {

        int stacksize = 2;

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];

        for (int i = 0; i < stacksize; i++) {
            colors[i] = ColorTemplate.VORDIPLOM_COLORS[i];
        }

        return colors;
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
            HttpGet httpGet = new HttpGet(urlList+idParsing);
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
