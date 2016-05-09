package floo.com.mpm_mandiri.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Executors;

import floo.com.mpm_mandiri.R;
import floo.com.mpm_mandiri.calendar.CalendarDay;
import floo.com.mpm_mandiri.calendar.DayViewDecorator;
import floo.com.mpm_mandiri.calendar.DayViewFacade;
import floo.com.mpm_mandiri.calendar.MaterialCalendarView;
import floo.com.mpm_mandiri.calendar.OnDateSelectedListener;
import floo.com.mpm_mandiri.calendar.decorators.EventDecorator;
import floo.com.mpm_mandiri.data.CalendarTaskListActivity;
import floo.com.mpm_mandiri.utils.DataManager;

/**
 * Created by Floo on 3/3/2016.
 */
public class CalenderActivity extends Fragment {
    String url = DataManager.url;
    String urlTask = DataManager.urltaskList;
    private static final String title = "title";
    private static final String note = "note";
    private static final String company = "company";
    private static int strId, strExpire;
    String idParsing, strTitle, strNote, strCompany, formatDate;

    MaterialCalendarView calendarr;
    ArrayList<CalendarDay> dates;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_calender, container, false);
        dates = new ArrayList<>();
        initView(v);


        return v;
    }
    public void initView(View view) {
        idParsing = this.getArguments().getString("IDPARSING");
        calendarr = (MaterialCalendarView) view.findViewById(R.id.calendarView);

        //calendarr.setHeaderTextAppearance(R.style.TextAppearance_AppCompat_Large);
        calendarr.setDateTextAppearance(R.style.TextAppearance_AppCompat_Small);
        //calendarr.setWeekDayTextAppearance(R.style.TextAppearance_AppCompat_Medium);

        calendarr.addDecorator(new EnableOneToTenDecorator());

        Calendar calendar = Calendar.getInstance();
        calendarr.setSelectedDate(calendar.getTime());

        calendar.set(calendar.get(Calendar.YEAR), Calendar.JANUARY, 1);
        calendarr.setMinimumDate(calendar.getTime());

        calendar.set(calendar.get(Calendar.YEAR) + 2, Calendar.OCTOBER, 31);
        calendarr.setMaximumDate(calendar.getTime());

        //int color = R.color.lightblue;
        calendarr.setSelectionColor(this.getResources().getColor(R.color.lightblue));

        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());

        calendarr.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                long today;
                String str = getSelectedDatesString();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date date2 = null;
                try {
                    date2 = df.parse(str);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                today = date2.getTime()/1000;
                //Toast.makeText(getActivity(), String.valueOf(today), Toast.LENGTH_LONG).show();

                Intent task = new Intent(getActivity(), CalendarTaskListActivity.class);
                task.putExtra("IDPARSING", idParsing);
                //task.putExtra("date", String.valueOf(today));
                task.putExtra("date", getSelectedDatesString());

                startActivity(task);
                //Toast.makeText(getActivity(), getSelectedDatesString(), Toast.LENGTH_LONG).show();


            }
        });
    }
    private String getSelectedDatesString() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        CalendarDay date = calendarr.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return df.format(date.getDate());
    }

    private static class EnableOneToTenDecorator implements DayViewDecorator {

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return day.getDay() <= 10;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(false);
        }
    }

    public static String epochtodate(int epoch){
        Date date = new Date(epoch * 1000L);
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        format.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        return format.format(date);
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {

            try {
                JSONArray jsonArray = new JSONArray(DataManager.MyHttpGet(urlTask+idParsing));
                JSONObject jsonObject;
                for (int i=0; i<jsonArray.length();i++){
                    jsonObject = jsonArray.getJSONObject(i);

                    strId = jsonObject.getInt("task_id");
                    strTitle = jsonObject.getString(title);
                    strExpire = jsonObject.getInt("expire");
                    strNote = jsonObject.getString(note);
                    strCompany = jsonObject.getString(company);

                    CalendarDay dayy;
                    dayy = CalendarDay.from(new Date(epochtodate(strExpire)));
                    //Log.d("tanggal", String.valueOf(dayy));

                    dates.add(dayy);

                }


            }catch (JSONException e){
                e.printStackTrace();
            }


            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            calendarr.addDecorator(new EventDecorator(getActivity().getResources().getColor(R.color.lightyellow), calendarDays));
        }
    }
}