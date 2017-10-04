package com.shane.baking.ui;

import android.app.Dialog;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.shane.baking.models.Step;
import com.shane.baking.utils.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shane.baking.utils.Constants.ARG_STEP;


public class StepFragment extends Fragment implements ExoPlayer.EventListener {
    public static final String TAG = StepFragment.class.getName();
    public static final String STATE_SEEK_POSITION = "seek_position";
    public static final String STATE_IS_PLAYING = "is_playing";

    @BindView(R.id.description_text_view) TextView descriptionTextView;
    @BindView(R.id.player_view) SimpleExoPlayerView exoPlayerView;

    private PlaybackStateCompat.Builder playbackStateBuilder;
    private MediaSessionCompat mediaSession;
    private SimpleExoPlayer exoPlayer;
    private Dialog fullscreenDialog;
    private Step step;

    public StepFragment() {}

    public static StepFragment newInstance(Step step) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_STEP, step);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null && getArguments().getParcelable(ARG_STEP) != null) {
            step = getArguments().getParcelable(ARG_STEP);
            setupViewsWithStep(step);
        }
    }

    private void setupViewsWithStep(@NonNull Step step) {
        descriptionTextView.setText(step.getDescription());
        String url = getVideoUrl(step);

        if (StringUtil.EMPTY.equals(url)) {
            exoPlayerView.setVisibility(View.GONE);
        } else {
            initializeMediaSession();

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                startVideoInFullscreen();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (exoPlayerView.getVisibility() != View.GONE) {
            initializeVideoPlayer(getVideoUrl(step));
        }
    }

    private void startVideoInFullscreen() {
        fullscreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                closeFullscreenVideo();
            }
        };
        ((ViewGroup) exoPlayerView.getParent()).removeView(exoPlayerView);
        fullscreenDialog.addContentView(exoPlayerView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        fullscreenDialog.show();

    }

    private void closeFullscreenVideo() {
        ((StepActivity) getContext()).finish();
    }

    private String getVideoUrl(@NonNull Step step) {
        if ( ! TextUtils.isEmpty(step.getVideoUrl())) return step.getVideoUrl();
        if ( ! TextUtils.isEmpty(step.getThumbnailUrl())) return step.getThumbnailUrl();
        return StringUtil.EMPTY;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mediaSession != null) {
            mediaSession.setActive(false);
        }
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

    private void releasePlayer() {
        if (exoPlayer == null) return;
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
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
    public void onPositionDiscontinuity() {}

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
