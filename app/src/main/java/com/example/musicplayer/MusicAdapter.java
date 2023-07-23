package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.service.Result;
import com.example.musicplayer.service.SongChangeListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    private final List<Result> list;
    private final Context context;
    private int playPosition = 0;
    private final SongChangeListener songChangeListener;

    public MusicAdapter(List<Result> list, Context context) {
        this.list = list;
        this.context = context;
        this.songChangeListener = ((SongChangeListener)context);
    }

    @NonNull
    @Override
    public MusicAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.music_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.MyViewHolder holder, int position) {
        Result list2 = list.get(position);

        if(list2.isPlaying()) {
            playPosition = position;
            holder.rootLayout.setBackgroundResource(R.drawable.background_purple);
        } else {
            holder.rootLayout.setBackgroundResource(R.drawable.background_black);
        }

        String generateDuration = String.format(Locale.getDefault(), "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(list2.getTrackTimeMillis())),
                TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(list2.getTrackTimeMillis())) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(list2.getTrackTimeMillis()))));

        holder.title.setText(list2.getTrackName());
        holder.artist.setText(list2.getArtistName());
        Picasso.get().load(list2.getArtworkUrl100()).into(holder.imgPhoto);
        holder.musicDuration.setText(generateDuration);

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.get(playPosition).setPlaying(false);
                list2.setPlaying(true);

                songChangeListener.onChanged(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout rootLayout;
        private final TextView title;
        private final TextView artist;

        private final ImageView imgPhoto;

        private final TextView musicDuration;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            rootLayout = itemView.findViewById(R.id.musicLayout);
            title = itemView.findViewById(R.id.musicTitle);
            artist = itemView.findViewById(R.id.musicArtist);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            musicDuration = itemView.findViewById(R.id.musicDuration);
        }
    }
}
