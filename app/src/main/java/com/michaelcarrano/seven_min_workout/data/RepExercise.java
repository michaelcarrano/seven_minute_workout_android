package com.michaelcarrano.seven_min_workout.data;

public class RepExercise implements ExerciseStats {
    private String exerciseName = "";
    private int personalBest = 0;
    private int persoanlAvg = 0;
    private int completedLastTime = 0;

    public RepExercise() {
    }

    public RepExercise(String exerciseName) {
        this.exerciseName = exerciseName;;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
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
}
