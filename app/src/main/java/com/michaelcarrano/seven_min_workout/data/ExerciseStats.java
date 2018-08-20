package com.michaelcarrano.seven_min_workout.data;

public abstract class ExerciseStats {
    private int workoutsCompleted;

    public int getWorkoutsCompleted() {
        return workoutsCompleted;
    }

    public void setWorkoutsCompleted(int workoutsCompleted) {
        this.workoutsCompleted = workoutsCompleted;
    }

    public void incrementWorkoutsCompleted() {
        workoutsCompleted++;
    }
}
