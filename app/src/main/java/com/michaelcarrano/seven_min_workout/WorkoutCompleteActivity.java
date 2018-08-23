package com.michaelcarrano.seven_min_workout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.michaelcarrano.seven_min_workout.data.ExerciseStats;
import com.michaelcarrano.seven_min_workout.data.RepExercise;
import com.michaelcarrano.seven_min_workout.data.Stats;
import com.michaelcarrano.seven_min_workout.data.TimeExercise;

import java.sql.Time;

public class WorkoutCompleteActivity extends AppCompatActivity {

    private Stats stats = null;
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

        //ExerciseStats[] eList = stats.getStats();
        //for (int i = 0; i < 12; i++) {
        //    setDataField(eList[i], views[i]);
        //}

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

    }

    private void setRepStats(int index) {

    }
}
