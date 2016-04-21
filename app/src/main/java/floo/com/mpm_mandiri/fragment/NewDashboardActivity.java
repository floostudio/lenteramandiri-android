package floo.com.mpm_mandiri.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import floo.com.mpm_mandiri.R;
import floo.com.mpm_mandiri.data.ImageActivity;

/**
 * Created by Floo on 4/21/2016.
 */
public class NewDashboardActivity extends Fragment {
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> mylist;
    ListView listView;
    SimpleAdapter adapter;
    String[] subject = new String[]{"FY 2014", "YTD Jul"};
    String[] pt = new String[]{"24.1 T", "3.7 T", "14.2 T", "3.5 T"};
    String[] ss = new String[]{"BMRI Share", "BMRI Share"};
    String[] pp = new String[]{"3.7 T", "3.5 T"};

    Button tfd, cash, dlr, agf, avg, bakidebet, detail;
    Spinner spinner;
    ArrayAdapter<String> spinnerArrayAdapter;
    String[] strtfd, strcash, strdlr, stragf, stravg, strbaki;
    TextView text1, text2, title_blue;

    String[] aa = new String[]{"170","170","170"};
    String[] bb = new String[]{"150","160", "150"};
    String[] cc = new String[]{"165","180", "180"};
    String[] dd = new String[]{"140","160", "150"};
    String[] ee = new String[]{"180","130", "190"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_new_dashboard, container, false);
        initView(v);



