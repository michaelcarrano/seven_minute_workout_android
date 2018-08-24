package com.michaelcarrano.seven_min_workout.data;

import android.content.Context;

import com.michaelcarrano.seven_min_workout.R;

public class ExerciseData {
    private ExerciseStats[] exerciseStats = new ExerciseStats[12];

    public ExerciseData() {
    }

    public ExerciseData(Context context) {

        final String[] workoutNames = context.getResources().getStringArray(R.array.workout_names);
        exerciseStats[0] = new RepExercise(workoutNames[0]);
        exerciseStats[1] = new TimeExercise(workoutNames[1]);
        exerciseStats[2] = new RepExercise(workoutNames[2]);
        exerciseStats[3] = new RepExercise(workoutNames[3]);
        exerciseStats[4] = new RepExercise(workoutNames[4]);
        exerciseStats[5] = new RepExercise(workoutNames[5]);
        exerciseStats[6] = new RepExercise(workoutNames[6]);
        exerciseStats[7] = new TimeExercise(workoutNames[7]);
        exerciseStats[8] = new RepExercise(workoutNames[8]);
        exerciseStats[9] = new RepExercise(workoutNames[9]);
        exerciseStats[10] = new RepExercise(workoutNames[10]);
        exerciseStats[11] = new TimeExercise(workoutNames[11]);
    }

    public ExerciseStats[] getExerciseStats() {
        return exerciseStats;
    }

    public void setExerciseStats(ExerciseStats[] exerciseStats) {
        this.exerciseStats = exerciseStats;
    }
}
