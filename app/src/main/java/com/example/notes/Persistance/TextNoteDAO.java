package com.example.notes.Persistance;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notes.bean.TextNote;

import java.util.List;

@Dao
public interface TextNoteDAO {
       @Query("SELECT * FROM textnote")
    LiveData<List<TextNote>> getNotes();


    @Insert
    long insertTextNote (TextNote note);

    @Delete
    void deleteTextNote (TextNote note);

    @Update
    void updateTextNote (TextNote note);

   /* @Query("DROP TABLE textnote")
    void deleteAllTextNote();

*/
}
