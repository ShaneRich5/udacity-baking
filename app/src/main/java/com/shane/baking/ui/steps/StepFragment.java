package com.shane.baking.ui.steps;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.shane.baking.R;
import com.shane.baking.data.Step;
import com.shane.baking.ui.base.BaseFragment;

import butterknife.BindView;


public class StepFragment extends BaseFragment implements StepContract.View, ExoPlayer.EventListener {
    @BindView(R.id.player_frame)
    FrameLayout playerFrame;

    @BindView(R.id.description_text_view)
    TextView descriptionTextView;

    @BindView(R.id.player_view)
    SimpleExoPlayerView exoPlayerView;

    public static final String TAG = StepFragment.class.getSimpleName();
    public static final String EXTRA_STEP = "step";
    public static final String STATE_SEEK_POSITION = "seek_position";
    public static final String STATE_IS_PLAYING = "is_playing";

    private PlaybackStateCompat.Builder playbackStateBuilder;
    private MediaSessionCompat mediaSession;
    private SimpleExoPlayer exoPlayer;
    private Dialog fullscreenDialog;

    private Step cachedStep;
    private long seekPosition = 0;

    public StepFragment() {
    }

    public static StepFragment newInstance(@NonNull Step step) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_STEP, step);

        StepFragment fragment = new StepFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step, container, false);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(STATE_SEEK_POSITION, seekPosition);
        outState.putParcelable(EXTRA_STEP, cachedStep);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            seekPosition = savedInstanceState.getLong(STATE_SEEK_POSITION, 0);
            final Step step = savedInstanceState.getParcelable(EXTRA_STEP);

            if (step == null) {
                Toast.makeText(getContext(), "Error loading step", Toast.LENGTH_SHORT).show();
            } else {
                cachedStep = step;
                updateStepViews(step);
            }
        }
    }

    @Override
    public void updateStepViews(@NonNull Step step) {
        final String description = step.getDescription();
        final String videoUrl = step.formatVideoUrl();

        showDescription(description);

        if (TextUtils.isEmpty(videoUrl)) {
            hideVideoPlayer();
        } else {
            setupVideoPlayer(videoUrl);
        }
    }

    @Override
    public void showDescription(@NonNull String description) {
        descriptionTextView.setText(description);
    }

    @Override
    public void hideVideoPlayer() {
        exoPlayerView.setVisibility(View.GONE);
    }

    @Override
    public void setupVideoPlayer(@NonNull String url) {
        initializeMediaSession();
        initializeVideoPlayer(url);
    }

    @Override
    public void releaseVideoPlayer() {
        if (exoPlayer == null) return;
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }

    private void initializeVideoPlayer(String url) {
        if (exoPlayer != null) return;

        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();

        exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        exoPlayerView.setPlayer(exoPlayer);

        Uri videoUri = Uri.parse(url);
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);


        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource videoSource = new ExtractorMediaSource(videoUri, dataSourceFactory, extractorsFactory, null, null);

        exoPlayer.seekTo(seekPosition);
        exoPlayer.addListener(this);
        exoPlayer.prepare(videoSource);
        exoPlayer.setPlayWhenReady(false);
    }

    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(getContext(), TAG);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mediaSession.setMediaButtonReceiver(null);
        playbackStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY
                        | PlaybackStateCompat.ACTION_PAUSE
                        | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                        | PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(playbackStateBuilder.build());
        mediaSession.setCallback(new MediaSession());
        mediaSession.setActive(true);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private class MediaSession extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            super.onPlay();
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            super.onPause();
            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            exoPlayer.seekTo(0);
        }
    }
}
