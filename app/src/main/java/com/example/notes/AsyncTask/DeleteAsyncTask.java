package com.example.notes.AsyncTask;

import android.os.AsyncTask;

import com.example.notes.Persistance.TextNoteDAO;
import com.example.notes.bean.TextNote;

public class DeleteAsyncTask extends AsyncTask<TextNote , Void ,Void> {
    private TextNoteDAO dao;

    public DeleteAsyncTask(TextNoteDAO dao ) {
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(TextNote... textNotes) {
        dao.deleteTextNote(textNotes[0]);
        return null;
    }
}
