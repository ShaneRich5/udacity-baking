package com.shane.baking.ui.steps;

import android.support.annotation.NonNull;

import com.shane.baking.data.Step;

public class StepContract {

    interface View {
        void showDescription(@NonNull String description);

        void hideVideoPlayer();

        void setupVideoPlayer(@NonNull String url);

        void updateStepViews(@NonNull Step step);

        void releaseVideoPlayer();

    }
}
