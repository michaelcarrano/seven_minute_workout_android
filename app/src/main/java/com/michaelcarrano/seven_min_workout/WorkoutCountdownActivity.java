package com.michaelcarrano.seven_min_workout;

import android.os.Bundle;
import android.widget.Toast;

/**
 * An Activity that represents the Workout timer. Each workout is 30 seconds long with 10 seconds of
 * rest in between.
 * <p>
 * TODO: Allow for customizing the workout period and rest period
 *
 * @author michaelcarrano
 */
public class WorkoutCountdownActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workout_countdown);

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
