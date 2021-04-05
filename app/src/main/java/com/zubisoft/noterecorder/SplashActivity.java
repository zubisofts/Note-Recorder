package com.zubisoft.noterecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        Timer for splash
        CountDownTimer timer = new CountDownTimer(600,30) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                startActivity(new Intent(SplashActivity.this,CreatePinActivity.class));
                finish();

            }
        }.start();
    }
}