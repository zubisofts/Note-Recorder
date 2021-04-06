package com.zubisoft.noterecorder.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

@Dao
public interface NoteDao {

    @Insert
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);
}
