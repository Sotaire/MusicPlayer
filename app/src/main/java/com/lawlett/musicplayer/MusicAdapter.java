package com.lawlett.musicplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lawlett.musicplayer.interfaces.OnSongClickListener;
import com.lawlett.musicplayer.models.SongModel;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    ArrayList<SongModel> songModels = new ArrayList<>();
    OnSongClickListener listener;

    public void setListener(OnSongClickListener listener) {
        this.listener = listener;
    }

    public void setSongs(ArrayList<SongModel> songModels) {
        this.songModels = songModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MusicViewHolder(LayoutInflater
        .from(parent.getContext())
        .inflate(R.layout.player_recycler_view_holder,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        holder.setSong(songModels.get(position).getArtist(),songModels.get(position).getSong());
    }

    @Override
    public int getItemCount() {
        return songModels.size();
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder {

        TextView artist;
        TextView song;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            artist = itemView.findViewById(R.id.artist_name_tv);
            song = itemView.findViewById(R.id.song_name_tv);
            itemView.findViewById(R.id.play_iv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onSongClick(getAdapterPosition());
                }
            });
        }

        public void setSong(String artistName, String songName){
            artist.setText(artistName);
            song.setText(songName);
        }
    }
}
