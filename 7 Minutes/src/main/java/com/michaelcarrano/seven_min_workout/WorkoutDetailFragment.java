package com.michaelcarrano.seven_min_workout;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import com.michaelcarrano.seven_min_workout.data.DeveloperKey;
import com.michaelcarrano.seven_min_workout.data.WorkoutContent;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A fragment representing a single Workout detail screen. This fragment is either contained in a
 * {@link WorkoutListActivity} in two-pane mode (on tablets) or a {@link WorkoutDetailActivity} on
 * handsets.
 */
public class WorkoutDetailFragment extends Fragment {

    /**
     * The fragment argument representing the Workout ID that this fragment represents.
     */
    public static final String ARG_WORKOUT_POS = "workout_pos";

    /**
     * The Workout content this fragment is presenting.
     */
    private WorkoutContent.Workout mWorkout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon
     * screen orientation changes).
     */
    public WorkoutDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_WORKOUT_POS)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mWorkout = WorkoutContent.WORKOUTS.get(getArguments().getInt(ARG_WORKOUT_POS));
            Log.i("7min", "Frag: " + mWorkout.name);
        }

        video();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout_detail, container, false);

        // Show the workout content as text in a TextView.
        if (mWorkout != null) {
            TextView content = (TextView) rootView.findViewById(R.id.workout_detail);
            content.setText(mWorkout.content);
            content.setTextColor(Color.WHITE);
        }

        return rootView;
    }

    // TODO: Handle rotation so video does not start from beginning.
    public void video() {
        YouTubePlayerSupportFragment youTubePlayerSupportFragment = YouTubePlayerSupportFragment
                .newInstance();
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.youtube_fragment, youTubePlayerSupportFragment).commit();

        youTubePlayerSupportFragment
                .initialize(DeveloperKey.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                            YouTubePlayer youTubePlayer, boolean b) {
                        if (!b) {
                            youTubePlayer.cueVideo(mWorkout.video);
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                            YouTubeInitializationResult youTubeInitializationResult) {
                        if (youTubeInitializationResult.isUserRecoverableError()) {
                            youTubeInitializationResult.getErrorDialog(getActivity(), 1).show();
                        } else {
                            String errorMessage = String.format(getString(R.string.error_player),
                                    youTubeInitializationResult.toString());
                            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
