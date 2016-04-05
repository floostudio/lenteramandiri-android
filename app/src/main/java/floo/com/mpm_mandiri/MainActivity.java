package floo.com.mpm_mandiri;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import floo.com.mpm_mandiri.fragment.CalenderActivity;
import floo.com.mpm_mandiri.fragment.DashboardActivity;
import floo.com.mpm_mandiri.fragment.NewsActivity;
import floo.com.mpm_mandiri.fragment.PortofolioActivity;
import floo.com.mpm_mandiri.fragment.ProfilActivity;
import floo.com.mpm_mandiri.fragment.TaskActivity;
import floo.com.mpm_mandiri.utils.SessionManager;
import floo.com.mpm_mandiri.utils.CircleImageView;
import floo.com.mpm_mandiri.utils.RoundedImageView;

public class MainActivity extends AppCompatActivity {
    TextView title;
    ActionBar actionBar;
    DrawerLayout drawer;
    String idParsing, strFirstname, strLastname, strProfpic;
    public static final String first_name = "first_name";
    public static final String last_name = "last_name";
    public static final String profpic = "profpic";
    public static final String IDPARSING = "IDPARSING";
    RoundedImageView img;
    SessionManager session;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Intent i = getIntent();
        idParsing = i.getStringExtra("IDPARSING");
        strFirstname = i.getStringExtra(first_name);
        strLastname = i.getStringExtra(last_name);
        strProfpic = i.getStringExtra(profpic);*/

        session = new SessionManager(getApplicationContext());
        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
        //session.isLoggedIn();
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        idParsing = user.get(IDPARSING);
        strFirstname = user.get(first_name);
        strLastname = user.get(last_name);
        strProfpic = user.get(profpic);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText("DASHBOARD");

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        CircleImageView img = (CircleImageView) headerView.findViewById(R.id.img_profil);




        if (strProfpic.trim().equals("http://play.floostudio.com/lenteramandiri/static/images/users/profile/http://play.floostudio")){

            //Drawable myDrawable = getResources().getDrawable(R.drawable.profile);
            //Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();
            img.setImageResource(R.drawable.profile);
            Log.d("kosong", strProfpic);
        }else {
            new ImageLoadTask(strProfpic, img).execute();
            Log.d("isiiii", strProfpic);
        }

        TextView name = (TextView) headerView.findViewById(R.id.txtname);
        name.setText(strFirstname + " " + strLastname);
        ImageView img_edit = (ImageView) headerView.findViewById(R.id.img_edit);
        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setText("PROFILE");
                setFragment(0);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), strProfpic, Toast.LENGTH_LONG).show();
                title.setText("PROFILE");
                setFragment(0);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        if (navigationView != null){
            setupNavigationDrawerContent(navigationView);
        }
        setupNavigationDrawerContent(navigationView);

        setFragment(1);
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
                    Log.d("isiiii", url);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.nav_home:
                                title.setText("DASHBOARD");
                                item.setChecked(true);
                                setFragment(1);
                                drawer.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.nav_task:
                                title.setText("TASK");
                                item.setChecked(true);
                                setFragment(2);
                                drawer.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.nav_portfolio:
                                title.setText("PORTFOLIO");
                                item.setChecked(true);
                                setFragment(3);
                                drawer.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.nav_calender:
                                title.setText("CALENDAR");
                                item.setChecked(true);
                                setFragment(4);
                                drawer.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.nav_news:
                                title.setText("NEWS");
                                item.setChecked(true);
                                setFragment(5);
                                drawer.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.nav_logout:
                                new logoutuser().execute();

                                //session.logoutUser();
                                //finish();
                                //Intent signout=new Intent(MainActivity.this, LoginActivity.class);
                                //startActivity(signout);
                                //finish();
                                return true;
                        }
                        return true;
                    }
                }
        );
    }

    public void setFragment(int position){
        FragmentManager fm;
        FragmentTransaction ft;
        Bundle bundle = new Bundle();
        bundle.putString("IDPARSING", idParsing);
        switch (position){
            case 0:
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ProfilActivity profil = new ProfilActivity();
                profil.setArguments(bundle);
                ft.replace(R.id.content_fragment, profil);
                ft.commit();
                break;
            case 1:
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                DashboardActivity dashboar = new DashboardActivity();
                ft.replace(R.id.content_fragment, dashboar);
                ft.commit();
                break;
            case 2:
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                TaskActivity task= new TaskActivity();
                task.setArguments(bundle);
                ft.replace(R.id.content_fragment, task);
                ft.commit();
                break;
            case 3:
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                PortofolioActivity porto = new PortofolioActivity();
                ft.replace(R.id.content_fragment, porto);
                ft.commit();
                break;
            case 4:
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                CalenderActivity calender = new CalenderActivity();
                calender.setArguments(bundle);
                ft.replace(R.id.content_fragment, calender);
                ft.commit();
                break;
            case 5:
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                NewsActivity news = new NewsActivity();
                ft.replace(R.id.content_fragment, news);
                ft.commit();
                break;
        }
    }
    private class logoutuser extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this, "", "Logging Out", true);

        }

        @Override
        protected Void doInBackground(Void... params) {

            session.logoutUser();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            progressDialog.dismiss();

           finish();

        }
    }

    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0,
                                                int arg1) {
                                Intent intent = new Intent(
                                        Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                finish();
                                startActivity(intent);
                            }
                        }).create().show();

    }
}
