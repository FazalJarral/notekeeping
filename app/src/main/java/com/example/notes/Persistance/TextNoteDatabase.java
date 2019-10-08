package com.example.notes.Persistance;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notes.bean.TextNote;

@Database(entities = {TextNote.class} , version = 3)
public abstract class TextNoteDatabase extends RoomDatabase {
    public abstract TextNoteDAO getDAO();
    private static TextNoteDatabase INSTANCE;

  public static TextNoteDatabase getInstance(Context context){
      if (INSTANCE == null){
          INSTANCE = Room.databaseBuilder(context.getApplicationContext() , TextNoteDatabase.class , "NOTES")
                 .fallbackToDestructiveMigration().build();


      }
      return INSTANCE;
  }
}
