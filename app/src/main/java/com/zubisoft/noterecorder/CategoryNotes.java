package com.zubisoft.noterecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.zubisoft.noterecorder.adapters.NoteListAdapter;
import com.zubisoft.noterecorder.data.Note;
import com.zubisoft.noterecorder.viewmodels.CategoryViewModel;
import com.zubisoft.noterecorder.viewmodels.CategoryViewModelFactory;
import com.zubisoft.noterecorder.viewmodels.NotesViewModel;
import com.zubisoft.noterecorder.viewmodels.NotesViewModelFactory;

import java.io.File;
import java.util.List;

public class CategoryNotes extends AppCompatActivity implements NoteListAdapter.NoteItemInteractionListener {

    private static final String TAG = "CategoryNotes";
    private NotesViewModel notesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_notes);
        RecyclerView recyclerView = findViewById(R.id.categoryRecycler);
        NotesViewModelFactory notesViewModelFactory = new NotesViewModelFactory(getApplication());
        notesViewModel = notesViewModelFactory.create(NotesViewModel.class);
        NoteListAdapter adapter = new NoteListAdapter();
        adapter.setNoteItemInteractionListener(this);
        recyclerView.setAdapter(adapter);

        int catId = getIntent().getIntExtra("categoryId", -1);

        notesViewModel.findNoteByCategory(catId).observe(this, notes -> {
            if (!notes.isEmpty()) {
                adapter.setNoteList(notes);
                Log.i(TAG, "onCreate: "+notes.size());
            }
        });
    }

    @Override
    public void onPlayPressed(Note note) {
        Intent intent = new Intent(getApplicationContext(), RecordPlayerActivity.class);
        intent.putExtra("recordPath", note.getFilePath());
        intent.putExtra("title", note.getTitle());
        startActivity(intent);
    }

    @Override
    public void onReadPressed(Note note) {
        Intent intent = new Intent(getApplicationContext(), NewTextNoteActivity.class);
        intent.putExtra("text", note.getText());
        intent.putExtra("id", note.getId());
        intent.putExtra("title", note.getTitle());
        intent.putExtra("catId", note.getCategoryId());
        intent.putExtra("timestamp", note.getTimestamp());
        intent.putExtra("for", "view");
        startActivity(intent);
    }

    @Override
    public void onDeleteNote(Note note) {
        new AlertDialog.Builder(getApplication())
                .setMessage("Do you want to delete this note?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    notesViewModel.deleteNote(note);
                    if (note.getType().equals("Record")) {
                        boolean delete = new File(note.getFilePath()).delete();
                    }
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
}