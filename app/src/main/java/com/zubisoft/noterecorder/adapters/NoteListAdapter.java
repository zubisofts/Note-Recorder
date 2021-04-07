package com.zubisoft.noterecorder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.zubisoft.noterecorder.R;
import com.zubisoft.noterecorder.data.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteItemViewHolder> {

    private List<Note> noteList = new ArrayList<>();
    private NoteItemInteractionListener noteItemInteractionListener;

    @NonNull
    @Override
    public NoteItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_item, parent, false);
        return new NoteItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteItemViewHolder holder, int position) {
        Note note = noteList.get(position);
        if (note != null) {
            holder.txtNoteTitle.setText(note.getTitle());
            String date = new SimpleDateFormat("d MMM, yyyy hh:mm a", Locale.getDefault()).format(note.getTimestamp());
            holder.txtNoteDate.setText(date);

            if (note.getType().equals("Record")) {
                holder.btnRead.setVisibility(View.GONE);
            } else {
                holder.btnRead.setVisibility(View.VISIBLE);
            }

            holder.btnPlay.setOnClickListener(view -> noteItemInteractionListener.onPlayPressed(note));

            holder.btnRead.setOnClickListener(view -> noteItemInteractionListener.onReadPressed(note));
        }
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
        notifyDataSetChanged();
    }

    public void setNoteItemInteractionListener(NoteItemInteractionListener noteItemInteractionListener) {
        this.noteItemInteractionListener = noteItemInteractionListener;
    }

    static class NoteItemViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNoteTitle;
        private TextView txtNoteDate;
        private FrameLayout bgCategoryIndicator;
        private MaterialButton btnPlay, btnRead;

        public NoteItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNoteTitle = itemView.findViewById(R.id.txtNoteTitle);
            txtNoteDate = itemView.findViewById(R.id.txtDate);
            bgCategoryIndicator = itemView.findViewById(R.id.catIndicator);
            btnPlay = itemView.findViewById(R.id.btnPlay);
            btnRead = itemView.findViewById(R.id.btnRead);
        }
    }

    public interface NoteItemInteractionListener {
        void onPlayPressed(Note note);

        void onReadPressed(Note note);
    }
}
