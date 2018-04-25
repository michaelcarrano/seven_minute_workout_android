package com.michaelcarrano.seven_min_workout;

import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by michaelcarrano on 12/26/13.
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

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
