package com.michaelcarrano.seven_min_workout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.michaelcarrano.seven_min_workout.data.WorkoutContent;

/**
 * An activity representing a single Workout detail screen. This activity is only used on handset
 * devices. On tablet-size devices, item details are presented side-by-side with a list of items in
 * a {@link WorkoutListActivity}. <p> This activity is mostly just a 'shell' activity containing
 * nothing more than a {@link WorkoutDetailFragment}.
 */
public class WorkoutDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);

        // Get the Workout that was selected
        int selected = getIntent().getExtras().getInt(WorkoutDetailFragment.ARG_WORKOUT_POS);
        WorkoutContent.Workout workout = (WorkoutContent.Workout) WorkoutContent.MENU_ITEMS.get(selected);
        Log.i("7min", "Selected: " + selected + " " + workout.name);

        toolbar.setTitle(workout.name);
        toolbar.setBackgroundColor(workout.light);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(workout.dark);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();

            arguments.putInt(WorkoutDetailFragment.ARG_WORKOUT_POS, selected);
            WorkoutDetailFragment fragment = new WorkoutDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.workout_detail_container,
                    fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, WorkoutListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
