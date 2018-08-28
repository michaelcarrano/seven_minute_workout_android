package com.michaelcarrano.seven_min_workout.data;

public class ExerciseStats {
    private int workoutsCompleted;
    private String exerciseName = "";

    public ExerciseStats() {
    }

    public int getWorkoutsCompleted() {
        return workoutsCompleted;
    }

    public void setWorkoutsCompleted(int workoutsCompleted) {
        this.workoutsCompleted = workoutsCompleted;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public void incrementWorkoutsCompleted() {
        workoutsCompleted++;
    }
}
