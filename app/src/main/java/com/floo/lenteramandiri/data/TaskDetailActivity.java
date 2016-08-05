package com.floo.lenteramandiri.data;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import dmax.dialog.SpotsDialog;

import com.floo.lenteramandiri.MainActivity;
import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.adapter.Escalateds;
import com.floo.lenteramandiri.adapter.TaskDetailAdapter;
import com.floo.lenteramandiri.adapter.TaskDetailFromAdapter;
import com.floo.lenteramandiri.utils.DataManager;
import com.floo.lenteramandiri.utils.DialogMediaActivity;
import com.floo.lenteramandiri.utils.DialogUniversalWarningUtils;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

/**
 * Created by Floo on 2/25/2016.
 */
public class TaskDetailActivity extends AppCompatActivity {
    String url = DataManager.url;
    String urlDetailTask = DataManager.urltaskDetails;
    String idTaskParsing, struserid, strepoch,strTitle,  strNote, strCompany,strDetail,
            strDetailDesc, formatDate, strPosition;
    int strExpire, strid, strDetailTaskid;
    TextView txtSubject, txtPt, txtTgl, txtID, txtEscalated, txtEscalatedTo;
    Toolbar toolbar;
    LinearLayout line;
    TextView titleToolbar, save;
    Button btnNote, btnReport, btnDone;
    ImageView img_list_task;
    private static final String taskid = "taskid";
    private static final String description = "description";
    private static final String title = "title";
    private static final String note = "note";
    private static final String company = "company";
    private static final String escalated_from = "escalated_from";
    private static final String escalated_to = "escalated_to";
    private static final String Escalated = "Escalated";
    private static final String report = "report";
    private static final String user_id = "user_id";
    private static final String esc_position = "esc_position";
    private SpotsDialog pDialog;
    HashMap<String, String> hashmapfromTaskList;
    HashMap<String, String> hashMaptoTaskList;
    ArrayList<HashMap<String, String>> arrayfromTaskList;
    ArrayList<HashMap<String, String>> arraytoTaskList;
    ExpandableHeightListView listDetailTaskList, listEscalatedTo;
    //SimpleAdapter adapterDetailTaskList;

    public static final String status_code = "status_code";
    public static final String message = "message";
    String urlDone = DataManager.urltaskDone;
    String strStatus, strMessage;

    TaskDetailAdapter adapter;
    ArrayList<Escalateds> arrayListTo;

