package com.floo.lenteramandiri.data;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.floo.lenteramandiri.R;
import com.floo.lenteramandiri.utils.ConnectivityReceiver;
import com.floo.lenteramandiri.utils.DataManager;
import com.floo.lenteramandiri.utils.MyLenteraMandiri;
import com.floo.lenteramandiri.utils.TouchImageView;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import dmax.dialog.SpotsDialog;

/**
 * Created by Floo on 4/21/2016.
 */
public class ImageActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    TouchImageView img;
    private SpotsDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Intent i = getIntent();
        String gambar = i.getStringExtra("gambar");

        img = (TouchImageView)findViewById(R.id.img_img);

        String url = "http://sandbox.floostudio.com/lenteramandiri/portofolio_csv/tfd_"+gambar+".jpg";

        new ProfilLoadTask(url, img).execute();

        //Picasso.with(getApplicationContext())
        //        .load("http://sandbox.floostudio.com/lenteramandiri/portofolio_csv/tfd_"+gambar+".jpg")
        //        .into(img);
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

    public class ProfilLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private TouchImageView imageView;

        public ProfilLoadTask(String url, TouchImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new SpotsDialog(ImageActivity.this, R.style.CustomProgress);
            pDialog.setMessage("Please wait...!!!");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected Bitmap doInBackground(Void... params) {
            try {

                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();

                Bitmap myBitmap = null;
                if (connection.getResponseCode()==200){

                    InputStream input = connection.getInputStream();
                    myBitmap = BitmapFactory.decodeStream(input);
                }else {
                    myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile);
                }


                return myBitmap;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            imageView.setImageBitmap(result);
        }
    }
}
