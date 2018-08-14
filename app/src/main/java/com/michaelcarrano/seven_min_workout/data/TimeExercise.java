package com.michaelcarrano.seven_min_workout.data;

public class TimeExercise implements ExerciseStats {
    private String exerciseName = "";
    private boolean completedLastTime = false;
    private double completedPercentage = 0;

    public TimeExercise() {
    }

    public TimeExercise(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public boolean isCompletedLastTime() {
        return completedLastTime;
    }

    public void setCompletedLastTime(boolean completedLastTime) {
        this.completedLastTime = completedLastTime;
    }

    public double getCompletedPercentage() {
        return completedPercentage;
    }

    public void setCompletedPercentage(double completedPercentage) {
        this.completedPercentage = completedPercentage;
    }
}
