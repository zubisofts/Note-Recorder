package com.zubisoft.noterecorder;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zubisoft.noterecorder.adapters.CategoryListAdapter;
import com.zubisoft.noterecorder.adapters.NoteListAdapter;
import com.zubisoft.noterecorder.data.Category;
import com.zubisoft.noterecorder.data.Note;
import com.zubisoft.noterecorder.viewmodels.CategoryViewModel;
import com.zubisoft.noterecorder.viewmodels.CategoryViewModelFactory;
import com.zubisoft.noterecorder.viewmodels.NotesViewModel;
import com.zubisoft.noterecorder.viewmodels.NotesViewModelFactory;

import java.io.File;
import java.util.List;


public class HomeFragment extends Fragment implements CategoryListAdapter.CategoryInteractionListener, NoteListAdapter.NoteItemInteractionListener {


    public HomeFragment() {
        // Required empty public constructor
    }


    private CategoryViewModel categoryViewModel;
   private NotesViewModel notesViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);



        TextView txt_moreNotes = view.findViewById(R.id.txt_moreNotes);
        FrameLayout noRecent = view.findViewById(R.id.noRecent);
        TextView txt_moreCat = view.findViewById(R.id.txt_moreCat);
        FrameLayout noCat = view.findViewById(R.id.noCatigory);

        txt_moreNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.container,new NotesFragment()).addToBackStack("tag").commit();
            }
        });
        txt_moreCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.container,new CategoryFragment()).addToBackStack("tag").commit();
            }
        });

        NotesViewModelFactory notesViewModelFactory = new NotesViewModelFactory(getActivity().getApplication());
       notesViewModel = notesViewModelFactory.create(NotesViewModel.class);
        CategoryViewModelFactory categoryViewModelFactory=new CategoryViewModelFactory(getActivity().getApplication());
        categoryViewModel= categoryViewModelFactory.create(CategoryViewModel.class);

        RecyclerView recentList = view.findViewById(R.id.recentRecycler);
        NoteListAdapter noteListAdapter = new NoteListAdapter();
        noteListAdapter.setNoteItemInteractionListener(this);
        recentList.setAdapter(noteListAdapter);

        notesViewModel.getRecentNotes().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                noteListAdapter.setNoteList(notes);

                //        Checking for notes
                if (notes.size()<1){
                    noRecent.setVisibility(View.VISIBLE);
                    txt_moreNotes.setVisibility(View.GONE);
                }else{
                    noRecent.setVisibility(View.GONE);
                    txt_moreNotes.setVisibility(View.VISIBLE);
                }
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.catRecycler);
        CategoryListAdapter adapter=new CategoryListAdapter();
        adapter.setCategoryInteractionListener(this);
        recyclerView.setAdapter(adapter);

        categoryViewModel.getSomeCategories().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                if(!categories.isEmpty()){
                    adapter.setCategories(categories);

                    //        Checking for categories
                    int item = adapter.getItemCount();
                    if (categories.isEmpty()){
                        noCat.setVisibility(View.VISIBLE);
                        txt_moreCat.setVisibility(View.GONE);
                    }else{
                        noCat.setVisibility(View.GONE);
                        txt_moreCat.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        return  view;
    }

    @Override
    public void onCategoryClicked(Category category) {
        Intent intent=new Intent(getContext(), CategoryNotes.class);
        intent.putExtra("categoryId", category.getId());
        startActivity(intent);
    }

    @Override
    public void onPlayPressed(Note note) {
        Intent intent=new Intent(getContext(), RecordPlayerActivity.class);
        intent.putExtra("recordPath", note.getFilePath());
        intent.putExtra("title", note.getTitle());
        startActivity(intent);
    }

    @Override
    public void onReadPressed(Note note) {
        Intent intent=new Intent(getContext(), NewTextNoteActivity.class);
        intent.putExtra("text", note.getText());
        intent.putExtra("id", note.getId());
        intent.putExtra("title", note.getTitle());
        intent.putExtra("catId", note.getCategoryId());
        intent.putExtra("timestamp", note.getTimestamp());
        intent.putExtra("for", "view");
        startActivity(intent);
    }

    @Override
    public void onDeleteNote(Note note) {
        new AlertDialog.Builder(getActivity())
                .setMessage("Do you want to delete this note?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    notesViewModel.deleteNote(note);
                    if (note.getType().equals("Record")){
                        boolean delete=new File(note.getFilePath()).delete();
                    }
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
}