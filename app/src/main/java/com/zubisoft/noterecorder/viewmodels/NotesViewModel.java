package com.zubisoft.noterecorder.viewmodels;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.zubisoft.noterecorder.data.Category;
import com.zubisoft.noterecorder.data.Note;
import com.zubisoft.noterecorder.data.UserData;
import com.zubisoft.noterecorder.repository.DatabaseRepository;

import java.util.List;

public class NotesViewModel extends ViewModel {

    DatabaseRepository databaseRepository;
    private  LiveData<List<Note>> notes;
    public NotesViewModel(Application application) {
        databaseRepository=new DatabaseRepository(application);
        notes=databaseRepository.getAllNotes();
    }

    public LiveData<List<Note>> getAllNotes(){
        return notes;
    }

    public void addNote(Note note){
        databaseRepository.addNote(note);
    }

    public void updateNote(Note note) {
        databaseRepository.updateNote(note);
    }

    public void deleteNote(Note note) {
        databaseRepository.deleteNote(note);
    }
}
