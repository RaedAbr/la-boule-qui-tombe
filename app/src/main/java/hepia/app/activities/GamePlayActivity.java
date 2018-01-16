package hepia.app.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.widget.TextView;

import java.util.List;

import hepia.app.R;
import hepia.app.game.GamePlayEngine;
import hepia.app.game.GamePlayView;
import hepia.app.model.Block;
import hepia.app.resources.GameDifficulty;
import hepia.app.resources.IntentConst;

public class GamePlayActivity extends AppCompatActivity {
    TextView textView;
    private GamePlayEngine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GamePlayView view = new GamePlayView(this);
        setContentView(view);
        this.engine = new GamePlayEngine(this);
        Display display = getWindowManager().getDefaultDisplay();
        Point dim = new Point();
        display.getSize(dim);
        int width = dim.x;
        int height = dim.y;
        int size = (dim.x / 20);
//        Boule b = new Boule(rayon);
//        mView.setBoule(b);
//        mEngine.setBoule(b);

        List<Block> list = engine.buildLabyrinthe(size);
        view.setBlocks(list);
    }
}
