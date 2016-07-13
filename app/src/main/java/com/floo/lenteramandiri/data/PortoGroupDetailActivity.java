package com.floo.lenteramandiri.data;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.StringTokenizer;

import com.floo.lenteramandiri.R;

/**
 * Created by Floo on 3/11/2016.
 */
public class PortoGroupDetailActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout line;
    TextView titleToolbar, save, txtCif, txtGroupId, txtGroupLimit, txtGroupBalance, txtAccAmount,
            txtFacilityAmount, txtFee, txtBunga, txtUtilisasi, txtCompany;
    String pCif, pGroupId, pGroupLimit, pGroupBalance, pAccAmount,
            pFacilityAmount, pFee, pBunga, pUtilisasi, pCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portofolio_group_detail);
        initView();
    }
    public void initView(){
        Intent i = getIntent();
        pCif = i.getStringExtra(PortoGroupActivity.cif);
        pGroupId  = i.getStringExtra(PortoGroupActivity.group_id);
        pAccAmount = i.getStringExtra(PortoGroupActivity.acc_num);
        pGroupLimit = i.getStringExtra(PortoGroupActivity.group_limit);
        pGroupBalance  = i.getStringExtra(PortoGroupActivity.group_balance);
        pFacilityAmount  = i.getStringExtra(PortoGroupActivity.type_facility);
        pFee = i.getStringExtra(PortoGroupActivity.fee);
        pBunga  = i.getStringExtra(PortoGroupActivity.bunga);
        pUtilisasi = i.getStringExtra(PortoGroupActivity.utilisasi);
        pCompany  = i.getStringExtra(PortoGroupActivity.company_name);
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        titleToolbar = (TextView)toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("PORTFOLIO DEBITUR");
        save = (TextView)findViewById(R.id.txt_save);
        line = (LinearLayout) findViewById(R.id.linier_toolbar);
        txtCif = (TextView)findViewById(R.id.txt_group_detail_cif);
        txtGroupId = (TextView)findViewById(R.id.txt_group_detail_id);
        txtGroupLimit = (TextView) findViewById(R.id.txt_group_detail_limit);
        txtGroupBalance = (TextView)findViewById(R.id.txt_group_detail_balance);
        txtAccAmount = (TextView) findViewById(R.id.txt_group_detail_account);
        txtFacilityAmount = (TextView)findViewById(R.id.txt_group_detail_facility);
        txtFee = (TextView) findViewById(R.id.txt_group_detail_fee);
        txtBunga = (TextView)findViewById(R.id.txt_group_detail_bunga);
        txtUtilisasi = (TextView) findViewById(R.id.txt_group_detail_utilisasi);
        txtCompany = (TextView)findViewById(R.id.txt_group_detail_company);
        txtCif.setText(pCif);
        txtGroupId.setText(pGroupId);
        txtGroupLimit.setText("IDR "+pGroupLimit+"d");
        txtGroupBalance.setText(pGroupBalance);
        txtAccAmount.setText(pAccAmount);
        txtFacilityAmount.setText(pFacilityAmount);
        txtFee.setText(pFee);
        txtBunga.setText(pBunga);
        txtUtilisasi.setText(pUtilisasi+"%");
        txtCompany.setText(pCompany);
        save.setVisibility(View.INVISIBLE);
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PortoGroupDetailActivity.this.finish();
            }
        });

    }

    private String getDecimalFormat(String value) {
        StringTokenizer lst = new StringTokenizer(value, ".");
        String str1 = value;
        String str2 = "";
        if (lst.countTokens() > 1) {
            str1 = lst.nextToken();
            str2 = lst.nextToken();
        }
        String str3 = "";
        int i = 0;
        int j = -1 + str1.length();
        if (str1.charAt(-1 + str1.length()) == '.') {
            j--;
            str3 = ".";
        }
        for (int k = j; ; k--) {
            if (k < 0) {
                if (str2.length() > 0)
                    str3 = str3 + "." + str2;
                return str3;
            }
            if (i == 3) {
                str3 = "." + str3;
                i = 0;
            }
            str3 = str1.charAt(k) + str3;
            i++;
        }
    }

    private String Spacer(String number){
        StringBuilder strB = new StringBuilder();
        strB.append(number);
        int Three = 0;

        for(int i=number.length();i>0;i--){
            Three++;
            if(Three == 3){
                strB.insert(i-1, ".");
                Three = 0;
            }
        }
        return strB.toString();
    }
}
