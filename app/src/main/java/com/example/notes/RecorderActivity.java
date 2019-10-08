package com.example.notes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notes.Listeners.AudioInsert;
import com.example.notes.bean.TextNote;

import java.io.File;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;

public class RecorderActivity extends AppCompatActivity {
    TextNote note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        String folder_main = "NotesAudio";

        File file = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!file.exists()) {
            file.mkdirs();
        }
        Long tsLong = System.currentTimeMillis()/1000;
        String timestamp = tsLong.toString();
        String filePath = file + "/"+ timestamp+".wav";
         note = new TextNote();
        note.setFilepath(filePath);
        note.setItemtype(1);
        int color = getResources().getColor(R.color.colorPrimaryDark);
        int requestCode = 0;


        AndroidAudioRecorder.with(this)
                // Required
                .setFilePath(filePath)
                .setColor(color)
                .setRequestCode(requestCode)

                // Optional
                .setSource(AudioSource.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_48000)
                .setAutoStart(true)
                .setKeepDisplayOn(true)

                // Start recording
                .record();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                    Intent intent = new Intent(this , MainActivity.class);

                    intent.putExtra("audio" , note);
                Log.e("audio" , note.getFilepath());
                    startActivity(intent);
            } else if (resultCode == RESULT_CANCELED) {
                // Oops! User has canceled the recording
            }
        }
    }

}
