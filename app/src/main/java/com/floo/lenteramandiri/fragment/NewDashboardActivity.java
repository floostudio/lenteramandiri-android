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
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.floo.lenteramandiri.adapter.DashboardAGFAdapter;
import com.floo.lenteramandiri.adapter.DashboardTFDAdapter;
import com.floo.lenteramandiri.utils.ConnectivityReceiver;
import com.floo.lenteramandiri.utils.MyLenteraMandiri;
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
import java.util.List;

import dmax.dialog.SpotsDialog;
import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.data.ImageActivity;
import com.floo.lenteramandiri.utils.DataManager;
import com.floo.lenteramandiri.utils.MyValueFormatter;
import com.floo.lenteramandiri.utils.MyYAxisValueFormatter;

/**
 * Created by Floo on 4/21/2016.
 */
public class NewDashboardActivity extends Fragment implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener{
    HashMap<String, String> hashMapTFD, hashMapCashio, hashMapCashout, hashMapDpk, hashMapLcf, hashMapMonth, hashMapAGF;
    ArrayList<HashMap<String, String>> mylistTFD, mylistCashin, mylistCashout, mylistDpk, mylistLcf,
            mylistMonth, myListMonthCashout, myListMonthDPK, myListMonthLCF, mylistAGF;
    ExpandableHeightListView listTFD, listAGF;
    SimpleAdapter adapterTFD, adapterAGF;
    Button btntfd, btncash, btncashout, btndpk, btnlcf, btndlr, btnagf, btnavg, btnbakidebet, btndetail;
    Spinner spinner, spin_top;
    TextView text1, text2, title_blue, title_yellow, title1 ;

    private SpotsDialog pDialog;
    String urlGetperAccount = DataManager.urlGetperAccountSementara;
    String strAcc_num,strCif, idParsing;
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

    BarChart chart_Lcf, chart_Dpk, chart_dlr;
    CombinedChart chart_CashIn, chat_CashOut, chart_BakiDebet1, chart_BakiDebet2;
    LinearLayout lineBakiDebet, linear_text2;
    Button btnBakiDebetTrue, btnPercentageTrue;
    HorizontalScrollView horizontalAGF;
    RelativeLayout relative_cash_in;
    LinearLayout linier_cash_out, linier_dpk, linier_lcf, linier_bakidebet1, linier_bakidebet2, linier_dlr;
    Button btn_toggle_line_cashIn, btn_toggle_bar_cashIn, btn_toggle_line_cashOut, btn_toggle_bar_cashOut,
            btn_toggle_line_bakidebet1, btn_toggle_bar_bakidebet1, btn_toggle_line_bakidebet2, btn_toggle_bar_bakidebet2;
    HashMap<Button, Integer> mapBtnCashIn, mapBtnCashOut, mapBtnBakiDebet1, mapBtnBakiDebet2, mapBtnDlr;
    GridView grid;


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

        spin_top.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String text = spin_top.getSelectedItem().toString().replaceAll("\\s+","");
                int index = text.indexOf("-");
                new DataSpinner(text.substring(0, index)).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView txt = (TextView)view.findViewById(R.id.text1);
                String text = spin_top.getSelectedItem().toString().replaceAll("\\s+","");
                int index = text.indexOf("-");

