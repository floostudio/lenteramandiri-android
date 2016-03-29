package floo.com.mpm_mandiri.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import floo.com.mpm_mandiri.R;
import floo.com.mpm_mandiri.data.PortoAccountActivity;
import floo.com.mpm_mandiri.data.PortoGroupActivity;

/**
 * Created by Floo on 3/3/2016.
 */
public class PortofolioActivity extends Fragment {
    ImageView group, account;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_portofolio, container, false);
        initView(v);


        return v;
    }
    public void initView(View view) {
        group = (ImageView)view.findViewById(R.id.img_portofolio_group);
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextGroup=new Intent(getActivity(), PortoGroupActivity.class);
                startActivity(nextGroup);
            }
        });

        account = (ImageView)view.findViewById(R.id.img_portofolio_account);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextAccount=new Intent(getActivity(), PortoAccountActivity.class);
                startActivity(nextAccount);
            }
        });

    }
}
