package com.example.namjiseong.bamboo1;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class StreamingActivity extends AppCompatActivity {

    private SimpleExoPlayer player;
    private PlayerView exoPlayerView;

    private Boolean playWhenReady = true;
    private int currentWindow = 0;
    private Long playbackPosition = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming);

        //String uri_sample = "http://192.168.43.39:8091/stream.html";
        String uri_sample = "https://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

        exoPlayerView = findViewById(R.id.exoPlayerView);

        player = ExoPlayerFactory.newSimpleInstance(getApplicationContext());
        // Bind the player to the view.
        exoPlayerView.setPlayer(player);


        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(),
                Util.getUserAgent(getApplicationContext(), "Bamboo1"));

        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(uri_sample));
        // Prepare the player with the source.
        player.prepare(videoSource);

        player.setPlayWhenReady(true);
    }

    public void onBackPressed(){
        player.stop();
        //android.os.Process.killProcess(android.os.Process.myPid());
        super.onBackPressed();
    }

}
