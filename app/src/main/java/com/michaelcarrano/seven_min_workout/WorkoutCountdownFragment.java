package com.michaelcarrano.seven_min_workout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.animation.LayoutTransition;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.michaelcarrano.seven_min_workout.Utils.RuntimeTypeAdapterFactory;
import com.michaelcarrano.seven_min_workout.data.ExerciseStats;
import com.michaelcarrano.seven_min_workout.data.RepExercise;
import com.michaelcarrano.seven_min_workout.data.ExerciseData;
import com.michaelcarrano.seven_min_workout.data.TimeExercise;
import com.michaelcarrano.seven_min_workout.data.WorkoutContent;
import com.michaelcarrano.seven_min_workout.widget.CircularProgressBar;
import com.ohoussein.playpause.PlayPauseView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by michaelcarrano on 12/6/13.
 */
public class WorkoutCountdownFragment extends Fragment {

    /**
     * The fields used to track the current workout
     */
    private boolean isPaused = false;
    private boolean isResting = true;
    private boolean isRep = false;
    private boolean mWorkoutComplete = false;
    private boolean countdownMediaPlayerIsPaused = false;
    private MediaPlayer countdownMediaPlayer = new MediaPlayer();
    /**
     * Tracks if the workout has started
     */
    private boolean workoutInProgress = false;


    /**
     * The views modified in the class
     */
    private TextView lastExerciseTextView;
    private EditText repsCompletedPlainText;
    private CheckBox isCompletedCheckBox;
    private PlayPauseView playPauseView;
    private TextView workoutCountdownId;
    private TextView workoutCountdownName;
    private LinearLayout statsContainer;
    private LinearLayout completedCheckLayout;
    private LinearLayout repCompLayout;
    private LinearLayout repExerciseStats;
    private TextView avgStatTextView;
    private TextView bestStatTextView;
    private TextView completedLastStatTextView;
    private LinearLayout timeExerciseStats;
    private TextView completePercentageStatTextView;
    private LinearLayout statsLayout = null;

    /**
     * The time spent for each activity (exercise or rest)
     */
    private final int EXERCISE_TIME = 3000;     // 30 seconds

    private final int REST_TIME = 1000;         // 10 seconds

    private final int COUNTDOWN_INTERVAL = 10;  // 10 milliseconds between updates

    private static float REMAINING_TIME;        // Time remaining (ie: device rotation)

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
     * Used to store exercise stats using Gson
     */
    private RuntimeTypeAdapterFactory<ExerciseStats> runtimeTypeAdapterFactory;

    private ExerciseData stats = new ExerciseData();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain instance on device rotation
        setRetainInstance(true);

        // Remove all descriptions from the workout list
        WorkoutContent.removeDescriptions();

        // Set mWorkout to the first workout
        mWorkout = (WorkoutContent.Workout) WorkoutContent.MENU_ITEMS.get(mWorkoutPos);

        runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(ExerciseStats.class, "type")
                .registerSubtype(RepExercise.class, "rep")
                .registerSubtype(TimeExercise.class, "time");

