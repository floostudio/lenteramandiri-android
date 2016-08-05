package com.floo.lenteramandiri.data;

import android.content.Intent;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.utils.TouchImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by Floo on 4/21/2016.
 */
public class ImageActivity extends AppCompatActivity {
    TouchImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        //Intent i = getIntent();
        //String gambar = i.getStringExtra("gambar");

        img = (TouchImageView)findViewById(R.id.img_img);

        Picasso.with(getApplicationContext())
                .load("http://sandbox.floostudio.com/lenteramandiri/portofolio_csv/tfd.jpg")
                .into(img);
        /*img.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
            @Override
            public void onMove() {
                PointF point = img.getScrollPosition();
                RectF rect = img.getZoomedRect();
                float currentZoom = img.getCurrentZoom();
                boolean isZoomed = img.isZoomed();
            }
        });*/

       // new ImageLoadTask(gambar, img).execute();
    }
}
