package com.example.MemGame.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import com.example.MemGame.R;

/**
 * The tile view which is a square inside the board view.
 */
public class TileView extends View{
    private int mColor = Color.RED;
    private boolean mIsFlipped = false;
    private ColorSelectedListener mColorSelectedListener;
    private int mRow = 0;
    private int mCol = 0;

    public TileView(Context context) {
        this(context, null);
    }

    public TileView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color;
                if(mIsFlipped) {
                    color = getResources().getColor(R.color.white);
                }
                else {
                    color = mColor;
                }
                mIsFlipped = !mIsFlipped;
                setBackgroundColor(color);
                if(mColorSelectedListener != null) {
                    mColorSelectedListener.colorSelected(mIsFlipped, color, mRow, mCol);
                }
            }
        });
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 100;
        int desiredHeight = 100;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    public void setFlipped(boolean flipped) {
        mIsFlipped = flipped;
    }

    public void setLocation(int row, int col) {
        this.mRow = row;
        this.mCol = col;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public void setColorSelectedListener(ColorSelectedListener csListener) {
        mColorSelectedListener = csListener;
    }

    public interface ColorSelectedListener {
        void colorSelected(boolean flipped, int color, int row, int col);
    }
}
