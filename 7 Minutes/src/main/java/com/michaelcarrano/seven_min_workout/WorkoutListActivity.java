package com.michaelcarrano.seven_min_workout;

import com.crashlytics.android.Crashlytics;
import com.michaelcarrano.seven_min_workout.data.WorkoutContent;
import com.michaelcarrano.seven_min_workout.data.WorkoutContent.Workout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


/**
 * An activity representing a list of Workouts. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link WorkoutDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two vertical panes. <p>
 * The activity makes heavy use of fragments. The list of items is a {@link WorkoutListFragment} and
 * the item details (if present) is a {@link WorkoutDetailFragment}. <p> This activity also
 * implements the required {@link WorkoutListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class WorkoutListActivity extends FragmentActivity implements WorkoutListFragment.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);

        // Add workouts to display
        if (WorkoutContent.WORKOUTS.isEmpty()) {
            WorkoutContent.addWorkout(new Workout("1", getString(R.string.jumping_jacks),
                    getString(R.string.jumping_jacks_desc), getString(R.string.jumping_jacks_video),
                    getString(R.color.jumping_jacks_dark), getString(R.color.jumping_jacks_light)));
            WorkoutContent.addWorkout(new Workout("2", getString(R.string.wall_sits),
                    getString(R.string.wall_sits_desc), getString(R.string.wall_sits_video),
                    getString(R.color.wall_sits_dark), getString(R.color.wall_sits_light)));
            WorkoutContent.addWorkout(new Workout("3", getString(R.string.push_ups),
                    getString(R.string.push_ups_desc), getString(R.string.push_ups_video),
                    getString(R.color.push_ups_dark), getString(R.color.push_ups_light)));
            WorkoutContent.addWorkout(new Workout("4", getString(R.string.abdominal_crunches),
                    getString(R.string.abdominal_crunches_desc),
                    getString(R.string.abdominal_crunches_video),
                    getString(R.color.abdominal_crunches_dark),
                    getString(R.color.abdominal_crunches_light)));
            WorkoutContent.addWorkout(new Workout("5", getString(R.string.step_ups_onto_a_chair),
                    getString(R.string.step_ups_onto_a_chair_desc),
                    getString(R.string.step_ups_onto_a_chair_video),
                    getString(R.color.step_ups_onto_a_chair_dark),
                    getString(R.color.step_ups_onto_a_chair_light)));
            WorkoutContent.addWorkout(
                    new Workout("6", getString(R.string.squats), getString(R.string.squats_desc),
                            getString(R.string.squats_video), getString(R.color.squats_dark),
                            getString(R.color.squats_light)));
            WorkoutContent.addWorkout(new Workout("7", getString(R.string.triceps_dips_on_a_chair),
                    getString(R.string.triceps_dips_on_a_chair_desc),
                    getString(R.string.triceps_dips_on_a_chair_video),
                    getString(R.color.triceps_dips_on_a_chair_dark),
                    getString(R.color.triceps_dips_on_a_chair_light)));
            WorkoutContent.addWorkout(
                    new Workout("8", getString(R.string.planks), getString(R.string.planks_desc),
                            getString(R.string.planks_video), getString(R.color.planks_dark),
                            getString(R.color.planks_light)));
            WorkoutContent.addWorkout(
                    new Workout("9", getString(R.string.high_knees_running_in_place),
                            getString(R.string.high_knees_running_in_place_desc),
                            getString(R.string.high_knees_running_in_place_video),
                            getString(R.color.high_knees_running_in_place_dark),
                            getString(R.color.high_knees_running_in_place_light)));
            WorkoutContent.addWorkout(
                    new Workout("10", getString(R.string.lunges), getString(R.string.lunges_desc),
                            getString(R.string.lunges_video), getString(R.color.lunges_dark),
                            getString(R.color.lunges_light)));
            WorkoutContent.addWorkout(new Workout("11", getString(R.string.push_ups_and_rotations),
                    getString(R.string.push_ups_and_rotations_desc),
                    getString(R.string.push_ups_and_rotations_video),
                    getString(R.color.push_ups_and_rotations_dark),
                    getString(R.color.push_ups_and_rotations_light)));
            WorkoutContent.addWorkout(new Workout("12", getString(R.string.side_planks),
                    getString(R.string.side_planks_desc), getString(R.string.side_planks_video),
                    getString(R.color.side_planks_dark), getString(R.color.side_planks_light)));
        }

        setContentView(R.layout.activity_workout_list);

        getActionBar().setTitle(getString(R.string.app_label));

    }

    /**
     * Callback method from {@link WorkoutListFragment.Callbacks} indicating that the workout with
     * the given ID was selected.
     */
    @Override
    public void onItemSelected(int position) {
        // Start the detail activity for the selected workout ID.
        Intent detailIntent = new Intent(this, WorkoutDetailActivity.class);
        detailIntent.putExtra(WorkoutDetailFragment.ARG_WORKOUT_POS, position);
        startActivity(detailIntent);
    }

    /**
     * Add menu items to ActionBar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Handle menu item clicks
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about_app:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
            case R.id.menu_start_workout:
                Intent workoutIntent = new Intent(this, WorkoutCountdownActivity.class);
                startActivity(workoutIntent);
                break;
        }
        return true;
    }
}
