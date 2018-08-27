package com.michaelcarrano.seven_min_workout;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.michaelcarrano.seven_min_workout.data.WorkoutContent;


/**
 * An activity representing a list of Workouts. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link WorkoutDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two vertical panes. <p>
 * The activity makes heavy use of fragments. The list of items is a {@link WorkoutListFragment} and
 * the item details (if present) is a {@link WorkoutDetailFragment}. <p> This activity also
 * implements the required {@link WorkoutListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class WorkoutListActivity extends BaseActivity implements WorkoutListFragment.Callbacks {

    WorkoutListFragment workoutList;
    ListView workoutListView;
    boolean isActiveDescription = false;
    int activeDescriptionPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        addFab().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent workoutIntent = new Intent(WorkoutListActivity.this, WorkoutCountdownActivity.class);
                startActivity(workoutIntent);
            }
        });
        addResumeFab();
        workoutListView = (ListView) findViewById(android.R.id.list);
    }

    /**
     * Callback method from {@link WorkoutListFragment.Callbacks} indicating that the workout with
     * the given ID was selected.
     */
    @Override
    public void onItemSelected(int position) {

        if (WorkoutContent.MENU_ITEMS.get(position) instanceof WorkoutContent.Workout) {
            if (!isActiveDescription) {
                isActiveDescription = true;
                activeDescriptionPos = position;
                addDescription(position, false);
            } else if (position == activeDescriptionPos) {
                isActiveDescription = false;
                WorkoutContent.removeDescriptions();
            } else if (position > activeDescriptionPos) {
                activeDescriptionPos = position;
                WorkoutContent.removeDescriptions();
                addDescription(position, true);
            } else {
                activeDescriptionPos = position;
                WorkoutContent.removeDescriptions();
                addDescription(position, false);
            }
            BaseAdapter adapter = (BaseAdapter) workoutListView.getAdapter();
            adapter.notifyDataSetChanged();
        }
    }

    private void addDescription(int position, boolean offsetNeeded) {
        Resources resources = getResources();
        final String[] workoutNames = resources.getStringArray(R.array.workout_names);
        final String[] workoutDescriptions = resources.getStringArray(R.array.workout_descriptions);
        final String[] workoutVideos = resources.getStringArray(R.array.workout_videos);
        final int[] darkColors = resources.getIntArray(R.array.darkColors);
        final int[] lightColors = resources.getIntArray(R.array.lightColors);

        int offsetPos = offsetNeeded ? 0 : 1;
        int offsetData = offsetNeeded ? -1 : 0;
        WorkoutContent.insertDescription(new WorkoutContent.Description(
                String.valueOf(position + offsetPos),
                workoutNames[position + offsetData],
                workoutDescriptions[position + offsetData],
                workoutVideos[position + offsetData],
                darkColors[position + offsetData],
                lightColors[position + offsetData]), position + offsetPos);
    }

    /**
     * Add menu items to ActionBar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Handle menu item clicks
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about_app:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
        }
        return true;
    }
}
