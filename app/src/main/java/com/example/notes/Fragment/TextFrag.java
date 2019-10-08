package com.example.notes.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.notes.Listeners.InsertNote;
import com.example.notes.Listeners.UpdateNote;
import com.example.notes.R;
import com.example.notes.bean.TextNote;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TextFrag extends Fragment {
    EditText title;
    EditText description;
    TextNote note;
    InsertNote insertNote;
    UpdateNote updateNotelistner;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v= inflater.inflate(R.layout.textfrag , container , false);
        title = v.findViewById(R.id.note_title);
        description = v.findViewById(R.id.note_desc);
//used to update
        if(getArguments()!=null){
        Bundle bundle  = getArguments();
      note = (TextNote)  bundle.getSerializable("note");
      title.setText(note.getTitle());
      description.setText(note.getDescription());

        }



        return v;

    }

    private void updateNote() {
        String title1 = title.getText().toString();
        String desc = description.getText().toString();
        note.setTitle(title1);
        note.setDescription(desc);
        updateNotelistner.onNoteUpdated(note , true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InsertNote){
            insertNote = (InsertNote) context;
        }
        if (context instanceof UpdateNote){
            updateNotelistner = (UpdateNote) context;
        }
        else throw new RuntimeException(context.toString()+"must implelement PushNote");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_text_frag , menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                if (getArguments() == null) {
                    String title1 = title.getText().toString();
                    String desc = description.getText().toString();
                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    TextNote newNote = new TextNote();
                    newNote.setTitle(title1);
                    newNote.setDescription(desc);
                    newNote.setPublished(date);

                    insertNote.OnNewNoteListner(newNote , true);
                }
                else
                updateNote();
        }
        return true;
    }
}
