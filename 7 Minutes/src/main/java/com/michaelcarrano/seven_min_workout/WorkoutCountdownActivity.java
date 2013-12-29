package com.michaelcarrano.seven_min_workout;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * An Activity that represents the Workout timer. Each workout is 30 seconds long with 10 seconds of
 * rest in between.
 *
 * TODO: Allow for customizing the workout period and rest period
 *
 * @author michaelcarrano
 */
public class WorkoutCountdownActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workout_countdown);

        // Set the ActionBar title and up button
        getActionBar().setTitle(getString(R.string.app_label));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            WorkoutCountdownFragment fragment = new WorkoutCountdownFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.workout_countdown_container,
                    fragment)
                    .commit();
        } else {
            Toast.makeText(this, "Countdown Activity", Toast.LENGTH_LONG).show();
        }

    }

}
