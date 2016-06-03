package com.floo.lenteramandiri.data;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.floo.lenteramandiri.R;

/**
 * Created by Floo on 5/9/2016.
 */
public class NoteActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView titleToolbar, save, txtisiNote;
    String isiNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initView();
    }

    public void initView(){
        Intent i = getIntent();
        isiNote = i.getStringExtra("note");

        toolbar = (Toolbar)findViewById(R.id.id_toolbar);
        titleToolbar = (TextView)toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("NOTE");
        save = (TextView)findViewById(R.id.txt_save);
        save.setVisibility(View.GONE);
        LinearLayout line = (LinearLayout) findViewById(R.id.linier_toolbar);

        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteActivity.this.finish();
            }
        });

        txtisiNote = (TextView)findViewById(R.id.txt_note);
        txtisiNote.setText(isiNote);


    }
}
