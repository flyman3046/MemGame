package com.example.MemGame.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.example.MemGame.R;

import java.util.Random;

/**
 * The board view which users clicks to have consecutive same color.
 */
public class BoardView extends LinearLayout implements TileView.ColorSelectedListener{

    private final static int NUMBER_ROW = 5;
    private final static String COLOR_STATE = "colorStates";
    private int mNumGuess = 0;
    private int mSelectedColor = -1;
    private final static int[] COLORS = {Color.BLACK, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.RED, Color.GREEN, Color.BLUE};
    private GameStatusListener mListener;
    private int[] mPermColors = new int[NUMBER_ROW * NUMBER_ROW];

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
            mPermColors[i] = COLORS[mPermColors[i]];
        }

        for(int i = 0; i < NUMBER_ROW; i++) {
            LinearLayout row_view = new LinearLayout(context);
            row_view.setOrientation(HORIZONTAL);
            for(int j = 0; j < NUMBER_ROW; j++) {
                TileView tv = new TileView(context);
                tv.setLocation(i, j);

                tv.setColor(mPermColors[i * NUMBER_ROW + j]);
                tv.setColorSelectedListener(this);
                row_view.addView(tv, 160, 160);
            }
            addView(row_view);
        }
    }

    @Override
    public void colorSelected(boolean changed, int color, int row, int col) {
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
                    }
                }
            }
        }
    }
    public void setGameStatusListener(GameStatusListener listener) {
        mListener = listener;
    }
    public interface GameStatusListener {
        void winGameCallback();
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

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putIntArray(COLOR_STATE, mPermColors);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.mPermColors = bundle.getIntArray(COLOR_STATE);
            saveColors();
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }
}
