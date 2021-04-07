package com.zubisoft.noterecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.util.Objects;

public class RecordPlayerActivity extends AppCompatActivity {

    SimpleExoPlayer player;
    private StyledPlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_player);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view->onBackPressed());

        player = new SimpleExoPlayer.Builder(this).build();

        // Bind the player to the view.
        playerView = findViewById(R.id.player_view);
        playerView.setPlayer(player);

        String noteTitle=getIntent().getStringExtra("title");
        Objects.requireNonNull(getSupportActionBar()).setTitle(noteTitle);
        String noteRecPath=getIntent().getStringExtra("recordPath");
        File file=new File(noteRecPath);

        // Build the media item.
        MediaItem mediaItem = MediaItem.fromUri(Uri.fromFile(file));
        // Set the media item to be played.
        player.setMediaItem(mediaItem);
        // Prepare the player.
        player.prepare();
        // Start the playback.
        player.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        if (Util.SDK_INT > 23) {
//            if (playerView != null) {
//                playerView.onPause();
//            }
//            releasePlayer();
//        }
//    }

    protected void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;

        }
    }
}