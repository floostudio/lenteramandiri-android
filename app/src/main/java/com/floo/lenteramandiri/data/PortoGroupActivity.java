package com.floo.lenteramandiri.data;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.floo.lenteramandiri.utils.DataManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import dmax.dialog.SpotsDialog;
import com.floo.lenteramandiri.R;

/**
 * Created by Floo on 3/3/2016.
 */
public class PortoGroupActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout line;
    TextView titleToolbar, save;
    ListView listportoGroup;
    String[] subject, pt, idr;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> mylist;

    String url = DataManager.url;
    String urlPortGroup = DataManager.urlPortGroup;
    private SpotsDialog pDialog;
    public static final String cif = "cif";
    public static final String group_id = "group_id";
    public static final String group_limit = "group_limit";
    public static final String group_balance = "group_balance";
    public static final String fee = "fee";
    public static final String bunga = "bunga";
    public static final String utilisasi = "utilisasi";
    public static final String company_name = "company_name";
    public static final String acc_num = "acc_num";
    public static final String type_facility = "type_facility";

    String strCif, strGroupId, strGroupLimit, strGroupBalance, strFee,
            strBunga, strUtilisasi, strCompanyName, strAcc_amount,
            strFacility_amount, idParsing;

    SimpleAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portofolio_group);
        initView();
        mylist = new ArrayList<HashMap<String, String>>();
        new DataGroup().execute();

        /*subject = new String[]{"G9741123123", "G9741123123", "G9741123123",
                "G9741123123", "G9741123123", "G9741123123", "G9741123123"};

        pt = new String[]{"PT. MASPINA", "PT. MASPINA", "PT. MASPINA", "PT. MASPINA",
                "PT. MASPINA", "PT. MASPINA", "PT. MASPINA"};

        idr = new String[]{"580.000.000.000d","580.000.000.000d","580.000.000.000d",
                "580.000.000.000d","580.000.000.000d","580.000.000.000d","580.000.000.000d"};

        mylist = new ArrayList<HashMap<String, String>>();
        for (int i=0; i<subject.length;i++){
            hashMap = new HashMap<String, String>();
            hashMap.put("subject", subject[i]);
            hashMap.put("pt", pt[i]);
            hashMap.put("idr", idr[i]);
            mylist.add(hashMap);
        }

        adapter = new SimpleAdapter(getApplicationContext(), mylist, R.layout.list_row_porto_group,
                new String[]{"subject", "pt", "idr"}, new int[]{R.id.txt_porto_group_subject,
                R.id.txt_porto_group_pt, R.id.txt_porto_group_idr});
        portoGroup.setAdapter(adapter);*/

        listportoGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtcif = (TextView) view.findViewById(R.id.txt_porto_group_cif);
                TextView txtid = (TextView) view.findViewById(R.id.txt_porto_group_id);
                TextView txtlimit = (TextView) view.findViewById(R.id.txt_porto_group_limit);
                TextView txtbalance = (TextView) view.findViewById(R.id.txt_porto_group_balance);
                TextView txtacc = (TextView) view.findViewById(R.id.txt_porto_group_acc_amount);
                TextView txtfacility = (TextView) view.findViewById(R.id.txt_porto_group_facility_amount);
                TextView txtfee = (TextView) view.findViewById(R.id.txt_porto_group_fee);
                TextView txtbunga = (TextView) view.findViewById(R.id.txt_porto_group_bunga);
                TextView txtutilisasi = (TextView) view.findViewById(R.id.txt_porto_group_utilisasi);
                TextView txtcompany = (TextView) view.findViewById(R.id.txt_porto_group_company);
                Intent GroupDetail = new Intent(PortoGroupActivity.this, PortoGroupDetailActivity.class);
                GroupDetail.putExtra(cif, txtcif.getText().toString());
                GroupDetail.putExtra(group_id, txtid.getText().toString());
                GroupDetail.putExtra(group_limit, txtlimit.getText().toString());
                GroupDetail.putExtra(group_balance, txtbalance.getText().toString());
                GroupDetail.putExtra(acc_num, txtacc.getText().toString());
                GroupDetail.putExtra(type_facility, txtfacility.getText().toString());
                GroupDetail.putExtra(fee, txtfee.getText().toString());
                GroupDetail.putExtra(bunga, txtbunga.getText().toString());
                GroupDetail.putExtra(utilisasi, txtutilisasi.getText().toString());
                GroupDetail.putExtra(company_name, txtcompany.getText().toString());
                startActivity(GroupDetail);

            }
        });

    }
    public void initView(){
        Intent i = getIntent();
        idParsing  = i.getStringExtra("IDPARSING");
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        titleToolbar = (TextView)toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("PORTFOLIO DEBITUR");
        save = (TextView)findViewById(R.id.txt_save);
        line = (LinearLayout) findViewById(R.id.linier_toolbar);
        listportoGroup = (ListView) findViewById(R.id.list_porto_group);

        save.setVisibility(View.INVISIBLE);
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PortoGroupActivity.this.finish();
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
    

    private class DataGroup extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new SpotsDialog(PortoGroupActivity.this, R.style.CustomProgress);
            pDialog.setMessage("Please wait...!!!");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {

                JSONArray jsonArray = new JSONArray(DataManager.MyHttpGet(urlPortGroup+idParsing));
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    strCif = jsonObject.getString(cif);
                    strGroupId = jsonObject.getString(group_id);
                    strAcc_amount = jsonObject.getString(acc_num);
                    strGroupLimit = jsonObject.getString(group_limit);
                    strGroupBalance = jsonObject.getString(group_balance);
                    strFacility_amount = jsonObject.getString(type_facility);
                    strFee = jsonObject.getString(fee);
                    strBunga = jsonObject.getString(bunga);
                    strUtilisasi = jsonObject.getString(utilisasi);
                    strCompanyName = jsonObject.getString(company_name);


                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put(cif, strCif);
                    hashMap.put(group_id, strGroupId);
                    hashMap.put(acc_num, strAcc_amount);
                    hashMap.put(group_limit, getDecimalFormat(strGroupLimit));
                    hashMap.put(group_balance, getDecimalFormat(strGroupBalance));
                    hashMap.put(type_facility, strFacility_amount);
                    hashMap.put(fee, getDecimalFormat(strFee));
                    hashMap.put(bunga, getDecimalFormat(strBunga));
                    hashMap.put(utilisasi, strUtilisasi);
                    hashMap.put(company_name, strCompanyName);

                    mylist.add(hashMap);
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

            adapter = new SimpleAdapter(getApplicationContext(), mylist, R.layout.list_row_porto_group,
                    new String[]{cif, group_id, group_limit, group_balance, acc_num, type_facility, fee, bunga, utilisasi, company_name},
                    new int[]{R.id.txt_porto_group_cif, R.id.txt_porto_group_id, R.id.txt_porto_group_limit, R.id.txt_porto_group_balance, R.id.txt_porto_group_acc_amount,
                    R.id.txt_porto_group_facility_amount, R.id.txt_porto_group_fee, R.id.txt_porto_group_bunga, R.id.txt_porto_group_utilisasi, R.id.txt_porto_group_company});

            listportoGroup.setAdapter(adapter);



        }
    }

}
