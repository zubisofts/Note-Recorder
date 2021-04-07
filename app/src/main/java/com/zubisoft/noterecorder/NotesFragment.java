package com.zubisoft.noterecorder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zubisoft.noterecorder.adapters.NoteListAdapter;
import com.zubisoft.noterecorder.data.Note;
import com.zubisoft.noterecorder.viewmodels.NotesViewModel;
import com.zubisoft.noterecorder.viewmodels.NotesViewModelFactory;

import java.util.List;


public class NotesFragment extends Fragment implements NoteListAdapter.NoteItemInteractionListener {

    private NotesViewModel notesViewModel;

    public NotesFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotesViewModelFactory notesViewModelFactory=new NotesViewModelFactory(getActivity().getApplication());
        notesViewModel=notesViewModelFactory.create(NotesViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_notes, container, false);

        RecyclerView noteList=view.findViewById(R.id.noteList);
        NoteListAdapter adapter=new NoteListAdapter();
        adapter.setNoteItemInteractionListener(this);
        noteList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        noteList.setAdapter(adapter);

        notesViewModel.getAllNotes().observe(getViewLifecycleOwner(), notes -> {
            if(!notes.isEmpty()){
                adapter.setNoteList(notes);
            }
        });

        return view;
    }

    @Override
    public void onPlayPressed(Note note) {

    }

    @Override
    public void onReadPressed(Note note) {

    }
}