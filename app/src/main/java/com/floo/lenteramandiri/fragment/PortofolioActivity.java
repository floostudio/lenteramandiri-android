package com.floo.lenteramandiri.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.floo.lenteramandiri.data.PortoAccountActivity;
import com.floo.lenteramandiri.data.PortoGroupActivity;

import com.floo.lenteramandiri.R;

/**
 * Created by Floo on 3/3/2016.
 */
public class PortofolioActivity extends Fragment {
    ImageView group, account;
    String idParsing;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_portofolio, container, false);
        initView(v);


        return v;
    }
    public void initView(View view) {
        idParsing = this.getArguments().getString("IDPARSING");
        group = (ImageView)view.findViewById(R.id.img_portofolio_group);
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextGroup=new Intent(getActivity(), PortoGroupActivity.class);
                nextGroup.putExtra("IDPARSING", idParsing);
                startActivity(nextGroup);
            }
        });

        account = (ImageView)view.findViewById(R.id.img_portofolio_account);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextAccount=new Intent(getActivity(), PortoAccountActivity.class);
                nextAccount.putExtra("IDPARSING", idParsing);
                startActivity(nextAccount);
            }
        });

    }

}
