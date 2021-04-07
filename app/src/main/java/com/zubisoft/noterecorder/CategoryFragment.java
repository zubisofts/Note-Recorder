package com.zubisoft.noterecorder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zubisoft.noterecorder.adapters.CategoryListAdapter;
import com.zubisoft.noterecorder.data.Category;
import com.zubisoft.noterecorder.viewmodels.CategoryViewModel;
import com.zubisoft.noterecorder.viewmodels.CategoryViewModelFactory;

import java.util.Date;
import java.util.List;

public class CategoryFragment extends Fragment implements CategoryListAdapter.CategoryInteractionListener {

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
        View view=inflater.inflate(R.layout.fragment_category, container, false);

        RecyclerView catList=view.findViewById(R.id.catList);
        CategoryListAdapter adapter=new CategoryListAdapter();
        adapter.setCategoryInteractionListener(this);
        catList.setAdapter(adapter);

        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                if(!categories.isEmpty()){
                    adapter.setCategories(categories);
                }
            }
        });

        return view;
    }

    @Override
    public void onCategoryClicked(Category category) {

    }
}