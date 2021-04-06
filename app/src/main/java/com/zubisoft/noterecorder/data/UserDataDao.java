package com.zubisoft.noterecorder.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDataDao {

    @Insert
    Long insertUserData(UserData userData);

    @Query("SELECT * FROM userdata WHERE password IN (:password)")
    LiveData<UserData> getUserData(String password);

    @Update
    void updateUserData(UserData userData);

}
