package com.floo.lenteramandiri.data;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.floo.lenteramandiri.utils.DataManager;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

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
public class PortoAccountActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    Toolbar toolbar;
    LinearLayout line;
    TextView titleToolbar, save;
    DynamicListView portoAccount;
    String[] subject, pt, idr;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> mylist;
    SimpleAdapter adapter;

    String url = DataManager.url;
    String urlPortAccount = DataManager.urlPortAccount;
    private SpotsDialog pDialog;
    public static final String cif = "cif";
    public static final String id = "id";
    public static final String acc_num = "acc_num";
    public static final String valuta = "valuta";
    public static final String saldo = "saldo";
    public static final String limit = "limit";
    public static final String tunggakan = "tunggakan";
    public static final String kolektibilitas = "kolektibilitas";
    public static final String jatuh_tempo = "jatuh_tempo";
    public static final String trans_debet = "trans_debet";
    public static final String trans_kredit = "trans_kredit";
    public static final String saldo_rata = "saldo_rata";
    public static final String company_name = "company_name";
    public static final String covenant = "covenant";

    String strId, strAccNumber, strValuta, strSaldo, strLimit,
            strTunggakan, strKolektibilitas, strJmlhTempo, strTransDebet,
            strTransKredit, strSaldoRata, strCompanyName, strCif, idParsing;

    ArrayList<String> arrayKey;
    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portofolio_account);
        initView();
        mylist = new ArrayList<HashMap<String, String>>();
        arrayKey = new ArrayList<>();
        new DataAccount().execute();

    }
    public void initView(){
        Intent i = getIntent();
        idParsing  = i.getStringExtra("IDPARSING");
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        titleToolbar = (TextView)toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("PORTFOLIO ACCOUNT");
        save = (TextView)findViewById(R.id.txt_save);
        line = (LinearLayout) findViewById(R.id.linier_toolbar);
        portoAccount = (DynamicListView) findViewById(R.id.list_porto_account);

        save.setVisibility(View.INVISIBLE);
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PortoAccountActivity.this.finish();
            }
        });

        portoAccount.setOnItemClickListener(this);

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView txtCif = (TextView) view.findViewById(R.id.txt_porto_account_cif);
        TextView txtCompany = (TextView) view.findViewById(R.id.txt_porto_account_company);
        TextView txtAcc_number = (TextView) view.findViewById(R.id.txt_porto_account_acc);
        Intent GroupDetail = new Intent(PortoAccountActivity.this, PortoAccountDetailActivity.class);
        GroupDetail.putExtra(cif, txtCif.getText().toString());
        GroupDetail.putExtra(company_name, txtCompany.getText().toString());
        GroupDetail.putExtra(acc_num, txtAcc_number.getText().toString());
        GroupDetail.putStringArrayListExtra(valuta, arrayKey);
        GroupDetail.putExtra(covenant, jsonArray.toString());
        startActivity(GroupDetail);
    }

    private class DataAccount extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new SpotsDialog(PortoAccountActivity.this, R.style.CustomProgress);
            pDialog.setMessage("Please wait...!!!");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                jsonArray = new JSONArray(DataManager.MyHttpGet(urlPortAccount+idParsing));
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String is_title = jsonObject.getString("is_title");
                    if (is_title.trim().equals("1")){
                        JSONArray arrayRow = jsonObject.getJSONArray("row");
                        for (int a=0;a<arrayRow.length();a++){
                            arrayKey.add(arrayRow.getString(a));
                        }
                    }else {
                        JSONArray arrayRow = jsonObject.getJSONArray("row");
                        strCif = (String) arrayRow.get(0);
                        strAccNumber = (String) arrayRow.get(2);
                        strCompanyName = (String) arrayRow.get(3);
                        strSaldo = (String) arrayRow.get(4);

                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put(cif, strCif);
                        hashMap.put(acc_num, strAccNumber);
                        hashMap.put(saldo, strSaldo);
                        hashMap.put(company_name, strCompanyName);

                        mylist.add(hashMap);


                    }
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

            adapter = new SimpleAdapter(getApplicationContext(), mylist, R.layout.list_row_porto_account,
                    new String[]{cif, acc_num,saldo, company_name},
                new int[]{R.id.txt_porto_account_cif, R.id.txt_porto_account_acc,R.id.txt_porto_account_saldo,R.id.txt_porto_account_company});

            portoAccount.setAdapter(adapter);

        }
    }
}
