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
import com.floo.lenteramandiri.adapter.AccountDetailAdapter;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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
    ArrayList<String> arrayKey, arrayValues;
    ArrayList<HashMap<String, String>> arrayList;
    ExpandableHeightListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portofolio_account_detail);
        arrayKey = new ArrayList<>();
        arrayValues = new ArrayList<>();
        arrayList = new ArrayList<>();
        initView();
    }
    public void initView(){
        Intent i = getIntent();
        pCif = i.getStringExtra(PortoAccountActivity.cif);
        pCompany  = i.getStringExtra(PortoAccountActivity.company_name);
        pAccNumber = i.getStringExtra(PortoAccountActivity.acc_num);
        arrayKey = i.getStringArrayListExtra(PortoAccountActivity.valuta);
        pId = i.getStringExtra(PortoAccountActivity.covenant);

        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        titleToolbar = (TextView)toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("AKUN PORTOFOLIO");
        save = (TextView)findViewById(R.id.txt_save);
        line = (LinearLayout) findViewById(R.id.linier_toolbar);

        save.setVisibility(View.INVISIBLE);
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PortoAccountDetailActivity.this.finish();
            }
        });

        txtCompany = (TextView)findViewById(R.id.txt_account_detail_company_new);
        listView = (ExpandableHeightListView)findViewById(R.id.list_account_detail);
        txtCompany.setText(pCompany);

        try {
            JSONArray jsonArray = new JSONArray(pId);
            for(int z=0; z<jsonArray.length(); z++){
                JSONObject jsonObject = jsonArray.getJSONObject(z);
                String strTitle = jsonObject.getString("is_title");

                if (strTitle.trim().equals("0")){
                    JSONArray arrayRow = jsonObject.getJSONArray("row");
                    String value = (String) arrayRow.get(0);
                    if (value.trim().equals(pCif)){
                        for (int a=0;a<arrayRow.length();a++){
                            arrayValues.add(arrayRow.getString(a));

                        }
                    }
                }
            }

            for (int a=0;a<arrayKey.size();a++){
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("key", arrayKey.get(a));
                hashMap.put("variable", arrayValues.get(a));

                arrayList.add(hashMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        AccountDetailAdapter adapter = new AccountDetailAdapter(getApplicationContext(), arrayList);
        listView.setAdapter(adapter);
        listView.setExpanded(true);

        convenant = (Button)findViewById(R.id.btn_account_detail_convnant);
        convenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent conv = new Intent(PortoAccountDetailActivity.this, ConvenantActivity.class);
                conv.putExtra("acc_number", pAccNumber);
                conv.putExtra("company_name", pCompany);
                startActivity(conv);
            }
        });

    }
}