        SharedPreferences mPrefs = getActivity().getSharedPreferences("exercise_stats", MODE_PRIVATE);
        if (mPrefs.contains("stats")) { //try to get stats from shared pref
            Gson gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();
            String json = mPrefs.getString("stats", "");
            getStats().setExerciseStats(gson.fromJson(json, ExerciseStats[].class));
        } else {
            // Initialize the stats variable
            setStats(new ExerciseData(getActivity()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater
                .inflate(R.layout.fragment_workout_countdown, container, false);

        // Initialize Views
        mCircularProgressBar = (CircularProgressBar) rootView.findViewById(R.id.workout_countdown_time);
        lastExerciseTextView = (TextView) rootView.findViewById(R.id.lastExerciseTextview);
        repsCompletedPlainText = (EditText) rootView.findViewById(R.id.repsCompletedPlainText);
        isCompletedCheckBox = (CheckBox) rootView.findViewById(R.id.isCompletedCheckBox);
        playPauseView = (PlayPauseView) rootView.findViewById(R.id.play_pause_view);
        workoutCountdownId = (TextView) rootView.findViewById(R.id.workout_countdown_id);
        workoutCountdownName = (TextView) rootView.findViewById(R.id.workout_countdown_name);
        statsContainer = (LinearLayout) rootView.findViewById(R.id.stats_container);
        completedCheckLayout = (LinearLayout) rootView.findViewById(R.id.completedCheckLayout);
        repCompLayout = (LinearLayout) rootView.findViewById(R.id.repCompLayout);
        repExerciseStats = (LinearLayout) rootView.findViewById(R.id.repExerciseStats);
        avgStatTextView = (TextView) rootView.findViewById(R.id.avgStatTextView);
        bestStatTextView = (TextView) rootView.findViewById(R.id.bestStatTextView);
        completedLastStatTextView = (TextView) rootView.findViewById(R.id.completedLastStatTextView);
        timeExerciseStats = (LinearLayout) rootView.findViewById(R.id.timeExerciseStats);
        completePercentageStatTextView = (TextView) rootView.findViewById(R.id.completePercentageStatTextView);


        ((ViewGroup) rootView.findViewById(R.id.workout_countdown_info_container)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        ((ViewGroup) rootView.findViewById(R.id.countdown)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        pauseAndPlayButtonSetUp(rootView);

        // Resume workout if selected by user
        boolean resumePressed = getArguments().getBoolean("ResumePressed", false);
        if (resumePressed) {
            resumeWorkout();
        }
        // Start off with 10 second rest, then alternate
        else if (!workoutInProgress) {
            rest(rootView);
        } else {
            exercise(rootView);
        }

        return rootView;
    }

    private void resumeWorkout() {
        // Setup
        SharedPreferences prefs = getActivity().getSharedPreferences("PausedWorkout", MODE_PRIVATE);

        // Get current stats
        if (prefs.contains("CurrentStats")) {
            Gson gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();
            String json = prefs.getString("CurrentStats", "");
            stats.setExerciseStats(gson.fromJson(json, ExerciseStats[].class));
        }

        // Get current exercise
        isResting = prefs.getBoolean("IsResting", true);
        mWorkoutPos = prefs.getInt("CurrentExercise", -1);
        isRep = prefs.getBoolean("IsRep", true);
        mWorkout = (WorkoutContent.Workout) WorkoutContent.MENU_ITEMS.get(mWorkoutPos);

        // Get current time remaining
        REMAINING_TIME = prefs.getFloat("SecondsRemaining", 10000);

        // Resume the workout
        if (isResting) {
            rest(getView());
        } else {
            isRep = (stats.getExerciseStats()[mWorkoutPos] instanceof RepExercise);
            exercise(getView());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // Cancel the countdown
        mCountDownTimer.cancel();

        if(!mWorkoutComplete) {
            WorkoutCountdownActivity activity = (WorkoutCountdownActivity) getActivity();
            if (activity != null) {
                activity.saveWorkout();
            }
        }
    }

    /**
     * Creates a CountDownTimer that will begin at millisInFuture.
     * Updates the CircularProgressBar every time the onTick is called while the timer is not paused.
     * onFinish() will shift the View based on which is currently shown.
     *
     * @param millisInFuture    The number of milliseconds remaining on the timer
     * @param progressBarMax    The number of milliseconds the CicularProgressBar holds at the original start of the timer
     */
    private void setupCountDownTimer(final int millisInFuture, final int progressBarMax) {
        mCircularProgressBar.setMax(progressBarMax / 1000);
        mCountDownTimer = new CountDownTimer(millisInFuture, COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isResting && REMAINING_TIME < 3.05 && REMAINING_TIME > 2.94) {
                    countdownMediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.exercise_start);
                    countdownMediaPlayer.start();
                }

                REMAINING_TIME = (millisUntilFinished / 1000.0f);
                mCircularProgressBar.setProgress(REMAINING_TIME);
            }

            @Override
            public void onFinish() {
                if (isResting) {
                    workoutInProgress = true;
                    exercise(getView());
                    if (mWorkoutPos != 0) {
                        lastExerciseTextView.setText("");
                        ExerciseStats exercise = stats.getExerciseStats()[mWorkoutPos - 1];
                        if (isRep) {
                            RepExercise re = (RepExercise) exercise;
                            if (!repsCompletedPlainText.getText().toString().equals("")) {
                                int reps = Integer.valueOf(repsCompletedPlainText.getText().toString());
                                re.setCurrentReps(reps);
                                //do this stuff upon completion
//                                re.setCompletedLastTime(reps);
//                                if (reps > re.getPersonalBest()) {
//                                    re.setPersonalBest(reps);
//                                }
//                                re.addToTotalReps(reps);
//                                re.setPersonalAvg(re.getTotalReps() / re.getWorkoutsCompleted());
                            } else {
                                re.setCurrentReps(0);
                            }
                        } else {
                            TimeExercise te = (TimeExercise) exercise;
                            te.setCurrentStatus(isCompletedCheckBox.isChecked());
                            //do upon completion
//                            if (cb.isChecked()) {
//                                te.setTotalCompleted(te.getTotalCompleted() + 1);
//                            }
//                            te.setCompletedLastTime(cb.isChecked());
//                            te.setCompletedPercentage(te.getWorkoutsCompleted() / te.getTotalCompleted());
                        }
                    }
                } else {
                    if (++mWorkoutPos < WorkoutContent.MENU_ITEMS.size()) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.ting);
                        mediaPlayer.start();
                        mWorkout = (WorkoutContent.Workout) WorkoutContent.MENU_ITEMS.get(mWorkoutPos);
                        rest(getView());
                    } else {
                        finish(getView());
                    }
                }
            }
        };
    }

