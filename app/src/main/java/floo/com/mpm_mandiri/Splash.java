package floo.com.mpm_mandiri;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

import floo.com.mpm_mandiri.utils.SessionManager;

public class Splash extends AppCompatActivity {
    private static final int SPLASH_DURATION = 3000;
    SessionManager session;
    public static final String escalated_group = "escalated_group";
    String escalated;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        session = new SessionManager(getApplicationContext());
        //session.checkLogin();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (session.isLoggedIn()){
                    HashMap<String, String> user = session.getUserDetails();
                    escalated = user.get(escalated_group);
                    if (escalated.trim().equals("1")){
                        Intent i = new Intent(Splash.this, MainActivity.class);
                        startActivity(i);
                        finish();

                    }else {
                        Intent i = new Intent(Splash.this, MenuActivity.class);
                        startActivity(i);
                        finish();
                    }

                }else {
                    Intent i = new Intent(Splash.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_DURATION);
    }
}
