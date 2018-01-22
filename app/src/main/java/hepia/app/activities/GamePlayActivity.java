package hepia.app.activities;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

import java.util.List;

import hepia.app.game.GamePlayEngine;
import hepia.app.game.GamePlayView;
import hepia.app.game.GameTimer;
import hepia.app.model.Ball;
import hepia.app.model.Block;
import hepia.app.resources.GameDifficulty;
import hepia.app.resources.IntentConst;

public class GamePlayActivity extends AppCompatActivity {
    public static final int BLOCKINLINE = 30;

    private GamePlayEngine engine;

    public GamePlayView getView() {
        return view;
    }

    private GamePlayView view;
    private GameDifficulty gameDifficulty;
    private GameTimer gameTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new GamePlayView(this);
        setContentView(view);
        this.engine = new GamePlayEngine(this);
        // Calcul de la taille d'un bloc par rapport à 30 blocs par ligne
        Display display = getWindowManager().getDefaultDisplay();
        Point dim = new Point();
        display.getSize(dim);
        int width = dim.x;
        int size = (width / BLOCKINLINE);

        // récupérer le niveau de jeu de l'avtivité précédente
        Intent intent = getIntent();
        int retreivedValue = intent.getIntExtra(IntentConst.DIFFICULTY.name(), 0);
        gameDifficulty = GameDifficulty.updateValue(retreivedValue);
        Ball ball = new Ball(size / 2, gameDifficulty);
        view.setBall(ball);
        engine.setBall(ball);

        // consruction de la grille et des points de score à gagner
        List<Block> blocks = engine.buildMap(size);
        view.setBlocks(blocks);
        view.setScores(engine.getScoreValues());

        gameTimer = new GameTimer(this);
//        gameTimer.startTimer();
    }

    public GameDifficulty getGameDifficulty() {
        return gameDifficulty;
    }

    @Override
    protected void onResume() {
        super.onResume();
        engine.resume();
        view.resume();
        gameTimer.startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        engine.stop();
        view.stop();
        gameTimer.stopTimerTask();
    }

    @Override
    protected void onStop() {
        super.onStop();
        engine.stop();
        view.stop();
        gameTimer.stopTimerTask();
    }
}
