package com.michaelcarrano.seven_min_workout.data;

import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * WorkoutContent Class provides data about individual Workouts.
 *
 * @author michaelcarrano
 */
public class WorkoutContent {

    /**
     * An array of workouts.
     */
    public static List<MenuItem> MENU_ITEMS = new ArrayList<MenuItem>();

    public static void addWorkout(Workout workout) {
        MENU_ITEMS.add(workout);
    }

    public static void insertDescription(Description workout, int position) {
        MENU_ITEMS.add(position, workout);
    }

    public static void removeDescriptions() {
        for (int i = MENU_ITEMS.size() - 1; i >= 0; i--) {
            if (MENU_ITEMS.get(i) instanceof Description) {
                MENU_ITEMS.remove(MENU_ITEMS.get(i));
            }
        }
    }

    /**
     * A MenuItem for the base class used in the ListView holding both Workouts and Descriptions
     */
    public static abstract class MenuItem {

    }

    /**
     * A Description holding the YouTube video link and description of the item.
     */
    public static class Description extends MenuItem {

        public String id;

        public String name;

        public String content;

        public String video;

        public int dark;

        public int light;

        /**
         * A Workout representing information related to the workout item.
         */


        public Description(@NonNull String id,
                           @NonNull String name,
                           @NonNull String content,
                           @NonNull String video,
                           @ColorRes int dark,
                           @ColorRes int light) {
            this.id = id;
            this.name = name;
            this.content = content;
            this.video = video;
            this.dark = dark;
            this.light = light;
        }
        public String getVideo() {
            return video;
        }
    }
    public static class Workout extends MenuItem {


        public String id;

        public String name;

        public String content;

        public String video;

        public int dark;

        public int light;

        public Workout(@NonNull String id,
                       @NonNull String name,
                       @NonNull String content,
                       @NonNull String video,
                       @ColorRes int dark,
                       @ColorRes int light) {
            this.id = id;
            this.name = name;
            this.content = content;
            this.video = video;
            this.dark = dark;
            this.light = light;
        }
    }
}