        return v;
    }

    private void initView(View v){
        strtfd = new String[] {"Transaction Flow Diagram"};
        strcash = new String[]{"Cash in Flow vs Omset"};
        stragf = new String[]{"Status AGF"};
        strbaki  = new String[]{"Saldo Rata2, Terendah, Tertinggi"};
        stravg = new String[]{"Status AVG"};
        strdlr = new String[]{"Depositto Loan Ratio"};

        text1 = (TextView)v.findViewById(R.id.txt_dasboard_1);
        text2 = (TextView)v.findViewById(R.id.txt_dasboard_2);
        title_blue = (TextView)v.findViewById(R.id.txt_title_blue);
        spinner = (Spinner)v.findViewById(R.id.spin_array);
        tfd = (Button)v.findViewById(R.id.btn_tfd);
        cash = (Button)v.findViewById(R.id.btn_cash);
        dlr = (Button)v.findViewById(R.id.btn_dlr);
        agf = (Button)v.findViewById(R.id.btn_agf);
        avg = (Button)v.findViewById(R.id.btn_avg);
        bakidebet = (Button)v.findViewById(R.id.btn_BakiDebet);
        detail = (Button)v.findViewById(R.id.btn_detail);
        listView = (ListView)v.findViewById(R.id.list_dasboard);




        tfd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_new_spinner, strtfd);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner.setAdapter(spinnerArrayAdapter);
                title_blue.setText("Transaction Flow Diagram");
                text1.setText("Collection");
                text2.setText("Payment");
                tfd.setBackgroundResource(R.drawable.activity_btn_blue);
                tfd.setTextColor(Color.parseColor("#ffffff"));
                cash.setBackgroundResource(R.drawable.activity_btn);
                cash.setTextColor(Color.parseColor("#0b3a77"));
                dlr.setBackgroundResource(R.drawable.activity_btn);
                dlr.setTextColor(Color.parseColor("#0b3a77"));
                agf.setBackgroundResource(R.drawable.activity_btn);
                agf.setTextColor(Color.parseColor("#0b3a77"));
                avg.setBackgroundResource(R.drawable.activity_btn);
                avg.setTextColor(Color.parseColor("#0b3a77"));
                bakidebet.setBackgroundResource(R.drawable.activity_btn);
                bakidebet.setTextColor(Color.parseColor("#0b3a77"));

                mylist = new ArrayList<HashMap<String, String>>();
                for (int i=0; i<subject.length;i++){
                    hashMap = new HashMap<String, String>();
                    hashMap.put("subject", subject[i]);
                    hashMap.put("pt", pt[i]);
                    hashMap.put("ss", ss[i]);
                    hashMap.put("pp", pp[i]);
                    mylist.add(hashMap);
                }

                adapter = new SimpleAdapter(getActivity(), mylist, R.layout.list_row_dashboard,
                        new String[]{"subject", "pt", "subject", "pt","ss","pp","ss","pp"}, new int[]{R.id.txt_background,
                        R.id.btn_background, R.id.txt_light, R.id.btn_light, R.id.txt_blue, R.id.btn_blue, R.id.txt_yellow, R.id.btn_yellow});
                listView.setAdapter(adapter);

                detail.setVisibility(View.VISIBLE);

            }
        });

        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_new_spinner, strcash);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner.setAdapter(spinnerArrayAdapter);
                title_blue.setText("CashInOut");
                text1.setText("Cashin Target | Cashin Actual");
                text2.setText("Cashout Target | Cashout Actual");
                tfd.setBackgroundResource(R.drawable.activity_btn);
                tfd.setTextColor(Color.parseColor("#0b3a77"));
                cash.setBackgroundResource(R.drawable.activity_btn_blue);
                cash.setTextColor(Color.parseColor("#ffffff"));
                dlr.setBackgroundResource(R.drawable.activity_btn);
                dlr.setTextColor(Color.parseColor("#0b3a77"));
                agf.setBackgroundResource(R.drawable.activity_btn);
                agf.setTextColor(Color.parseColor("#0b3a77"));
                avg.setBackgroundResource(R.drawable.activity_btn);
                avg.setTextColor(Color.parseColor("#0b3a77"));
                bakidebet.setBackgroundResource(R.drawable.activity_btn);
                bakidebet.setTextColor(Color.parseColor("#0b3a77"));

                mylist = new ArrayList<HashMap<String, String>>();
                for (int i=0; i<aa.length;i++){
                    hashMap = new HashMap<String, String>();
                    hashMap.put("aa", aa[i]);
                    hashMap.put("bb", bb[i]);
                    hashMap.put("cc", cc[i]);
                    hashMap.put("dd", dd[i]);
                    hashMap.put("ee", ee[i]);
                    mylist.add(hashMap);
                }

                adapter = new SimpleAdapter(getActivity(), mylist, R.layout.list_row_dashboard,
                        new String[]{"aa", "bb", "aa", "dd","aa","cc","aa","ee"}, new int[]{R.id.txt_background,
                        R.id.btn_background, R.id.txt_light, R.id.btn_light, R.id.txt_blue, R.id.btn_blue, R.id.txt_yellow, R.id.btn_yellow});
                listView.setAdapter(adapter);

                detail.setVisibility(View.VISIBLE);
            }
        });

        dlr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_new_spinner, strdlr);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner.setAdapter(spinnerArrayAdapter);
                tfd.setBackgroundResource(R.drawable.activity_btn);
                tfd.setTextColor(Color.parseColor("#0b3a77"));
                cash.setBackgroundResource(R.drawable.activity_btn);
                cash.setTextColor(Color.parseColor("#0b3a77"));
                dlr.setBackgroundResource(R.drawable.activity_btn_blue);
                dlr.setTextColor(Color.parseColor("#ffffff"));
                agf.setBackgroundResource(R.drawable.activity_btn);
                agf.setTextColor(Color.parseColor("#0b3a77"));
                avg.setBackgroundResource(R.drawable.activity_btn);
                avg.setTextColor(Color.parseColor("#0b3a77"));
                bakidebet.setBackgroundResource(R.drawable.activity_btn);
                bakidebet.setTextColor(Color.parseColor("#000000"));

            }
        });

        agf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_new_spinner, stragf);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner.setAdapter(spinnerArrayAdapter);
                tfd.setBackgroundResource(R.drawable.activity_btn);
                tfd.setTextColor(Color.parseColor("#0b3a77"));
                cash.setBackgroundResource(R.drawable.activity_btn);
                cash.setTextColor(Color.parseColor("#0b3a77"));
                dlr.setBackgroundResource(R.drawable.activity_btn);
                dlr.setTextColor(Color.parseColor("#0b3a77"));
                agf.setBackgroundResource(R.drawable.activity_btn_blue);
                agf.setTextColor(Color.parseColor("#ffffff"));
                avg.setBackgroundResource(R.drawable.activity_btn);
                avg.setTextColor(Color.parseColor("#0b3a77"));
                bakidebet.setBackgroundResource(R.drawable.activity_btn);
                bakidebet.setTextColor(Color.parseColor("#0b3a77"));
            }
        });

        avg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_new_spinner, stravg);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner.setAdapter(spinnerArrayAdapter);
                tfd.setBackgroundResource(R.drawable.activity_btn);
                tfd.setTextColor(Color.parseColor("#0b3a77"));
                cash.setBackgroundResource(R.drawable.activity_btn);
                cash.setTextColor(Color.parseColor("#0b3a77"));
                dlr.setBackgroundResource(R.drawable.activity_btn);
                dlr.setTextColor(Color.parseColor("#0b3a77"));
                agf.setBackgroundResource(R.drawable.activity_btn);
                agf.setTextColor(Color.parseColor("#0b3a77"));
                avg.setBackgroundResource(R.drawable.activity_btn_blue);
                avg.setTextColor(Color.parseColor("#ffffff"));
                bakidebet.setBackgroundResource(R.drawable.activity_btn);
                bakidebet.setTextColor(Color.parseColor("#0b3a77"));
            }
        });

        bakidebet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_new_spinner, strbaki);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner.setAdapter(spinnerArrayAdapter);
                tfd.setBackgroundResource(R.drawable.activity_btn);
                tfd.setTextColor(Color.parseColor("#0b3a77"));
                cash.setBackgroundResource(R.drawable.activity_btn);
                cash.setTextColor(Color.parseColor("#0b3a77"));
                dlr.setBackgroundResource(R.drawable.activity_btn);
                dlr.setTextColor(Color.parseColor("#0b3a77"));
                agf.setBackgroundResource(R.drawable.activity_btn);
                agf.setTextColor(Color.parseColor("#0b3a77"));
                avg.setBackgroundResource(R.drawable.activity_btn);
                avg.setTextColor(Color.parseColor("#0b3a77"));
                bakidebet.setBackgroundResource(R.drawable.activity_btn_blue);
                bakidebet.setTextColor(Color.parseColor("#ffffff"));
            }
        });

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent im=new Intent(getActivity(), ImageActivity.class);
                startActivity(im);
            }
        });

    }
}
