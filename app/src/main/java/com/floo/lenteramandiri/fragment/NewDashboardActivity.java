package com.floo.lenteramandiri.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;
import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.data.ImageActivity;
import com.floo.lenteramandiri.utils.DataManager;
import com.floo.lenteramandiri.utils.MyValueFormatter;
import com.floo.lenteramandiri.utils.MyYAxisValueFormatter;

/**
 * Created by Floo on 4/21/2016.
 */
public class NewDashboardActivity extends Fragment implements View.OnClickListener{
    HashMap<String, String> hashMapTFD, hashMapCashio, hashMapCashout, hashMapDpk, hashMapLcf, hashMapMonth, hashMapAGF;
    ArrayList<HashMap<String, String>> mylistTFD, mylistCashin, mylistCashout, mylistDpk, mylistLcf,
            mylistMonth, myListMonthCashout, myListMonthDPK, myListMonthLCF, mylistAGF;
    ExpandableHeightListView listTFD, listAGF;
    SimpleAdapter adapterTFD, adapterAGF;
    Button btntfd, btncash, btncashout, btndpk, btnlcf, btndlr, btnagf, btnavg, btnbakidebet, btndetail;
    Spinner spinner, spin_top;
    TextView text1, text2, title_blue, title_yellow, title1 ;

    private SpotsDialog pDialog;
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

    BarChart chart_Lcf, chart_Dpk;
    CombinedChart chart_CashIn, chat_CashOut, chart_BakiDebet1, chart_BakiDebet2;
    LinearLayout lineBakiDebet;
    Button btnBakiDebetTrue, btnPercentageTrue;
    HorizontalScrollView horizontalAGF;


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
        mylistAGF = new ArrayList<HashMap<String, String>>();

        //new DataSpinner().execute();
        new DataSpinnerTop().execute();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView txt = (TextView)view.findViewById(R.id.text1);

                //Toast.makeText(getActivity(), txt.getText().toString(),Toast.LENGTH_LONG).show();

