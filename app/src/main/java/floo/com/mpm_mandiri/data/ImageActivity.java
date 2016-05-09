package floo.com.mpm_mandiri.data;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import floo.com.mpm_mandiri.R;

/**
 * Created by Floo on 4/21/2016.
 */
public class ImageActivity extends AppCompatActivity {
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Intent i = getIntent();
        String gambar = i.getStringExtra("gambar");

        img = (ImageView)findViewById(R.id.img_img);

       // new ImageLoadTask(gambar, img).execute();
    }
}
