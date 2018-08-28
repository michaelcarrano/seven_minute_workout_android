package com.michaelcarrano.seven_min_workout.data;

public class RepExercise extends ExerciseStats {
    private int personalBest = 0;
    private int persoanlAvg = 0;
    private int completedLastTime = 0;
    private int totalReps = 0;
    private int currentReps = 0;

    public RepExercise() {
    }

    public RepExercise(String exerciseName) {
        this.setExerciseName(exerciseName);
    }

    public void addToTotalReps(int reps) {
        totalReps += reps;
    }

    public int getPersonalBest() {
        return personalBest;
    }

    public void setPersonalBest(int personalBest) {
        this.personalBest = personalBest;
    }

    public int getPersoanlAvg() {
        return persoanlAvg;
    }

    public void setPersoanlAvg(int persoanlAvg) {
        this.persoanlAvg = persoanlAvg;
    }

    public int getCompletedLastTime() {
        return completedLastTime;
    }

    public void setCompletedLastTime(int completedLastTime) {
        this.completedLastTime = completedLastTime;
    }

    public int getTotalReps() {
        return totalReps;
    }

    public void setTotalReps(int totalReps) {
        this.totalReps = totalReps;
    }

    public int getCurrentReps() {
        return currentReps;
    }

    public void setCurrentReps(int currentReps) {
        this.currentReps = currentReps;
    }
}
