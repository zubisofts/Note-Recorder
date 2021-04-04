package com.zubisoft.noterecorder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends  RecyclerView.Adapter<RecyclerAdapter.ListItemHolder> {

    ArrayList<Notes> notesList = new ArrayList<>();

    public RecyclerAdapter() {
        this.notesList = Notes.getNotes();
    }

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout,parent,false);
        return new ListItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemHolder holder, int position) {
        holder.noteTittle.setText(notesList.get(position).noteTittle);
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class  ListItemHolder extends RecyclerView.ViewHolder{

        private TextView noteTittle;
        public ListItemHolder(@NonNull View itemView) {
            super(itemView);
            noteTittle = itemView.findViewById(R.id.noteTittle);
        }
    }

    static  class Notes {
        String noteTittle;

        public Notes(String noteTittle) {
            this.noteTittle = noteTittle;
        }

        public static ArrayList<Notes> getNotes(){
            ArrayList<Notes> notes = new ArrayList<>();
            notes.add(new Notes("web Development"));
            notes.add(new Notes("web Development"));
            notes.add(new Notes("web Development"));
            notes.add(new Notes("web Development"));
            return notes;
        }
    }

}
