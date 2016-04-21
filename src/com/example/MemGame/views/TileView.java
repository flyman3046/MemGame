package com.example.MemGame.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
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
        init();
    }

    public void setFlipped(boolean flipped) {
        mIsFlipped = flipped;
    }

    public void setLocation(int row, int col) {
        this.mRow = row;
        this.mCol = col;
    }

    private void init() {
        setBackgroundColor(Color.WHITE);
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.stateToSave = this.mColor;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState)state;
        this.mColor = ss.stateToSave;
        super.onRestoreInstanceState(ss.getSuperState());
    }

    public void setColorSelectedListener(ColorSelectedListener csListener) {
        mColorSelectedListener = csListener;
    }

    private static class SavedState extends View.BaseSavedState {
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
