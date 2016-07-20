package com.floo.lenteramandiri;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.floo.lenteramandiri.alarm.AlarmServiceBroadcastReciever;
import com.floo.lenteramandiri.alarm.Call;
import com.floo.lenteramandiri.alarm.Database;
import com.floo.lenteramandiri.alarm.MainActivityAdapter;
import com.floo.lenteramandiri.fragment.InfoActivity;
import com.floo.lenteramandiri.fragment.NewDashboardActivity;
import com.floo.lenteramandiri.fragment.NewsActivity;
import com.floo.lenteramandiri.fragment.NotificationActivity;
import com.floo.lenteramandiri.fragment.ProfilActivity;
import com.floo.lenteramandiri.fragment.TaskActivity;
import com.floo.lenteramandiri.utils.CircleImageView;
import com.floo.lenteramandiri.utils.DataManager;
import com.floo.lenteramandiri.utils.ImageLoader;
import com.floo.lenteramandiri.utils.SessionManager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import com.floo.lenteramandiri.fragment.CalenderActivity;
import com.floo.lenteramandiri.fragment.PortofolioActivity;

import com.floo.lenteramandiri.utils.RoundedImageView;
import com.floo.lenteramandiri.utils.calendarphone.CalendarHelper;
import com.floo.lenteramandiri.utils.calendarphone.CustomOnItemSelectedListener;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView title;
    ActionBar actionBar;
    DrawerLayout drawer;
    String idParsing, strFirstname, strLastname, strProfpic, strEsclated;
    public static final String first_name = "first_name";
    public static final String last_name = "last_name";
    public static final String profpic = "profpic";
    public static final String IDPARSING = "IDPARSING";
    public static final String escalated_group = "escalated_group";
    RoundedImageView img;
    SessionManager session;
    ProgressDialog progressDialog;
    Bitmap myBitmap;
    String frgment="";
    ImageLoader imageLoader;
    NavigationView navigationView;


    //calendar
    private Hashtable<String,String> calendarIdTable;
    Spinner calendarIdSpinner;

    //wake up call
    ArrayList<Long> arrayList = new ArrayList<>();
    Long[] dataCall ;
    AlarmManager[] alarmManager = new AlarmManager[24];
    PendingIntent pendingIntent;
    ArrayList<PendingIntent> arrayListInten = new ArrayList<>();
    ArrayList<Long> arrayListTime = new ArrayList<>();

    ListView listView;
    MainActivityAdapter mainActivityAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.list_alarm);
        mainActivityAdapter = new MainActivityAdapter(MainActivity.this);

        /*Intent i = getIntent();
        idParsing = i.getStringExtra("IDPARSING");
        strFirstname = i.getStringExtra(first_name);
        strLastname = i.getStringExtra(last_name);
        strProfpic = i.getStringExtra(profpic);*/

        session = new SessionManager(getApplicationContext());
        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
        //session.isLoggedIn();
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        idParsing = user.get(IDPARSING);
        strFirstname = user.get(first_name);
        strLastname = user.get(last_name);
        strProfpic = user.get(profpic);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        //title.setText("DASHBOARD");

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        CircleImageView img = (CircleImageView) headerView.findViewById(R.id.img_profil);

        new ProfilLoadTask(strProfpic, img).execute();
        //new ProfilLoadTask("http://play.floostudio.com/lenteramandiri/static/images/users/profile/femadadle.png", img);
        //imageLoader = new ImageLoader();
        //imageLoader.DisplayProfile("http://play.floostudio.com/lenteramandiri/static/images/users/profile/femadadle.png", img);
        //imageLoader.DisplayProfile(strProfpic, img);

        /*if (strProfpic.trim().equals("http://play.floostudio.com/lenteramandiri/static/images/users/profile/http://play.floostudio")){

            img.setImageResource(R.drawable.profile);

        }else {
            //imageLoader = new ImageLoader(strProfpic, img);
            imageLoader.DisplayImage(strProfpic, img);
            //new ImageLoadTask(strProfpic, img).execute();

        }*/

        TextView name = (TextView) headerView.findViewById(R.id.txtname);
        name.setText(strFirstname + " " + strLastname);
        ImageView img_edit = (ImageView) headerView.findViewById(R.id.img_edit);
        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //title.setText("PROFILE");
                setFragment(0);
                drawer.closeDrawer(GravityCompat.START);
                setChecked();
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), strProfpic, Toast.LENGTH_LONG).show();

                setFragment(0);

                drawer.closeDrawer(GravityCompat.START);

                setChecked();
            }
        });
        if (navigationView != null){
            setupNavigationDrawerContent(navigationView);
        }
        setupNavigationDrawerContent(navigationView);

        Intent i = getIntent();
        frgment = i.getStringExtra("fragment");

        if (frgment==null){
            setFragment(1);
        }else {
            navigationView.getMenu().getItem(1).setChecked(true);

            setFragment(2);
        }

        //Calendar
        calendarIdSpinner = new Spinner(this);
        calendarIdSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        if (CalendarHelper.haveCalendarReadWritePermissions(this))
        {
            //Load calendars
            calendarIdTable = CalendarHelper.listCalendarId(this);

            updateCalendarIdSpinner();

        }

        //Log.d("datatgl", String.valueOf(getTime()));

        if (CalendarHelper.haveCalendarReadWritePermissions(MainActivity.this)){
            new DataFetcherTask().execute();
            //addNewEvent();


        }else {
            CalendarHelper.requestCalendarReadWritePermission(MainActivity.this);
        }

    }

    @Override
    protected void onPause() {
        // setListAdapter(null);
        Database.deactivate();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAlarmList();
    }

    private void updateCalendarIdSpinner()
    {
        if (calendarIdTable==null)
        {
            return;
        }

        List<String> list = new ArrayList<String>();

        Enumeration e = calendarIdTable.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            list.add(key);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        calendarIdSpinner.setAdapter(dataAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode==CalendarHelper.CALENDARHELPER_PERMISSION_REQUEST_CODE)
        {
            if (CalendarHelper.haveCalendarReadWritePermissions(this))
            {
                Toast.makeText(this, (String)"Have Calendar Read/Write Permission.",
                        Toast.LENGTH_LONG).show();

            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void addNewEvent(String title, long mili)
    {
        if (calendarIdTable==null)
        {
            Toast.makeText(this, (String)"No calendars found. Please ensure at least one google account has been added.",
                    Toast.LENGTH_LONG).show();
            //Load calendars
            calendarIdTable = CalendarHelper.listCalendarId(this);

            updateCalendarIdSpinner();

            return;
        }


        final long oneHour = 1000 * 60 * 60;
        final long tenMinutes = 1000 * 60 * 10;
        final long fiveMinutes = 1000 * 60 * 5;
        final long twoMinutes = 1000 * 60 * 2;

        long oneHourFromNow = (new Date()).getTime() + oneHour;
        long tenMinutesFromNow = (new Date()).getTime() + tenMinutes;
        long twoMinutesFromNow = (new Date()).getTime() + twoMinutes;

        //long mili = DataManager.dateTomiliSecond(DataManager.epochtodateTime(1466211960));
        //Log.d("datamasuk", String.valueOf(tenMinutesFromNow));


        String calendarString = calendarIdSpinner.getSelectedItem().toString();
        //Log.d("spinner", calendarString);
        //Log.d("table", "table>"+calendarIdTable.get(calendarString)+" spnner>"+calendarString);
        int calendar_id = Integer.parseInt(calendarIdTable.get(calendarString));

        CalendarHelper.MakeNewCalendarEntry(this, title, "Add event", "Somewhere",mili,mili,false,true,calendar_id,4);

    }

    public class DataFetcherTask extends AsyncTask<Void, Void, Void> {
        String strTitle, strNote, strCompany, strNotifi;
        int strId, strExpire;

        int expire;
        int day = 86400;




        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                JSONArray jsonArray = new JSONArray(DataManager.MyHttpGet(DataManager.urltaskList+idParsing));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    strId = jsonObject.getInt("task_id");
                    strTitle = jsonObject.getString("title");
                    strExpire = jsonObject.getInt("expire");
                    strNote = jsonObject.getString("note");
                    strCompany = jsonObject.getString("company");

                    if (jsonObject.has("notification")){
                        JSONArray arrayNotification = jsonObject.getJSONArray("notification");
                        for (int a=0; a<arrayNotification.length();a++){
                            String number = arrayNotification.getString(a);

                            if (!number.trim().equals("0")){
                                int nilai = Integer.parseInt(number);
                                expire = strExpire - (day*nilai);
                                addNewEvent(strTitle, DataManager.dateTomiliSecond(DataManager.epochtodateTime(expire)));

                            }

                           /* if (!number.trim().equals("0")){
                                if (number.trim().equals("7")){
                                    expire = strExpire - week;
                                }else if (number.trim().equals("14")){
                                    expire = strExpire - (week*2);
                                }else if (number.trim().equals("21")){
                                    expire = strExpire - (week*3);
                                }


                                if (expire>0){

                                    addNewEvent(strTitle, DataManager.dateTomiliSecond(DataManager.epochtodateTime(expire)));
                                }


                            }*/
                        }
                    }

                    if (jsonObject.has("wakeup_call")){
                        Database.deleteAll();
                        JSONArray arrayWakeUp = jsonObject.getJSONArray("wakeup_call");
                        for (int a=0; a<arrayWakeUp.length();a++){
                            String number = arrayWakeUp.getString(a);

                            if (!number.trim().equals("0")){
                                int nilai = Integer.parseInt(number);
                                expire = strExpire - (day*nilai);
                                long data = DataManager.dateTomiliSecond(DataManager.epochtodateTime(expire));
                                if (data > getTime()){
                                    Call call = new Call();
                                    call.setId(strId);
                                    call.setTitle(strTitle);
                                    call.setDate(DataManager.dateTomiliSecond(DataManager.epochtodateTime(expire)));
                                    call.setActive(call.getActive());
                                    Database.create(call);
                                }
                            }

                            /*if (!number.trim().equals("0")) {
                                if (number.trim().equals("7")) {
                                    expire = strExpire - week;
                                    long data = DataManager.dateTomiliSecond(DataManager.epochtodateTime(expire));
                                    if (data > getTime()){
                                        Call call = new Call();
                                        call.setId(strId);
                                        call.setTitle(strTitle);
                                        call.setDate(DataManager.dateTomiliSecond(DataManager.epochtodateTime(expire)));
                                        call.setActive(call.getActive());
                                        Database.create(call);
                                    }
                                } else if (number.trim().equals("14")) {
                                    expire = strExpire - (week * 2);
                                    long data = DataManager.dateTomiliSecond(DataManager.epochtodateTime(expire));
                                    if (data > getTime()){
                                        Call call = new Call();
                                        call.setId(strId);
                                        call.setTitle(strTitle);
                                        call.setDate(DataManager.dateTomiliSecond(DataManager.epochtodateTime(expire)));
                                        call.setActive(call.getActive());
                                        Database.create(call);
                                    }
                                } else if (number.trim().equals("21")) {
                                    expire = strExpire - (week * 3);
                                    long data = DataManager.dateTomiliSecond(DataManager.epochtodateTime(expire));
                                    if (data > getTime()){
                                        Call call = new Call();
                                        call.setId(strId);
                                        call.setTitle(strTitle);
                                        call.setDate(data);
                                        call.setActive(call.getActive());
                                        Database.create(call);
                                    }
                                }
                            }*/
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

            //Log.d("datamasuk", String.valueOf(arrayListTime.size()));

            callMathAlarmScheduleService();

            MainActivity.this.listView.setAdapter(mainActivityAdapter);

        }
    }

    private long getTime(){

        String str = DataManager.dateNow();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date2 = null;
        try {
            date2 = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date2.getTime();


    }

    private void setChecked(){
        navigationView.getMenu().getItem(0).setChecked(false);
        navigationView.getMenu().getItem(1).setChecked(false);
        navigationView.getMenu().getItem(2).setChecked(false);
        navigationView.getMenu().getItem(3).setChecked(false);
        navigationView.getMenu().getItem(4).setChecked(false);
        navigationView.getMenu().getItem(5).setChecked(false);
        navigationView.getMenu().getItem(6).setChecked(false);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.combined, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.nav_home:
                                //title.setText("DASHBOARD");
                                item.setChecked(true);
                                setFragment(1);
                                drawer.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.nav_task:
                                item.setChecked(true);
                                setFragment(2);
                                drawer.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.nav_portfolio:

                                item.setChecked(true);
                                setFragment(3);
                                drawer.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.nav_calender:

                                item.setChecked(true);
                                setFragment(4);
                                drawer.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.nav_news:

                                item.setChecked(true);
                                setFragment(5);
                                drawer.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.nav_info:

                                item.setChecked(true);
                                setFragment(6);
                                drawer.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.nav_logout:
                                new logoutuser().execute();

                                //session.logoutUser();
                                //finish();
                                //Intent signout=new Intent(MainActivity.this, LoginActivity.class);
                                //startActivity(signout);
                                //finish();
                                return true;
                        }
                        return true;
                    }
                }
        );
    }

    public void setFragment(int position){
        FragmentManager fm;
        FragmentTransaction ft;
        Bundle bundle = new Bundle();
        bundle.putString("IDPARSING", idParsing);

        switch (position){
            case 0:
                title.setText("PROFIL");
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ProfilActivity profil = new ProfilActivity();
                profil.setArguments(bundle);
                ft.replace(R.id.content_fragment, profil);
                ft.commit();
                break;
            case 1:
                title.setText("BERANDA");
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                NewDashboardActivity dashboar = new NewDashboardActivity();
                dashboar.setArguments(bundle);
                ft.replace(R.id.content_fragment, dashboar);
                ft.commit();
                break;
            case 2:
                title.setText("TUGAS");
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                TaskActivity task= new TaskActivity();
                task.setArguments(bundle);
                ft.replace(R.id.content_fragment, task);
                ft.commit();
                break;
            case 3:
                title.setText("PORTOFOLIO");
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                PortofolioActivity porto = new PortofolioActivity();
                porto.setArguments(bundle);
                ft.replace(R.id.content_fragment, porto);
                ft.commit();
                break;
            case 4:
                title.setText("KALENDAR");
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                CalenderActivity calender = new CalenderActivity();
                calender.setArguments(bundle);
                ft.replace(R.id.content_fragment, calender);
                ft.commit();
                break;
            case 5:
                title.setText("BERITA");
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                NewsActivity news = new NewsActivity();
                ft.replace(R.id.content_fragment, news);
                ft.commit();
                break;
            case 6:
                title.setText("INFO");
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                InfoActivity info = new InfoActivity();
                ft.replace(R.id.content_fragment, info);
                ft.commit();
                break;
            case 7:
                title.setText("NOTIFICATION");
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                NotificationActivity notification = new NotificationActivity();
                ft.replace(R.id.content_fragment, notification);
                ft.commit();
                break;
        }
    }
    private class logoutuser extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this, "", "Logging Out", true);

        }

        @Override
        protected Void doInBackground(Void... params) {

            session.logoutUser();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            progressDialog.dismiss();

           finish();

        }
    }

    public void updateAlarmList() {
        Database.init(MainActivity.this);
        final List<Call> alarms = Database.getAll();
        mainActivityAdapter.setMathAlarms(alarms);

        runOnUiThread(new Runnable() {
            public void run() {
                // reload content
                MainActivity.this.mainActivityAdapter.notifyDataSetChanged();


            }
        });
    }


    protected void callMathAlarmScheduleService() {
        Intent mathAlarmServiceIntent = new Intent(this, AlarmServiceBroadcastReciever.class);
        sendBroadcast(mathAlarmServiceIntent, null);
    }

    public class ProfilLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ProfilLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {

                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();

                Bitmap myBitmap = null;
                if (connection.getResponseCode()==200){

                    InputStream input = connection.getInputStream();
                    myBitmap = BitmapFactory.decodeStream(input);
                }else {
                    myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile);
                }


                return myBitmap;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }
    }




    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0,
                                                int arg1) {
                                Intent intent = new Intent(
                                        Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                finish();
                                startActivity(intent);
                            }
                        }).create().show();

    }


}
