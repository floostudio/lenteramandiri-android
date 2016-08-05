package com.floo.lenteramandiri.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.utils.DataManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Floo on 3/4/2016.
 */
public class DashboardAGFAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<HashMap<String, String>> list;
    private int length;

    public DashboardAGFAdapter(Context c, ArrayList<HashMap<String, String>> list, int length) {
        mContext = c;
        this.list = list;
        this.length = length;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.list_row_dashboard_agf_new, null);
            TextView textView = (TextView) grid.findViewById(R.id.txt_agf_pinjam);
            HashMap<String, String> hashMap = list.get(position);
            if (position<=(length-1)){
                textView.setBackgroundColor(mContext.getResources().getColor(R.color.yellow));
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                llp.setMargins(0, 2, 2, 2); // llp.setMargins(left, top, right, bottom);
                textView.setLayoutParams(llp);
            }
            String agf = hashMap.get("values");
            if (position==length){
                textView.setText(agf);
            }else{
                if (agf.matches("\\d+(?:\\.\\d+)?")){
                    if (agf.length()>3){
                        textView.setText(DataManager.getDecimalFormat(agf));
                    }else {
                        textView.setText(agf);
                    }

                }else {
                    textView.setText(agf);
                }
            }


        } else {
            grid = (View) convertView;
        }


        return grid;
    }
}
