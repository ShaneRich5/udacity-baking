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
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_step, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Step> steps;

        public SectionsPagerAdapter(FragmentManager fm, List<Step> steps) {
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
            final int POSITION_OFFSET = 1;
            return String.format(Locale.getDefault(), "Step %d", (position + POSITION_OFFSET));
        }


    }
}
