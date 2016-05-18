package com.example.MemGame.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.example.MemGame.R;

import java.util.Random;

/**
 * The board view which user clicks to have consecutive same color.
 */
public class BoardView extends LinearLayout implements TileView.ColorSelectedListener{

    private final static int NUMBER_ROW = 5;
    private final static String COLOR_STATE = "colorStates";
    private final static String FLIP_STATE = "flipState";
    private final static String SAVE_STATE = "saveState";
    private int mNumGuess = 0;
    private int mSelectedColor = -1;
    private int[] colors = new int[NUMBER_ROW];
    private GameStatusListener mListener;
    private int[] mPermColors = new int[NUMBER_ROW * NUMBER_ROW];
    private boolean[] mFlipped = new boolean[NUMBER_ROW * NUMBER_ROW];
    private Paint mLinePaint;
    private static final int COLOR_LEVEL = 256;


    public BoardView(Context context) {
        this(context, null);
    }
    public BoardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    private void init(Context context) {
        setOrientation(VERTICAL);
        removeAllViews();
        initColors();

        for(int i = 0; i < NUMBER_ROW; i++) {
            LinearLayout row_view = new LinearLayout(context);
            row_view.setOrientation(HORIZONTAL);
            row_view.setLayoutParams(new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT, 1.0f));

            for(int j = 0; j < NUMBER_ROW; j++) {
                TileView tv = new TileView(context);
                tv.setLocation(i, j);

                tv.setColor(mPermColors[i * NUMBER_ROW + j]);
                tv.setColorSelectedListener(this);
                row_view.addView(tv, new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT, 1.0f));
            }
            addView(row_view);
        }

        mLinePaint = new Paint();
        int lineWidth = 10;
        mLinePaint.setColor(Color.RED);
        mLinePaint.setStrokeWidth(lineWidth);
        mLinePaint.setStyle(Paint.Style.STROKE);
        setWillNotDraw(false);
    }


    private void initColors() {
        Random random = new Random();
        for(int i = 0; i < NUMBER_ROW; i++) {
            int color = 0xFF << 24;
            color += random.nextInt(COLOR_LEVEL) << 16;
            color += random.nextInt(COLOR_LEVEL) << 8;
            color += random.nextInt(COLOR_LEVEL);
            colors[i] = color;
        }

        for(int i = 0; i < NUMBER_ROW; i++) {
            for(int j = 0; j < NUMBER_ROW; j++) {
                mPermColors[i * NUMBER_ROW + j] = i;
            }
        }
        for(int i = 0; i < mPermColors.length; i++) {
            int val = new Random().nextInt(mPermColors.length);
            int temp = mPermColors[i];
            mPermColors[i] = mPermColors[val];
            mPermColors[val] = temp;
        }
        for(int i = 0; i < mPermColors.length; i++) {
            mPermColors[i] = colors[mPermColors[i]];
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        int width = getWidth();
        int height = getHeight();
        int unitX = width / NUMBER_ROW;
        int unitY = height / NUMBER_ROW;

        for(int i = 1; i < NUMBER_ROW; i++) {
            canvas.drawLine(0, i * unitY, width, i * unitY, mLinePaint);
            canvas.drawLine(i * unitX, 0, i * unitX, height, mLinePaint);
        }
    }

    @Override
    public void colorSelected(boolean flipped, int color, int row, int col) {
        mFlipped[NUMBER_ROW * row + col] = flipped;
        if(mSelectedColor == color) {
            mNumGuess++;
            if(mNumGuess == NUMBER_ROW) {
                if(mListener != null) {
                    mListener.winGameCallback();
                }
            }
        }
        else {
            mSelectedColor = color;
            mNumGuess = 1;
            for(int i = 0; i < NUMBER_ROW; i++) {
                for(int j = 0; j < NUMBER_ROW; j++) {
                    if(i == row && j == col) {
                        continue;
                    }
                    else {
                        TileView mView = (TileView) ((ViewGroup) getChildAt(i)).getChildAt(j);
                        mView.setBackgroundColor(getResources().getColor(R.color.white));
                        mView.setFlipped(false);
                        mFlipped[i * NUMBER_ROW + j] = false;
                    }
                }
            }
        }
    }
    public void setGameStatusListener(GameStatusListener listener) {
        mListener = listener;
    }

    private void saveColors() {
        for(int i = 0; i < NUMBER_ROW; i++) {
            LinearLayout row_view = (LinearLayout) getChildAt(i);
            row_view.setOrientation(HORIZONTAL);
            for(int j = 0; j < NUMBER_ROW; j++) {
                TileView tv = (TileView) row_view.getChildAt(j);

                tv.setColor(mPermColors[i * NUMBER_ROW + j]);
            }
        }
    }

    private void saveFlipped() {
        for(int i = 0; i < NUMBER_ROW; i++) {
            LinearLayout row_view = (LinearLayout) getChildAt(i);
            row_view.setOrientation(HORIZONTAL);
            for(int j = 0; j < NUMBER_ROW; j++) {
                TileView tv = (TileView) row_view.getChildAt(j);
                if(mFlipped[i * NUMBER_ROW + j]) {
                    tv.setBackgroundColor(mPermColors[i * NUMBER_ROW + j]);
                }
            }
        }
    }

    public interface GameStatusListener {
        void winGameCallback();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SAVE_STATE, super.onSaveInstanceState());
        bundle.putIntArray(COLOR_STATE, mPermColors);
        bundle.putBooleanArray(FLIP_STATE, mFlipped);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.mPermColors = bundle.getIntArray(COLOR_STATE);
            this.mFlipped = bundle.getBooleanArray(FLIP_STATE);
            saveColors();
            saveFlipped();
            state = bundle.getParcelable(SAVE_STATE);
        }
        super.onRestoreInstanceState(state);
    }
}
