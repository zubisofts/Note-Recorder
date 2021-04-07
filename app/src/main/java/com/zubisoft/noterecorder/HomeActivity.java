package com.zubisoft.noterecorder;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.github.dhaval2404.colorpicker.model.ColorSwatch;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.zubisoft.noterecorder.data.Category;
import com.zubisoft.noterecorder.viewmodels.CategoryViewModel;
import com.zubisoft.noterecorder.viewmodels.CategoryViewModelFactory;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Date;
import java.util.List;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;

public class HomeActivity extends AppCompatActivity {

    private String filePath;
    private DrawerLayout drawerLayout;
    private int chosenColor;
    private CategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        CategoryViewModelFactory categoryViewModelFactory=new CategoryViewModelFactory(getApplication());
        categoryViewModel= categoryViewModelFactory.create(CategoryViewModel.class);

        drawerLayout = findViewById(R.id.drawer);
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
                        switchFragment(new HomeFragment());
                        findViewById(R.id.btn_add_category).setVisibility(View.GONE);
                        findViewById(R.id.btnNewNote).setVisibility(View.VISIBLE);
                        break;

                    case R.id.favorite:
                        switchFragment(new FavoritesFragment());
                        findViewById(R.id.btn_add_category).setVisibility(View.GONE);
                        findViewById(R.id.btnNewNote).setVisibility(View.VISIBLE);
                        break;

                    case R.id.categories:
                        switchFragment(new CategoryFragment());
                        findViewById(R.id.btn_add_category).setVisibility(View.VISIBLE);
                        findViewById(R.id.btnNewNote).setVisibility(View.GONE);
                        break;
                    case R.id.notes:
                        switchFragment(new NotesFragment());
                        findViewById(R.id.btn_add_category).setVisibility(View.GONE);
                        findViewById(R.id.btnNewNote).setVisibility(View.VISIBLE);
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

        findViewById(R.id.btn_add_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenColor=R.color.red;
                View view= LayoutInflater.from(HomeActivity.this).inflate(R.layout.new_category_layout, null,false);
                TextInputEditText editText=view.findViewById(R.id.edt_cat_title);
                MaterialCardView btnColor=view.findViewById(R.id.btnChooseColor);
                btnColor.setOnClickListener(v1 -> new MaterialColorPickerDialog
                        .Builder(HomeActivity.this)
                        .setTitle("Pick Category Color")
                        .setColorShape(ColorShape.SQAURE)
                        .setColorSwatch(ColorSwatch._300)
                        .setDefaultColor(R.color.red)
                        .setColorListener((color, colorHex) -> {
                            chosenColor=Color.parseColor(colorHex);
                            btnColor.setCardBackgroundColor(chosenColor);
                        })
                        .show());
                new AlertDialog.Builder(HomeActivity.this)
                        .setView(view)
                        .setPositiveButton("Save Now", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(editText.getText().toString().isEmpty()){
                                    Toast.makeText(HomeActivity.this, "Category title must not be empty", Toast.LENGTH_SHORT).show();
                                }else {
                                    Category category = new Category(editText.getText().toString(), chosenColor, new Date().getTime());
                                    categoryViewModel.addCategory(category);
                                    dialog.dismiss();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create().show();
            }
        });

    }

    private void switchFragment(Fragment fragment){
        getSupportFragmentManager().
                beginTransaction()
                .replace(R.id.container, fragment).commit();
        drawerLayout.closeDrawer(Gravity.LEFT);
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