    /**
     * Edits the pauseAndPlayTimer to begin in a playing state.
     * When clicked and timer is currenly playing, the animation for the circularPrograsBar will stop and the timer as well.
     * If clicked and the timer is paused setupCountDownTimer() will be called.
     * Handles sound if pressed while sound is active.
     *
     * @param rootView The parent view for the pauseAndPlayButton
     */
    private void pauseAndPlayButtonSetUp(View rootView) {
        playPauseView.toggle();
        playPauseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPauseView.toggle();
                if (isPaused) {
                    isPaused = false;

                    int progressBarMax = isResting ? REST_TIME : EXERCISE_TIME;
                    setupCountDownTimer((int) (REMAINING_TIME * 1000), progressBarMax);
                    mCountDownTimer.start();
                    if (countdownMediaPlayerIsPaused) {
                        countdownMediaPlayer.start();
                        countdownMediaPlayerIsPaused = false;
                    }

                } else {
                    if (countdownMediaPlayer.isPlaying()) {
                        countdownMediaPlayer.pause();
                        countdownMediaPlayerIsPaused = true;
                    }
                    if (mCountDownTimer != null) {
                        mCountDownTimer.cancel();
                    }
                    isPaused = true;
                }
            }
        });
    }



    private void setStatsPanel(View rootView, boolean isRestCalling) {
        ExerciseStats usingStat;
        if (isRestCalling) {
            if (mWorkoutPos != 0) {
                usingStat = getStats().getExerciseStats()[mWorkoutPos - 1];
                lastExerciseTextView.setVisibility(View.VISIBLE);
                lastExerciseTextView.setText(usingStat.getExerciseName());
                if (usingStat instanceof TimeExercise) {
                    setStatsPanelHelper(rootView, usingStat, true, true);
                } else if (usingStat instanceof RepExercise) {
                    setStatsPanelHelper(rootView, usingStat, false, true);
                }
            }
        } else {
            usingStat = getStats().getExerciseStats()[mWorkoutPos];
            if (usingStat instanceof TimeExercise) {
                setStatsPanelHelper(rootView, usingStat, true, false);
            } else if (usingStat instanceof RepExercise) {
                setStatsPanelHelper(rootView, usingStat, false, false);
            }
        }
        statsLayout.setVisibility(View.VISIBLE);
    }

    private void setStatsPanelHelper(View rootView, ExerciseStats usingStat, boolean isTimeExercise, boolean isRestCalling) {
        if (isRestCalling && isTimeExercise) {
            timeExerciseStatsPopulator(rootView, usingStat, true);

            completedCheckLayout.setVisibility(View.VISIBLE);
            isRep = false;
        } else if (isRestCalling && !isTimeExercise) {
            repExerciseStatsPopulator(rootView, usingStat, true);

            repCompLayout.setVisibility(View.VISIBLE);
            isRep = true;
        } else if (!isRestCalling && isTimeExercise) {
            timeExerciseStatsPopulator(rootView, usingStat, false);

            completedCheckLayout.setVisibility(View.GONE);
        } else if (!isRestCalling && !isTimeExercise) {
            repExerciseStatsPopulator(rootView, usingStat, false);

            repCompLayout.setVisibility(View.GONE);
        }
    }

    private void repExerciseStatsPopulator(View rootView, ExerciseStats usingStat, boolean isRestCalling) {
        if (isRestCalling) {
            repsCompletedPlainText.setText("");
        }
        RepExercise re = (RepExercise) usingStat;
        statsLayout = repExerciseStats;
        avgStatTextView.setText(re.getPersoanlAvg() + "");
        bestStatTextView.setText(re.getPersonalBest() + "");
        completedLastStatTextView.setText(re.getCompletedLastTime() + "");
    }

    private void timeExerciseStatsPopulator(View rootView, ExerciseStats usingStat, boolean isRestCalling) {
        if (isRestCalling) {
            isCompletedCheckBox.setChecked(false);
        }
        TimeExercise te = (TimeExercise) usingStat;
        statsLayout = timeExerciseStats;
        completePercentageStatTextView.setText(te.getCompletedPercentage() + "%");
    }

    private void rest(final View rootView) {
        isResting = true;
        workoutCountdownId.setText(mWorkout.id);
        workoutCountdownId.setBackgroundColor(mWorkout.dark);

        workoutCountdownName.setBackgroundColor(mWorkout.light);
        if (!workoutInProgress) {
            workoutCountdownName.setText(R.string.get_ready);
            statsContainer.setVisibility(View.GONE);
        } else {
            statsContainer.setVisibility(View.VISIBLE);
            workoutCountdownName.setText(R.string.rest);
            setStatsPanel(rootView, true);
        }
        REMAINING_TIME = REST_TIME / 1000.0f;
        setupCountDownTimer(REST_TIME, REST_TIME);
        mCountDownTimer.start();

        if (((WorkoutCountdownActivity) getActivity()).isTextDisplayed()) {
            ((WorkoutCountdownActivity) getActivity()).closeBar();
        }
    }

    private void exercise(final View rootView) {
        statsContainer.setVisibility(View.VISIBLE);
        repExerciseStats.setVisibility(View.GONE);
        timeExerciseStats.setVisibility(View.GONE);
        lastExerciseTextView.setVisibility(View.GONE);
        repCompLayout.setVisibility(View.GONE);
        isResting = false;

        workoutCountdownName.setText(mWorkout.name);
        workoutCountdownId.setText(mWorkout.id);
        workoutCountdownName.setBackgroundColor(mWorkout.light);
        workoutCountdownId.setBackgroundColor(mWorkout.dark);

        setStatsPanel(rootView, false);

        REMAINING_TIME = EXERCISE_TIME / 1000.0f;
        setupCountDownTimer(EXERCISE_TIME, EXERCISE_TIME);
        mCountDownTimer.start();
    }

    private void finish(View rootView) {
        mCountDownTimer.cancel();
        mWorkoutComplete = true;

        // Play workout completed audio
        MediaPlayer mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.cheer3);
        mediaPlayer.start();

        // Store stats from workout
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();
        String json = gson.toJson(getStats().getExerciseStats());

        // Save boolean indicating workout is not paused
        SharedPreferences mPrefs = getActivity().getSharedPreferences("PausedWorkout", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putBoolean("WorkoutIsPaused", false);
        prefsEditor.apply();

        // Start workoutCompleteActivity
        Intent intent = new Intent(this.getActivity(), WorkoutCompleteActivity.class);
        intent.putExtra("stats_array", json);
        startActivity(intent);
        getActivity().finish();
    }

    // Getters and Setters

    public ExerciseData getStats() {
        return stats;
    }

    public void setStats(ExerciseData stats) {
        this.stats = stats;
    }

    public int getmWorkoutPos() {
        return mWorkoutPos;
    }

    public void setmWorkoutPos(int mWorkoutPos) {
        this.mWorkoutPos = mWorkoutPos;
    }

    public boolean getWorkoutInProgress() {
        return workoutInProgress;
    }

    public boolean getIsResting() {
        return isResting;
    }

    public float getRemainingTime() {
        return REMAINING_TIME;
    }

    public PlayPauseView getPlayPauseView() {
        return playPauseView;
    }

    public boolean isRep() {
        return isRep;
    }

    public boolean isPaused() {
        return isPaused;
    }
}
