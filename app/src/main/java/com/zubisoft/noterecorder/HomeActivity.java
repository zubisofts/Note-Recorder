package com.zubisoft.noterecorder;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.Date;
import java.util.List;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;

public class HomeActivity extends AppCompatActivity {

    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView = findViewById(R.id.navigationView);
        Toolbar toolbar = findViewById(R.id.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, (R.string.open), (R.string.close));
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);

//        Setting Home fragments
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

//        On navigationClick
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new HomeFragment()).commit();
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;

                    case R.id.favorite:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new FavoritesFragment()).commit();
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;

                    case R.id.categories:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new CategoryFragment()).commit();
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    case R.id.notes:
                        getSupportFragmentManager().
                                beginTransaction()
                                .replace(R.id.container, new NotesFragment()).commit();
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;

                }
                return true;
            }
        });

        findViewById(R.id.btnNewNote).setOnClickListener(v -> {

            Dexter.withContext(HomeActivity.this)
                    .withPermissions(Manifest.permission.RECORD_AUDIO
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                File file=new File(Environment.getExternalStorageDirectory()+"/RecordIt");
                                if(!file.exists()){
                                    file.mkdir();
                                }
                                long timestamp=new Date().getTime();
                                filePath = file.getPath() + "/record_it_"+timestamp+".wav";
                                int color = getResources().getColor(R.color.red);
                                int requestCode = 1002;
                                AndroidAudioRecorder.with(HomeActivity.this)
                                        // Required
                                        .setFilePath(filePath)
                                        .setColor(color)
                                        .setRequestCode(requestCode)

                                        // Optional
                                        .setSource(AudioSource.MIC)
                                        .setChannel(AudioChannel.STEREO)
                                        .setSampleRate(AudioSampleRate.HZ_48000)
                                        .setAutoStart(false)
                                        .setKeepDisplayOn(true)

                                        // Start recording
                                        .record();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                        }
                    })
                    .onSameThread()
                    .check();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1002) {
            if (resultCode == RESULT_OK) {
                // Great! User has recorded and saved the audio file
//                String path=data.getData().toString();
                Toast.makeText(this, "Note record saved successfully", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Oops! User has canceled the recording
                File file=new File(filePath);
                if (file.exists()){
                    boolean deleted=file.delete();
                    if (deleted){
                        Toast.makeText(this, "Note record was canceled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}