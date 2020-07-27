package com.example.internshipproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class FilmPlayer {
    private static String TAG = FilmPlayer.class.getSimpleName();
    private boolean videoPlayerIsShowed;

    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private Context context;
    private String filmUri;
    private static FilmPlayer filmPlayer;

    private FilmPlayer() {
    }

    public static FilmPlayer newInstance(Context context){
        if (filmPlayer == null){
            filmPlayer = new FilmPlayer();
            filmPlayer.context = context;
            filmPlayer.filmUri = "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4";
        }
        return filmPlayer;
    }

    public void initializePlayer() {
        player = new SimpleExoPlayer.Builder(context).build();
        Uri uri = Uri.parse(filmUri);
        MediaSource mediaSource = buildMediaSource(uri);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, false, false);
    }

    public void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    /**
     * Builds media source
     * @param uri is a uri of video that have to be displays on the screen
     * @return built media source
     */
    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(context,
                        Util.getUserAgent(context, "FilmLover"));
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }
//
//    @SuppressLint("InlinedApi")
//    private void hideSystemUi() {
//        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//    }
    /**
     * Sets player on pause.
     */
    public void pausePlayer(){
        if (BuildConfig.DEBUG) Log.d(TAG, "pausePlayer: player sets on pause");
        player.setPlayWhenReady(false);
        player.getPlaybackState();
    }

    /**
     * Starts player playing.
     */
    public void startPlayer(){
        if (BuildConfig.DEBUG) Log.d(TAG, "startPlayer: player is starting");
        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }

    public SimpleExoPlayer getPlayer() {
        return player;
    }

    public void setPlayer(SimpleExoPlayer player) {
        this.player = player;
    }
}
