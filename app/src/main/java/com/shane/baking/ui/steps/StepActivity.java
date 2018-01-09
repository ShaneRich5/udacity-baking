package com.shane.baking.ui.steps;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.shane.baking.R;
import com.shane.baking.data.Step;
import com.shane.baking.ui.BaseActivity;

import java.util.List;

import butterknife.BindView;

public class StepActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.container)
    ViewPager viewPager;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    public static final String EXTRA_STEP_LIST = "extra_step_list";
    public static final String EXTRA_STEP_SELECTED = "extra_step_selected";

    private StepContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        setSupportActionBar(toolbar);

        Intent startingIntent = getIntent();

        if ( ! startingIntent.hasExtra(EXTRA_STEP_LIST)) {
            finish();
            return;
        }

        List<Step> steps = startingIntent.getParcelableArrayListExtra(EXTRA_STEP_LIST);

        StepPagerAdapter stepPagerAdapter = new StepPagerAdapter(getSupportFragmentManager(), steps);
        viewPager.setAdapter(stepPagerAdapter);

        if (startingIntent.hasExtra(EXTRA_STEP_SELECTED)) {
            Step selectedStep = startingIntent.getParcelableExtra(EXTRA_STEP_SELECTED);

            int currentPosition = findSelectedStep(steps, selectedStep);

            if (currentPosition >= 0) {
                viewPager.setCurrentItem(currentPosition, true);
            }
        }

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        ActionBar actionBar = getSupportActionBar();

        if ( actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private int findSelectedStep(@NonNull List<Step> steps, @NonNull Step selectedStep) {
        for (int i = 0; i < steps.size(); i++) {
            Step step = steps.get(i);
            if (step.getNumber() == selectedStep.getNumber()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class StepPagerAdapter extends FragmentStatePagerAdapter {

        @NonNull
        private final List<Step> steps;

        StepPagerAdapter(FragmentManager fm, @NonNull List<Step> steps) {
            super(fm);
            this.steps = steps;
        }

        @Override
        public Fragment getItem(int position) {
            final Step step = steps.get(position);
            final StepFragment fragment = new StepFragment();
            presenter = new StepPresenter(fragment, step);
            return fragment;
        }

        @Override
        public int getCount() {
            return steps.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            Resources resources = getResources();
            if (position == 0) {
                return resources.getString(R.string.title_activity_step_introduction);
            } else {
                return String.format(resources.getString(R.string.title_activity_step), position);
            }
        }
    }
}
