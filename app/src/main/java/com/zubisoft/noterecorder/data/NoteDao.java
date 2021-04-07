package com.zubisoft.noterecorder.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("SELECT * FROM Note")
    LiveData<List<Note>> getAllNotes();
}
