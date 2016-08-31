package com.floo.lenteramandiri.data;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.adapter.GroupDetailAdapter;
import com.floo.lenteramandiri.utils.ConnectivityReceiver;
import com.floo.lenteramandiri.utils.DataManager;
import com.floo.lenteramandiri.utils.MyLenteraMandiri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Floo on 3/11/2016.
 */
public class PortoGroupDetailActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    Toolbar toolbar;
    LinearLayout line;
    TextView titleToolbar, save, txtCif, txtGroupId, txtGroupLimit, txtGroupBalance, txtAccAmount,
            txtFacilityAmount, txtFee, txtBunga, txtUtilisasi, txtCompany;
    String pCif, pGroupId, pGroupLimit, pGroupBalance, pAccAmount,
            pFacilityAmount, pFee, pBunga, pUtilisasi, pCompany;
    ArrayList<String> arrayVar, arrayKey;
    GroupDetailAdapter adapter;
    ListView listView;
    ArrayList<HashMap<String, String>> arrayList;
    static String strArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portofolio_group_detail);
        arrayVar = new ArrayList<>();
        arrayKey = new ArrayList<>();
        arrayList = new ArrayList<>();
        initView();
    }
    public void initView(){
        Intent i = getIntent();
        pCif = i.getStringExtra(PortoGroupActivity.cif);
        pCompany  = i.getStringExtra(PortoGroupActivity.company_name);
        arrayVar = i.getStringArrayListExtra("variable");
        strArray = i.getStringExtra("key");
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        titleToolbar = (TextView)toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("PORTOFOLIO DEBITUR");
        save = (TextView)findViewById(R.id.txt_save);
        line = (LinearLayout) findViewById(R.id.linier_toolbar);

        txtCompany = (TextView)findViewById(R.id.txt_group_detail_company);
        listView = (ListView)findViewById(R.id.list_group_detail);
        txtCompany.setText(pCompany);

        try {
            JSONArray jsonArray = new JSONArray(strArray);
            for(int z=0; z<jsonArray.length(); z++){
                JSONObject jsonObject = jsonArray.getJSONObject(z);
                String strTitle = jsonObject.getString("is_title");

                if (strTitle.trim().equals("0")){
                    JSONArray arrayRow = jsonObject.getJSONArray("row");
                    String value = (String) arrayRow.get(1);
                    if (value.trim().equals(pCif)){
                        for (int a=0;a<arrayRow.length();a++){
                            arrayKey.add(arrayRow.getString(a));

                        }
                    }
                }
            }

            for (int a=0;a<arrayVar.size();a++){
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("variable", arrayVar.get(a));
                hashMap.put("key", arrayKey.get(a));

                arrayList.add(hashMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new GroupDetailAdapter(getApplicationContext(), arrayList);
        listView.setAdapter(adapter);

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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        DataManager.showSnack(getApplicationContext(), isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyLenteraMandiri.getInstance().setConnectivityListener(this);
        DataManager.checkConnection(getApplicationContext());
    }
}
