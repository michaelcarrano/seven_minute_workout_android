package com.michaelcarrano.seven_min_workout;

import com.michaelcarrano.seven_min_workout.data.WorkoutContent;
import com.michaelcarrano.seven_min_workout.widget.CircularProgressBar;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by michaelcarrano on 12/6/13.
 */
public class WorkoutCountdownFragment extends Fragment {

    private static int REMAINING_TIME;          // Time remaining (ie: device rotation)

    /**
     * The time spent for each activity (exercise or rest)
     */
    private final int EXERCISE_TIME = 30000;    // 30 seconds

    private final int REST_TIME = 10000;        // 10 seconds

    /**
     * Keeps track of the current workout
     */
    private WorkoutContent.Workout mWorkout;

    /**
     * Keeps track of the workout position (ie: 3/12)
     */
    private int mWorkoutPos = 0;

    /**
     * Keeps track of the current CountDownTimer
     */
    private CountDownTimer mCountDownTimer;

    /**
     * The CircularProgressBar used
     */
    private CircularProgressBar mCircularProgressBar;

    /**
     * Tracks if the workout has started
     */
    private boolean workoutInProgress = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain instance on device rotation
        setRetainInstance(true);

        // Set mWorkout to the first workout
        mWorkout = WorkoutContent.WORKOUTS.get(mWorkoutPos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View rootView = inflater
                .inflate(R.layout.fragment_workout_countdown, container, false);
        mCircularProgressBar = (CircularProgressBar) rootView
                .findViewById(R.id.workout_countdown_time);
//        rootView.setBackgroundColor(Color.parseColor(mWorkout.light));

        // Start off with 10 second rest, then alternate
        if (!workoutInProgress) {
            rest(rootView);
        } else {
            exercise(rootView);
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Cancel the countdown
        mCountDownTimer.cancel();
    }

    private void rest(final View rootView) {
        mCountDownTimer = new CountDownTimer(REST_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                REMAINING_TIME = (int) (millisUntilFinished / 1000);

                TextView name = (TextView) rootView.findViewById(R.id.workout_countdown_name);
                if (!workoutInProgress) {
                    name.setText(R.string.get_ready);
                } else {
                    name.setText(R.string.rest);
                }

                TextView id = (TextView) rootView.findViewById(R.id.workout_countdown_id);
                id.setText(mWorkout.id);

                id.setBackgroundColor(Color.parseColor(mWorkout.dark));
                name.setBackgroundColor(Color.parseColor(mWorkout.light));

//                TextView time = (TextView) rootView.findViewById(R.id.workout_countdown_time);
//                time.setText("" + millisUntilFinished / 1000);
                mCircularProgressBar.setMax(REST_TIME / 1000);
                mCircularProgressBar.setProgress(REMAINING_TIME);

            }

            @Override
            public void onFinish() {
                workoutInProgress = true;
                exercise(rootView);
            }
        };
        mCountDownTimer.start();
    }

    private void exercise(final View rootView) {
        mCountDownTimer = new CountDownTimer(EXERCISE_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                REMAINING_TIME = (int) (millisUntilFinished / 1000);

                TextView ready = (TextView) rootView.findViewById(R.id.workout_countdown_name);
                ready.setText(mWorkout.name);

                TextView id = (TextView) rootView.findViewById(R.id.workout_countdown_id);
                id.setText(mWorkout.id);

//                TextView time = (TextView) rootView.findViewById(R.id.workout_countdown_time);
//                time.setText("" + millisUntilFinished / 1000);
                mCircularProgressBar.setMax(EXERCISE_TIME / 1000);
                mCircularProgressBar.setProgress(REMAINING_TIME);

            }

            @Override
            public void onFinish() {
                if (++mWorkoutPos < WorkoutContent.WORKOUTS.size()) {
                    mWorkout = WorkoutContent.WORKOUTS.get(mWorkoutPos);
//                    rootView.setBackgroundColor(Color.parseColor(mWorkout.light));
                    rest(rootView);
                } else {
                    finish(rootView);
                }
            }
        };
        mCountDownTimer.start();
    }

    private void finish(View rootView) {
        mCountDownTimer.cancel();
        // hide the current views
        LinearLayout info = (LinearLayout) rootView.findViewById(R.id.workout_countdown_info);
        info.setVisibility(View.GONE);
        mCircularProgressBar.setVisibility(View.GONE);

        // display "finished"
        TextView textView = (TextView) rootView.findViewById(R.id.workout_countdown_finished);
        textView.setVisibility(View.VISIBLE);
    }

}
