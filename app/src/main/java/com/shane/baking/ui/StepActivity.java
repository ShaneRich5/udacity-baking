package com.shane.baking.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.shane.baking.R;
import com.shane.baking.models.Recipe;
import com.shane.baking.models.Step;
import com.shane.baking.utils.Constants;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.container) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Intent startingIntent = getIntent();

        if ( ! startingIntent.hasExtra(Constants.EXTRA_RECIPE)) {
            Toast.makeText(this, "failed to load steps", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Recipe recipe = startingIntent.getParcelableExtra(Constants.EXTRA_RECIPE);
        List<Step> steps = recipe.getSteps();

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), steps);
        viewPager.setAdapter(sectionsPagerAdapter);

        if (startingIntent.hasExtra(Constants.EXTRA_STEP_ID)) {
            int currentPosition = startingIntent.getIntExtra(Constants.EXTRA_STEP_ID, 0);
            viewPager.setCurrentItem(currentPosition, true);
        }

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Step> steps;

        SectionsPagerAdapter(FragmentManager fm, List<Step> steps) {
            super(fm);
            this.steps = steps;
        }

        @Override
        public Fragment getItem(int position) {
            Step step = steps.get(position);
            return StepFragment.newInstance(step);
        }

        @Override
        public int getCount() {
            return steps.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return "Intro";
            return String.format(Locale.getDefault(), "Step %d", position);
        }
    }
}
