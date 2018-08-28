package com.michaelcarrano.seven_min_workout.widget;

import com.michaelcarrano.seven_min_workout.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by michaelcarrano on 12/17/13.
 */
public class CircularProgressBar extends View {

    private static final int DEFAULT_MAX_VALUE = 100;

    private static final float ADJUST_FOR_12_OCLOCK = 270f;

    // properties for the background circle
    private final Paint mBgPaint;

    // properties for the progress circle
    private final Paint mProgressPaint;

    private final Paint mTextPaint;

    // text properties for the countdown text
    private boolean mShowText;

    // maximum number of points in the circle default is 100
    private int mMax;

    // current progress between 0 and mMax
    private float mProgress;

    // diameter (in dp) of the circle
    private float mDiameter;

    // margin between circle and edges (default is 4dp)
    // NOTE: you will need to include some margin to account for the stroke width, so min padding is strokeWidth/2
    private int mLayoutMargin;

    // area to draw the progress arc
    private RectF mArcBounds;

    // height taken to draw text with the current settings
    private Rect mTextBounds;

    public CircularProgressBar(final Context context, final AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CircularProgressBar(final Context context, final AttributeSet attrs,
            final int defStyle) {
        super(context, attrs, defStyle);

        // extract params (if provided)
        final TypedArray args = context
                .obtainStyledAttributes(attrs, R.styleable.CircularProgressBar);

        final float defaultDiameter = TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, this.getResources()
                        .getDisplayMetrics());
        final float defaultStrokeWidth = TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, this.getResources()
                        .getDisplayMetrics());
        final float defaultMargin = TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, this.getResources()
                        .getDisplayMetrics());
        final float defaultTextSize = TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, this.getResources()
                        .getDisplayMetrics());

        try {
            final int bgColor = args
                    .getColor(R.styleable.CircularProgressBar_bgColor, getResources().getColor(R.color.black));
            final int bgStrokeWidth = args
                    .getDimensionPixelSize(R.styleable.CircularProgressBar_bgStrokeWidth,
                            (int) defaultStrokeWidth);

            final int progressColor = args
                    .getColor(R.styleable.CircularProgressBar_progressColor, getResources().getColor(R.color.white));
            final int progressStrokeWidth = args.getDimensionPixelSize(
                    R.styleable.CircularProgressBar_progressStrokeWidth, (int) defaultStrokeWidth);

            this.mShowText = args.getBoolean(R.styleable.CircularProgressBar_showText, false);
            final int textSize = args
                    .getDimensionPixelSize(R.styleable.CircularProgressBar_android_textSize,
                            (int) defaultTextSize);
            final int textColor = args
                    .getInt(R.styleable.CircularProgressBar_android_textColor, R.color.white);

            this.mLayoutMargin = args
                    .getDimensionPixelSize(R.styleable.CircularProgressBar_android_layout_margin,
                            (int) defaultMargin);

            this.mMax = args.getInt(R.styleable.CircularProgressBar_max, DEFAULT_MAX_VALUE);

            this.mDiameter = args
                    .getDimension(R.styleable.CircularProgressBar_diameter, defaultDiameter);

            // create paint settings based on supplied args
            this.mBgPaint = new Paint();
            this.mBgPaint.setColor(bgColor);
            this.mBgPaint.setStyle(Style.STROKE);
            this.mBgPaint.setAntiAlias(true);
            this.mBgPaint.setStrokeWidth(bgStrokeWidth);

            this.mProgressPaint = new Paint();
            this.mProgressPaint.setColor(progressColor);
            this.mProgressPaint.setStyle(Style.STROKE);
            this.mProgressPaint.setAntiAlias(true);
            this.mProgressPaint.setStrokeWidth(progressStrokeWidth);

            this.mTextPaint = new Paint();
            this.mTextPaint.setColor(textColor);
            this.mTextPaint.setAntiAlias(true);
            this.mTextPaint.setStyle(Style.STROKE);
            this.mTextPaint.setTextAlign(Align.CENTER);
            this.mTextPaint.setFakeBoldText(true);
            this.mTextPaint.setTextSize(textSize);
        } finally {
            args.recycle();
        }
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        if (this.mArcBounds == null) {
            // set view bounds for arc drawing
            this.mArcBounds = new RectF(this.mLayoutMargin, this.mLayoutMargin,
                    this.mLayoutMargin + this.mDiameter,
                    this.mLayoutMargin + this.mDiameter);
        }

        // draw bg circle in the center
        final float radius = this.mDiameter / 2;
        final float center = radius + this.mLayoutMargin;
        canvas.drawCircle(center, center, radius, this.mBgPaint);

        // draw any progress over the top
        // why is this BigDecimal crap even needed? java why?
        final BigDecimal percentage = BigDecimal.valueOf(this.mProgress)
                .divide(BigDecimal.valueOf(this.mMax), 4,
                        RoundingMode.HALF_DOWN);
        final BigDecimal sweepAngle = percentage.multiply(BigDecimal.valueOf(360));

        // bounds are same as the bg circle, so diameter width and height moved in by margin
        canvas.drawArc(this.mArcBounds, CircularProgressBar.ADJUST_FOR_12_OCLOCK,
                sweepAngle.floatValue(), false,
                this.mProgressPaint);

        if (this.mShowText) {
            if (this.mTextBounds == null) {
                // Reference: http://stackoverflow.com/questions/3654321/measuring-text-height-to-be-drawn-on-canvas-android
                // answer #2
                this.mTextBounds = new Rect();
                this.mTextPaint.getTextBounds("0", 0, 1, this.mTextBounds); //$NON-NLS-1$
            }

            // draw text in the center
            canvas.drawText(String.valueOf((int)(this.mProgress + 1)), center,
                    center + (this.mTextBounds.height() >> 1),
                    this.mTextPaint);
        }
    }

    public void setProgress(final float progress) {
        this.mProgress = progress;

        // force redraw
        this.invalidate();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        // size will always be diameter + margin on add sides
        final int size = (int) this.mDiameter + (this.mLayoutMargin * 2);
        this.setMeasuredDimension(size, size);
    }

    public void setMax(final int max) {
        this.mMax = max;
    }

    public void setBgColor(final int bgColor) {
        this.mBgPaint.setColor(bgColor);
    }

    public void setBgStrokeWidth(final int bgStrokeWidth) {
        this.mBgPaint.setStrokeWidth(bgStrokeWidth);
    }

    public void setProgressColor(final int progressColor) {
        this.mProgressPaint.setColor(progressColor);
    }

    public void setProgressStrokeWidth(final int progressStrokeWidth) {
        this.mProgressPaint.setStrokeWidth(progressStrokeWidth);
    }

    public void setShowText(final boolean showText) {
        this.mShowText = showText;
    }

    public void setTextSize(final int textSize) {
        this.mTextPaint.setTextSize(textSize);
    }

    public void setTextColor(final int textColor) {
        this.mTextPaint.setColor(textColor);
    }

    public void setDiameter(final float diameter) {
        this.mDiameter = diameter;
    }
}
