package floo.com.mpm_mandiri.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import dmax.dialog.SpotsDialog;
import floo.com.mpm_mandiri.MainActivity;
import floo.com.mpm_mandiri.data.TaskDetailActivity;
import floo.com.mpm_mandiri.R;
import floo.com.mpm_mandiri.adapter.Task;
import floo.com.mpm_mandiri.adapter.TaskAdapter;
import floo.com.mpm_mandiri.utils.DataManager;

/**
 * Created by Floo on 2/23/2016.
 */
public class TaskActivity extends Fragment {
    //Listview
    HashMap<String, String> hashmapTask;
    ArrayList<HashMap<String, String>> arraylistTask;
    ListView listTask;
    int[] tgl = new int[]{1455206700,1460390700,1463108700,1463836320,1467306420,
            1475633280,1478364980,1480904520,1478364960};

    private SpotsDialog pDialog;
    Button btn_expired, btn_willexpired, btn_months;
    String url = DataManager.url;
    String urlTask = DataManager.urltaskList;
    String idParsing, formatDate, strTitle, strNote, strCompany;
    ImageView imageView;
    long today;

    ArrayList<Task> taskArray;
    TaskAdapter taskAdapter;

    //public static long today;
    public static String dateNow;

    private static int strId, strExpire;

    private static final String title = "title";
    private static final String note = "note";
    private static final String company = "company";

