package com.floo.lenteramandiri.data;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.floo.lenteramandiri.utils.DataManager;

import org.json.JSONException;
import org.json.JSONObject;

import com.floo.lenteramandiri.R;

/**
 * Created by Floo on 2/26/2016.
 */
public class ReportActivity extends AppCompatActivity {
    String url = DataManager.url;
    String urlNote = DataManager.urltaskNote;
    Toolbar toolbar;
    TextView titleToolbar, save;
    String isiNote, strNote, taskid, strStatus, strMessage;
    private static final String taskidd = "taskid";
    private static final String note = "note";
    public static final String status_code = "status_code";
    public static final String message = "message";

    EditText edt_add_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initView();
    }

    public void initView(){
        Intent i = getIntent();
        isiNote = i.getStringExtra(note);
        taskid = i.getStringExtra(taskidd);
        toolbar = (Toolbar)findViewById(R.id.id_toolbar);
        titleToolbar = (TextView)toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("REPORT");
        save = (TextView)findViewById(R.id.txt_save);
        edt_add_report = (EditText)findViewById(R.id.edt_add_report);
        edt_add_report.setText(isiNote);
        LinearLayout line = (LinearLayout) findViewById(R.id.linier_toolbar);

        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportActivity.this.finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportActivity.this);

                // set title
                alertDialogBuilder.setTitle("Message");

                // set dialog message
                alertDialogBuilder
                        .setMessage("If you believe this text click save, otherwise click cancel???")
                        .setCancelable(false)
                        .setPositiveButton("Save",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                strNote = edt_add_report.getText().toString();
                                new NoteAsync().execute();

                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });
    }

    class NoteAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String putNote = "";
            JSONObject objNote = new JSONObject();

            try {
                objNote.put(note, strNote);
                putNote = objNote.toString();

                JSONObject jsonObject = new JSONObject(DataManager.MyHttpPut(urlNote+taskid, putNote));
                strStatus = jsonObject.getString(status_code);
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
                ReportActivity.this.finish();
            }else {
                Toast.makeText(getApplicationContext(), strMessage, Toast.LENGTH_LONG).show();
            }

        }
    }
}
