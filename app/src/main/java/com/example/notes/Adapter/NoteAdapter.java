package com.example.notes.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.AsyncTask.DeleteAsyncTask;
import com.example.notes.Listeners.UpdateNote;
import com.example.notes.Persistance.TextNoteDatabase;
import com.example.notes.R;
import com.example.notes.bean.TextNote;

import java.io.IOException;
import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<TextNote> notes;
    private TextNote mRecentlyDeletedItem;
    TextNoteDatabase database;
    private UpdateNote listner;
    static final int TYPE_AUDIO = 1;
    static final int TYPE_TEXT = 0;

    public NoteAdapter(Context context, ArrayList<TextNote> notes, UpdateNote listner) {
        this.context = context;
        this.notes = notes;
        this.listner = listner;
        database = TextNoteDatabase.getInstance(context);
    }






    @Override
    public int getItemCount() {
        return notes.size();
    }


    public void deleteItem(int position) {
        mRecentlyDeletedItem = (TextNote) notes.get(position);
        DeleteAsyncTask deleteAsyncTask = new DeleteAsyncTask(database.getDAO());
        deleteAsyncTask.execute((TextNote) notes.get(position));
        notes.remove(position);
        notifyDataSetChanged();
        Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_AUDIO) {
            View view = LayoutInflater.from(context).inflate(R.layout.single_item_audio, parent, false);
            ViewHolderAudio viewHolder = new ViewHolderAudio(view);
            return viewHolder;
        } else if (viewType == TYPE_TEXT) {
            View view = LayoutInflater.from(context).inflate(R.layout.single_item_text_note, parent, false);
            ViewHolder viewHolder = new ViewHolder(view, listner);
            return viewHolder;
        } else {
            throw
                    new RuntimeException();
        }    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case TYPE_AUDIO:
                initLayoutAudio((ViewHolderAudio)holder, position);
                break;
            case TYPE_TEXT:
                initLayoutText((ViewHolder) holder, position);
                break;
            default:
                break;
        }




    }

    private void initLayoutAudio(final ViewHolderAudio holder, final int position) {
        holder.pause.setVisibility(View.INVISIBLE);
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = new MediaPlayer();

                try {
                    mediaPlayer.setDataSource(notes.get(position).getFilepath());
                    mediaPlayer.start();
                    holder.play.setVisibility(View.GONE);
                    holder.pause.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



    }
    private void initLayoutText(ViewHolder holder, int position) {
        holder.title.setText(notes.get(position).getTitle());
        holder.desc.setText(notes.get(position).getDescription());
        holder.publish_date.setText(String.valueOf(notes.get(position).getPublished()));

    }

        @Override
    public int getItemViewType(int position) {
        TextNote textNote = notes.get(position);
        if (textNote.getItemtype() == 1) {
            return TYPE_AUDIO;
        } else if (textNote.getItemtype() == 2) {
            return TYPE_TEXT;
        }
        return -1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView publish_date;
        TextView desc;


        public ViewHolder(@NonNull View itemView, final UpdateNote listner) {
            super(itemView);

            title = itemView.findViewById(R.id.note_title);
            publish_date = itemView.findViewById(R.id.note_publish_date);
            desc = itemView.findViewById(R.id.note_desc);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.onNoteUpdated((TextNote) notes.get(getAdapterPosition()), false);
                }
            });
        }


    }

    class ViewHolderAudio extends RecyclerView.ViewHolder {
            ImageButton pause , play;
            SeekBar seekBar;
        public ViewHolderAudio(@NonNull View itemView ) {
            super(itemView);
            pause = itemView.findViewById(R.id.btnPause);
            play = itemView.findViewById(R.id.btnPlay);
            seekBar = itemView.findViewById(R.id.seekbar);
        }
    }
}
