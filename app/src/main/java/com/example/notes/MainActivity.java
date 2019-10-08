package com.example.notes;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notes.AsyncTask.InsertAsyncTask;
import com.example.notes.AsyncTask.UpdateAsyncTask;
import com.example.notes.Fragment.MainFrag;
import com.example.notes.Fragment.TextFrag;
import com.example.notes.Listeners.InsertNote;
import com.example.notes.Listeners.UpdateNote;
import com.example.notes.Persistance.TextNoteDAO;
import com.example.notes.Persistance.TextNoteDatabase;
import com.example.notes.bean.TextNote;

public class MainActivity extends AppCompatActivity implements InsertNote,  UpdateNote {
    FragmentTransaction fragmentTransaction;
    TextNoteDAO dao;
    TextNoteDatabase database ;
    TextNote audiofile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = TextNoteDatabase.getInstance(this);
        Bundle extras = getIntent().getExtras();

        if(extras!= null){

             audiofile  = (TextNote) getIntent().getSerializableExtra("audio");
            Log.e("main" , audiofile.getFilepath());
            Bundle bundle = new Bundle();
            bundle.putSerializable("audio" , audiofile);
            Fragment fragment = new MainFrag();
            fragment.setArguments(bundle);
            updateFragment(fragment);


        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateFragment(new MainFrag());

    }


    public void updateFragment(Fragment fragment) {

        FragmentManager manager = getSupportFragmentManager();
        fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void OnNewNoteListner(TextNote note , boolean isInserted) {
        if (isInserted == true){
            InsertAsyncTask insertAsyncTask = new InsertAsyncTask(database.getDAO());
            insertAsyncTask.execute(note);
            Fragment fragment  = new MainFrag();
            updateFragment(fragment);
        }
        else {
            Fragment fragment = new TextFrag();
            updateFragment(fragment);
        }
    }



    @Override
    public void onNoteUpdated(TextNote note, boolean isUpdated) {
        Toast.makeText(this, ""+isUpdated, Toast.LENGTH_SHORT).show();
        if (!isUpdated) {
            Fragment fragment = new TextFrag();
            Bundle bundle = new Bundle();
            bundle.putSerializable("note", note);
            fragment.setArguments(bundle);
            updateFragment(fragment);



        } else {
            UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask(database.getDAO());
            updateAsyncTask.execute(note);
            updateFragment(new MainFrag());
        }
    }
}