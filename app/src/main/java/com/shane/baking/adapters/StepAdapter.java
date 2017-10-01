package com.shane.baking.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shane.baking.R;
import com.shane.baking.models.Step;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Shane on 9/30/2017.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private final Context context;
    private final OnClickHandler clickHandler;
    private List<Step> steps;


    public interface OnClickHandler {
        public void onClick(int stepId);
    }

    public StepAdapter(Context context, OnClickHandler clickHandler, List<Step> steps) {
        this.context = context;
        this.clickHandler = clickHandler;
        this.steps = steps;

        Timber.i(steps.toString());
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step_list, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        Step step = steps.get(position);
        holder.bind(step);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.description_text_view) TextView descriptionTextView;

        StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(@NonNull Step step) {
            int STEP_OFFSET = 1;
            int stepNumber = STEP_OFFSET + step.getId();
            descriptionTextView.setText(String.format(Locale.getDefault(),
                    "%d. %s", stepNumber, step.getSummary()));
            itemView.setId(step.getId());
        }

        @Override
        public void onClick(View view) {
            clickHandler.onClick(itemView.getId());
        }
    }
}
