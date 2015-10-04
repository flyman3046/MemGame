package com.example.MemGame.lib;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.example.MemGame.R;

/**
 * Created by FZ on 9/27/2015.
 */
public class TileView extends View{
    private int bgColor = Color.RED;
    private Context mContext;
    private boolean isFlipped = false;
    private ColorSelectedListener mColorSelectedListener;
    private int row = 0;
    private int col = 0;

    public TileView(Context context) {
        this(context, null);
    }

    public TileView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color;
                if(isFlipped) {
                    color = getResources().getColor(R.color.white);
                }
                else {
                    color = bgColor;
                }
                isFlipped = !isFlipped;
                setBackgroundColor(color);
                if(mColorSelectedListener != null) {
                    mColorSelectedListener.colorSelected(isFlipped, color, row, col);
                }
            }
        });
        init(context);
    }

    public void setLocation(int row, int col) {
        this.row = row;
        this.col = col;

    }

    private void init(Context context) {
        setBackgroundColor(Color.WHITE);


    }
//    public void setBackgroundColor(int color) {
//        bgColor = color;
//        setBackgroundColor(new ColorDrawable(color));
//    }
    public int getColor() {
        return bgColor;
    }

    public void setColor(int color) {
        bgColor = color;
    }
    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        ss.stateToSave = this.bgColor;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        //begin boilerplate code so parent classes can restore state
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());
        //end

        this.bgColor = ss.stateToSave;
    }

    public void setColorSelectedListener(ColorSelectedListener csListener) {
        mColorSelectedListener = csListener;
    }

    static class SavedState extends View.BaseSavedState {
        int stateToSave;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.stateToSave = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.stateToSave);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    public interface ColorSelectedListener {

        void colorSelected(boolean changed, int color, int row, int col);

    }


}
