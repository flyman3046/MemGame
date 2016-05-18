package com.example.MemGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.example.MemGame.views.BoardView;
import com.example.MemGame.views.ExplosionField;

public class MyActivity extends Activity implements BoardView.GameStatusListener{
    private ExplosionField mExplosionField;
    private BoardView mBoard;
    private static final int DELAY_NEW_GAME = 500;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mExplosionField = ExplosionField.attach2Window(this);
        mBoard = (BoardView) findViewById(R.id.board);
        mBoard.setGameStatusListener(this);
    }

    @Override
    public void winGameCallback() {
        mExplosionField.explode(mBoard);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }, DELAY_NEW_GAME);
    }
}