                new DataFetcherTask(txt.getText().toString()).execute();
                /*title_yellow.setText("Transaction Flow Diagram");
                title_blue.setText("Transaction Flow Diagram");
                text1.setText("Collection");
                text2.setText("Payment");
                toggleButtonActive(false);
                btntfd.setBackgroundResource(R.drawable.activity_btn_blue);
                btntfd.setTextColor(Color.parseColor("#ffffff"));

                setGONE();

                //toggleListView(listTFD);
                listTFD.setVisibility(View.VISIBLE);

                btndetail.setVisibility(View.VISIBLE);*/



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return v;
    }

    private void toogleButtonBakiDebet(Boolean active){
        if(!active) {
            btnBakiDebetTrue.setBackgroundResource(R.drawable.activity_btn);
            btnBakiDebetTrue.setTextColor(Color.parseColor("#0b3a77"));
            btnPercentageTrue.setBackgroundResource(R.drawable.activity_btn);
            btnPercentageTrue.setTextColor(Color.parseColor("#0b3a77"));
        }else {
            btnBakiDebetTrue.setBackgroundResource(R.drawable.activity_btn_blue);
            btnBakiDebetTrue.setTextColor(Color.parseColor("#ffffff"));
            btnPercentageTrue.setBackgroundResource(R.drawable.activity_btn_blue);
            btnPercentageTrue.setTextColor(Color.parseColor("#ffffff"));
        }
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

    private void initView(View v){
        idParsing = this.getArguments().getString("IDPARSING");
        text1 = (TextView)v.findViewById(R.id.txt_dasboard_1);
        text2 = (TextView)v.findViewById(R.id.txt_dasboard_2);
        title_blue = (TextView)v.findViewById(R.id.txt_title_blue);
        title_yellow = (TextView)v.findViewById(R.id.txt_title_yellow);
        title1 = (TextView)v.findViewById(R.id.tfd_title_1);
        spinner = (Spinner)v.findViewById(R.id.spin_array);
        spin_top = (Spinner)v.findViewById(R.id.spin_array_top);
        spin_top.getBackground().setColorFilter(getResources().getColor(R.color.cpb_white), PorterDuff.Mode.SRC_ATOP);
        spinner.getBackground().setColorFilter(getResources().getColor(R.color.cpb_white), PorterDuff.Mode.SRC_ATOP);

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
        btntfd.setOnClickListener(this);
        btncash.setOnClickListener(this);
        btncashout.setOnClickListener(this);
        btndpk.setOnClickListener(this);
        btnlcf.setOnClickListener(this);
        btndlr.setOnClickListener(this);
        btnagf.setOnClickListener(this);
        btnavg.setOnClickListener(this);
        btnbakidebet.setOnClickListener(this);
        btndetail.setOnClickListener(this);

        btnBakiDebetTrue = (Button)v.findViewById(R.id.btn_BakiDebet_true);
        btnPercentageTrue = (Button)v.findViewById(R.id.btn_Percentage_true);
        btnBakiDebetTrue.setOnClickListener(this);
        btnPercentageTrue.setOnClickListener(this);

        listTFD = (ExpandableHeightListView)v.findViewById(R.id.list_dasboard);
        listTFD.setEnabled(false);
        chart_CashIn = (CombinedChart) v.findViewById(R.id.chart_cashIn);
        chat_CashOut = (CombinedChart)v.findViewById(R.id.chart_cashOut);
        chart_Lcf = (BarChart) v.findViewById(R.id.chart_lcf);
        chart_Dpk = (BarChart)v.findViewById(R.id.chart_dpk);
        chart_BakiDebet1 = (CombinedChart)v.findViewById(R.id.chart_bakidebet1);
        chart_BakiDebet2 = (CombinedChart)v.findViewById(R.id.chart_bakidebet2);
        lineBakiDebet = (LinearLayout)v.findViewById(R.id.linierBakiDebet) ;

        horizontalAGF = (HorizontalScrollView)v.findViewById(R.id.horizontalAGF);
        listAGF = (ExpandableHeightListView)v.findViewById(R.id.list_agf);
        listAGF.setEnabled(false);







    }

    @Override
    public void onClick(View v) {
        if (v==btntfd){
            title_yellow.setText("Transaction Flow Diagram");
            title_blue.setText("Transaction Flow Diagram");
            text1.setText("Collection");
            text2.setText("Payment");
            toggleButtonActive(false);
            btntfd.setBackgroundResource(R.drawable.activity_btn_blue);
            btntfd.setTextColor(Color.parseColor("#ffffff"));

            setGONE();

            listTFD.setVisibility(View.VISIBLE);
            btndetail.setVisibility(View.VISIBLE);

        }else if (v==btncash){
            title_yellow.setText("Cash In");
            title_blue.setText("Cash In");
            text1.setText("");
            text2.setText("");
            toggleButtonActive(false);
            btncash.setBackgroundResource(R.drawable.activity_btn_blue);
            btncash.setTextColor(Color.parseColor("#ffffff"));

            setGONE();
            toggleLineChart(chart_CashIn);


        }else if (v==btncashout){
            title_yellow.setText("Cash Out");
            title_blue.setText("Cash Out");
            text1.setText("");
            text2.setText("");
            toggleButtonActive(false);
            btncashout.setBackgroundResource(R.drawable.activity_btn_blue);
            btncashout.setTextColor(Color.parseColor("#ffffff"));

            setGONE();
            toggleLineChart(chat_CashOut);


        }else if (v==btndpk){
            title_yellow.setText("DPK");
            title_blue.setText("DPK");
            text1.setText("");
            text2.setText("");
            toggleButtonActive(false);
            btndpk.setBackgroundResource(R.drawable.activity_btn_blue);
            btndpk.setTextColor(Color.parseColor("#ffffff"));

            setGONE();
            toggleChart(chart_Dpk);

        }else if (v==btnlcf){
            title_yellow.setText("LCF");
            title_blue.setText("LCF");
            text1.setText("");
            text2.setText("");
            toggleButtonActive(false);
            btnlcf.setBackgroundResource(R.drawable.activity_btn_blue);
            btnlcf.setTextColor(Color.parseColor("#ffffff"));

            setGONE();
            toggleChart(chart_Lcf);


        }else if (v==btndlr){
            title_yellow.setText("Deposito Loan Ratio");
            title_blue.setText("Deposito Loan Ratio");
            text1.setText("");
            text2.setText("");
            toggleButtonActive(false);
            btndlr.setBackgroundResource(R.drawable.activity_btn_blue);
            btndlr.setTextColor(Color.parseColor("#ffffff"));
            setGONE();

        }else if (v==btnagf){
            title_yellow.setText("AGF");
            title_blue.setText("AGF");
            text1.setText("");
            text2.setText("");
            toggleButtonActive(false);
            btnagf.setBackgroundResource(R.drawable.activity_btn_blue);
            btnagf.setTextColor(Color.parseColor("#ffffff"));

            setGONE();
            horizontalAGF.setVisibility(View.VISIBLE);



        }else if (v==btnavg){
            toggleButtonActive(false);
            btnavg.setBackgroundResource(R.drawable.activity_btn_blue);
            btnavg.setTextColor(Color.parseColor("#ffffff"));



        }else if (v==btnbakidebet){
            title_yellow.setText("Baki Debet");
            title_blue.setText("Baki Debet");
            text1.setText("");
            text2.setText("");
            toggleButtonActive(false);
            btnbakidebet.setBackgroundResource(R.drawable.activity_btn_blue);
            btnbakidebet.setTextColor(Color.parseColor("#ffffff"));

            toogleButtonBakiDebet(false);
            btnBakiDebetTrue.setBackgroundResource(R.drawable.activity_btn_blue);
            btnBakiDebetTrue.setTextColor(Color.parseColor("#ffffff"));

            setGONE();
            lineBakiDebet.setVisibility(View.VISIBLE);
            toggleLineChart(chart_BakiDebet1);


        }else if (v==btndetail){
            Intent im=new Intent(getActivity(), ImageActivity.class);
            startActivity(im);

        }else if (v==btnBakiDebetTrue){
            toogleButtonBakiDebet(false);
            btnBakiDebetTrue.setBackgroundResource(R.drawable.activity_btn_blue);
            btnBakiDebetTrue.setTextColor(Color.parseColor("#ffffff"));
            toggleLineChart(chart_BakiDebet1);

        }else if (v==btnPercentageTrue){
            toogleButtonBakiDebet(false);
            btnPercentageTrue.setBackgroundResource(R.drawable.activity_btn_blue);
            btnPercentageTrue.setTextColor(Color.parseColor("#ffffff"));
            toggleLineChart(chart_BakiDebet2);
        }
    }

    private void toggleChart(BarChart bc){

        chart_Lcf.setVisibility(View.GONE);
        chart_Dpk.setVisibility(View.GONE);

        bc.setVisibility(View.VISIBLE);
    }

    private void setGONE(){

        listTFD.setVisibility(View.GONE);
        btndetail.setVisibility(View.GONE);

        chart_CashIn.setVisibility(View.GONE);
        chat_CashOut.setVisibility(View.GONE);
        chart_Lcf.setVisibility(View.GONE);
        chart_Dpk.setVisibility(View.GONE);

        lineBakiDebet.setVisibility(View.GONE);

        horizontalAGF.setVisibility(View.GONE);

    }

    private void toggleLineChart(CombinedChart cc){
        chart_CashIn.setVisibility(View.GONE);
        chat_CashOut.setVisibility(View.GONE);
        chart_BakiDebet1.setVisibility(View.GONE);
        chart_BakiDebet2.setVisibility(View.GONE);

        cc.setVisibility(View.VISIBLE);
    }

    private static String removeLastChar(String str) {
        return str.substring(0,str.length()-1);
    }



    public class DataFetcherTask extends AsyncTask<Void, Void, Void> {
        private String parsing;
        ArrayList<BarEntry> yCashIn = new ArrayList<BarEntry>();
        ArrayList<String> xCashIn = new ArrayList<String>();
        ArrayList<Entry> lineIn = new ArrayList<Entry>();

        ArrayList<BarEntry> yCashOut = new ArrayList<BarEntry>();
        ArrayList<String> xCashOut = new ArrayList<String>();
        ArrayList<Entry> lineOut = new ArrayList<Entry>();

        ArrayList<BarEntry> yLcf = new ArrayList<BarEntry>();
        ArrayList<String> xLcf = new ArrayList<String>();

        ArrayList<BarEntry> yDpk = new ArrayList<BarEntry>();
        ArrayList<String> xDpk = new ArrayList<String>();

        ArrayList<BarEntry> barBaki = new ArrayList<BarEntry>();
        ArrayList<Entry> lineBaki = new ArrayList<Entry>();
        ArrayList<String> blnBaki = new ArrayList<String>();
        ArrayList<BarEntry> barBakiPercen = new ArrayList<BarEntry>();
        ArrayList<Entry> lineBakiThres = new ArrayList<Entry>();

        public DataFetcherTask(String parsing) {
            this.parsing = parsing;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new SpotsDialog(getActivity(), R.style.CustomProgress);
            pDialog.setMessage("Please wait...!!!");

            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                JSONArray jsonArray = new JSONArray(DataManager.MyHttpGet(urlGetperAccount+parsing));
                Log.d("datadash", jsonArray.toString());
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

                            //yCashIn.add(new BarEntry(new float[]{fltCashIn}, a));
                            yCashIn.add(new BarEntry(fltCashIn, a));
                            lineIn.add(new Entry(fltRevenue, a));

                            //Log.d("datacash", yCashIn.toString());


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

                            yCashOut.add(new BarEntry(fltCashOut, a));
                            lineOut.add(new Entry(fltRevenue, a));

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

                        }
                    } else {
                        mylistLcf.clear();
                        myListMonthLCF.clear();
                        hashMapLcf = new HashMap<String, String>();
                        mylistLcf.add(hashMapLcf);
                        hashMapMonth = new HashMap<String, String>();
                        myListMonthLCF.add(hashMapMonth);
                    }

                    //Mapping Data AGF
                    if (jsonObject.has("agf")){
                        JSONArray arrayAGF = jsonObject.getJSONArray("agf");
                        for (int a = 0; a<arrayAGF.length();a++){
                            JSONObject objAGF = arrayAGF.getJSONObject(a);

                            String pinjam = objAGF.getString("rek_pinjaman");
                            String sumber = objAGF.getString("rek_sumber");
                            String tempo = objAGF.getString("jatuh_tempo");
                            String nominal = objAGF.getString("nominal");
                            String kolek = objAGF.getString("kolektibilitas");

                            mylistAGF.clear();
                            hashMapAGF = new HashMap<>();
                            hashMapAGF.put("no", Integer.toString(a+1));
                            hashMapAGF.put("pinjam", DataManager.getDecimalFormat(pinjam));
                            hashMapAGF.put("sumber", DataManager.getDecimalFormat(sumber));
                            hashMapAGF.put("tempo", tempo);
                            hashMapAGF.put("nominal", DataManager.getDecimalFormat(nominal));
                            hashMapAGF.put("kolek", kolek);

                            mylistAGF.add(hashMapAGF);

                        }
                    }else {
                        mylistAGF.clear();
                        hashMapAGF = new HashMap<String, String>();
                        mylistAGF.add(hashMapAGF);

                    }

                    // Mapping Data BakiDebet
                    if(jsonObject.has("baki_debet")) {
                        JSONArray arrayBaki = jsonObject.getJSONArray("baki_debet");
                        //Log.d("bakidebet", arrayBaki.toString());
                        for (int a = 0; a < arrayBaki.length(); a++){
                            JSONObject objBaki = arrayBaki.getJSONObject(a);

                            String strMonth = objBaki.getString("month");
                            String strBaki_debet = objBaki.getString("baki_debet");
                            String strlimit = objBaki.getString("limit");
                            String strthreshold = objBaki.getString("threshold");
                            String strpercentage = objBaki.getString("percentage");

                            int intBaki_debet = Integer.parseInt(strBaki_debet);
                            int intlimit = Integer.parseInt(strlimit);

                            float fltBaki_debet = (float) intBaki_debet;
                            float fltlimit = (float) intlimit;

                            blnBaki.add(strMonth);

                            barBaki.add(new BarEntry(fltBaki_debet, a));
                            lineBaki.add(new Entry(fltlimit, a));


                            //Log.d("karakter", removeLastChar(strpercentage));
                            int intPercen = Integer.parseInt(removeLastChar(strpercentage));
                            int intThres = Integer.parseInt(removeLastChar(strthreshold));

                            float fltPercen = (float) intPercen;
                            float fltThres = (float) intThres;

                            barBakiPercen.add(new BarEntry(fltPercen, a));
                            lineBakiThres.add(new Entry(fltThres, a));



                        }


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
            BarDataSet dataSetCashIn = new BarDataSet(yCashIn, "Cash In");
            dataSetCashIn.setColor(getResources().getColor(R.color.cpb_green));

            BarData dataCashIn = new BarData();
            dataCashIn.addDataSet(dataSetCashIn);

            LineDataSet lineDataSetCashIn = new LineDataSet(lineIn, "Target Revenue");
            //lineDataSetCashIn.setCircleColor(getResources().getColor(R.color.yellow));
            lineDataSetCashIn.setColor(getResources().getColor(R.color.yellow));

            LineData lineDataCashIn = new LineData();
            lineDataCashIn.addDataSet(lineDataSetCashIn);

            CombinedData comdataCashIn = new CombinedData(xCashIn);
            comdataCashIn.setData(dataCashIn);
            comdataCashIn.setData(lineDataCashIn);

            chart_CashIn.setData(comdataCashIn);
            chart_CashIn.setDescription("");
            chart_CashIn.animateXY(2000, 2000);
            XAxis xLabels = chart_CashIn.getXAxis();
            xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
            chart_CashIn.getXAxis().setDrawGridLines(false);
            chart_CashIn.setDoubleTapToZoomEnabled(false);
            chart_CashIn.setPinchZoom(false);
            chart_CashIn.getAxisRight().setEnabled(false);

            chart_CashIn.invalidate();


            //CASHOut
            BarDataSet BarsetCashOut = new BarDataSet(yCashOut, "Cash Out");
            BarsetCashOut.setColor(getResources().getColor(R.color.cpb_green));

            BarData barDataCashOut = new BarData();
            barDataCashOut.addDataSet(BarsetCashOut);

            LineDataSet LineSetCashOut = new LineDataSet(lineOut, "Target Revenue");
            LineSetCashOut.setColor(getResources().getColor(R.color.yellow));

            LineData lineDataCashOut = new LineData();
            lineDataCashOut.addDataSet(LineSetCashOut);

            CombinedData comdataCashOut = new CombinedData(xCashOut);
            comdataCashOut.setData(barDataCashOut);
            comdataCashOut.setData(lineDataCashOut);

            chat_CashOut.setData(comdataCashOut);
            chat_CashOut.setDescription("");
            chat_CashOut.animateXY(2000, 2000);
            XAxis xLabelsOut = chat_CashOut.getXAxis();
            xLabelsOut.setPosition(XAxis.XAxisPosition.BOTTOM);
            chat_CashOut.getXAxis().setDrawGridLines(false);
            chat_CashOut.setDoubleTapToZoomEnabled(false);
            chat_CashOut.setPinchZoom(false);
            chat_CashOut.getAxisRight().setEnabled(false);

            chat_CashOut.invalidate();


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
            YAxis leftAxis_DPK = chart_Dpk.getAxisLeft();
            leftAxis_DPK.setValueFormatter(new MyYAxisValueFormatter());
            leftAxis_DPK.setAxisMinValue(0f); // this replaces setStartAtZero(true)
            chart_Dpk.getAxisRight().setEnabled(false);
            chart_Dpk.getXAxis().setDrawGridLines(false);
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
            chart_Dpk.setDoubleTapToZoomEnabled(false);
            chart_Dpk.setPinchZoom(false);
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
            chart_Lcf.getXAxis().setDrawGridLines(false);
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
            chart_Lcf.setDoubleTapToZoomEnabled(false);
            chart_Lcf.setPinchZoom(false);
            chart_Lcf.invalidate();

            //AGF

            String[] agfColumns = new String[]{"no","pinjam","sumber","tempo","nominal","kolek"};

            int[] agfTags = new int[] {R.id.txt_agf_no, R.id.txt_agf_pinjam, R.id.txt_agf_sumber, R.id.txt_agf_tempo,
                    R.id.txt_agf_nominal, R.id.txt_agf_kolektibilitas};

            adapterAGF = new SimpleAdapter(getActivity(), mylistAGF, R.layout.list_row_agf, agfColumns, agfTags);
            listAGF.setAdapter(adapterAGF);
            listAGF.setExpanded(true);



            //Baki Debet

            /*BAKI DEBET*/
            BarDataSet BarsetBaki = new BarDataSet(barBaki, "Baki Debet");
            BarsetBaki.setColor(getResources().getColor(R.color.cpb_green));

            BarData barDataBaki = new BarData();
            barDataBaki.addDataSet(BarsetBaki);

            LineDataSet LineSetBaki = new LineDataSet(lineBaki, "Limit");
            LineSetBaki.setColor(getResources().getColor(R.color.yellow));

            LineData lineDataBaki = new LineData();
            lineDataBaki.addDataSet(LineSetBaki);

            CombinedData comdataBaki = new CombinedData(blnBaki);
            comdataBaki.setData(barDataBaki);
            comdataBaki.setData(lineDataBaki);

            chart_BakiDebet1.setData(comdataBaki);
            chart_BakiDebet1.setDescription("");
            chart_BakiDebet1.animateXY(2000, 2000);
            XAxis xLabelsBaki = chart_BakiDebet1.getXAxis();
            xLabelsBaki.setPosition(XAxis.XAxisPosition.BOTTOM);
            chart_BakiDebet1.getXAxis().setDrawGridLines(false);
            chart_BakiDebet1.setDoubleTapToZoomEnabled(false);
            chart_BakiDebet1.setPinchZoom(false);
            chart_BakiDebet1.getAxisRight().setEnabled(false);

            chart_BakiDebet1.invalidate();

            /*PERCENTAGE*/
            BarDataSet BarsetPercen = new BarDataSet(barBakiPercen, "Percentage");
            BarsetPercen.setColor(getResources().getColor(R.color.cpb_green));

            BarData barDataPercen = new BarData();
            barDataPercen.addDataSet(BarsetPercen);

            LineDataSet LineSetPercen = new LineDataSet(lineBakiThres, "Threshold");
            LineSetPercen.setColor(getResources().getColor(R.color.yellow));

            LineData lineDataPercen = new LineData();
            lineDataPercen.addDataSet(LineSetPercen);

            CombinedData comdataPercen = new CombinedData(blnBaki);
            comdataPercen.setData(barDataPercen);
            comdataPercen.setData(lineDataPercen);

            chart_BakiDebet2.setData(comdataPercen);
            chart_BakiDebet2.setDescription("%");
            chart_BakiDebet2.animateXY(2000, 2000);
            XAxis xLabelsPercen = chart_BakiDebet2.getXAxis();
            xLabelsPercen.setPosition(XAxis.XAxisPosition.BOTTOM);
            chart_BakiDebet2.getXAxis().setDrawGridLines(false);

            chart_BakiDebet2.getAxisRight().setEnabled(false);
            chart_BakiDebet2.setDoubleTapToZoomEnabled(false);
            chart_BakiDebet2.setPinchZoom(false);
            chart_BakiDebet2.invalidate();

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
        private String parsing;

        public DataSpinner(String parsing) {
            this.parsing = parsing;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            worldListDirectorate = new ArrayList<String>();

            try {
                JSONArray jsonArray = new JSONArray(DataManager.MyHttpGet(urlList+idParsing));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String strCif = jsonObject1.getString("cif");
                    if (strCif.trim().equals(parsing)) {

                        JSONArray array = jsonObject1.getJSONArray(acc_num);
                        for (int a = 0; a < array.length(); a++) {
                            strspinnum = array.getString(a);

                            worldListDirectorate.add(strspinnum);
                            //Log.d("kata", strspinnum);
                        }
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
            try {
                world = new ArrayAdapter<String>(getActivity(), R.layout.list_new_spinner, worldListDirectorate);
                world.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(world);
            }catch (Exception e){

            }

            //wo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //spinner.setAdapter(new ArrayAdapter<String>(getActivity(),
            //        R.layout.list_new_spinner, worldListDirectorate));
        }
    }

    private class DataSpinnerTop extends AsyncTask<Void, Void, Void> {
        String urlList = DataManager.urlListperAccount;
        private static final String acc_num = "acc_num";
        String strspinnum, strCif;
        ArrayList<String> worldListDirectorate;
        ArrayAdapter<String> world;

        ArrayList<String> kata;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            worldListDirectorate = new ArrayList<String>();

            kata = new ArrayList<>();

            try {
                JSONArray jsonArray = new JSONArray(DataManager.MyHttpGet(urlList+idParsing));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    strCif = jsonObject1.getString("cif");

                    worldListDirectorate.add(strCif);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                world = new ArrayAdapter<String>(getActivity(), R.layout.list_new_spinner, worldListDirectorate);
                world.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin_top.setAdapter(world);
            }catch (Exception e){

            }

            spin_top.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //Log.d("kata", list.get(position).getNumber().toString());
                    new DataSpinner(spin_top.getSelectedItem().toString()).execute();


                    //Toast.makeText(getActivity(), list.get(position).getAccnumber(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }
}