    private static String[] titles = new String[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list_task);
        initView();
        arrayfromTaskList = new ArrayList<HashMap<String, String>>();
        arraytoTaskList = new ArrayList<HashMap<String, String>>();
        arrayListTo = new ArrayList<Escalateds>();
        new DataFetcherDetailTask().execute();
    }

    public void initView(){
        Intent i = getIntent();
        idTaskParsing  = i.getStringExtra("task_id");
        struserid = i.getStringExtra("idParsing");
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        titleToolbar = (TextView)toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("DETIL TUGAS");
        save = (TextView)findViewById(R.id.txt_save);
        txtSubject = (TextView) findViewById(R.id.txt_detail_task_subject);
        txtPt = (TextView) findViewById(R.id.txt_detail_task_pt);
        txtTgl = (TextView) findViewById(R.id.txt_detail_task_tgl);
        txtID = (TextView) findViewById(R.id.txt_detail_task_id);
        img_list_task = (ImageView)findViewById(R.id.img_list_task);
        txtEscalated = (TextView)findViewById(R.id.txt_escalated_taskdetail);
        txtEscalatedTo = (TextView)findViewById(R.id.txt_escalated_taskdetail1);
        listDetailTaskList = (ExpandableHeightListView)findViewById(R.id.list_detail_task);
        listEscalatedTo = (ExpandableHeightListView)findViewById(R.id.list_detail_task1);
        listDetailTaskList.setEnabled(false);
        listEscalatedTo.setEnabled(false);

        btnNote = (Button)findViewById(R.id.btn_detail_task_note);
        btnDone = (Button) findViewById(R.id.btn_detail_task_done);
        save.setVisibility(View.INVISIBLE);
        line = (LinearLayout) findViewById(R.id.linier_toolbar);

        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskDetailActivity.this.finish();
            }
        });

        btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextNote = new Intent(TaskDetailActivity.this, NoteActivity.class);
                nextNote.putExtra("taskid", idTaskParsing);
                nextNote.putExtra(note, strNote);
                nextNote.putExtra("idParsing", struserid);
                finish();
                startActivity(nextNote);
            }
        });



        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMediaActivity dialog = new DialogMediaActivity(TaskDetailActivity.this, idTaskParsing, struserid);
                dialog.showDialog();
                //String report = "";
                //new DoneAsync(idTaskParsing, struserid, report).execute();

            }
        });
    }

    public long epoch(String str){
        long today;
        long epoch = 2592000;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date2 = null;
        try {
            date2 = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        today = date2.getTime()/1000;

        return today;
    }

    private String dateNow(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm", Locale.getDefault());
        Date date1 = new Date();
        return dateFormat.format(date1);
    }

    public void epochtodate(int epoch){
        Date date = new Date(epoch * 1000L);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        formatDate = format.format(date);
    }

    private class DataFetcherDetailTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new SpotsDialog(TaskDetailActivity.this, R.style.CustomProgress);
            pDialog.setMessage("Please wait...!!!");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String key = "";
            try {
                String coba = "http://sandbox.floostudio.com/lenteramandiri/api/v1/tasks/detail/66?user_id=63";
                JSONObject jsonObject = new JSONObject(DataManager.MyHttpGet(urlDetailTask+idTaskParsing+"?user_id="+struserid));
                strid = jsonObject.getInt("task_id");
                strTitle = jsonObject.getString(title);
                strExpire = jsonObject.getInt("expire");
                strNote = jsonObject.getString(note);
                strPosition = jsonObject.getString(esc_position);
                strCompany = jsonObject.getString(company);

                epochtodate(strExpire);

                JSONArray arrayEscalfrom = jsonObject.getJSONArray(escalated_from);
                for (int i=0; i<arrayEscalfrom.length();i++){
                    String strEscalated = arrayEscalfrom.getString(i);

                    //new DataFetcherTask(text.substring(0, index), txt.getText().toString()).execute();

                    if (strEscalated.indexOf(";")>0){
                        String[] items = strEscalated.split(";");
                        for (int s=0;s<items.length;s++){
                            String item = items[s];
                            if (s==1){
                                hashmapfromTaskList = new HashMap<String, String>();
                                hashmapfromTaskList.put("note", String.valueOf(1));
                                hashmapfromTaskList.put(escalated_from, item);
                                arrayfromTaskList.add(hashmapfromTaskList);
                            }else {
                                hashmapfromTaskList = new HashMap<String, String>();
                                hashmapfromTaskList.put("note", String.valueOf(0));
                                hashmapfromTaskList.put(escalated_from, item);
                                arrayfromTaskList.add(hashmapfromTaskList);
                            }

                        }
                    }else {
                        hashmapfromTaskList = new HashMap<String, String>();
                        hashmapfromTaskList.put("note", String.valueOf(0));
                        hashmapfromTaskList.put(escalated_from, strEscalated);
                        arrayfromTaskList.add(hashmapfromTaskList);
                    }
                }

                ArrayList<String> array = new ArrayList<String>();
                JSONObject objEscalTo = jsonObject.getJSONObject(escalated_to);
                for (int a=0; a<objEscalTo.length();a++) {
                    array.add(objEscalTo.names().getString(a));
                }
                Collections.sort(array, new Comparator<String>() {
                    @Override
                    public int compare(String lhs, String rhs) {
                        return lhs.compareToIgnoreCase(rhs);
                    }
                });

                for (int b=0; b<array.size();b++){
                    String data1=array.get(b);

                    Escalateds escall = new Escalateds();
                    escall.setEscalate(data1);
                    if (!data1.trim().equals("Eskalasi"+ " " + strPosition)) {

                        String ada = "1";

                        escall.setBold(ada);
                        arrayListTo.add(escall);

                        JSONArray arrayEscal = objEscalTo.getJSONArray(data1);
                        if (arrayEscal.length() > 0) {
                            for (int c = 0; c < arrayEscal.length(); c++) {

                                String data = arrayEscal.getString(c);

                                Escalateds escalateds = new Escalateds();
                                escalateds.setEscalate(data);
                                escalateds.setBold(ada);

                                arrayListTo.add(escalateds);
                            }
                        }
                    } else {
                        String tdk = "0";

                        escall.setBold(tdk);
                        arrayListTo.add(escall);

                        JSONArray arrayEscal = objEscalTo.getJSONArray(data1);
                        if (arrayEscal.length() > 0) {
                            for (int c = 0; c < arrayEscal.length(); c++) {

                                String data = arrayEscal.getString(c);

                                Escalateds escalateds = new Escalateds();
                                escalateds.setEscalate(data);
                                escalateds.setBold(tdk);

                                arrayListTo.add(escalateds);
                            }

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
            if (pDialog.isShowing())
                pDialog.dismiss();
            String id = Integer.toString(strid);
            txtSubject.setText(strTitle);
            txtPt.setText(strCompany);
            txtTgl.setText(formatDate);
            txtID.setText(id);

            long today;
            long epoch = 2592000;

            String str = dateNow();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date2 = null;
            try {
                date2 = df.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            today = date2.getTime()/1000;

            if (strExpire < today) {
                img_list_task.setImageResource(R.drawable.point_red);

            }else if (strExpire >= (today+epoch)){
                img_list_task.setImageResource(R.drawable.point_green);

            }else {
                img_list_task.setImageResource(R.drawable.point_orange);

            }
            if (arrayListTo.isEmpty() && arrayfromTaskList.isEmpty()){
                txtEscalated.setText("");
                txtEscalatedTo.setText("");
                txtEscalated.setVisibility(View.GONE);
                txtEscalatedTo.setVisibility(View.GONE);
                listEscalatedTo.setVisibility(View.GONE);
                listDetailTaskList.setVisibility(View.GONE);
            }else if (!arrayListTo.isEmpty() && !arrayfromTaskList.isEmpty()){
                txtEscalated.setVisibility(View.VISIBLE);
                txtEscalatedTo.setVisibility(View.VISIBLE);
                listEscalatedTo.setVisibility(View.VISIBLE);
                listDetailTaskList.setVisibility(View.VISIBLE);

                txtEscalatedTo.setText("Eskalasi Ke :");
                adapter = new TaskDetailAdapter(getApplicationContext(), arrayListTo);
                listEscalatedTo.setAdapter(adapter);
                listEscalatedTo.setExpanded(true);

                txtEscalated.setText("Eskalasi Dari :");
                TaskDetailFromAdapter adapterDetailTaskList = new TaskDetailFromAdapter(getApplicationContext(), arrayfromTaskList);
                listDetailTaskList.setAdapter(adapterDetailTaskList);
                listDetailTaskList.setExpanded(true);
            }else {
                if (arrayfromTaskList.isEmpty()){
                    txtEscalated.setVisibility(View.GONE);
                    listDetailTaskList.setVisibility(View.GONE);
                    txtEscalatedTo.setVisibility(View.VISIBLE);
                    listEscalatedTo.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, 0, 0);
                    txtEscalatedTo.setLayoutParams(params);
                    txtEscalatedTo.setText("Eskalasi Ke :");
                    adapter = new TaskDetailAdapter(getApplicationContext(), arrayListTo);
                    listEscalatedTo.setAdapter(adapter);
                    listEscalatedTo.setExpanded(true);

                }else {
                    txtEscalatedTo.setVisibility(View.GONE);
                    listEscalatedTo.setVisibility(View.GONE);
                    txtEscalated.setVisibility(View.VISIBLE);
                    listDetailTaskList.setVisibility(View.VISIBLE);
                    txtEscalated.setText("Eskalasi Dari :");
                    TaskDetailFromAdapter adapterDetailTaskList = new TaskDetailFromAdapter(getApplicationContext(), arrayfromTaskList);
                    listDetailTaskList.setAdapter(adapterDetailTaskList);
                    listDetailTaskList.setExpanded(true);
                }
            }
        }
    }

    class DoneAsync extends AsyncTask<Void, Void, Void> {

        private String idTaskParsing;
        private String userid;
        private String strReport;


        public DoneAsync(String idTaskParsing, String userid, String strReport) {
            this.idTaskParsing = idTaskParsing;
            this.userid = userid;
            this.strReport = strReport;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            JSONObject objReport = new JSONObject();
            String objstrReport = "";

            try {
                objReport.put(user_id, userid);
                objReport.put(report, strReport);
                objstrReport = objReport.toString();

                JSONObject jsonObject = new JSONObject(DataManager.MyHttpPut(urlDone+idTaskParsing, objstrReport));
                Log.d("json", jsonObject.toString());
                strStatus = jsonObject.getString(status_code);
                Log.d("detail", strStatus);
                strMessage = jsonObject.getString(message);


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (strStatus.trim().equals("200")){
                Toast.makeText(getApplicationContext(), strMessage, Toast.LENGTH_LONG).show();
                Intent back=new Intent(getApplicationContext(), MainActivity.class);
                back.putExtra("fragment", "fragment");
                finish();
                startActivity(back);

            }else {
                Toast.makeText(getApplicationContext(), strMessage, Toast.LENGTH_LONG).show();
            }

        }
    }
}
