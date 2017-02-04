package com.thermatk.android.princessviewer.controllers;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.ControllerChangeHandler;
import com.bluelinelabs.conductor.ControllerChangeType;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.thermatk.android.princessviewer.R;

import static com.thermatk.android.princessviewer.utils.BuildBundle.createBundleWithString;

public class VideoPlayController extends Controller{
    private final static String BUNDLE_KEY = "link";
    private String link;

    private SimpleExoPlayerView playerView;
    SimpleExoPlayer player;
    public VideoPlayController(@Nullable Bundle args) {
        super(args);
    }

    public VideoPlayController(String link) {
        this(createBundleWithString(BUNDLE_KEY,link));
    }

    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_videoplay, container, false);
        Context ctx = view.getContext();

        link = getArgs().getString(BUNDLE_KEY);
        playerView = (SimpleExoPlayerView) view.findViewById(R.id.player_view);


        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveVideoTrackSelection.Factory(null);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

        // 3. Create the player
        player =
                ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector, loadControl);

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getActivity(), "katyagram"));
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.

        Uri uri = Uri.parse(link);
        MediaSource videoSource = new ExtractorMediaSource(uri,dataSourceFactory, extractorsFactory, mainHandler, null);
        // Prepare the player with the source.
        player.prepare(videoSource);
        player.setPlayWhenReady(true);

        // Bind the player to the view.
        playerView.setPlayer(player);

        return view;
    }

    @Override
    protected void onChangeStarted(@NonNull ControllerChangeHandler changeHandler, @NonNull ControllerChangeType changeType) {
        setOptionsMenuHidden(!changeType.isEnter);
        if (changeType.isEnter) {
            Log.d("katyagram", "first creating");
        }
    }

    @Override
    protected void onDetach(View view) {

        Log.d("kg", player.getVideoFormat().toString() + player.getVideoScalingMode() + player.getVideoDecoderCounters().toString() + player.getVideoFormat().frameRate);
        player.release();
        super.onDetach(view);
    }


    @Override
    protected void onDestroyView(View view) {

        Log.d("kg", "VIEW DESTROYED");
        super.onDestroyView(view);
    }

}
