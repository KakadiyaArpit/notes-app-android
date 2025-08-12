package com.example.notes_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.notes_app.databinding.ActivityAddnoteDetailScreenBinding;
import com.example.notes_app.model.Note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class addnote_detail_screen extends AppCompatActivity {

    ActivityAddnoteDetailScreenBinding binding;
    String title,content,docid;
    boolean iseditmode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddnoteDetailScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.savenotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savenote();
            }
        });

        // receive  data from intent
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docid = getIntent().getStringExtra("id");

        if (!(docid ==null) && !docid.isEmpty()){
            iseditmode = true;
        }

        binding.titleEdittext.setText(title);
        binding.contentEdittext.setText(content);

        if (iseditmode){
            binding.pageTitleTxtview.setText("Edit Notes");
            binding.deletenotesFat.setVisibility(View.VISIBLE);
        }

        binding.deletenotesFat.setOnClickListener((V)->deletenotesfromfirebase());
    }

    void savenote() {
        String notetitle = binding.titleEdittext.getText().toString();
        String notecontent = binding.contentEdittext.getText().toString();

        if (notetitle == null || notetitle.isEmpty()) {
            binding.titleEdittext.setError("Title is required");
            return;
        }

        Note note = new Note();
        note.setTitle(notetitle);
        note.setContent(notecontent);
        note.setTimestamp(Timestamp.now());

        savenotetofirebase(note);
    }

    void savenotetofirebase(Note note) {
        DocumentReference documentReference;
        if (iseditmode){
            documentReference = Utility.getcollectionreferencefornotes().document(docid);
        }else {
            documentReference = Utility.getcollectionreferencefornotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // note added
                    Utility.showToast(addnote_detail_screen.this,"Note Addeed Successfully");
                    finish();
                } else {
                    // note not added
                    Utility.showToast(addnote_detail_screen.this,"Filed while Note Adding");
                }
            }
        });
    }

    void deletenotesfromfirebase(){
        DocumentReference documentReference;

        documentReference = Utility.getcollectionreferencefornotes().document(docid);


        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // note added
                    Utility.showToast(addnote_detail_screen.this,"Note Deleted Successfully");
                    finish();
                } else {
                    // note not added
                    Utility.showToast(addnote_detail_screen.this,"Filed while Note Deleting");
                }
            }
        });
    }
}