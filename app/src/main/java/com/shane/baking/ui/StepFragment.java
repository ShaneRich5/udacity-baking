package com.shane.baking.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shane.baking.R;
import com.shane.baking.models.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shane.baking.utils.Constants.ARG_STEP;


public class StepFragment extends Fragment {

    @BindView(R.id.description_text_view) TextView descriptionTextView;

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

        if (getArguments() == null) return;

        Step step = getArguments().getParcelable(ARG_STEP);

        if (step == null) return;

        descriptionTextView.setText(step.getDescription());
    }
}
