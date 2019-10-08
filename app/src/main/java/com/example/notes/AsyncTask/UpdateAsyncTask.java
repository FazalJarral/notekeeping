package com.example.notes.AsyncTask;

import android.os.AsyncTask;

import com.example.notes.Persistance.TextNoteDAO;
import com.example.notes.bean.TextNote;

public class UpdateAsyncTask extends AsyncTask<TextNote , Void ,Void> {
    private TextNoteDAO dao;

    public UpdateAsyncTask(TextNoteDAO dao ) {
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(TextNote... textNotes) {
        dao.updateTextNote(textNotes[0]);
        return null;
    }
}
