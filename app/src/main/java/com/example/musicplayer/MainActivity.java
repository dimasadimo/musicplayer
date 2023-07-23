package com.example.musicplayer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.musicplayer.service.ItunesMusicServices;
import com.example.musicplayer.service.Result;
import com.example.musicplayer.service.SongChangeListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SongChangeListener {

    private MediaPlayer mediaPlayer;
    private EditText txtSearch;
    private List<Result> results;
    private ItunesMusicServices service;
    private RecyclerView musicRecyclerView;
    private boolean isPlaying = false;
    private ImageView playImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        View decodeView =  getWindow().getDecorView();

        int options = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decodeView.setSystemUiVisibility(options);

        setContentView(R.layout.activity_main);

        txtSearch = findViewById(R.id.txtSearch);
        musicRecyclerView = findViewById(R.id.musicRecycleViewer);
        final CardView playBtn = findViewById(R.id.playBtn);
        playImg = findViewById(R.id.playImg);
        final ImageView prevBtn = findViewById(R.id.prevBtn);
        final ImageView nextBtn = findViewById(R.id.nextBtn);

        musicRecyclerView.setHasFixedSize(true);
        musicRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        service = new ItunesMusicServices();
        mediaPlayer = new MediaPlayer();

        permissionAccess();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void permissionAccess(){
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if ( permission == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permission Granted ", Toast.LENGTH_SHORT).show();
        } else{
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        }
    }

    public void getMusicInfo(String name) {
        results = new ArrayList<>();
        service.searchSongsByTerm(name,(isNetworkError, statusCode, root) -> {
            if (!isNetworkError) {
                if (statusCode == 200) {
                    for (Result e:  root.getResults()) {
                        results.add(new Result(e.getTrackId(), e.getArtistName(), e.getTrackName(), e.getPreviewUrl(), e.getArtworkUrl100(), e.getTrackTimeMillis(), e.isPlaying()));
                    }
                    runOnUiThread(() -> {
                        musicRecyclerView.setAdapter(new MusicAdapter(results, this));
                    });
                } else {
                    Log.d("iTunes", "Service error");
                }
            } else {
                Log.d("iTunes", "Network error");
            }
        });
    }

    public void btnGetInfoOnClick(View view){
        getMusicInfo(txtSearch.getText().toString());
    }

    @Override
    public void onChanged(int position) {
        mediaPlayer.pause();
        mediaPlayer.reset();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mediaPlayer.setAudioAttributes(
                            new AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .build()
                    );
                    mediaPlayer.setDataSource(results.get(position).getPreviewUrl());
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        playImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying) {
                    isPlaying = false;
                    mediaPlayer.pause();
                    playImg.setImageResource(R.drawable.play_icon);
                } else {
                    isPlaying = true;
                    mediaPlayer.start();
                    playImg.setImageResource(R.drawable.pause_icon);
                }
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isPlaying = true;
                mp.start();
                playImg.setImageResource(R.drawable.pause_icon);
            }
        });
    }
}