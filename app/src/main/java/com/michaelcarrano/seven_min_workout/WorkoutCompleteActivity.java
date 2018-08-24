package com.michaelcarrano.seven_min_workout;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;
import com.michaelcarrano.seven_min_workout.data.ExerciseStats;
import com.michaelcarrano.seven_min_workout.data.RepExercise;
import com.michaelcarrano.seven_min_workout.data.ExerciseData;
import com.michaelcarrano.seven_min_workout.data.TimeExercise;

public class WorkoutCompleteActivity extends AppCompatActivity {

    private ExerciseData exerciseData = new ExerciseData();
    private ExerciseStats[] eList;
    private View[] views = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_complete);

        String json = getIntent().getStringExtra("stats_array");
        ExerciseStats[] statsArray = new Gson().fromJson(json, ExerciseStats[].class);
        eList = statsArray;
        exerciseData.setExerciseStats(statsArray);
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

        for (int i = 0; i < 12; i++) {
            //1,7,11
            if (i ==1 || i == 7 || i == 11) {
                setDataField(eList[i], views[i], false);
            } else {
                setDataField(eList[i], views[i], true);
            }
        }

    }

    private void setDataField(ExerciseStats exercise, View view, boolean isRep) {
        if (isRep) {
            EditText et = (EditText) view;
            RepExercise re = (RepExercise) exercise;
            et.setText(re.getCurrentReps());
        } else {
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
        String json = gson.toJson(exerciseData);
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
