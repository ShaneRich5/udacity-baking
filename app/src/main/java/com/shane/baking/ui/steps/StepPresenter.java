package com.shane.baking.ui.steps;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.shane.baking.data.Step;
import com.shane.baking.utils.StringUtil;

import io.reactivex.disposables.CompositeDisposable;

public class StepPresenter implements StepContract.Presenter {

    @NonNull
    private StepContract.View stepView;

    @NonNull
    private CompositeDisposable compositeDisposable;

    @NonNull
    private Step step;

    public StepPresenter(@NonNull StepContract.View stepView,
                         @NonNull Step step) {
        this.stepView = stepView;
        this.compositeDisposable = new CompositeDisposable();
        this.step = step;
    }

    @Override
    public void subscribe() {
        stepView.showDescription(step.getDescription());
        final String videoUrl = getVideoUrl(step);

        if (TextUtils.isEmpty(videoUrl)) {
            stepView.hideVideoPlayer();
        } else {
            stepView.setupVideoPlayer(videoUrl);
        }
    }

    @Override
    public void unsubscribe() {

    }

    private String getVideoUrl(@NonNull Step step) {
        if ( ! TextUtils.isEmpty(step.getVideoUrl())) return step.getVideoUrl();
        if ( ! TextUtils.isEmpty(step.getThumbnailUrl())) return step.getThumbnailUrl();
        return StringUtil.EMPTY;
    }
}
