package floo.com.mpm_mandiri.data;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import floo.com.mpm_mandiri.R;

/**
 * Created by Floo on 3/11/2016.
 */
public class ConvenantActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout line;
    TextView titleToolbar, save, txtConvenant, txtCompany;
    String pConvenant, pCompany;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convenant);
        initView();
    }
    public void initView(){
        Intent i = getIntent();
        pCompany  = i.getStringExtra("company_name");
        pConvenant = i.getStringExtra("covenant");

        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        titleToolbar = (TextView)toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("CONVENANT");
        save = (TextView)findViewById(R.id.txt_save);
        line = (LinearLayout) findViewById(R.id.linier_toolbar);

        txtConvenant = (TextView)findViewById(R.id.txt_convenant_isi);
        txtCompany = (TextView)findViewById(R.id.txt_convenant_company);
        txtConvenant.setText(pConvenant);
        txtCompany.setText(pCompany);

        save.setVisibility(View.INVISIBLE);
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConvenantActivity.this.finish();
            }
        });

    }
}
