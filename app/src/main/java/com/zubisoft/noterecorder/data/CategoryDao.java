package com.zubisoft.noterecorder.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert
    void insertCategory(Category category);

    @Update
    void updateCategory(Category... categories);

    @Delete
    public void deleteCategory(Category category);

    @Query("SELECT * FROM Category")
    LiveData<List<Category>> getAllCategories();

}
