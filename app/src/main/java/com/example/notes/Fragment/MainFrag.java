package com.example.notes.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.notes.Adapter.SwipeToDeleteCallback;
import com.example.notes.Adapter.NoteAdapter;
import com.example.notes.Listeners.UpdateNote;
import com.example.notes.Persistance.TextNoteDatabase;
import com.example.notes.RecorderActivity;
import com.example.notes.bean.TextNote;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.notes.R;
import com.example.notes.Listeners.InsertNote;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.ContentValues.TAG;

public class MainFrag extends Fragment implements UpdateNote {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    FloatingActionButton fab_voice;
    NoteAdapter adapter;
    FloatingActionMenu floatingActionMenu;
    ArrayList<TextNote> notes;
    TextNoteDatabase database;
    TextNote audioFile;
    TextNote note;
    private InsertNote listner;
    private UpdateNote updateNotelistner;
    private int RequestPermissionCode = 200;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_main, container, false);
        recyclerView = v.findViewById(R.id.recyclerview_textnote);
        floatingActionMenu = v.findViewById(R.id.menu_fab);
        fab = v.findViewById(R.id.fab);
        fab_voice = v.findViewById(R.id.fab_voice);
        notes = new ArrayList<>();
        database = TextNoteDatabase.getInstance(getContext());
        setRecyclerView();
   //     readAudioDir();
        if (getArguments() != null) {
            getIncomingArg();
            Toast.makeText(getContext(), "Recieved", Toast.LENGTH_SHORT).show();
        }
        database.getDAO().getNotes().observe(this, new Observer<List<TextNote>>() {
            @Override
            public void onChanged(List<TextNote> textNotes) {
                if (notes.size() > 0) {
                    notes.clear();
                }
                notes.addAll(textNotes);
                adapter.notifyDataSetChanged();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note = new TextNote();
                listner.OnNewNoteListner(note, false);
            }
        });
        fab_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission()) {
                    requestPermission();
                }
                if (checkPermission()) {
                    Intent intent = new Intent(getContext(), RecorderActivity.class);
                    startActivity(intent);
                }
            }
        });

        return v;
    }

    private void readAudioDir() {
        String path = Environment.getExternalStorageDirectory().toString()+"/NotesAudio";
        Log.e("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.e("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            Log.e("Files", "FileName:" + files[i].getName());
        }

    }

    private void getIncomingArg() {

        audioFile = (TextNote) getArguments().getSerializable("audio");
        Log.e("Frag", audioFile.getFilepath());


    }

    public void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NoteAdapter(getContext(), notes, this);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InsertNote) {
            this.listner = (InsertNote) context;
        }
        if (context instanceof UpdateNote) {
            this.updateNotelistner = (UpdateNote) context;
        } else throw new RuntimeException(context.toString() + "implement listner");
    }


    @Override
    public void onNoteUpdated(TextNote note, boolean isUpdated) {
        Log.e(TAG, "onNoteUpdated: " + note.getTitle());
        updateNotelistner.onNoteUpdated(note, false);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);

    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }


}