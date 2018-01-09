package com.shane.baking.ui.steps;

import android.support.annotation.NonNull;

import com.shane.baking.ui.BasePresenter;
import com.shane.baking.ui.BaseView;

public class StepContract {

    interface View extends BaseView<Presenter> {
        void showDescription(@NonNull String description);

        void hideVideoPlayer();

        void setupVideoPlayer(@NonNull String url);

        void releaseVideoPlayer();
    }

    public interface Presenter extends BasePresenter {

    }
}
