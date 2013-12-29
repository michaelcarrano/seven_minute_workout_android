package com.michaelcarrano.seven_min_workout;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

/**
 * Created by michaelcarrano on 12/26/13.
 */
public class AboutActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        // Set the ActionBar title and up button
        getActionBar().setTitle(getString(R.string.app_label));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the text
        TextView textView = (TextView) findViewById(R.id.about_text);
        textView.append(getString(R.string.about_part1));
        textView.append("\n\n");
        textView.append(getString(R.string.about_part2));
        textView.append("\n\n");
        textView.append(getString(R.string.about_part3));
        textView.append("\n\n\n\n");
        textView.append(getString(R.string.about_part4));
        textView.append("\n\n");
        textView.append(getString(R.string.about_part5));
        textView.append("\n\n");
        textView.append(getString(R.string.about_part6));

    }
}
