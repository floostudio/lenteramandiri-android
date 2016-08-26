package com.floo.lenteramandiri.data;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.floo.lenteramandiri.utils.ConnectivityReceiver;
import com.floo.lenteramandiri.utils.DataManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import dmax.dialog.SpotsDialog;
import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.utils.MyLenteraMandiri;

/**
 * Created by Floo on 3/3/2016.
 */
public class PortoGroupActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, ConnectivityReceiver.ConnectivityReceiverListener{
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
    ArrayList<String> arrayVar, arrayKey;
    JSONArray jsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portofolio_group);
        initView();
        mylist = new ArrayList<HashMap<String, String>>();
        arrayVar = new ArrayList<>();
        arrayKey = new ArrayList<>();
        new DataGroup().execute();

    }
    public void initView(){
        Intent i = getIntent();
        idParsing  = i.getStringExtra("IDPARSING");
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        titleToolbar = (TextView)toolbar.findViewById(R.id.titleToolbar);
        titleToolbar.setText("PORTOFOLIO DEBITUR");
        save = (TextView)findViewById(R.id.txt_save);
        line = (LinearLayout) findViewById(R.id.linier_toolbar);
        listportoGroup = (ListView) findViewById(R.id.list_porto_group);

        listportoGroup.setOnItemClickListener(this);

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView txtCompany = (TextView)view.findViewById(R.id.txt_porto_group_company);
        TextView txtCif = (TextView)view.findViewById(R.id.txt_porto_group_cif);
        Intent GroupDetail = new Intent(PortoGroupActivity.this, PortoGroupDetailActivity.class);
        GroupDetail.putExtra(cif, txtCif.getText().toString());
        GroupDetail.putExtra(company_name, txtCompany.getText().toString());
        GroupDetail.putStringArrayListExtra("variable", arrayVar);
        GroupDetail.putExtra("key", jsonArray.toString());
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

                jsonArray = new JSONArray(DataManager.MyHttpGet(urlPortGroup+idParsing));
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String is_title = jsonObject.getString("is_title");
                    if (is_title.trim().equals("1")){
                        JSONArray arrayRow = jsonObject.getJSONArray("row");
                        String var = (String) arrayRow.get(i);
                        for (int a=0;a<arrayRow.length();a++){
                            arrayVar.add(arrayRow.getString(a));

                        }
                    }

                    if (is_title.trim().equals("0")){
                        JSONArray arrayRow = jsonObject.getJSONArray("row");
                        strCif = (String) arrayRow.get(0);
                        strGroupId = (String) arrayRow.get(1);
                        strCompanyName = arrayRow.getString(3);
                        strGroupLimit = (String) arrayRow.get(4);

                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put(cif, strCif);
                        hashMap.put(group_id, strGroupId);
                        hashMap.put(group_limit, strGroupLimit);
                        hashMap.put(company_name, strCompanyName);
                        mylist.add(hashMap);
                        //hashMap.put(group_limit, DataManager.getDecimalFormat(strGroupLimit));

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

           adapter = new SimpleAdapter(getApplicationContext(), mylist,R.layout.list_row_porto_group,
                    new String[]{cif, group_id, group_limit, company_name}, new int[]{R.id.txt_porto_group_cif, R.id.txt_porto_group_id, R.id.txt_porto_group_limit, R.id.txt_porto_group_company});
            listportoGroup.setAdapter(adapter);

        }
    }

}
