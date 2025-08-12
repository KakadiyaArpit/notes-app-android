package com.example.notes_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes_app.R;
import com.example.notes_app.Utility;
import com.example.notes_app.addnote_detail_screen;
import com.example.notes_app.model.Note;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {
    Context context;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.titletextview.setText(note.getTitle());
        holder.contenttextview.setText(note.getContent());
        holder.timestamptextview.setText(Utility.timestamptostring(note.getTimestamp()));

        holder.itemView.setOnClickListener((V)->{
            Intent intent = new Intent(context, addnote_detail_screen.class);
            intent.putExtra("title",note.getTitle());
            intent.putExtra("content",note.getContent());
            String docid = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("id",docid);
            context.startActivity(intent);
        });

    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view   = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item,parent,false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView titletextview,contenttextview,timestamptextview;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titletextview = itemView.findViewById(R.id.note_title_textview);
            contenttextview = itemView.findViewById(R.id.note_content_textview);
            timestamptextview = itemView.findViewById(R.id.note_timestamp);
        }
    }
}
