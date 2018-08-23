package com.michaelcarrano.seven_min_workout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;
import com.michaelcarrano.seven_min_workout.data.ExerciseStats;
import com.michaelcarrano.seven_min_workout.data.RepExercise;
import com.michaelcarrano.seven_min_workout.data.Stats;
import com.michaelcarrano.seven_min_workout.data.TimeExercise;

import java.sql.Time;

public class WorkoutCompleteActivity extends AppCompatActivity {

    private Stats stats = null;
    private ExerciseStats[] eList;
    private View[] views = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_complete);

        stats = getIntent().getParcelableExtra("stats_extra");
        views = new View[] {
                findViewById(R.id.jumpingJackRepsEditText),
                findViewById(R.id.wallsitsCompletedCheckBox),
                findViewById(R.id.pushupsRepsEditText),
                findViewById(R.id.crunchesRepsEditText),
                findViewById(R.id.stepupsRepsEditText),
                findViewById(R.id.squatsRepsEditText),
                findViewById(R.id.tricepdipsRepsEditText),
                findViewById(R.id.planksCompletedCheckBox),
                findViewById(R.id.highkneesRepsEditText),
                findViewById(R.id.lungeRepsEditText),
                findViewById(R.id.pushuprotationsRepsEditText),
                findViewById(R.id.sideplanksCompletedCheckBox)
        };
        eList = stats.getStats();

        for (int i = 0; i < 12; i++) {
            setDataField(eList[i], views[i]);
        }

    }

    private void setDataField(ExerciseStats exercise, View view) {
        if (exercise instanceof RepExercise) {
            EditText et = (EditText) view;
            RepExercise re = (RepExercise) exercise;
            et.setText(re.getCurrentReps());
        } else if (exercise instanceof  TimeExercise) {
            CheckBox cb = (CheckBox) view;
            TimeExercise te = (TimeExercise) exercise;
            cb.setChecked(te.isCurrentStatus());
        }
    }

    public void compleBtnOnClick(View view){
        //Time
        //Completed last time
        //total completed
        //complete percentage

        //Reps
        //reps last time
        //total reps
        //average
        //best
        for (ExerciseStats e:
             eList) {
            setRepStats(e);
        }

        //shared preferences
        SharedPreferences  mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(stats);
        prefsEditor.putString("stats", json);
        prefsEditor.commit();
    }

    private void setRepStats(ExerciseStats exercise) {
        if (exercise instanceof RepExercise) {
            RepExercise re = (RepExercise) exercise;
            re.setCompletedLastTime(re.getCurrentReps());
            re.setTotalReps(re.getTotalReps()+re.getCompletedLastTime());
            re.setPersoanlAvg(re.getTotalReps()/re.getWorkoutsCompleted());
            if (re.getCompletedLastTime() > re.getPersonalBest()) {
                re.setPersonalBest(re.getCompletedLastTime());
            }
        } else if (exercise instanceof  TimeExercise) {
            TimeExercise te = (TimeExercise) exercise;
            te.setCompletedLastTime(te.isCurrentStatus());
            if (te.isCurrentStatus()) {
                te.setTotalCompleted(te.getTotalCompleted() + 1);
            }
            te.setCompletedPercentage((te.getTotalCompleted() / te.getWorkoutsCompleted()) * 100);
        }
    }
}
