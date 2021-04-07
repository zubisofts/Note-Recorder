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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

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
import com.zubisoft.noterecorder.data.Note;
import com.zubisoft.noterecorder.viewmodels.CategoryViewModel;
import com.zubisoft.noterecorder.viewmodels.CategoryViewModelFactory;
import com.zubisoft.noterecorder.viewmodels.NotesViewModel;
import com.zubisoft.noterecorder.viewmodels.NotesViewModelFactory;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;

public class HomeActivity extends AppCompatActivity {

    private String filePath;
    private int selectedCatId;
    private DrawerLayout drawerLayout;
    private int chosenColor;
    private CategoryViewModel categoryViewModel;
    private NotesViewModel notesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        CategoryViewModelFactory categoryViewModelFactory = new CategoryViewModelFactory(getApplication());
        categoryViewModel = categoryViewModelFactory.create(CategoryViewModel.class);

        NotesViewModelFactory notesViewModelFactory = new NotesViewModelFactory(getApplication());
        notesViewModel = notesViewModelFactory.create(NotesViewModel.class);

        drawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView = findViewById(R.id.navigationView);
        Toolbar toolbar = findViewById(R.id.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, (R.string.open), (R.string.close));
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);

//        Setting Home fragments
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

//        On navigationClick
        navigationView.setNavigationItemSelectedListener(item -> {

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
        });

        findViewById(R.id.btnNewNote).setOnClickListener(v -> {
            makeNote();
        });

        findViewById(R.id.btn_add_category).setOnClickListener(v -> {
            chosenColor = R.color.red;
            View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.new_category_layout, null, false);
            TextInputEditText editText = view.findViewById(R.id.edt_cat_title);
            MaterialCardView btnColor = view.findViewById(R.id.btnChooseColor);
            btnColor.setOnClickListener(v1 -> new MaterialColorPickerDialog
                    .Builder(HomeActivity.this)
                    .setTitle("Pick Category Color")
                    .setColorShape(ColorShape.SQAURE)
                    .setColorSwatch(ColorSwatch._300)
                    .setDefaultColor(R.color.red)
                    .setColorListener((color, colorHex) -> {
                        chosenColor = Color.parseColor(colorHex);
                        btnColor.setCardBackgroundColor(chosenColor);
                    })
                    .show());
            new AlertDialog.Builder(HomeActivity.this)
                    .setView(view)
                    .setPositiveButton("Save Now", (dialog, which) -> {
                        if (editText.getText().toString().isEmpty()) {
                            Toast.makeText(HomeActivity.this, "Category title must not be empty", Toast.LENGTH_SHORT).show();
                        } else {
                            Category category = new Category(editText.getText().toString(), chosenColor, new Date().getTime());
                            categoryViewModel.addCategory(category);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {

                    })
                    .create().show();
        });

    }

    private void makeNote() {

        View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.note_type_selection_dialog_layout, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.btnRecord).setOnClickListener(v -> {
            recordNote();
            dialog.dismiss();
        });

        view.findViewById(R.id.btnText).setOnClickListener(v -> {
            makeTextNote();
            dialog.dismiss();
        });


    }

    private void makeTextNote() {
        View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.category_selection_dialog_layout, null, false);
        ListView listView=view.findViewById(R.id.catListView);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();
        categoryViewModel.getCategories().observe(this, categories -> {
            if(!categories.isEmpty()){
                ArrayList<String> cats=new ArrayList<>();
                for(Category category:categories){
                    cats.add(category.getTitle());
                }
                ArrayAdapter arrayAdapter=new ArrayAdapter(HomeActivity.this, android.R.layout.simple_selectable_list_item,cats);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener((parent, view1, position, id) -> {
                    Intent intent=new Intent(HomeActivity.this, NewTextNoteActivity.class);
                    intent.putExtra("categoryId", categories.get(position).getId());
                    startActivity(intent);

                });
            }

        });
    }

    private void recordNote() {

        View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.category_selection_dialog_layout, null, false);
        ListView listView=view.findViewById(R.id.catListView);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();
        categoryViewModel.getCategories().observe(this, categories -> {
            if(!categories.isEmpty()){
                ArrayList<String> cats=new ArrayList<>();
                for(Category category:categories){
                    cats.add(category.getTitle());
                }
                ArrayAdapter arrayAdapter=new ArrayAdapter(HomeActivity.this, android.R.layout.simple_selectable_list_item,cats);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener((parent, view1, position, id) -> {
                    File file = new File(Environment.getExternalStorageDirectory() + "/RecordIt");
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    long timestamp = new Date().getTime();
                    filePath = file.getPath() + "/record_it_" + timestamp + ".wav";
                    selectedCatId=categories.get(position).getId();
                    int color = categories.get(position).getColor();
                    int requestCode = 1002;
                    AndroidAudioRecorder.with(HomeActivity.this)
                            // Required
                            .setFilePath(filePath)
                            .setColor(color)
                            .setRequestCode(requestCode)
                            // Optional
                            .setSource(AudioSource.MIC)
                            .setChannel(AudioChannel.STEREO)
                            .setSampleRate(AudioSampleRate.HZ_8000)
                            .setAutoStart(false)
                            .setKeepDisplayOn(true)

                            // Start recording
                            .record();
                    dialog.dismiss();
                });
            }

        });




    }

    private void switchFragment(Fragment fragment) {
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
                saveNote();
            } else if (resultCode == RESULT_CANCELED) {
                // Oops! User has canceled the recording
                File file = new File(filePath);
                if (file.exists()) {
                    boolean deleted = file.delete();
                    if (deleted) {
                        Toast.makeText(this, "Note record was canceled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void saveNote() {

        View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.note_save_name_layout, null, false);
        TextInputEditText edtTitle=view.findViewById(R.id.edtNoteTitle);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setView(view);
        builder.setPositiveButton("Save", (dialog, which) -> {
            if(edtTitle.getText().toString().isEmpty()){
                Toast.makeText(HomeActivity.this, "You must enter a title for your notes", Toast.LENGTH_SHORT).show();
            }else{
                Note note=new Note(
                        selectedCatId,
                        edtTitle.getText().toString(),
                        filePath,
                        "",
                        "Record",
                        new Date().getTime()
                );
                notesViewModel.addNote(note);
                dialog.dismiss();
                Toast.makeText(HomeActivity.this, "Note record saved successfully", Toast.LENGTH_SHORT).show();

            }
        })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    File file = new File(filePath);
                    if (file.exists()) {
                        boolean deleted = file.delete();
                        if (deleted) {
                            Toast.makeText(HomeActivity.this, "Note record was canceled", Toast.LENGTH_SHORT).show();
                        }
                    }
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();


    }
}