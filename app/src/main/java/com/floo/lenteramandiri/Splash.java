package com.floo.lenteramandiri;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.floo.lenteramandiri.utils.SessionManager;

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
                    Intent i = new Intent(Splash.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }else {
                    Intent i = new Intent(Splash.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_DURATION);
    }
}