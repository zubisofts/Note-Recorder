package com.zubisoft.noterecorder.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("SELECT * FROM Note")
    LiveData<List<Note>> getAllNotes();
    @Query("SELECT * FROM Note ORDER BY id DESC LIMIT 3")
    LiveData<List<Note>> getRecentNotes();

    @Query("SELECT * FROM Note WHERE category_id IN(:categoryId)")
    LiveData<List<Note>> findNotesByCategory(int categoryId);

    @Update
    void updateNote(Note note);
}
