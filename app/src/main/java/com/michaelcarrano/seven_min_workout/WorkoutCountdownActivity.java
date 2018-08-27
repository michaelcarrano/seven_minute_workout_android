package com.michaelcarrano.seven_min_workout;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.michaelcarrano.seven_min_workout.Utils.RuntimeTypeAdapterFactory;
import com.michaelcarrano.seven_min_workout.data.ExerciseData;
import com.michaelcarrano.seven_min_workout.data.ExerciseStats;
import com.michaelcarrano.seven_min_workout.data.RepExercise;
import com.michaelcarrano.seven_min_workout.data.TimeExercise;

/**
 * An Activity that represents the Workout timer. Each workout is 30 seconds long with 10 seconds of
 * rest in between.
 * <p>
 * TODO: Allow for customizing the workout period and rest period
 *
 * @author michaelcarrano
 */
public class WorkoutCountdownActivity extends BaseActivity {

    WorkoutCountdownFragment fragment;
    private boolean textDisplayed = false;
    private LinearLayout workout_countdown_info_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workout_countdown);

        boolean resumePressed = getIntent().getBooleanExtra("ResumePressed", false);
        if (savedInstanceState == null) {
            fragment = new WorkoutCountdownFragment();

            // Pass ResumePressed to fragment
            Bundle bundle = new Bundle();
            bundle.putBoolean("ResumePressed", resumePressed);
            fragment.setArguments(bundle);

            // Start fragment
            getSupportFragmentManager().beginTransaction().add(R.id.workout_countdown_container,
                    fragment)
                    .commit();
        } else {
            Toast.makeText(this, "Countdown Activity", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {

        if (fragment.getWorkoutInProgress()) {

            if (!fragment.isPaused()) {
                fragment.getPlayPauseView().callOnClick();
            }

            // Setup
            SharedPreferences mPrefs = getSharedPreferences("PausedWorkout", MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();

            // Save boolean indicating workout is paused
            prefsEditor.putBoolean("WorkoutIsPaused", true);

            // Save current stats
            RuntimeTypeAdapterFactory runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                    .of(ExerciseStats.class, "type")
                    .registerSubtype(RepExercise.class, "rep")
                    .registerSubtype(TimeExercise.class, "time");
            Gson gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();
            String json = gson.toJson(fragment.getStats().getExerciseStats());
            prefsEditor.putString("CurrentStats", json);

            // Save current exercise
            int workoutPos = fragment.getmWorkoutPos();
            boolean isResting = fragment.getIsResting();
            boolean isRep = fragment.isRep();
            prefsEditor.putBoolean("CurrentState", isResting);
            prefsEditor.putBoolean("IsRep", isRep);
            prefsEditor.putInt("CurrentExercise", workoutPos);

            // Save current time remaining
            float remainingTimeInSeconds = fragment.getRemainingTime();
            prefsEditor.putFloat("SecondsRemaining", remainingTimeInSeconds);

            // Apply changes to shared preference
            prefsEditor.apply();
        }
        super.onBackPressed();
    }

    /**
     * Called when the bottom bar layout in the workoutCountdownFragment is tapped and animates up or down depending on the current position.
     *
     * @param view
     */
    public void layoutClicked(View view) {
        workout_countdown_info_container = (LinearLayout) findViewById(R.id.workout_countdown_info_container);
        ViewGroup.LayoutParams params = workout_countdown_info_container.getLayoutParams();
        String text = ((TextView) findViewById(R.id.workout_countdown_name)).getText().toString();
        if (!textDisplayed && !(text.equals("Get Ready") || text.equals("Rest"))) {
            params.height = params.height + 900;
            workout_countdown_info_container.setLayoutParams(params);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 600, 1f);
            layoutParams.setMargins(40, 0, 40, 0);
            TextView textView = new TextView(this);
            applyText(textView, text);
            textView.setLayoutParams(layoutParams);
            textView.setTextSize(20);
            textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            textView.setGravity(Gravity.CENTER);
            workout_countdown_info_container.addView(textView);
            textDisplayed = true;
        } else if (text.equals("Get Ready") || text.equals("Rest")) {
            // Lock bar
        } else {
            workout_countdown_info_container.removeView(workout_countdown_info_container.getChildAt(workout_countdown_info_container.getChildCount() - 1));
            params.height = params.height - 900;
            workout_countdown_info_container.setLayoutParams(params);
            textDisplayed = false;
        }
    }

    /**
     * Closes the bottom bar in the WorkoutCountdownFragment when entering a rest stage.
     */
    public void closeBar() {
        ViewGroup.LayoutParams params = workout_countdown_info_container.getLayoutParams();
        workout_countdown_info_container.removeView(workout_countdown_info_container.getChildAt(workout_countdown_info_container.getChildCount() - 1));
        params.height = params.height - 900;
        workout_countdown_info_container.setLayoutParams(params);
        textDisplayed = false;
    }

    /**
     * Helper method that applies exercise instruction text inside the bottom bar in the WorkoutCountdownFragment.
     *
     * @param tv
     */
    private void applyText(TextView tv, String string) {
        String text = "";
        switch (string) {
            case "Jumping jacks":
                text = getString(R.string.jumping_jacks_desc);
                break;
            case "Wall sits":
                text = getString(R.string.wall_sits_desc);
                break;
            case "Push-ups":
                text = getString(R.string.push_ups_desc);
                break;
            case "Abdominal crunches":
                text = getString(R.string.abdominal_crunches_desc);
                break;
            case "Step-ups onto a chair":
                text = getString(R.string.step_ups_onto_a_chair_desc);
                break;
            case "Squats":
                text = getString(R.string.squats_desc);
                break;
            case "Triceps dips on a chair":
                text = getString(R.string.triceps_dips_on_a_chair_desc);
                break;
            case "Planks":
                text = getString(R.string.planks_desc);
                break;
            case "High knees running in place":
                text = getString(R.string.high_knees_running_in_place_desc);
                break;
            case "Lunges":
                text = getString(R.string.lunges_desc);
                break;
            case "Push-ups and rotations":
                text = getString(R.string.push_ups_and_rotations_desc);
                break;
            case "Side planks":
                text = getString(R.string.side_planks_desc);
                break;
        }
        tv.setText(text);
    }

    public boolean isTextDisplayed() {
        return textDisplayed;
    }

}
