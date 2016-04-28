package floo.com.mpm_mandiri.fragment;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import floo.com.mpm_mandiri.MainActivity;
import floo.com.mpm_mandiri.data.DetailTaskActivity;
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

    private ProgressDialog pDialog;
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


        listTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView taID = (TextView) view.findViewById(R.id.txt_list_task_id);
                Intent detailTask = new Intent(getActivity(), DetailTaskActivity.class);
                detailTask.putExtra("task_id", taID.getText().toString());
                startActivity(detailTask);
                //Toast.makeText(getActivity(), taID.getText().toString(),Toast.LENGTH_LONG).show();

            }
        });
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
            HttpConnectionParams.setSoTimeout(myParams, 5000);

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
            HttpGet httpGet = new HttpGet(urlTask + idParsing);
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("X-Header_access_key", access_key);
            httpGet.setHeader("Accept-Language", "en-us");
            httpGet.setHeader("X-Timezone", "Asia/Jakarta");
            Log.d("urllist", urlTask+idParsing);
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                serverData = EntityUtils.toString(httpEntity);
                Log.d("data", serverData);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("UrlData", url);
            Log.d("IDParsing", idParsing);
            String coba = "";
            try {
                taskArray = new ArrayList<Task>();
                JSONArray jsonArray = new JSONArray(serverData);
                Log.d("taskasd", jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    strId = jsonObject.getInt("task_id");
                    strTitle = jsonObject.getString(title);
                    strExpire = jsonObject.getInt("expire");
                    strNote = jsonObject.getString(note);
                    strCompany = jsonObject.getString(company);

                    epochtodate(strExpire);

                    Task task = new Task();
                    task.setTask_id(strId);
                    task.setTitle(strTitle);
                    task.setExpire(strExpire);
                    task.setNote(strNote);
                    task.setCompany(strCompany);
                    taskArray.add(task);


                    // tmp hashmap for single contact
                    //HashMap<String, String> hashmapTask = new HashMap<String, String>();
                    //hashmapTask.put("task_id", Integer.toString(strId));
                    //hashmapTask.put(company, strCompany);
                    //hashmapTask.put(title, strTitle);
                    //hashmapTask.put("expire", formatDate);


                    //arraylistTask.add(hashmapTask);

                    coba = title;
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

            //adapter = new SimpleAdapter(getActivity(), arraylistTask,
            //        R.layout.list_row_task,
            //        new String[]{company, title, "expire", "task_id"},
            //        new int[]{R.id.txt_list_task_pt, R.id.txt_list_task_subject, R.id.txt_list_task_tgl, R.id.txt_list_task_id});
            taskAdapter = new TaskAdapter(getActivity(), taskArray);
            listTask.setAdapter(taskAdapter);

            //expired(strExpire);
            //Log.d("sekarang", expired(strExpire));

        }
    }

}

