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

import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.utils.DataManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Floo on 5/9/2016.
 */
public class NoteActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView titleToolbar, save;
    String isiNote;
    EditText txtisiNote;
    String strNote, taskid, strStatus, strMessage, struserid;
    private static final String taskidd = "taskid";
    private static final String note = "note";
    public static final String status_code = "status_code";
    public static final String message = "message";
    String urlNote = DataManager.urltaskNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initView();
    }

    public void initView(){
        Intent i = getIntent();
        taskid = i.getStringExtra(taskidd);
        isiNote = i.getStringExtra("note");
        struserid = i.getStringExtra("idParsing");
        toolbar = (Toolbar)findViewById(R.id.id_toolbar);
        titleToolbar = (TextView)toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("NOTE");
        save = (TextView)findViewById(R.id.txt_save);
        LinearLayout line = (LinearLayout) findViewById(R.id.linier_toolbar);

        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), TaskDetailActivity.class);
                intent.putExtra("task_id", taskid);
                intent.putExtra("idParsing", struserid);
                finish();
                startActivity(intent);
            }
        });

        txtisiNote = (EditText)findViewById(R.id.txt_note);
        txtisiNote.setText(isiNote);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NoteActivity.this);

                // set title
                alertDialogBuilder.setTitle("Pesan");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Apakah anda ingin menyimpan note ini?")
                        .setCancelable(false)
                        .setPositiveButton("Simpan",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                strNote = txtisiNote.getText().toString();
                                new NoteAsync().execute();

                            }
                        })
                        .setNegativeButton("Batal",new DialogInterface.OnClickListener() {
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
                Intent intent = new Intent(getApplicationContext(), TaskDetailActivity.class);
                intent.putExtra("task_id", taskid);
                intent.putExtra("idParsing", struserid);
                finish();
                startActivity(intent);
            }else {
                Toast.makeText(getApplicationContext(), strMessage, Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), TaskDetailActivity.class);
        intent.putExtra("task_id", taskid);
        intent.putExtra("idParsing", struserid);
        finish();
        startActivity(intent);
    }
}
