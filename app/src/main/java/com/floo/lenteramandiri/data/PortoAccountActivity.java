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

import com.floo.lenteramandiri.utils.ConnectivityReceiver;
import com.floo.lenteramandiri.utils.DataManager;
import com.floo.lenteramandiri.utils.MyLenteraMandiri;
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
public class PortoAccountActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, ConnectivityReceiver.ConnectivityReceiverListener{
    Toolbar toolbar;
    LinearLayout line;
    TextView titleToolbar, save;
    DynamicListView portoAccount;
    ArrayList<HashMap<String, String>> mylist;
    SimpleAdapter adapter;

    String urlPortAccount = DataManager.urlPortAccount;
    private SpotsDialog pDialog;
    public static final String cif = "cif";
    public static final String id = "id";
    public static final String acc_num = "acc_num";
    public static final String valuta = "valuta";
    public static final String company_name = "company_name";
    public static final String covenant = "covenant";

    String strAccNumber, strCompanyName, strCif, idParsing;

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
        titleToolbar.setText("AKUN PORTOFOLIO");
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView txtCif = (TextView) view.findViewById(R.id.txt_porto_account_acc);
        TextView txtCompany = (TextView) view.findViewById(R.id.txt_porto_account_company);
        TextView txtAcc_number = (TextView) view.findViewById(R.id.txt_porto_account_saldo);
        Intent GroupDetail = new Intent(PortoAccountActivity.this, PortoAccountDetailActivity.class);
        GroupDetail.putExtra(cif, txtCif.getText().toString());
        GroupDetail.putExtra(company_name, txtCompany.getText().toString());
        GroupDetail.putExtra(acc_num, txtAcc_number.getText().toString());
        GroupDetail.putStringArrayListExtra(valuta, arrayKey);
        GroupDetail.putExtra(covenant, jsonArray.toString());
        startActivity(GroupDetail);
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
                        strCif = (String) arrayRow.get(1);
                        strCompanyName = (String) arrayRow.get(2);
                        strAccNumber = (String) arrayRow.get(3);

                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put(cif, strCif);
                        hashMap.put(acc_num, strAccNumber);
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
                    new String[]{cif, company_name, acc_num},
                new int[]{R.id.txt_porto_account_acc, R.id.txt_porto_account_company,R.id.txt_porto_account_saldo});

            portoAccount.setAdapter(adapter);

        }
    }
}
