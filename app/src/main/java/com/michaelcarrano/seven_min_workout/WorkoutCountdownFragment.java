package com.michaelcarrano.seven_min_workout;

import android.content.Context;
import android.media.MediaPlayer;
import android.animation.LayoutTransition;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.michaelcarrano.seven_min_workout.data.ExerciseStats;
import com.michaelcarrano.seven_min_workout.data.RepExercise;
import com.michaelcarrano.seven_min_workout.data.Stats;
import com.michaelcarrano.seven_min_workout.data.TimeExercise;
import com.michaelcarrano.seven_min_workout.data.WorkoutContent;
import com.michaelcarrano.seven_min_workout.widget.CircularProgressBar;
import com.ohoussein.playpause.PlayPauseView;

import org.w3c.dom.Text;

import java.sql.Time;

/**
 * Created by michaelcarrano on 12/6/13.
 */
public class WorkoutCountdownFragment extends Fragment {
    private boolean isPaused = false;
    private boolean isResting = true;
    private int secondOnTimer = 0;
    private static float REMAINING_TIME;          // Time remaining (ie: device rotation)
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

    private Stats stats;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain instance on device rotation
        setRetainInstance(true);

        // Set mWorkout to the first workout
        mWorkout = WorkoutContent.WORKOUTS.get(mWorkoutPos);

        // Initialize the stats variable
        stats = new Stats(getActivity());
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

