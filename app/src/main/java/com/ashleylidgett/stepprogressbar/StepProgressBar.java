package com.ashleylidgett.stepprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ashleylidgett on 17/02/2018.
 */

public class StepProgressBar extends View
{

    private Paint mBasePaint;
    private Paint mCompletePaint;
    private Paint mCurrentPaint;
    private Paint mOutlinePaint;
    private int mProgressPoints, mProgressPointsComplete;
    private TextPaint mTextPaint;
    private float mTextCenter, mStartX, mEndX, mRadius;
    private float mCurrentPointRadius;

    public StepProgressBar(Context context, int progressPoints, int progressPointsComplete)
    {
        super(context);
        this.mProgressPoints = progressPoints;
        this.mProgressPointsComplete = progressPointsComplete;
        init();
    }

    public StepProgressBar(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init();
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StepProgressBar,
                0, 0);

        try
        {
            mBasePaint.setColor(a.getInteger(R.styleable.StepProgressBar_background_color, Color.GRAY));
            mCompletePaint.setColor(a.getInteger(R.styleable.StepProgressBar_completed_point_color, Color.BLACK));
            mCurrentPaint.setColor(a.getInteger(R.styleable.StepProgressBar_current_point_color, Color.DKGRAY));

//            mRadius = a.getFloat(R.styleable.AshProgressBar2_point_size, 30);

            mProgressPoints = a.getInteger(R.styleable.StepProgressBar_progress_points, 1);
            mProgressPointsComplete = a.getInteger(R.styleable.StepProgressBar_completed_progress_points, 0);
        }
        finally
        {
            a.recycle();
        }
    }

    public StepProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        init();
    }

    public StepProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init()
    {
        mRadius = 30;
        mCurrentPointRadius = 40;
        mStartX = 0 + mRadius;
        mEndX = getMeasuredWidth() - mRadius;

        mBasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBasePaint.setStyle(Paint.Style.FILL);
        mBasePaint.setColor(Color.GRAY);
        mCompletePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCompletePaint.setStyle(Paint.Style.FILL);
        mCompletePaint.setColor(Color.BLACK);
        mCurrentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCurrentPaint.setStyle(Paint.Style.FILL);
        mCurrentPaint.setColor(Color.BLUE);
        mOutlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOutlinePaint.setStyle(Paint.Style.STROKE);
        mOutlinePaint.setStrokeWidth(5);
        mOutlinePaint.setColor(Color.BLACK);
        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(26);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextCenter = mCurrentPointRadius - ((mTextPaint.descent() + mTextPaint.ascent()) / 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int desiredWidth = getMeasuredWidth();
        int desiredHeight = 80;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY)
        {
            //Must be this size
            width = widthSize;
        }
        else if (widthMode == MeasureSpec.AT_MOST)
        {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        }
        else
        {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY)
        {
            //Must be this size
            height = heightSize;
        }
        else if (heightMode == MeasureSpec.AT_MOST)
        {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        }
        else
        {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        /**
         * Draw all corresponding points depending on how many reading points there are
         */
        float increment = getMeasuredWidth() / (mProgressPoints + 1);
        drawRectangles(canvas, increment, mProgressPoints);
        drawPoints(canvas, increment, (mProgressPoints));
    }

    public void setProgress(int progress)
    {
        this.mProgressPoints = progress;
//        invalidate();
    }

    public void setProgressComplete(int progressComplete)
    {
        this.mProgressPointsComplete = progressComplete;
//        invalidate();
    }

    public void setBackgroundColor(int color)
    {
        this.mBasePaint.setColor(color);
    }

    public void setCompleteColor(int color)
    {
        this.mCompletePaint.setColor(color);
    }

    public void setCurrentColor(int color)
    {
        this.mCurrentPaint.setColor(color);
    }

    private void drawRectangles(Canvas canvas, float increment, int amountOfPoints)
    {
        drawRect(canvas, 20, getMeasuredWidth() - 20, amountOfPoints);
        drawRect(canvas, 20, increment, 0);

        for (int j = 1; j <= (amountOfPoints - 1); j++)
        {
            drawRect(canvas, increment * (j), increment * (j + 1), (j));
        }
    }

    private void drawRect(Canvas canvas, float start, float end, int value)
    {
        canvas.drawRect(start, mCurrentPointRadius - 6, end, mCurrentPointRadius + 6,
                mProgressPointsComplete >= value ? mCompletePaint : mBasePaint);
    }

    private void drawPoints(Canvas canvas, float increment, int amountOfPoints)
    {
        for (int i = 1; i <= amountOfPoints; i++)
        {
            drawPoint(canvas, increment * i, i - 1, String.valueOf(i));
        }
    }

    private void drawPoint(Canvas canvas, float x, int value, String text)
    {
        if (mProgressPointsComplete == value)
        {
            canvas.drawCircle(x, mCurrentPointRadius, mRadius, mCurrentPaint);
        }
        else
        {
            canvas.drawCircle(x, mCurrentPointRadius, mRadius, mProgressPointsComplete > value ? mCompletePaint : mBasePaint);
        }
        canvas.drawText(text, x, mTextCenter, mTextPaint);
    }

}

