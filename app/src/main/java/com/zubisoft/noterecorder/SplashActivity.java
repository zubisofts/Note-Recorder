package com.zubisoft.noterecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.zubisoft.noterecorder.utils.NoteRecorderPreference;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        Timer for splash
        new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                NoteRecorderPreference recorderPreference=NoteRecorderPreference.getInstance(SplashActivity.this);
                if(recorderPreference.isFirstUser()){
                    Toast.makeText(SplashActivity.this, "First User", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SplashActivity.this,CreatePinActivity.class));
                }else{
                    Toast.makeText(SplashActivity.this, "Regular User", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                }
                finish();
            }
        }.start();
    }
}