                new DataFetcherTask(text.substring(0, index), txt.getText().toString()).execute();
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
        linear_text2 = (LinearLayout)v.findViewById(R.id.linear_text2);
        title_blue = (TextView)v.findViewById(R.id.txt_title_blue);
        title_yellow = (TextView)v.findViewById(R.id.txt_title_yellow);
        title1 = (TextView)v.findViewById(R.id.tfd_title_1);
        spinner = (Spinner)v.findViewById(R.id.spin_array);
        spinner.setVisibility(View.INVISIBLE);
        spin_top = (Spinner)v.findViewById(R.id.spin_array_top);
        spin_top.getBackground().setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);
        spinner.getBackground().setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);

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
        chart_dlr= (BarChart) v.findViewById(R.id.chart_dlr);
        chart_BakiDebet1 = (CombinedChart)v.findViewById(R.id.chart_bakidebet1);
        chart_BakiDebet2 = (CombinedChart)v.findViewById(R.id.chart_bakidebet2);
        lineBakiDebet = (LinearLayout)v.findViewById(R.id.linierBakiDebet) ;

        horizontalAGF = (HorizontalScrollView)v.findViewById(R.id.horizontalAGF);
        grid = (GridView)v.findViewById(R.id.grid);

        relative_cash_in = (RelativeLayout)v.findViewById(R.id.relative_cash_in);
        btn_toggle_line_cashIn = (Button)v.findViewById(R.id.btn_toggle_line_cashIn);
        btn_toggle_bar_cashIn = (Button)v.findViewById(R.id.btn_toggle_bar_cashIn);
        btn_toggle_line_cashIn.setOnClickListener(this);
        btn_toggle_bar_cashIn.setOnClickListener(this);
        mapBtnCashIn = new HashMap<Button, Integer>();
        btn_toggle_line_cashIn.setBackgroundResource(R.drawable.activity_btn_blue);
        btn_toggle_bar_cashIn.setBackgroundResource(R.drawable.activity_btn_blue);
        mapBtnCashIn.put(btn_toggle_line_cashIn, R.drawable.activity_btn_blue);
        mapBtnCashIn.put(btn_toggle_bar_cashIn, R.drawable.activity_btn_blue);


        linier_cash_out = (LinearLayout)v.findViewById(R.id.linier_cash_out);
        btn_toggle_line_cashOut = (Button)v.findViewById(R.id.btn_toggle_line_cashOut);
        btn_toggle_bar_cashOut = (Button)v.findViewById(R.id.btn_toggle_bar_cashOut);
        btn_toggle_line_cashOut.setOnClickListener(this);
        btn_toggle_bar_cashOut.setOnClickListener(this);
        mapBtnCashOut = new HashMap<Button, Integer>();
        btn_toggle_line_cashOut.setBackgroundResource(R.drawable.activity_btn_blue);
        btn_toggle_bar_cashOut.setBackgroundResource(R.drawable.activity_btn_blue);
        mapBtnCashOut.put(btn_toggle_line_cashOut, R.drawable.activity_btn_blue);
        mapBtnCashOut.put(btn_toggle_bar_cashOut, R.drawable.activity_btn_blue);

        linier_dpk = (LinearLayout)v.findViewById(R.id.linier_dpk);
        linier_lcf = (LinearLayout)v.findViewById(R.id.linier_lcf);

        linier_dlr = (LinearLayout)v.findViewById(R.id.linier_dlr);

        mapBtnDlr = new HashMap<Button, Integer>();

        linier_bakidebet1 = (LinearLayout)v.findViewById(R.id.linier_bakidebet1);
        btn_toggle_line_bakidebet1 = (Button)v.findViewById(R.id.btn_toggle_line_bakidebet1);
        btn_toggle_bar_bakidebet1 = (Button)v.findViewById(R.id.btn_toggle_bar_bakidebet1);
        btn_toggle_line_bakidebet1.setOnClickListener(this);
        btn_toggle_bar_bakidebet1.setOnClickListener(this);
        mapBtnBakiDebet1 = new HashMap<Button, Integer>();
        btn_toggle_line_bakidebet1.setBackgroundResource(R.drawable.activity_btn_blue);
        btn_toggle_bar_bakidebet1.setBackgroundResource(R.drawable.activity_btn_blue);
        mapBtnBakiDebet1.put(btn_toggle_line_bakidebet1, R.drawable.activity_btn_blue);
        mapBtnBakiDebet1.put(btn_toggle_bar_bakidebet1, R.drawable.activity_btn_blue);

        linier_bakidebet2 = (LinearLayout)v.findViewById(R.id.linier_bakidebet2);
        btn_toggle_line_bakidebet2 = (Button)v.findViewById(R.id.btn_toggle_line_bakidebet2);
        btn_toggle_bar_bakidebet2 = (Button)v.findViewById(R.id.btn_toggle_bar_bakidebet2);
        btn_toggle_line_bakidebet2.setOnClickListener(this);
        btn_toggle_bar_bakidebet2.setOnClickListener(this);
        mapBtnBakiDebet2 = new HashMap<Button, Integer>();
        btn_toggle_line_bakidebet2.setBackgroundResource(R.drawable.activity_btn_blue);
        btn_toggle_bar_bakidebet2.setBackgroundResource(R.drawable.activity_btn_blue);
        mapBtnBakiDebet2.put(btn_toggle_line_bakidebet2, R.drawable.activity_btn_blue);
        mapBtnBakiDebet2.put(btn_toggle_bar_bakidebet2, R.drawable.activity_btn_blue);

    }

    @Override
    public void onClick(View v) {
        if (v==btntfd){
            title_yellow.setText("Transaction Flow Diagram");
            title_blue.setText("Transaction Flow Diagram");
            text1.setText("Collection");
            text1.setPadding(0, 0, 0, 0);
            text2.setText("Payment");
            linear_text2.setVisibility(View.VISIBLE);
            toggleButtonActive(false);
            btntfd.setBackgroundResource(R.drawable.activity_btn_blue);
            btntfd.setTextColor(Color.parseColor("#ffffff"));

            setGONE();

            listTFD.setVisibility(View.VISIBLE);
            btndetail.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.INVISIBLE);

        }else if (v==btncash){
            title_yellow.setText("Cash In");
            title_blue.setText("Cash In");
            text1.setText("");
            text2.setText("");
            toggleButtonActive(false);
            btncash.setBackgroundResource(R.drawable.activity_btn_blue);
            btncash.setTextColor(Color.parseColor("#ffffff"));

            setGONE();
            relative_cash_in.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);


        }else if (v==btncashout){
            title_yellow.setText("Cash Out");
            title_blue.setText("Cash Out");
            text1.setText("");
            text2.setText("");
            toggleButtonActive(false);
            btncashout.setBackgroundResource(R.drawable.activity_btn_blue);
            btncashout.setTextColor(Color.parseColor("#ffffff"));

            setGONE();
            linier_cash_out.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);


        }else if (v==btndpk){
            title_yellow.setText("DPK");
            title_blue.setText("DPK");
            text1.setText("");
            text2.setText("");
            toggleButtonActive(false);
            btndpk.setBackgroundResource(R.drawable.activity_btn_blue);
            btndpk.setTextColor(Color.parseColor("#ffffff"));

            setGONE();
            linier_dpk.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.INVISIBLE);
        }else if (v==btnlcf){
            title_yellow.setText("LCF");
            title_blue.setText("LCF");
            text1.setText("");
            text2.setText("");
            toggleButtonActive(false);
            btnlcf.setBackgroundResource(R.drawable.activity_btn_blue);
            btnlcf.setTextColor(Color.parseColor("#ffffff"));

            setGONE();
            linier_lcf.setVisibility(View.VISIBLE);

        }else if (v==btndlr){
            title_yellow.setText("Deposito Loan Ratio");
            title_blue.setText("Deposito Loan Ratio");
            text1.setText("");
            text2.setText("");
            toggleButtonActive(false);
            btndlr.setBackgroundResource(R.drawable.activity_btn_blue);
            btndlr.setTextColor(Color.parseColor("#ffffff"));
            setGONE();

            spinner.setVisibility(View.INVISIBLE);
            linier_dlr.setVisibility(View.VISIBLE);

        }else if (v==btnagf){
            title_yellow.setText("AGF");
            title_blue.setText("AGF");
            text1.setText("*Saldo yang ditampilkan saldo tanggal 20 untuk saldo terakhir harap cek di BDS");
            text1.setPadding(10, 0, 10, 0);
            linear_text2.setVisibility(View.GONE);
            toggleButtonActive(false);
            btnagf.setBackgroundResource(R.drawable.activity_btn_blue);
            btnagf.setTextColor(Color.parseColor("#ffffff"));

            setGONE();
            horizontalAGF.setVisibility(View.VISIBLE);

            spinner.setVisibility(View.INVISIBLE);

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
            linier_bakidebet1.setVisibility(View.VISIBLE);
            linier_bakidebet2.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);

        }else if (v==btndetail){
            Intent im=new Intent(getActivity(), ImageActivity.class);
            startActivity(im);

        }else if (v==btnBakiDebetTrue){
            toogleButtonBakiDebet(false);
            btnBakiDebetTrue.setBackgroundResource(R.drawable.activity_btn_blue);
            btnBakiDebetTrue.setTextColor(Color.parseColor("#ffffff"));
            linier_bakidebet1.setVisibility(View.VISIBLE);
            linier_bakidebet2.setVisibility(View.GONE);

        }else if (v==btnPercentageTrue){
            toogleButtonBakiDebet(false);
            btnPercentageTrue.setBackgroundResource(R.drawable.activity_btn_blue);
            btnPercentageTrue.setTextColor(Color.parseColor("#ffffff"));
            linier_bakidebet1.setVisibility(View.GONE);
            linier_bakidebet2.setVisibility(View.VISIBLE);

        }else if (v==btn_toggle_bar_cashIn){
            if (mapBtnCashIn.get(btn_toggle_bar_cashIn) == R.drawable.activity_btn) {
                btn_toggle_bar_cashIn.setText("Hide Bar Values");
                btn_toggle_bar_cashIn.setTextColor(getResources().getColor(R.color.cpb_white));
                btn_toggle_bar_cashIn.setBackgroundResource(R.drawable.activity_btn_blue);
                mapBtnCashIn.put(btn_toggle_bar_cashIn, R.drawable.activity_btn_blue);
                chart_CashIn.getBarData().setDrawValues(true);
                chart_CashIn.invalidate();
            }else {
                btn_toggle_bar_cashIn.setText("Show Bar Values");
                btn_toggle_bar_cashIn.setTextColor(getResources().getColor(R.color.background));
                btn_toggle_bar_cashIn.setBackgroundResource(R.drawable.activity_btn);
                mapBtnCashIn.put(btn_toggle_bar_cashIn, R.drawable.activity_btn);
                chart_CashIn.getBarData().setDrawValues(false);
                chart_CashIn.invalidate();
            }

        }else if (v==btn_toggle_line_cashIn){
            if (mapBtnCashIn.get(btn_toggle_line_cashIn) == R.drawable.activity_btn) {
                btn_toggle_line_cashIn.setText("Hide Line Values");
                btn_toggle_line_cashIn.setTextColor(getResources().getColor(R.color.cpb_white));
                btn_toggle_line_cashIn.setBackgroundResource(R.drawable.activity_btn_blue);
                mapBtnCashIn.put(btn_toggle_line_cashIn, R.drawable.activity_btn_blue);
                chart_CashIn.getLineData().setDrawValues(true);
                chart_CashIn.invalidate();

            }else {
                btn_toggle_line_cashIn.setText("Show Line Values");
                btn_toggle_line_cashIn.setTextColor(getResources().getColor(R.color.background));
                btn_toggle_line_cashIn.setBackgroundResource(R.drawable.activity_btn);
                mapBtnCashIn.put(btn_toggle_line_cashIn, R.drawable.activity_btn);
                chart_CashIn.getLineData().setDrawValues(false);
                chart_CashIn.invalidate();
            }
        }else if (v==btn_toggle_bar_cashOut){
            if (mapBtnCashOut.get(btn_toggle_bar_cashOut) == R.drawable.activity_btn) {
                btn_toggle_bar_cashOut.setText("Hide Bar Values");
                btn_toggle_bar_cashOut.setTextColor(getResources().getColor(R.color.cpb_white));
                btn_toggle_bar_cashOut.setBackgroundResource(R.drawable.activity_btn_blue);
                mapBtnCashOut.put(btn_toggle_bar_cashOut, R.drawable.activity_btn_blue);
                chat_CashOut.getBarData().setDrawValues(true);
                chat_CashOut.invalidate();
            }else {
                btn_toggle_bar_cashOut.setText("Show Bar Values");
                btn_toggle_bar_cashOut.setTextColor(getResources().getColor(R.color.background));
                btn_toggle_bar_cashOut.setBackgroundResource(R.drawable.activity_btn);
                mapBtnCashOut.put(btn_toggle_bar_cashOut, R.drawable.activity_btn);
                chat_CashOut.getBarData().setDrawValues(false);
                chat_CashOut.invalidate();
            }

        }else if (v==btn_toggle_line_cashOut){
            if (mapBtnCashOut.get(btn_toggle_line_cashOut) == R.drawable.activity_btn) {
                btn_toggle_line_cashOut.setText("Hide Line Values");
                btn_toggle_line_cashOut.setTextColor(getResources().getColor(R.color.cpb_white));
                btn_toggle_line_cashOut.setBackgroundResource(R.drawable.activity_btn_blue);
                mapBtnCashOut.put(btn_toggle_line_cashOut, R.drawable.activity_btn_blue);
                chat_CashOut.getLineData().setDrawValues(true);
                chat_CashOut.invalidate();

            }else {
                btn_toggle_line_cashOut.setText("Show Line Values");
                btn_toggle_line_cashOut.setTextColor(getResources().getColor(R.color.background));
                btn_toggle_line_cashOut.setBackgroundResource(R.drawable.activity_btn);
                mapBtnCashOut.put(btn_toggle_line_cashOut, R.drawable.activity_btn);
                chat_CashOut.getLineData().setDrawValues(false);
                chat_CashOut.invalidate();
            }
        }else if (v==btn_toggle_bar_bakidebet1){
            if (mapBtnBakiDebet1.get(btn_toggle_bar_bakidebet1) == R.drawable.activity_btn) {
                btn_toggle_bar_bakidebet1.setText("Hide Bar Values");
                btn_toggle_bar_bakidebet1.setTextColor(getResources().getColor(R.color.cpb_white));
                btn_toggle_bar_bakidebet1.setBackgroundResource(R.drawable.activity_btn_blue);
                mapBtnBakiDebet1.put(btn_toggle_bar_bakidebet1, R.drawable.activity_btn_blue);
                chart_BakiDebet1.getBarData().setDrawValues(true);
                chart_BakiDebet1.invalidate();

            }else {
                btn_toggle_bar_bakidebet1.setText("Show Bar Values");
                btn_toggle_bar_bakidebet1.setTextColor(getResources().getColor(R.color.background));
                btn_toggle_bar_bakidebet1.setBackgroundResource(R.drawable.activity_btn);
                mapBtnBakiDebet1.put(btn_toggle_bar_bakidebet1, R.drawable.activity_btn);
                chart_BakiDebet1.getBarData().setDrawValues(false);
                chart_BakiDebet1.invalidate();
            }

        }else if (v==btn_toggle_line_bakidebet1){
            if (mapBtnBakiDebet1.get(btn_toggle_line_bakidebet1) == R.drawable.activity_btn) {
                btn_toggle_line_bakidebet1.setText("Hide Line Values");
                btn_toggle_line_bakidebet1.setTextColor(getResources().getColor(R.color.cpb_white));
                btn_toggle_line_bakidebet1.setBackgroundResource(R.drawable.activity_btn_blue);
                mapBtnBakiDebet1.put(btn_toggle_line_bakidebet1, R.drawable.activity_btn_blue);
                chart_BakiDebet1.getLineData().setDrawValues(true);
                chart_BakiDebet1.invalidate();

            }else {
                btn_toggle_line_bakidebet1.setText("Show Line Values");
                btn_toggle_line_bakidebet1.setTextColor(getResources().getColor(R.color.background));
                btn_toggle_line_bakidebet1.setBackgroundResource(R.drawable.activity_btn);
                mapBtnBakiDebet1.put(btn_toggle_line_bakidebet1, R.drawable.activity_btn);
                chart_BakiDebet1.getLineData().setDrawValues(false);
                chart_BakiDebet1.invalidate();
            }

        }else if (v==btn_toggle_bar_bakidebet2){
            if (mapBtnBakiDebet2.get(btn_toggle_bar_bakidebet2) == R.drawable.activity_btn) {
                btn_toggle_bar_bakidebet2.setText("Hide Bar Values");
                btn_toggle_bar_bakidebet2.setTextColor(getResources().getColor(R.color.cpb_white));
                btn_toggle_bar_bakidebet2.setBackgroundResource(R.drawable.activity_btn_blue);
                mapBtnBakiDebet2.put(btn_toggle_bar_bakidebet2, R.drawable.activity_btn_blue);
                chart_BakiDebet2.getBarData().setDrawValues(true);
                chart_BakiDebet2.invalidate();

            }else {
                btn_toggle_bar_bakidebet2.setText("Show Bar Values");
                btn_toggle_bar_bakidebet2.setTextColor(getResources().getColor(R.color.background));
                btn_toggle_bar_bakidebet2.setBackgroundResource(R.drawable.activity_btn);
                mapBtnBakiDebet2.put(btn_toggle_bar_bakidebet2, R.drawable.activity_btn);
                chart_BakiDebet2.getBarData().setDrawValues(false);
                chart_BakiDebet2.invalidate();
            }

        }else if (v==btn_toggle_line_bakidebet2){
            if (mapBtnBakiDebet2.get(btn_toggle_line_bakidebet2) == R.drawable.activity_btn) {
                btn_toggle_line_bakidebet2.setText("Hide Line Values");
                btn_toggle_line_bakidebet2.setTextColor(getResources().getColor(R.color.cpb_white));
                btn_toggle_line_bakidebet2.setBackgroundResource(R.drawable.activity_btn_blue);
                mapBtnBakiDebet2.put(btn_toggle_line_bakidebet2, R.drawable.activity_btn_blue);
                chart_BakiDebet2.getLineData().setDrawValues(true);
                chart_BakiDebet2.invalidate();

            }else {
                btn_toggle_line_bakidebet2.setText("Show Line Values");
                btn_toggle_line_bakidebet2.setTextColor(getResources().getColor(R.color.background));
                btn_toggle_line_bakidebet2.setBackgroundResource(R.drawable.activity_btn);
                mapBtnBakiDebet2.put(btn_toggle_line_bakidebet2, R.drawable.activity_btn);
                chart_BakiDebet2.getLineData().setDrawValues(false);
                chart_BakiDebet2.invalidate();
            }

        }
    }

    private void setGONE(){

        listTFD.setVisibility(View.GONE);
        btndetail.setVisibility(View.GONE);

        relative_cash_in.setVisibility(View.GONE);
        linier_cash_out.setVisibility(View.GONE);
        linier_dpk.setVisibility(View.GONE);
        linier_lcf.setVisibility(View.GONE);
        lineBakiDebet.setVisibility(View.GONE);

        horizontalAGF.setVisibility(View.GONE);
        linier_dlr.setVisibility(View.GONE);

    }

    private static String removeLastChar(String str) {
        return str.substring(0,str.length()-1);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        DataManager.showSnack(getActivity(), isConnected);
    }

    @Override
    public void onResume() {
        super.onResume();
        MyLenteraMandiri.getInstance().setConnectivityListener(this);
        DataManager.checkConnection(getActivity());
    }

    public class DataFetcherTask extends AsyncTask<Void, Void, Void> {
        private String pCif, pAccount;
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

        ArrayList<BarEntry> yDlr = new ArrayList<BarEntry>();
        ArrayList<String> xDlr = new ArrayList<String>();
        ArrayList<Entry> lineDlr = new ArrayList<Entry>();

        ArrayList<BarEntry> barBaki = new ArrayList<BarEntry>();
        ArrayList<Entry> lineBaki = new ArrayList<Entry>();
        ArrayList<String> blnBaki = new ArrayList<String>();
        ArrayList<BarEntry> barBakiPercen = new ArrayList<BarEntry>();
        ArrayList<Entry> lineBakiThres = new ArrayList<Entry>();



        int lengthAGF;
        ArrayList<String> lengthList = new ArrayList<>();

        public DataFetcherTask(String pCif, String pAccount) {
            this.pCif = pCif;
            this.pAccount = pAccount;

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
            String json1 = "";
            JSONObject object1 = new JSONObject();
            try {
                object1.put("cif",pCif);
                object1.put("acc_num", pAccount);
                json1 = object1.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                JSONObject jsonObject = new JSONObject(DataManager.MyHttpPost(urlGetperAccount, json1));
                strCif = jsonObject.getString("cif");
                strAcc_num = jsonObject.getString(acc_num);
                String company = jsonObject.getString("company_name");

                if(jsonObject.has(tfd)) {
                    ArrayList<String> arrayListKey1 = new ArrayList<>();
                    ArrayList<String> arrayListKey2 = new ArrayList<>();
                    ArrayList<String> arrayListValue1 = new ArrayList<>();
                    ArrayList<String> arrayListValue2 = new ArrayList<>();
                    JSONArray arrayTFD = jsonObject.getJSONArray(tfd);
                    for (int a=0; a<arrayTFD.length();a++){
                        JSONObject objTFD = arrayTFD.getJSONObject(a);
                        String title = objTFD.getString("is_title");
                        if (title.trim().equals("1")){
                            JSONArray arrayKey = objTFD.getJSONArray("row");
                            for (int i=0; i<arrayKey.length();i++) {

                                if (i>=2){
                                    if (i%2==0){
                                        arrayListKey1.add(arrayKey.getString(i));
                                    }else {
                                        arrayListKey2.add(arrayKey.getString(i));
                                    }
                                }
                            }
                        }else {
                            JSONArray arrayKey = objTFD.getJSONArray("row");
                            for (int i=0; i<arrayKey.length();i++) {

                                if (i>=2){
                                    if (i%2==0){
                                        arrayListValue1.add(arrayKey.getString(i));
                                    }else {
                                        arrayListValue2.add(arrayKey.getString(i));
                                    }
                                }
                            }
                        }
                    }

                    mylistTFD.clear();
                    for (int z=0; z<arrayListValue1.size();z++){
                        hashMapTFD = new HashMap<String, String>();
                        hashMapTFD.put("data1", arrayListKey1.get(z));
                        hashMapTFD.put("data2", arrayListValue1.get(z));
                        hashMapTFD.put("data3", arrayListKey2.get(z));
                        hashMapTFD.put("data4", arrayListValue2.get(z));
                        mylistTFD.add(hashMapTFD);
                    }
                }

                // Mapping Data DPK
                if(jsonObject.has("dpk")) {
                    JSONArray arrayDPK = jsonObject.getJSONArray("dpk");
                    for (int a = 0; a < arrayDPK.length(); a++) {
                        JSONObject objDPK = arrayDPK.getJSONObject(a);

                        String strBlnDPK = objDPK.getString(month);
                        String strdpkkk = objDPK.getString("giro");
                        String strcredittt = objDPK.getString("tabungan");
                        String strdeposit = objDPK.getString("deposito");

                        int intdpk = Integer.parseInt(strdpkkk);
                        int intcredit = Integer.parseInt(strcredittt);
                        int intdeposit = Integer.parseInt(strdeposit);

                        float fltDPK = (float) intdpk;
                        float fltCredit = (float) intcredit;
                        float fltDeposit = (float)intdeposit;


                        xDpk.add(strBlnDPK);

                        yDpk.add(new BarEntry(new float[]{fltDPK, fltCredit, fltDeposit}, a));

                    }
                }

                //Mapping Data DLR
                if (jsonObject.has("dlr")){
                    JSONArray arrayDLR = jsonObject.getJSONArray("dlr");
                    for (int a=0; a<arrayDLR.length();a++){
                        JSONObject objDLR = arrayDLR.getJSONObject(a);

                        String strMonth = objDLR.getString(month);
                        String strLoan = objDLR.getString("outstanding_loan");
                        String strDpk = objDLR.getString("dpk");

                        int intLoan = Integer.parseInt(strLoan);
                        int intDpk = Integer.parseInt(strDpk);

                        float fltLoan = (float) intLoan;
                        float fltDpk = (float) intDpk;

                        xDlr.add(strMonth);

                        yDlr.add(new BarEntry(new float[]{fltLoan, fltDpk}, a));
                        //lineDlr.add(new Entry(fltDpk, a));
                    }
                }
                //Mapping Data AGF
                if (jsonObject.has("agf")){

                    mylistAGF.clear();
                    JSONArray arrayAGF = jsonObject.getJSONArray("agf");
                    for (int a = 0; a<arrayAGF.length();a++){
                        JSONObject objAGF = arrayAGF.getJSONObject(a);
                        String title = objAGF.getString("is_title");

                        if (title.trim().equals("1")){
                            JSONArray arrayRow = objAGF.getJSONArray("row");
                            for (int z=0;z<arrayRow.length();z++){
                                lengthList.add(arrayRow.getString(z));
                            }
                        }

                        JSONArray arrayRow = objAGF.getJSONArray("row");
                        for (int z=0;z<arrayRow.length();z++){
                            hashMapAGF = new HashMap<>();
                            hashMapAGF.put("values", arrayRow.getString(z));
                            mylistAGF.add(hashMapAGF);

                        }
                    }
                }else {
                    mylistAGF.clear();
                    hashMapAGF = new HashMap<String, String>();
                    mylistAGF.add(hashMapAGF);
                }

                // Mapping Data Cashin
                if(jsonObject.has("cashin")) {

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

                        yCashIn.add(new BarEntry(fltCashIn, a));
                        lineIn.add(new Entry(fltRevenue, a));

                    }
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

                    }
                }

                // Mapping Data BakiDebet
                if(jsonObject.has("baki_debet")) {
                    JSONArray arrayBaki = jsonObject.getJSONArray("baki_debet");
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

                        int intPercen = Integer.parseInt(removeLastChar(strpercentage));
                        int intThres = Integer.parseInt(removeLastChar(strthreshold));

                        float fltPercen = (float) intPercen;
                        float fltThres = (float) intThres;

                        barBakiPercen.add(new BarEntry(fltPercen, a));
                        lineBakiThres.add(new Entry(fltThres, a));

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

            //TFD
            DashboardTFDAdapter adapter = new DashboardTFDAdapter(getActivity(), mylistTFD);
            listTFD.setAdapter(adapter);
            listTFD.setExpanded(true);

            //CASHIN
            BarDataSet dataSetCashIn = new BarDataSet(yCashIn, "Cash In");
            dataSetCashIn.setColor(getResources().getColor(R.color.lightblue));

            BarData dataCashIn = new BarData();
            dataCashIn.addDataSet(dataSetCashIn);

            LineDataSet lineDataSetCashIn = new LineDataSet(lineIn, "Target Cash In");
            lineDataSetCashIn.setCircleColor(getResources().getColor(R.color.yellow));
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
            BarsetCashOut.setColor(getResources().getColor(R.color.lightblue));

            BarData barDataCashOut = new BarData();
            barDataCashOut.addDataSet(BarsetCashOut);

            LineDataSet LineSetCashOut = new LineDataSet(lineOut, "Target Cash Out");
            LineSetCashOut.setCircleColor(getResources().getColor(R.color.yellow));
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
            setDPK.setColors(getColors(3));
            setDPK.setStackLabels(new String[] { "Giro", "Tabungan", "Deposito" });

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

            //DLR
            BarDataSet setDLR = new BarDataSet(yDlr, "");
            setDLR.setColors(getColors(2));
            setDLR.setStackLabels(new String[] { "Outstanding Loan", "DPK"});

            ArrayList<BarDataSet> dataSetDLR = new ArrayList<BarDataSet>();
            dataSetDLR.add(setDLR);

            BarData dataDLR = new BarData(xDlr, dataSetDLR);
            dataDLR.setValueFormatter(new MyValueFormatter());

            chart_dlr.setDescription("");

            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            chart_dlr.setMaxVisibleValueCount(60);

            // scaling can now only be done on x- and y-axis separately
            chart_dlr.setPinchZoom(false);

            chart_dlr.setDrawGridBackground(false);
            chart_dlr.setDrawBarShadow(false);

            chart_dlr.setDrawValueAboveBar(false);

            // change the position of the y-labels
            YAxis leftAxis_DLR = chart_dlr.getAxisLeft();
            leftAxis_DLR.setValueFormatter(new MyYAxisValueFormatter());
            leftAxis_DLR.setAxisMinValue(0f); // this replaces setStartAtZero(true)
            chart_dlr.getAxisRight().setEnabled(false);
            chart_dlr.getXAxis().setDrawGridLines(false);
            XAxis xLabels_DLR = chart_dlr.getXAxis();
            xLabels_DLR.setPosition(XAxis.XAxisPosition.BOTTOM);

            // mChart.setDrawXLabels(false);
            // mChart.setDrawYLabels(false);

            Legend l_DLR = chart_dlr.getLegend();
            l_DLR.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
            l_DLR.setFormSize(8f);
            l_DLR.setFormToTextSpace(4f);
            l_DLR.setXEntrySpace(6f);

            chart_dlr.setData(dataDLR);
            chart_dlr.setDoubleTapToZoomEnabled(false);
            chart_dlr.setPinchZoom(false);
            chart_dlr.invalidate();


            //LCF
            /*BarDataSet setLCF = new BarDataSet(yLcf, "");
            setLCF.setColors(getColors(2));
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
            chart_Lcf.invalidate();*/

            //AGF

            grid.setNumColumns(lengthList.size());
            DashboardAGFAdapter adapter1 = new DashboardAGFAdapter(getActivity(), mylistAGF, lengthList.size());
            grid.setAdapter(adapter1);

            //Baki Debet

            /*BAKI DEBET*/
            BarDataSet BarsetBaki = new BarDataSet(barBaki, "Baki Debet");
            BarsetBaki.setColor(getResources().getColor(R.color.lightblue));

            BarData barDataBaki = new BarData();
            barDataBaki.addDataSet(BarsetBaki);

            LineDataSet LineSetBaki = new LineDataSet(lineBaki, "Limit");
            LineSetBaki.setColor(getResources().getColor(R.color.yellow));
            LineSetBaki.setCircleColor(getResources().getColor(R.color.yellow));

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
            BarsetPercen.setColor(getResources().getColor(R.color.lightblue));

            BarData barDataPercen = new BarData();
            barDataPercen.addDataSet(BarsetPercen);

            LineDataSet LineSetPercen = new LineDataSet(lineBakiThres, "Threshold");
            LineSetPercen.setColor(getResources().getColor(R.color.yellow));
            LineSetPercen.setCircleColor(getResources().getColor(R.color.yellow));

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

    private int[] getColors(int size) {

        //int stacksize = 2;

        // have as many colors as stack-values per entry
        int[] colors = new int[size];

        for (int i = 0; i < size; i++) {
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
                    String company = jsonObject1.getString("company_name");



                    worldListDirectorate.add(strCif+" - "+company);

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
        }
    }
}
