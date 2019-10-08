package com.example.notes.AsyncTask;

import android.os.AsyncTask;

import com.example.notes.Persistance.TextNoteDAO;
import com.example.notes.bean.TextNote;

public class InsertAsyncTask extends AsyncTask<TextNote , Void ,Void> {
    private TextNoteDAO dao;

    public InsertAsyncTask(TextNoteDAO dao ) {
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(TextNote... textNotes) {
        dao.insertTextNote(textNotes[0]);
        return null;
    }
}
