package com.shane.baking.ui.steps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.shane.baking.R;
import com.shane.baking.ui.BaseFragment;

import butterknife.BindView;


public class StepFragment extends BaseFragment implements StepContract.View {
    @BindView(R.id.player_frame)
    FrameLayout playerFrame;

    @BindView(R.id.description_text_view)
    TextView descriptionTextView;

    @BindView(R.id.player_view)
    SimpleExoPlayerView exoPlayerView;


    private StepContract.Presenter presenter;

    public StepFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public void setPresenter(StepContract.Presenter presenter) {
        this.presenter = presenter;
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

    }
}
