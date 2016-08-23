package com.floo.lenteramandiri.data;

import android.content.Intent;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.utils.ConnectivityReceiver;
import com.floo.lenteramandiri.utils.DataManager;
import com.floo.lenteramandiri.utils.MyLenteraMandiri;
import com.floo.lenteramandiri.utils.TouchImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by Floo on 4/21/2016.
 */
public class ImageActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    TouchImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Intent i = getIntent();
        String gambar = i.getStringExtra("gambar");

        img = (TouchImageView)findViewById(R.id.img_img);

        Picasso.with(getApplicationContext())
                .load("http://sandbox.floostudio.com/lenteramandiri/portofolio_csv/tfd_"+gambar+".jpg")
                .into(img);
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
