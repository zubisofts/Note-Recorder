package com.zubisoft.noterecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.zubisoft.noterecorder.data.Note;
import com.zubisoft.noterecorder.viewmodels.NotesViewModel;
import com.zubisoft.noterecorder.viewmodels.NotesViewModelFactory;

import java.util.Date;
import java.util.Objects;

public class NewTextNoteActivity extends AppCompatActivity {

    private TextInputEditText edtTitle, edtText;
    private NotesViewModel notesViewModel;
    private int catId;
    private String title;
    private String text;
    private int id;
    private long timestamp;
    private String reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_text_note);

        NotesViewModelFactory notesViewModelFactory = new NotesViewModelFactory(getApplication());
        notesViewModel = notesViewModelFactory.create(NotesViewModel.class);

        catId = getIntent().getIntExtra("categoryId", -1);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        edtTitle = findViewById(R.id.edt_title);
        edtText = findViewById(R.id.edt_text);

        reason = getIntent().getStringExtra("for");
        if (reason.equals("view")) {
            initViews();
        }
    }

    private void initViews() {
        title = getIntent().getStringExtra("title");
        text = getIntent().getStringExtra("text");
        id = getIntent().getIntExtra("id", -1);
        timestamp = getIntent().getLongExtra("timestamp", 0L);

        edtTitle.setText(title);
        edtText.setText(text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_save_note) {
            saveNote();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        title = edtTitle.getText().toString();
        text = edtText.getText().toString();
        if (timestamp==0){
            timestamp=new Date().getTime();
        }
        if (title.isEmpty()) {
            title = new Date().toString();
        }

        if (text.isEmpty()) {
            text = "";
        }

        Note note=new Note(
                catId,
                title,
                "",
                text,
                "Text",
                timestamp
        );
        if (reason.equals("view")){
            note.setId(id);
            notesViewModel.updateNote(note);
            Toast.makeText(this, "Note saved successfully", Toast.LENGTH_SHORT).show();
        }else{
            notesViewModel.addNote(note);
            Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}