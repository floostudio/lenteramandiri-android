package floo.com.mpm_mandiri.data;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import floo.com.mpm_mandiri.R;
import floo.com.mpm_mandiri.adapter.CalendarListAdapter;
import floo.com.mpm_mandiri.adapter.Task;
import floo.com.mpm_mandiri.utils.DataManager;

/**
 * Created by Floo on 2/23/2016.
 */
public class CalendarTaskListActivity extends AppCompatActivity {

    ListView listTask;

    private ProgressDialog pDialog;
    String url = DataManager.url;
    String urlTask = DataManager.urltaskList;
    String idParsing, expireParsing, strTitle, strNote, strCompany;

    ArrayList<Task> taskArray;
    CalendarListAdapter taskAdapter;

    //public static long today;
    public static String dateNow;

    private static int strId, strExpire;

    private static final String title = "title";
    private static final String note = "note";
    private static final String company = "company";
    CharSequence ss;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_tasklist);
        initView();

        new DataFetcherTask().execute();
        listTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView taID = (TextView)view.findViewById(R.id.txt_list_task_id);
                Intent detailTask = new Intent(CalendarTaskListActivity.this, DetailTaskActivity.class);
                detailTask.putExtra("task_id", taID.getText().toString());
                startActivity(detailTask);
            }
        });



    }

    public void initView(){
        Intent i = getIntent();
        idParsing  = i.getStringExtra("IDPARSING");
        //expireParsing = i.getStringExtra("date");
        ss = i.getCharSequenceExtra("date");

        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        TextView titleToolbar = (TextView)toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("TASK DETAIL");
        TextView save = (TextView)findViewById(R.id.txt_save);
        save.setVisibility(View.INVISIBLE);
        LinearLayout line = (LinearLayout) findViewById(R.id.linier_toolbar);
        listTask = (ListView) findViewById(R.id.listCalendar_Tasklist);

        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarTaskListActivity.this.finish();
            }
        });



    }

    private class DataFetcherTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CalendarTaskListActivity.this);
            pDialog.setMessage("Please wait...!!!");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                taskArray = new ArrayList<Task>();
                JSONArray jsonArray = new JSONArray(DataManager.MyHttpGet(urlTask+idParsing));
                //Log.e("task", jsonArray.toString());
                for (int i=0; i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    strId = jsonObject.getInt("task_id");
                    strTitle = jsonObject.getString(title);
                    strExpire = jsonObject.getInt("expire");
                    strNote = jsonObject.getString(note);
                    strCompany = jsonObject.getString(company);


                    Task task = new Task();
                    task.setTask_id(strId);
                    task.setTitle(strTitle);
                    task.setExpire(strExpire);
                    task.setNote(strNote);
                    task.setCompany(strCompany);
                    taskArray.add(task);


                }


            }catch (JSONException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            taskAdapter = new CalendarListAdapter(CalendarTaskListActivity.this, taskArray);
            taskAdapter.filter(ss.toString());
            listTask.setAdapter(taskAdapter);


        }
    }
}

