package com.michaelcarrano.seven_min_workout.data;

public class TimeExercise extends ExerciseStats {
    private boolean completedLastTime = false;
    private int totalCompleted = 0;
    private int completedPercentage = 0;
    private boolean currentStatus = false;

    public TimeExercise(String exerciseName) {
        this.setExerciseName(exerciseName);
    }

    public boolean isCompletedLastTime() {
        return completedLastTime;
    }

    public void setCompletedLastTime(boolean completedLastTime) {
        this.completedLastTime = completedLastTime;
    }

    public int getCompletedPercentage() {
        return completedPercentage;
    }

    public void setCompletedPercentage(int completedPercentage) {
        this.completedPercentage = completedPercentage;
    }

    public int getTotalCompleted() {
        return totalCompleted;
    }

    public void setTotalCompleted(int totalCompleted) {
        this.totalCompleted = totalCompleted;
    }

    public boolean isCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(boolean currentStatus) {
        this.currentStatus = currentStatus;
    }
}
