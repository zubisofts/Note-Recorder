package com.zubisoft.noterecorder.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class NoteRecorderPreference {

    private static NoteRecorderPreference INSTANCE;
    private final SharedPreferences sharedPreferences;

    private NoteRecorderPreference(SharedPreferences sharedPreferences){
        this.sharedPreferences=sharedPreferences;
    }

    public static NoteRecorderPreference getInstance(Context context){
        if (INSTANCE==null){
            SharedPreferences preferences=context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
            return new NoteRecorderPreference(preferences);
        }

        return INSTANCE;
    }

    public void setFirstUser(boolean isFirstUser){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(Constants.FIRST_USER, isFirstUser);
        editor.apply();
    }

    public boolean isFirstUser(){
        return sharedPreferences.getBoolean(Constants.FIRST_USER,true);
    }
}
