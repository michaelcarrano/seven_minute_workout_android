package com.michaelcarrano.seven_min_workout;

import com.michaelcarrano.seven_min_workout.data.WorkoutContent;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A fragment representing a single Workout detail screen. This fragment is either contained in a
 * {@link WorkoutListActivity} in two-pane mode (on tablets) or a {@link WorkoutDetailActivity} on
 * handsets.
 */
public class WorkoutDetailFragment extends Fragment {

    /**
     * The fragment argument representing the item ID that this fragment represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private WorkoutContent.Workout mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon
     * screen orientation changes).
     */
    public WorkoutDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = WorkoutContent.WORKOUTS.get(
                    Integer.parseInt(getArguments().getString(ARG_ITEM_ID)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            TextView content = (TextView) rootView.findViewById(R.id.workout_detail);
            content.setText(mItem.content);
            content.setTextColor(Color.WHITE);
        }

        return rootView;
    }
}
