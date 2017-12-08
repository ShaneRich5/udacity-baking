package com.shane.baking.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shane.baking.R;
import com.shane.baking.data.Step;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shane on 9/30/2017.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private final Context context;
    private final OnClickHandler clickHandler;
    private List<Step> steps;
    private int selectedIndex = RecyclerView.NO_POSITION;

    public interface OnClickHandler {
        public void onClick(Step step);
    }

    public StepAdapter(Context context, OnClickHandler clickHandler, List<Step> steps) {
        this.context = context;
        this.clickHandler = clickHandler;
        this.steps = steps;
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
        int colorId = (selectedIndex != position) ? Color.WHITE :
                ResourcesCompat.getColor(context.getResources(), R.color.primary_light, null);
        holder.itemView.setBackgroundColor(colorId);
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
            String description;

            if (getAdapterPosition() == 0) description = step.getSummary();
            else description = String.format(Locale.getDefault(),
                    "%d. %s", getAdapterPosition(), step.getSummary());

            descriptionTextView.setText(description);
        }


        @Override
        public void onClick(View view) {
            final int INDEX_OFFSET = 1;
            clickHandler.onClick(steps.get(getAdapterPosition()));

            if (selectedIndex == getAdapterPosition()
                    || getAdapterPosition() == RecyclerView.NO_POSITION) return;

            int oldSelectedIndex = selectedIndex;
            selectedIndex = getAdapterPosition();
            notifyItemChanged(oldSelectedIndex);
            notifyItemChanged(selectedIndex);
        }
    }
}
