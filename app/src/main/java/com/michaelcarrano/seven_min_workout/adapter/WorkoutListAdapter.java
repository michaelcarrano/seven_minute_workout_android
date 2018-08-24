package com.michaelcarrano.seven_min_workout.adapter;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.michaelcarrano.seven_min_workout.BuildConfig;
import com.michaelcarrano.seven_min_workout.R;
import com.michaelcarrano.seven_min_workout.WorkoutDetailFragment;
import com.michaelcarrano.seven_min_workout.data.WorkoutContent;
import com.michaelcarrano.seven_min_workout.data.WorkoutContent.MenuItem;
import com.michaelcarrano.seven_min_workout.data.WorkoutContent.Description;
import com.michaelcarrano.seven_min_workout.data.WorkoutContent.Workout;

/**
 * Custom adapter for {@link com.michaelcarrano.seven_min_workout.WorkoutListFragment } to display
 * the ID and Name of Workout in a ListView row.
 *
 * @author michaelcarrano
 */
public class WorkoutListAdapter extends BaseAdapter {

    private static LayoutInflater mLayoutInflater = null;
    private Activity activity;

    public WorkoutListAdapter(Activity ctx) {
        activity = ctx;
        this.mLayoutInflater = ctx.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return WorkoutContent.MENU_ITEMS.size();
    }

    @Override
    public Object getItem(int position) {
        return WorkoutContent.MENU_ITEMS.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MenuItem menuItem = (MenuItem) getItem(position);
        if (menuItem instanceof Workout) {

            ViewHolder holder;
            Workout workout = (Workout) menuItem;

            //if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.adapter_workout_row, parent, false);

                holder = new ViewHolder();
                holder.id = (TextView) convertView.findViewById(R.id.workout_id);
                holder.name = (TextView) convertView.findViewById(R.id.workout_name);

                convertView.setTag(holder);
            //} else {
                //holder = (ViewHolder) convertView.getTag();
            //}

            // Set the content for the ListView row
            holder.id.setText(workout.id);
            holder.name.setText(workout.name);

            // Set the color for the ListView row
            holder.id.setBackgroundColor(workout.dark);
            holder.name.setBackgroundColor(workout.light);

            return convertView;
        }
        else {
            DescriptionViewHolder holder;
            final Description description = (Description) menuItem;

            //if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.fragment_workout_detail, parent, false);

                holder = new DescriptionViewHolder();
                holder.youtube_fragment = (FrameLayout) convertView.findViewById(R.id.youtube_fragment);
                holder.workout_detail = (TextView) convertView.findViewById(R.id.workout_detail);

                convertView.setTag(holder);
            //} else {
            //    holder = (DescriptionViewHolder) convertView.getTag();
            //}
            holder.workout_detail.setText(description.content);
            convertView.setTranslationZ(-1f);
            Animation animation = AnimationUtils
                    .loadAnimation(convertView.getContext(), R.anim.down_from_top);
            convertView.startAnimation(animation);
            return convertView;
        }
    }

    private static class ViewHolder {

        public TextView id;

        public TextView name;
    }

    private static class DescriptionViewHolder {

        public FrameLayout youtube_fragment;

        public TextView workout_detail;
    }
}
