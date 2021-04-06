package com.zubisoft.noterecorder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zubisoft.noterecorder.data.Category;
import com.zubisoft.noterecorder.viewmodels.CategoryViewModel;
import com.zubisoft.noterecorder.viewmodels.CategoryViewModelFactory;

import java.util.Date;

public class CategoryFragment extends Fragment {

    private CategoryViewModel categoryViewModel;

    public CategoryFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CategoryViewModelFactory categoryViewModelFactory=new CategoryViewModelFactory(getActivity().getApplication());
        categoryViewModel= categoryViewModelFactory.create(CategoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        categoryViewModel.addCategory(new Category("My Category", "#FFFF", new Date().getTime()));
        return inflater.inflate(R.layout.fragment_category, container, false);
    }
}