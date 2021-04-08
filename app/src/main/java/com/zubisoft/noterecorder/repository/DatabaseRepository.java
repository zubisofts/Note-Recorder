package com.zubisoft.noterecorder.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.zubisoft.noterecorder.data.Category;
import com.zubisoft.noterecorder.data.CategoryDao;
import com.zubisoft.noterecorder.data.Note;
import com.zubisoft.noterecorder.data.NoteDao;
import com.zubisoft.noterecorder.data.NoteDatabase;
import com.zubisoft.noterecorder.data.UserData;
import com.zubisoft.noterecorder.data.UserDataDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseRepository {
    private NoteDatabase db;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public DatabaseRepository(Application application) {
        this.db = NoteDatabase.getDatabase(application);

        mCategoryDao = db.categoryDao();
        mUserDataDao = db.userDataDao();
        mNoteDao=db.noteDao();
        mAllCategories = mCategoryDao.getAllCategories();
    }

    private final CategoryDao mCategoryDao;
    private final NoteDao mNoteDao;
    private final UserDataDao mUserDataDao;
    private final LiveData<List<Category>> mAllCategories;

    // Note that in order to unit test the DatabaseRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples

    public LiveData<List<Category>> getAllCategories() {
        return mAllCategories;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void addCategory(Category category) {
        databaseWriteExecutor.execute(() -> {
            mCategoryDao.insertCategory(category);
        });
    }

    public LiveData<UserData> getUserData(String password) {
        return mUserDataDao.getUserData(password);
    }

    public void setUserData(UserData userData) {
        databaseWriteExecutor.execute(() -> {
            mUserDataDao.insertUserData(userData);
        });
    }

    public void addNote(Note note) {
        databaseWriteExecutor.execute(() -> {
            mNoteDao.insertNote(note);
        });
    }

    public LiveData<List<Note>> getAllNotes() {
        return mNoteDao.getAllNotes();
    }

    public void updateNote(Note note) {
        databaseWriteExecutor.execute(() -> {
            mNoteDao.updateNote(note);
        });
    }

    public void deleteNote(Note note) {
        databaseWriteExecutor.execute(() -> {
            mNoteDao.deleteNote(note);
        });

    }

    public LiveData<List<Note>> findNotesByCategory(int catId) {
        return mNoteDao.findNotesByCategory(catId);
    }
}
