package com.michaelcarrano.seven_min_workout;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An Activity that represents the Workout timer. Each workout is 30 seconds long with 10 seconds of
 * rest in between.
 * <p>
 * TODO: Allow for customizing the workout period and rest period
 *
 * @author michaelcarrano
 */
public class WorkoutCountdownActivity extends BaseActivity {

    WorkoutCountdownFragment fragment;
    private boolean textDisplayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workout_countdown);

        if (savedInstanceState == null) {
            fragment = new WorkoutCountdownFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.workout_countdown_container,
                    fragment)
                    .commit();
        } else {
            Toast.makeText(this, "Countdown Activity", Toast.LENGTH_LONG).show();
        }

    }

    public void layoutClicked(View view) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.workout_countdown_info_container);
        ViewGroup.LayoutParams params = ll.getLayoutParams();
        if (!textDisplayed) {
            params.height = params.height + 800;
            ll.setLayoutParams(params);
            TextView tv = new TextView(this);
            tv.setText("STUFF GOES HERE");
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv.setLayoutParams(layoutParams);
            ll.addView(tv);
            textDisplayed = true;
        } else {
            ll.removeView(ll.getChildAt(ll.getChildCount() - 1));
            params.height = params.height - 800;
            ll.setLayoutParams(params);
            textDisplayed = false;
        }
    }

}
