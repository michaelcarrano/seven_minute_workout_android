package com.michaelcarrano.seven_min_workout.widget;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by michaelcarrano on 10/3/16.
 */

// http://stackoverflow.com/a/35405095
// FIXME: This might apply multiple times?
public class PaddingFixScrollingViewBehavior extends AppBarLayout.ScrollingViewBehavior {
    public PaddingFixScrollingViewBehavior() {
        super();
    }

    public PaddingFixScrollingViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        boolean result = super.onDependentViewChanged(parent, child, dependency);

        AppBarLayout appBarLayout = (AppBarLayout) dependency;

        int bottomPadding = appBarLayout.getTotalScrollRange() + appBarLayout.getTop();
        boolean paddingChanged = bottomPadding != child.getPaddingBottom();

        if (paddingChanged) {
            child.setPadding(child.getPaddingLeft(), child.getPaddingTop(), child.getPaddingRight(), bottomPadding);
            child.requestLayout();
        }

        return paddingChanged || result;
    }
}