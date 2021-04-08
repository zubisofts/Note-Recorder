package com.zubisoft.noterecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.zubisoft.noterecorder.adapters.NoteListAdapter;
import com.zubisoft.noterecorder.data.Note;
import com.zubisoft.noterecorder.viewmodels.CategoryViewModel;
import com.zubisoft.noterecorder.viewmodels.CategoryViewModelFactory;
import com.zubisoft.noterecorder.viewmodels.NotesViewModel;
import com.zubisoft.noterecorder.viewmodels.NotesViewModelFactory;

import java.util.List;

public class CategoryNotes extends AppCompatActivity {

    private NotesViewModel notesViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_notes);
        RecyclerView recyclerView = findViewById(R.id.categoryRecycler);
       NotesViewModelFactory notesViewModelFactory=new NotesViewModelFactory(this.getApplication());
        notesViewModel= notesViewModelFactory.create(NotesViewModel.class);
        NoteListAdapter adapter = new NoteListAdapter();
        recyclerView.setAdapter(adapter);

        int cartId=getIntent().getIntExtra("categoryId",-'1');

        notesViewModel.findNoteByCategory(catId).observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNoteList(notes);
            }
        });
    }
}