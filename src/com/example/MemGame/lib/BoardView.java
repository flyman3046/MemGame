package com.example.MemGame.lib;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.example.MemGame.R;

import java.util.Random;

/**
 * Created by FZ on 9/27/2015.
 */
public class BoardView extends LinearLayout implements TileView.ColorSelectedListener{

    private final int NUMBER_ROW = 5;
    private int numGuess = 0;
    private LinearLayout.LayoutParams mLayoutParams;
    private Context mContext;
    private int selectedColor = -1;
    private final static int[] colors = {Color.BLACK, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.RED, Color.GREEN, Color.BLUE};
    public BoardView(Context context) {
        this(context, null);
    }
    public BoardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1);
        init(context);
    }
    private void init(Context context) {
        setOrientation(VERTICAL);
        int[] rand = new int[NUMBER_ROW * NUMBER_ROW];
        for(int i = 0; i < NUMBER_ROW; i++) {
            for(int j = 0; j < NUMBER_ROW; j++) {
                rand[i * NUMBER_ROW + j] = i;
            }
        }
        for(int i = 0; i < rand.length; i++) {
            int val = new Random().nextInt(rand.length);
            int temp = rand[i];
            rand[i] = rand[val];
            rand[val] = temp;
        }
        for(int i = 0; i < rand.length; i++) {
            rand[i] = colors[rand[i]];
        }
//        setBackgroundColor(Color.WHITE);
        for(int i = 0; i < NUMBER_ROW; i++) {
            LinearLayout row_view = new LinearLayout(context);
            row_view.setOrientation(HORIZONTAL);
//            LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams()
            for(int j = 0; j < NUMBER_ROW; j++) {
                TileView tv = new TileView(context);
                tv.setLocation(i, j);
                tv.setBackgroundColor(Color.WHITE);

                tv.setColor(rand[i * NUMBER_ROW + j]);
                tv.setColorSelectedListener(this);
                row_view.addView(tv, 160, 160);
            }
            addView(row_view);
        }
    }


    @Override
    public void colorSelected(boolean changed, int color, int row, int col) {
        if(changed) {
            if(selectedColor == color) {
                numGuess++;
                if(numGuess == NUMBER_ROW) {
                    Log.d("Fei", "Win the game");
                    init(mContext);
                }

            }
            else {
                selectedColor = color;
                numGuess = 1;
                for(int i = 0; i < NUMBER_ROW; i++) {
                    for(int j = 0; j < NUMBER_ROW; j++) {
                        if(i == row && j == col) {
                            continue;
                        }
                        else {
                            TileView mView = (TileView) ((ViewGroup) getChildAt(i)).getChildAt(j);
//                            mView.setColor(getResources().getColor(R.color.white));
                            mView.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                    }
                }
            }

        }


    }
}
