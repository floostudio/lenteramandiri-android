package floo.com.mpm_mandiri.coba;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import floo.com.mpm_mandiri.R;
import floo.com.mpm_mandiri.fragment.MyValueFormatter;
import floo.com.mpm_mandiri.fragment.MyYAxisValueFormatter;

/**
 * Created by Floo on 5/3/2016.
 */
public class AAAA_Coba extends AppCompatActivity {
    private BarChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aaaa_coba);

        mChart = (BarChart) findViewById(R.id.chart1);
        //mChart.setOnChartValueSelectedListener(this);


        ArrayList<String> labels = new ArrayList<>();
        labels.add("JAN");
        labels.add("FEB");
        labels.add("MAR");



        String[] mMonths = new String[] {
                "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
        };

        int[] var1 = new int[]{60, 50};




        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < mMonths.length; i++) {
            xVals.add(mMonths[i % mMonths.length]);

            float val1 = (float) 40;
            float val2 = (float) 70;

            yVals1.add(new BarEntry(new float[] { val1, val2 }, i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "Statistics Vienna 2014");
        set1.setColors(getColors());
        set1.setStackLabels(new String[] { "Births", "Divorces" });

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueFormatter(new MyValueFormatter());

        mChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(false);

        // change the position of the y-labels
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(new MyYAxisValueFormatter());
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        mChart.getAxisRight().setEnabled(false);

        XAxis xLabels = mChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);

        // mChart.setDrawXLabels(false);
        // mChart.setDrawYLabels(false);



        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);


        mChart.setData(data);
        mChart.invalidate();

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
}
