package floo.com.mpm_mandiri.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import floo.com.mpm_mandiri.R;

/**
 * Created by Floo on 5/9/2016.
 */
public class ImageLoader {

    public void DisplayProfile(String url, ImageView img) {
        if (url.trim().equals("http://play.floostudio.com/lenteramandiri/static/images/users/profile/http://play.floostudio")) {

            img.setImageResource(R.drawable.profile);

        } else {

            new ImageLoadTask(url, img).execute();

        }
    }

    public static Bitmap getBitmap(String urlImage){
        Bitmap myBitmap = null;
        try {
            URL urlConnection = new URL(urlImage);
            HttpURLConnection connection;
            connection = (HttpURLConnection) urlConnection.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myBitmap;

    }

    public void DisplayImage(String url, ImageView imageView){
        new ImageLoadTask(url, imageView).execute();
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {

                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                return myBitmap;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }
    }
}


