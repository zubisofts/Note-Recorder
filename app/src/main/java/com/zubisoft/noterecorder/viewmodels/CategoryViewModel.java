package com.zubisoft.noterecorder.viewmodels;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.zubisoft.noterecorder.data.Category;
import com.zubisoft.noterecorder.data.UserData;
import com.zubisoft.noterecorder.repository.DatabaseRepository;

import java.util.List;

public class CategoryViewModel extends ViewModel {

    DatabaseRepository databaseRepository;
    private  LiveData<List<Category>> categories;
    private  LiveData<List<Category>> someCategories;
    public CategoryViewModel(Application application) {
        databaseRepository=new DatabaseRepository(application);
        categories=databaseRepository.getAllCategories();
        someCategories = databaseRepository.getSomeCategories();
    }

    public LiveData<List<Category>> getCategories(){
        return categories;
    }
    public LiveData<List<Category>> getSomeCategories(){
        return someCategories;
    }

    public void addCategory(Category category){
        databaseRepository.addCategory(category);
    }

    public void createPassword(String password){
        databaseRepository.setUserData(new UserData(password,"zubitex40@gmail.com"));
    }

    public LiveData<UserData> getUserData(String password){
        return databaseRepository.getUserData(password);
    }
}