    public static TaskActivity ta;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_task, container, false);
        initView(v);
        arraylistTask = new ArrayList<HashMap<String, String>>();
        new DataFetcherTask().execute();



        String str = dateNow();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = null;
        try {
            date = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        today = date.getTime();


        return v;
    }

    public void refresh() {
        ((MainActivity) getActivity()).refresh();
    }


    public void initView(View view) {
        idParsing = this.getArguments().getString("IDPARSING");
        listTask = (ListView) view.findViewById(R.id.listTask);
        btn_expired = (Button) view.findViewById(R.id.btn_task_expired);
        btn_willexpired = (Button) view.findViewById(R.id.btn_task_willExp);
        btn_months = (Button) view.findViewById(R.id.btn_task_6months);


        final HashMap<Button, Integer> hashMap = new HashMap<Button, Integer>();
        btn_expired.setBackgroundResource(R.drawable.btn_expire_inactive);
        btn_months.setBackgroundResource(R.drawable.btn_6months_inactive);
        btn_willexpired.setBackgroundResource(R.drawable.btn_willexpire_inactive);
        hashMap.put(btn_expired, R.drawable.btn_expire_inactive);
        hashMap.put(btn_willexpired, R.drawable.btn_willexpire_inactive);
        hashMap.put(btn_months, R.drawable.btn_6months_inactive);

        listTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView taID = (TextView) view.findViewById(R.id.txt_list_task_id);
                TextView epoch = (TextView)view.findViewById(R.id.txt_list_task_tgl);
                Intent detailTask = new Intent(getActivity(), TaskDetailActivity.class);
                detailTask.putExtra("task_id", taID.getText().toString());
                //Log.d("idParsing", idParsing);
                detailTask.putExtra("idParsing", idParsing);
                //detailTask.putExtra("epoch", epoch.getText().toString());
                startActivity(detailTask);
                //Toast.makeText(getActivity(), taID.getText().toString(),Toast.LENGTH_LONG).show();

            }
        });

        btn_expired.setOnClickListener(new View.OnClickListener() {
            String a = Long.toString(tode());

            @Override
            public void onClick(View v) {

                if (hashMap.get(btn_expired) == R.drawable.btn_expire_inactive) {
                    btn_expired.setBackgroundResource(R.drawable.btn_expire_active);
                    hashMap.put(btn_expired, R.drawable.btn_expire_active);
                    btn_willexpired.setBackgroundResource(R.drawable.btn_willexpire_inactive);
                    hashMap.put(btn_willexpired, R.drawable.btn_willexpire_inactive);
                    btn_months.setBackgroundResource(R.drawable.btn_6months_inactive);
                    hashMap.put(btn_months, R.drawable.btn_6months_inactive);

                    //taskAdapter.filterRed("");
                    //taskAdapter = new TaskAdapter(getActivity(), taskArray);
                    taskAdapter.filterRed(a);
                    //listTask.setAdapter(taskAdapter);
                } else {
                    btn_expired.setBackgroundResource(R.drawable.btn_expire_inactive);
                    hashMap.put(btn_expired, R.drawable.btn_expire_inactive);

                    taskAdapter.filterRed("");

                }
            }
        });

        btn_willexpired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = Long.toString(tode());
                if (hashMap.get(btn_willexpired) == R.drawable.btn_willexpire_inactive) {
                    btn_willexpired.setBackgroundResource(R.drawable.btn_willexpire_active);
                    hashMap.put(btn_willexpired, R.drawable.btn_willexpire_active);
                    btn_expired.setBackgroundResource(R.drawable.btn_expire_inactive);
                    hashMap.put(btn_expired, R.drawable.btn_expire_inactive);
                    btn_months.setBackgroundResource(R.drawable.btn_6months_inactive);
                    hashMap.put(btn_months, R.drawable.btn_6months_inactive);


                    //taskAdapter = new TaskAdapter(getActivity(), taskArray);
                    taskAdapter.filterOrange(a);
                    //listTask.setAdapter(taskAdapter);
                } else {
                    btn_willexpired.setBackgroundResource(R.drawable.btn_willexpire_inactive);
                    hashMap.put(btn_willexpired, R.drawable.btn_willexpire_inactive);
                    taskAdapter.filterOrange("");
                }
            }
        });

        btn_months.setOnClickListener(new View.OnClickListener() {
            long nn = 2592000;
            String a = Long.toString(tode() + nn);

            @Override
            public void onClick(View v) {
                if (hashMap.get(btn_months) == R.drawable.btn_6months_inactive) {
                    btn_months.setBackgroundResource(R.drawable.btn_6months_active);
                    hashMap.put(btn_months, R.drawable.btn_6months_active);
                    btn_expired.setBackgroundResource(R.drawable.btn_expire_inactive);
                    hashMap.put(btn_expired, R.drawable.btn_expire_inactive);
                    btn_willexpired.setBackgroundResource(R.drawable.btn_willexpire_inactive);
                    hashMap.put(btn_willexpired, R.drawable.btn_willexpire_inactive);
                    //new DataFetcherTask().execute();
                    taskAdapter.filterGreen(a);
                    //listTask.setAdapter(taskAdapter);

                } else {
                    btn_months.setBackgroundResource(R.drawable.btn_6months_inactive);
                    hashMap.put(btn_months, R.drawable.btn_6months_inactive);

                    taskAdapter.filterGreen("");
                }
            }
        });

        /*subject = new String[]{"OTS ke Pabrik", "Transaksi tidak di Mandiri", "Saldo untuk AGF kurang",
                                "LK 2015 Audited", "New Task", "New Event", "New Auditions"};

        pt = new String[]{"PT. MASPINA", "PT. SampamaTbk", "PT. MASPINA", "PT. TELEMA Tbk",
                "PT. Owner", "PT. Founder", "PT. Industries"};

        tgl = new String[]{"Expire 30/10/2015 07:30","Expire 29/11/2015 07:30","Expire 30/10/2015 07:30",
                "Expire 23/02/2016 07:30","Expire 30/10/2016 07:30","Expire 30/10/2017 08:30","Expire 30/10/2018 07:30"};

        image = new Integer[]{R.drawable.point_red, R.drawable.point_orange, R.drawable.point_green, R.drawable.point_blue,
                R.drawable.point_blue, R.drawable.point_blue, R.drawable.point_blue};

        mylist = new ArrayList<HashMap<String, String>>();
        for (int i=0; i<subject.length;i++){
            hashMap = new HashMap<String, String>();
            hashMap.put("subject", subject[i]);
            hashMap.put("pt", pt[i]);
            hashMap.put("tgl", tgl[i]);
            hashMap.put("image", Integer.toString(image[i]));
            mylist.add(hashMap);
        }

        adapter = new SimpleAdapter(getActivity(), mylist, R.layout.list_row_task,
                new String[]{"subject", "pt", "tgl", "image"}, new int[]{R.id.txt_list_task_subject,
                R.id.txt_list_task_pt, R.id.txt_list_task_tgl, R.id.img_list_task});
        listTask.setAdapter(adapter);*/
    }

    private long tode() {
        String str = dateNow();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date2 = null;
        try {
            date2 = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date2.getTime() / 1000;
    }


    public void epochtodate(int epoch) {
        Date date = new Date(epoch * 1000L);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        formatDate = format.format(date);
    }

    private String dateNow() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm", Locale.getDefault());
        Date date1 = new Date();
        return dateFormat.format(date1);
    }

    public class DataFetcherTask extends AsyncTask<Void, Void, Void> {

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
                taskArray = new ArrayList<Task>();
                JSONArray jsonArray = new JSONArray(DataManager.MyHttpGet(urlTask+idParsing));
                //Log.d("taskasd", jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    strId = jsonObject.getInt("task_id");
                    strTitle = jsonObject.getString(title);
                    strExpire = jsonObject.getInt("expire");
                    //strExpire = tgl[i];
                    strNote = jsonObject.getString(note);
                    strCompany = jsonObject.getString(company);


                    Task task = new Task();
                    //if (strExpire<tode()){

                      //  if (strExpire>)

                        task.setTask_id(strId);
                        task.setTitle(strTitle);
                        task.setExpire(strExpire);
                        task.setNote(strNote);
                        task.setCompany(strCompany);
                        taskArray.add(task);
                    /*}else {
                        task.setTask_id(strId);
                        task.setTitle(strTitle);
                        task.setExpire(strExpire);
                        task.setNote(strNote);
                        task.setCompany(strCompany);
                        taskArray.add(task);
                    }*/


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            taskAdapter = new TaskAdapter(getActivity(), taskArray);
            //String day = String.valueOf(datenowwwwww());
            //taskAdapter.filter(day);
            listTask.setAdapter(taskAdapter);

        }
    }
}

