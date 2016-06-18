package com.floo.lenteramandiri.data;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.floo.lenteramandiri.R;

/**
 * Created by Floo on 3/11/2016.
 */
public class PortoAccountDetailActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout line;
    TextView titleToolbar, save, txtCif, txtAccNumber, txtValuta, txtSaldo, txtLimit, txtTunggakan,
            txtKolektibilitas, txtJmlhTempo, txtDebet, txtKredit, txtRata, txtCompany;
    String pCif, pAccNumber, pValuta, pSaldo, pLimit, pTunggakan, pKolektibilitas,
            pJmlhTempo, pDebet, pKredit, pRata, pCompany, pId;
    Button convenant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portofolio_account_detail);
        initView();
    }
    public void initView(){
        Intent i = getIntent();
        pCif = i.getStringExtra(PortoAccountActivity.cif);
        pId = i.getStringExtra(PortoAccountActivity.id);
        pAccNumber  = i.getStringExtra(PortoAccountActivity.acc_num);
        pValuta = i.getStringExtra(PortoAccountActivity.valuta);
        pSaldo  = i.getStringExtra(PortoAccountActivity.saldo);
        pLimit = i.getStringExtra(PortoAccountActivity.limit);
        pTunggakan  = i.getStringExtra(PortoAccountActivity.tunggakan);
        pKolektibilitas = i.getStringExtra(PortoAccountActivity.kolektibilitas);
        pJmlhTempo  = i.getStringExtra(PortoAccountActivity.jatuh_tempo);
        pDebet = i.getStringExtra(PortoAccountActivity.trans_debet);
        pKredit  = i.getStringExtra(PortoAccountActivity.trans_kredit);
        pRata = i.getStringExtra(PortoAccountActivity.saldo_rata);
        pCompany  = i.getStringExtra(PortoAccountActivity.company_name);
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        titleToolbar = (TextView)toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("PORTFOLIO ACCOUNT");
        save = (TextView)findViewById(R.id.txt_save);
        line = (LinearLayout) findViewById(R.id.linier_toolbar);
        txtCif = (TextView)findViewById(R.id.txt_account_detail_cif);
        txtAccNumber = (TextView)findViewById(R.id.txt_account_detail_acc);
        txtValuta = (TextView) findViewById(R.id.txt_account_detail_valuta);
        txtSaldo = (TextView)findViewById(R.id.txt_account_detail_saldo);
        txtLimit = (TextView) findViewById(R.id.txt_account_detail_limit);
        txtTunggakan = (TextView)findViewById(R.id.txt_account_detail_tunggakan);
        txtKolektibilitas = (TextView) findViewById(R.id.txt_account_detail_kolektibilas);
        txtJmlhTempo = (TextView)findViewById(R.id.txt_account_detail_jmlhTempo);
        txtDebet = (TextView) findViewById(R.id.txt_account_detail_debet);
        txtKredit = (TextView)findViewById(R.id.txt_account_detail_kredit);
        txtRata = (TextView) findViewById(R.id.txt_account_detail_rata_rata);
        txtCompany = (TextView)findViewById(R.id.txt_account_detail_company);
        txtCif.setText(pCif);
        txtAccNumber.setText(pAccNumber);
        txtValuta.setText(pValuta);
        txtSaldo.setText(pSaldo);
        txtLimit.setText(pLimit);
        txtTunggakan.setText(pTunggakan);
        txtKolektibilitas.setText(pKolektibilitas);
        txtJmlhTempo.setText(pJmlhTempo);
        txtDebet.setText(pDebet);
        txtKredit.setText(pKredit);
        txtRata.setText(pRata);
        txtCompany.setText(pCompany);

        save.setVisibility(View.INVISIBLE);
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PortoAccountDetailActivity.this.finish();
            }
        });

        convenant = (Button)findViewById(R.id.btn_account_detail_convnant);
        convenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent conv = new Intent(PortoAccountDetailActivity.this, ConvenantActivity.class);
                conv.putExtra("id", pId.toString());
                conv.putExtra("acc_number", pAccNumber.toString());
                conv.putExtra("company_name", txtCompany.getText().toString());
                startActivity(conv);
            }
        });

    }
}
