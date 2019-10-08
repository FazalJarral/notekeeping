package com.example.notes.Listeners;

import com.example.notes.bean.TextNote;


public interface UpdateNote {
    void onNoteUpdated(TextNote note , boolean updated);
}
