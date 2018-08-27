package com.michaelcarrano.seven_min_workout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.michaelcarrano.seven_min_workout.Utils.RuntimeTypeAdapterFactory;
import com.michaelcarrano.seven_min_workout.data.ExerciseStats;
import com.michaelcarrano.seven_min_workout.data.RepExercise;
import com.michaelcarrano.seven_min_workout.data.ExerciseData;
import com.michaelcarrano.seven_min_workout.data.TimeExercise;

import java.util.Arrays;

public class WorkoutCompleteActivity extends AppCompatActivity {

    private ExerciseData exerciseData = new ExerciseData();
    private ExerciseStats[] eList;
    private View[] views = null;
    private RuntimeTypeAdapterFactory<ExerciseStats> runtimeTypeAdapterFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_complete);

        String json = getIntent().getStringExtra("stats_array");

        runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(ExerciseStats.class, "type")
                .registerSubtype(RepExercise.class, "rep")
                .registerSubtype(TimeExercise.class, "time");
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();
        eList = gson.fromJson(json, ExerciseStats[].class);
        exerciseData.setExerciseStats(eList);
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

        EditText et;
        RepExercise re;

        CheckBox cb;
        TimeExercise te;

        for (int i = 0; i < 12; i++) {
            //1,7,11
            if (i ==1 || i == 7 || i == 11) {
                cb = (CheckBox) views[i];
                te = (TimeExercise) eList[i];
                cb.setChecked(te.isCurrentStatus());
            } else {
                et = (EditText) views[i];
                re = (RepExercise) eList[i];
                et.setText(re.getCurrentReps() + "");
            }
        }

        Button completeBtn = (Button) findViewById(R.id.completeBtn);
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    e.incrementWorkoutsCompleted();
                    if (e instanceof RepExercise) {
                        RepExercise re = (RepExercise) e;
                        EditText repEditText = (EditText) views[Arrays.asList(eList).indexOf(e)];
                        re.setCompletedLastTime(Integer.valueOf(repEditText.getText() + ""));
                        re.setTotalReps(re.getTotalReps()+re.getCompletedLastTime());
                        re.setPersoanlAvg(re.getTotalReps()/re.getWorkoutsCompleted());
                        if (re.getCompletedLastTime() > re.getPersonalBest()) {
                            re.setPersonalBest(re.getCompletedLastTime());
                        }
                    } else if (e instanceof  TimeExercise) {
                        TimeExercise te = (TimeExercise) e;
                        CheckBox isCompleteCheckBox = (CheckBox) views[Arrays.asList(eList).indexOf(e)];
                        te.setCompletedLastTime(isCompleteCheckBox.isChecked());
                        if (te.isCompletedLastTime()) {
                            te.setTotalCompleted(te.getTotalCompleted() + 1);
                        }
                        te.setCompletedPercentage(((double)te.getTotalCompleted() / (double)te.getWorkoutsCompleted()) * 100);
                    }
                }

                //shared preferences
                SharedPreferences  mPrefs = getSharedPreferences("exercise_stats", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                Gson gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();
                String json = gson.toJson(exerciseData.getExerciseStats());
                prefsEditor.putString("stats", json);
                prefsEditor.commit();

                finish();
            }
        });
    }
}
