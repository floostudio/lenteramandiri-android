package com.floo.lenteramandiri.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.floo.lenteramandiri.calendar.DayViewDecorator;
import com.floo.lenteramandiri.calendar.DayViewFacade;
import com.floo.lenteramandiri.calendar.OnDateSelectedListener;
import com.floo.lenteramandiri.data.CalendarTaskListActivity;
import com.floo.lenteramandiri.utils.ConnectivityReceiver;
import com.floo.lenteramandiri.utils.DataManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Executors;

import dmax.dialog.SpotsDialog;
import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.calendar.CalendarDay;
import com.floo.lenteramandiri.calendar.MaterialCalendarView;
import com.floo.lenteramandiri.calendar.decorators.EventDecorator;
import com.floo.lenteramandiri.utils.MyLenteraMandiri;

/**
 * Created by Floo on 3/3/2016.
 */
public class CalenderActivity extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener{
    String url = DataManager.url;
    String urlTask = DataManager.urltaskList;
    private static final String title = "title";
    private static final String note = "note";
    private static final String company = "company";
    private static int strId, strExpire;
    String idParsing, strTitle, strNote, strCompany, formatDate;
    private SpotsDialog pDialog;

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

        calendarr.setDateTextAppearance(R.style.TextAppearance_AppCompat_Small);

        calendarr.addDecorator(new EnableOneToTenDecorator());

        Calendar calendar = Calendar.getInstance();
        calendarr.setSelectedDate(calendar.getTime());

        calendar.set(calendar.get(Calendar.YEAR), Calendar.JANUARY, 1);
        calendarr.setMinimumDate(calendar.getTime());

        calendar.set(calendar.get(Calendar.YEAR) + 2, Calendar.OCTOBER, 31);
        calendarr.setMaximumDate(calendar.getTime());

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

                Intent task = new Intent(getActivity(), CalendarTaskListActivity.class);
                task.putExtra("IDPARSING", idParsing);
                task.putExtra("date", getSelectedDatesString());

                startActivity(task);
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
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new SpotsDialog(getActivity(), R.style.CustomProgress);
            pDialog.setMessage("Please wait...!!!");
            pDialog.setCancelable(false);
            pDialog.show();
        }

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
            if (pDialog.isShowing())
                pDialog.dismiss();

            calendarr.addDecorator(new EventDecorator(getActivity().getResources().getColor(R.color.lightyellow), calendarDays));
        }
    }
}