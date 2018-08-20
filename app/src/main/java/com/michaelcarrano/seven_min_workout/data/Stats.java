package com.michaelcarrano.seven_min_workout.data;

import android.content.Context;

import com.michaelcarrano.seven_min_workout.R;

public class Stats {
    private ExerciseStats[] stats = new ExerciseStats[12];

    public Stats(Context context) {

        final String[] workoutNames = context.getResources().getStringArray(R.array.workout_names);
        stats[0] = new RepExercise(workoutNames[0]);
        stats[1] = new TimeExercise(workoutNames[1]);
        stats[2] = new RepExercise(workoutNames[2]);
        stats[3] = new RepExercise(workoutNames[3]);
        stats[4] = new RepExercise(workoutNames[4]);
        stats[5] = new RepExercise(workoutNames[5]);
        stats[6] = new RepExercise(workoutNames[6]);
        stats[7] = new TimeExercise(workoutNames[7]);
        stats[8] = new RepExercise(workoutNames[8]);
        stats[9] = new RepExercise(workoutNames[9]);
        stats[10] = new RepExercise(workoutNames[10]);
        stats[11] = new TimeExercise(workoutNames[11]);
    }

    public ExerciseStats[] getStats() {
        return stats;
    }
}
