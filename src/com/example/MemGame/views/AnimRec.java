package com.example.MemGame.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * The Animated Rectangle View.
 */
public class AnimRec extends View{

    private float mProgress;
    private Paint mPaint;
    private static final int ANIMA_TIME = 2000;

    public AnimRec(Context context) {
        this(context, null);
    }

    public AnimRec(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimRec(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(30);

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(ANIMA_TIME);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mProgress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mProgress >= 1) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
            return;
        }
        int width = getWidth() - getPaddingStart() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        int perimeter = 2 * (width + height);

        int drawLength = (int) (perimeter * mProgress);

        if(drawLength <= height) {
            canvas.drawLine(0, 0, 0, drawLength, mPaint);
            return;
        }
        else {
            canvas.drawLine(0, 0, 0, height, mPaint);
            drawLength -= height;
        }

        if(drawLength <= width) {
            canvas.drawLine(0, height, drawLength, height, mPaint);
            return;
        }
        else {
            canvas.drawLine(0, height, width, height, mPaint);
            drawLength -= width;
        }

        if(drawLength <= height) {
            canvas.drawLine(width, height, width, height - drawLength, mPaint);
            return;
        }
        else {
            canvas.drawLine(width, height, width, 0, mPaint);
            drawLength -= height;
        }

        canvas.drawLine(width, 0, width - drawLength, 0, mPaint);
    }
}