        ((ViewGroup) rootView.findViewById(R.id.workout_countdown_info_container)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        ((ViewGroup) rootView.findViewById(R.id.countdown)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        pauseAndPlayButtonSetUp(rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Cancel the countdown
        mCountDownTimer.cancel();
    }

    private void setupCountDownTimer(final int millisInFuture, int countDownInterval, final int progressBarMax) {
        mCircularProgressBar.setMax(progressBarMax / 1000);
        mCountDownTimer = new CountDownTimer(millisInFuture, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isPaused) {
                    if (isResting) {
                        REMAINING_TIME = (millisUntilFinished / 1000.0f);
                        if (REMAINING_TIME < 3.59 && REMAINING_TIME > 3.49) {
                            MediaPlayer mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.mario_cart_start_sound);
                            mediaPlayer.start();
                        }
                        mCircularProgressBar.setProgress(REMAINING_TIME);

                    } else {
                        REMAINING_TIME = (millisUntilFinished / 1000.0f);
                        mCircularProgressBar.setProgress(REMAINING_TIME);
                    }
                }
            }

            @Override
            public void onFinish() {
                if (isResting) {
                    workoutInProgress = true;
                    exercise(getView());
                    if (mWorkoutPos != 0) {
                        TextView lastEx = (TextView) getView().findViewById(R.id.lastExerciseTextview);
                        lastEx.setText("");
                        ExerciseStats exercise = stats.getStats()[mWorkoutPos - 1];
                        exercise.incrementWorkoutsCompleted();
                        if (isRep) {
                            EditText tv = (EditText) getView().findViewById(R.id.repsCompletedPlainText);
                            if (!tv.getText().toString().equals("")) {
                                RepExercise re = (RepExercise) exercise;
                                int reps = Integer.valueOf(tv.getText().toString());
                                re.setCurrentReps(reps);
                                //do this stuff upon completion
//                                re.setCompletedLastTime(reps);
//                                if (reps > re.getPersonalBest()) {
//                                    re.setPersonalBest(reps);
//                                }
//                                re.addToTotalReps(reps);
//                                re.setPersoanlAvg(re.getTotalReps() / re.getWorkoutsCompleted());
                            }
                        } else {
                            CheckBox cb = (CheckBox) getView().findViewById(R.id.isCompletedCheckBox);
                            TimeExercise te = (TimeExercise) exercise;
                            te.setCurrentStatus(cb.isChecked());
                            //do upon completion
//                            if (cb.isChecked()) {
//                                te.setTotalCompleted(te.getTotalCompleted() + 1);
//                            }
//                            te.setCompletedLastTime(cb.isChecked());
//                            te.setCompletedPercentage(te.getWorkoutsCompleted() / te.getTotalCompleted());
                        }
                    }
                } else {
                    if (++mWorkoutPos < WorkoutContent.WORKOUTS.size()) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.ting);
                        mediaPlayer.start();
                        mWorkout = WorkoutContent.WORKOUTS.get(mWorkoutPos);
                        rest(getView());
                    } else {
                        finish(getView());
                    }
                }
            }
        };
    }

    private void pauseAndPlayButtonSetUp(View rootView) {
        final PlayPauseView playAndPause = (PlayPauseView) rootView.findViewById(R.id.play_pause_view);
        playAndPause.toggle();
        playAndPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAndPause.toggle();
                if (isPaused) {
                    isPaused = false;

                    setupCountDownTimer((int) (REMAINING_TIME * 1000), 10, mCircularProgressBar.getmMax() * 1000);
                    mCountDownTimer.start();

                } else {
                    mCountDownTimer.cancel();
                    isPaused = true;
                }
            }
        });
    }

    private LinearLayout statsLayout = null;
    private boolean isRep = false;

    private void rest(final View rootView) {
        isResting = true;
        TextView id = (TextView) rootView.findViewById(R.id.workout_countdown_id);
        id.setText(mWorkout.id);
        id.setBackgroundColor(mWorkout.dark);

        TextView name = (TextView) rootView.findViewById(R.id.workout_countdown_name);
        name.setBackgroundColor(mWorkout.light);
        if (!workoutInProgress) {
            name.setText(R.string.get_ready);
        } else {
            name.setText(R.string.rest);
            if (mWorkoutPos != 0) {
                ExerciseStats prev = stats.getStats()[mWorkoutPos - 1];
                TextView lastEx = (TextView) rootView.findViewById(R.id.lastExerciseTextview);
                lastEx.setText(prev.getExerciseName());
                if (prev instanceof TimeExercise) {
                    TimeExercise te = (TimeExercise) prev;
                    statsLayout = (LinearLayout) rootView.findViewById(R.id.timeExerciseStats);
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.isCompletedCheckBox);
                    cb.setChecked(false);
                    TextView compPerc = (TextView) rootView.findViewById(R.id.completePercentageStatTextView);
                    compPerc.setText("%" + te.getCompletedPercentage());
                    isRep = false;
                } else if (prev instanceof RepExercise) {
                    RepExercise re = (RepExercise) prev;
                    statsLayout = (LinearLayout) rootView.findViewById(R.id.repExerciseStats);
                    EditText repsCompleted = (EditText) rootView.findViewById(R.id.repsCompletedPlainText);
                    repsCompleted.setText("");
                    TextView avg = (TextView) rootView.findViewById(R.id.avgStatTextView);
                    avg.setText(re.getPersoanlAvg() + "");
                    TextView best = (TextView) rootView.findViewById(R.id.bestStatTextView);
                    best.setText(re.getPersonalBest() + "");
                    TextView lastTime = (TextView) rootView.findViewById(R.id.completedLastStatTextView);
                    lastTime.setText(re.getCompletedLastTime() + "");
                    isRep = true;
                }
                statsLayout.setVisibility(View.VISIBLE);
            }
        }
        REMAINING_TIME = REST_TIME / 1000.0f;
        setupCountDownTimer(REST_TIME, 10, REST_TIME);
        mCountDownTimer.start();
    }

    private void exercise(final View rootView) {
        (rootView.findViewById(R.id.repExerciseStats)).setVisibility(View.GONE);
        (rootView.findViewById(R.id.timeExerciseStats)).setVisibility(View.GONE);
        isResting = false;
        TextView ready = (TextView) rootView.findViewById(R.id.workout_countdown_name);
        ready.setText(mWorkout.name);

        TextView id = (TextView) rootView.findViewById(R.id.workout_countdown_id);
        id.setText(mWorkout.id);
        REMAINING_TIME = EXERCISE_TIME / 1000.0f;
        setupCountDownTimer(EXERCISE_TIME, 10, EXERCISE_TIME);
        mCountDownTimer.start();
    }

    private void finish(View rootView) {
        mCountDownTimer.cancel();

        MediaPlayer mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.cheer3);
        mediaPlayer.start();

        // hide the current views
        LinearLayout info = (LinearLayout) rootView.findViewById(R.id.workout_countdown_info);
        info.setVisibility(View.GONE);
        mCircularProgressBar.setVisibility(View.GONE);

        // display "finished"
        TextView textView = (TextView) rootView.findViewById(R.id.workout_countdown_finished);
        textView.setVisibility(View.VISIBLE);
    }

}
