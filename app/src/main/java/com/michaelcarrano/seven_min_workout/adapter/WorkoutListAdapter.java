package com.michaelcarrano.seven_min_workout.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
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
        addListenerToButton();
    }

    private void addListenerToButton() {

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
            final DescriptionViewHolder holder;
            final Description description = (Description) menuItem;

            //if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.fragment_workout_detail, parent, false);

            holder = new DescriptionViewHolder();

            createYoutubeThumbnailView(description);
            manageYoutubePlayButtonOnThumbnail(convertView, parent);


            holder.workout_detail = (TextView) convertView.findViewById(R.id.workout_detail);

            convertView.setTag(holder);
            //} else {
            //    holder = (DescriptionViewHolder) convertView.getTag();
            //}
            holder.workout_detail.setText(description.content);
            return convertView;
        }
    }

    private void manageYoutubePlayButtonOnThumbnail(View convertView, ViewGroup parent) {
        ImageButton playButton = (ImageButton) convertView.findViewById(R.id.playVideoButton);

        final View finalConvertView = convertView;
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YouTubeThumbnailView thumbnailView = (YouTubeThumbnailView) ((RelativeLayout) ((LinearLayout) finalConvertView.findViewById(R.id.detailLinearLayout)).getChildAt(0)).getChildAt(0);
                Intent intent = YouTubeStandalonePlayer.createVideoIntent(activity,
                        BuildConfig.YOUTUBE_API_KEY,
                        thumbnailView.getTag().toString(),//video id
                        0,     //after this time, video will start automatically
                        true,               //autoplay or not
                        false);             //lightbox mode or not; show the video in a small box
                activity.startActivity(intent);
            }
        });
    }

    private void createYoutubeThumbnailView(Description description) {
        final YouTubeThumbnailView ytthumbnail = new YouTubeThumbnailView(activity);
        ytthumbnail.setTag(description.getVideo());
        ytthumbnail.initialize(BuildConfig.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        RelativeLayout youtubeThumbnailLayoutParent = (RelativeLayout) activity.findViewById(R.id.relWithButton);
                        if(youtubeThumbnailLayoutParent.getChildCount() > 1){
                            youtubeThumbnailLayoutParent.getChildAt(0).setVisibility(View.GONE);
                            youtubeThumbnailLayoutParent.removeViewAt(0);
                        }
                        ytthumbnail.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                        youtubeThumbnailLayoutParent.addView(ytthumbnail,0);
                        youtubeThumbnailLayoutParent.findViewById(R.id.relWithButton).setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }
                });
                youTubeThumbnailLoader.setVideo(ytthumbnail.getTag().toString());
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
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
