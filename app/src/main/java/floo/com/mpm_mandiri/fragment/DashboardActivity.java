package floo.com.mpm_mandiri.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.achartengine.ChartFactory;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
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
import java.util.List;


import floo.com.mpm_mandiri.MainActivity;
import floo.com.mpm_mandiri.R;


/**
 * Created by Floo on 2/23/2016.
 */
public class DashboardActivity extends Fragment {
    private static String tanggal = "tanggal";
    private static String income = "income";
    String strTgl, strIncome;
    BarChart barChart;


    private static String bulan = "bulan";
    private static String persentage = "persentage";
    PieChart pieChart;
    ArrayList<Entry> entries;
    PieDataSet piedataset;
    ArrayList<String> labels;
    PieData data;
    String strBln;
    int strPersen;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_dashboard, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("DASHBOARD");
        barChart = (BarChart) v.findViewById(R.id.barchart);
        pieChart = (PieChart) v.findViewById(R.id.piechart);

        exampleBar();
        examplePie();
        //piechart();


        return v;
    }


    private void exampleBar(){
        ArrayList<BarEntry> group1 = new ArrayList<>();
        group1.add(new BarEntry(4f, 0));
        group1.add(new BarEntry(8f, 1));
        group1.add(new BarEntry(6f, 2));
        group1.add(new BarEntry(12f, 3));
        group1.add(new BarEntry(18f, 4));
        group1.add(new BarEntry(9f, 5));

        ArrayList<BarEntry> group2 = new ArrayList<>();
        group2.add(new BarEntry(6f, 0));
        group2.add(new BarEntry(7f, 1));
        group2.add(new BarEntry(8f, 2));
        group2.add(new BarEntry(12f, 3));
        group2.add(new BarEntry(15f, 4));
        group2.add(new BarEntry(10f, 5));

        BarDataSet barDataSet1 = new BarDataSet(group1, "Group 1");
        //barDataSet1.setColor(Color.rgb(0, 155, 0));
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

        BarDataSet barDataSet2 = new BarDataSet(group2, "Group 2");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);

        ArrayList<String> labels = new ArrayList<>();
        labels.add("JAN");
        labels.add("FEB");
        labels.add("MAR");
        labels.add("APR");
        labels.add("MAY");
        labels.add("JUN");


        BarData data = new BarData(labels, dataSets);
        barChart.setData(data);
        barChart.setTouchEnabled(false);
        //barChart.getXAxis().setEnabled(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        //barChart.getLegend().setEnabled(false);
        barChart.setDescription("");
        barChart.animateXY(2000, 2000);
        barChart.invalidate();
    }

    private void examplePie(){

        entries = new ArrayList<>();
        entries.add(new Entry(360, 0));
        entries.add(new Entry(80, 1));
        //Log.d("entries", String.valueOf(entries));


        piedataset = new PieDataSet(entries, "");

        labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");

        data = new PieData(labels, piedataset);
        pieChart.setData(data);
        piedataset.setDrawValues(false);
        piedataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        pieChart.setDescription("");
        pieChart.setHighlightPerTapEnabled(false);
        //pieChart.setHardwareAccelerationEnabled(false);
        pieChart.setDrawSliceText(false);
        //pieChart.getLegend().setEnabled(false);
        pieChart.setRotationEnabled(false);
        pieChart.setCenterText("KOLEKTIBILITAS\nDOWNGRADE\n75%");
        pieChart.animateXY(5000, 5000);



    }

    private void piechart(){
        entries = new ArrayList<>();
        labels = new ArrayList<String>();
        new DataPie().execute();






    }

    private class DataPie extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            String objek="";

            HttpParams myParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(myParams, 5000);
            HttpConnectionParams.setSoTimeout(myParams, 5000);

            String serverData="";
            DefaultHttpClient httpClient= new DefaultHttpClient(myParams);
            HttpGet httpGet = new HttpGet("http://192.168.1.127/piechart.json");

            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                serverData = EntityUtils.toString(httpEntity);

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
                    strBln = jsonObject.getString(bulan);
                    strPersen= jsonObject.getInt("persentage");

                    entries.add(new Entry(strPersen, i));

                    labels.add(strBln);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            piedataset = new PieDataSet(entries, "");
            data = new PieData(labels, piedataset);
            pieChart.setData(data);
            piedataset.setDrawValues(false);
            //piedataset.setHighlightEnabled(false);
            piedataset.setColors(ColorTemplate.COLORFUL_COLORS); //
            pieChart.setDescription("");
            pieChart.setHighlightPerTapEnabled(false);
            //pieChart.setHardwareAccelerationEnabled(false);
            pieChart.setDrawSliceText(false);
            pieChart.setRotationEnabled(false);
            pieChart.setCenterText("KOLEKTIBILITAS\nDOWNGRADE\n75%");



        }
    }



}
