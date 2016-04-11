package floo.com.mpm_mandiri.fragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;


import floo.com.mpm_mandiri.R;
import floo.com.mpm_mandiri.column.Axis;
import floo.com.mpm_mandiri.column.ChartUtils;
import floo.com.mpm_mandiri.column.Column;
import floo.com.mpm_mandiri.column.ColumnChartData;
import floo.com.mpm_mandiri.column.ColumnChartView;
import floo.com.mpm_mandiri.column.SubcolumnValue;

/**
 * Created by Floo on 2/23/2016.
 */
public class DashboardActivity extends Fragment {
    private View mChart;
    private String[] mMonth = new String[] { "6", "8", "10", "12", "14",
            "16", "18", "20", "22", "24", "26"};
    LinearLayout chartContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_dashboard, container, false);
        chartContainer = (LinearLayout) v.findViewById(R.id.chart);
        barChart();



        return v;
    }

    private void barChart() {
        int[] x = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
        int[] income = { 50, 40, 70, 60, 50, 40, 100, 80, 90, 88,
                85};

        // Creating an XYSeries for Income
        // Creating an XYSeries for Income
        XYSeries incomeSeries = new XYSeries("");
        // Creating an XYSeries for Expense
        XYSeries expenseSeries = new XYSeries("Expense");
        // Adding data to Income and Expense Series
        //for (int i = 0; i < income.length; i++) {
        //	incomeSeries.add(i, income[i]);
        //	//expenseSeries.add(i, expense[i]);
        //}

        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Income Series to the dataset
        dataset.addSeries(incomeSeries);
        // Adding Expense Series to dataset
        //dataset.addSeries(expenseSeries);

        // Creating XYSeriesRenderer to customize incomeSeries
        XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
        incomeRenderer.setColor(Color.BLUE); // color of the graph set to cyan
        incomeRenderer.setFillPoints(true);
        incomeRenderer.setLineWidth(2);
        incomeRenderer.setDisplayChartValues(true);
        incomeRenderer.setDisplayChartValuesDistance(10); // setting chart value
        // distance

        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
        expenseRenderer.setColor(Color.GREEN);
        expenseRenderer.setFillPoints(true);
        expenseRenderer.setLineWidth(2);
        expenseRenderer.setDisplayChartValues(true);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer
                .setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
        multiRenderer.setXLabels(0);
        //multiRenderer.setChartTitle("Income vs Expense Chart");


        /***
         * Customizing graphs
         */
        // setting text size of the title
        multiRenderer.setChartTitleTextSize(28);
        // setting text size of the axis title
        multiRenderer.setAxisTitleTextSize(24);
        // setting text size of the graph lable
        multiRenderer.setLabelsTextSize(50);
        // setting zoom buttons visiblity
        multiRenderer.setZoomButtonsVisible(false);
        // setting pan enablity which uses graph to move on both axis
        multiRenderer.setPanEnabled(false, false);
        // setting click false on graph
        multiRenderer.setClickEnabled(false);
        // setting zoom to false on both axis
        multiRenderer.setZoomEnabled(false, false);
        // setting lines to display on y axis
        multiRenderer.setShowGridY(false);
        // setting lines to display on x axis
        multiRenderer.setShowGridX(false);
        // setting legend to fit the screen size
        multiRenderer.setFitLegend(true);
        // setting displaying line on grid
        multiRenderer.setShowGrid(false);
        // setting zoom to false
        multiRenderer.setZoomEnabled(false);
        // setting external zoom functions to false
        multiRenderer.setExternalZoomEnabled(false);
        // setting displaying lines on graph to be formatted(like using
        // graphics)
        multiRenderer.setAntialiasing(true);
        // setting to in scroll to false
        multiRenderer.setInScroll(false);
        // setting to set legend height of the graph
        multiRenderer.setLegendHeight(30);
        // setting x axis label align
        multiRenderer.setXLabelsAlign(Paint.Align.CENTER);
        // setting y axis label to align
        multiRenderer.setYLabelsAlign(Paint.Align.LEFT);
        // setting text style
        //multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
        // setting no of values to display in y axis
        multiRenderer.setYLabels(10);
        // setting y axis max value, Since i'm using static values inside the
        // graph so i'm setting y max value to 4000.
        // if you use dynamic values then get the max y value and set here
        multiRenderer.setYAxisMax(100);
        multiRenderer.setYAxisMin(0);
        // setting used to move the graph on xaxiz to .5 to the right
        multiRenderer.setXAxisMin(-1.5);
        // setting max values to be display in x axis
        //multiRenderer.setXAxisMax(11);
        // setting bar size or space between two bars
        multiRenderer.setBarSpacing(1);
        // Setting background color of the graph to transparent
        multiRenderer.setBackgroundColor(Color.TRANSPARENT);
        // Setting margin color of the graph to transparent
        multiRenderer.setMarginsColor(getResources().getColor(
                R.color.transparent_background));
        multiRenderer.setApplyBackgroundColor(true);

        // setting the margin size for the graph in the order top, left, bottom,
        // right
        multiRenderer.setMargins(new int[] { 30, 30, 30, 30 });

        for (int i = 0; i < income.length; i++) {
            incomeSeries.add(i, income[i]);
            multiRenderer.addXTextLabel(i, mMonth[i]);
        }

        // Adding incomeRenderer and expenseRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to
        // multipleRenderer
        // should be same
        multiRenderer.addSeriesRenderer(incomeRenderer);
        //multiRenderer.addSeriesRenderer(expenseRenderer);

        // this part is used to display graph on the xml

        // remove any views before u paint the chart
        chartContainer.removeAllViews();
        // drawing bar chart
        mChart = ChartFactory.getBarChartView(getActivity(), dataset,
                multiRenderer, BarChart.Type.DEFAULT);

        // adding the view to the linearlayout
        chartContainer.addView(mChart);


    }
}
