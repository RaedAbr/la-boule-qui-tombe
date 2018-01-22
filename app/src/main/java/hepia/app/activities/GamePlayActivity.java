package hepia.app.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import hepia.app.R;
import hepia.app.game.GamePlayEngine;
import hepia.app.game.GamePlayView;
import hepia.app.game.GameTimer;
import hepia.app.model.Ball;
import hepia.app.model.Block;
import hepia.app.resources.PauseDialogView;
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

    public GameTimer getGameTimer() {
        return gameTimer;
    }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pause_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pause_button:
                displayPauseDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayPauseDialog() {
        final PauseDialogView alert = new PauseDialogView(this);
        alert.showDialog(new View.OnClickListener() {
            PauseDialogView pauseDialogView;
            private View.OnClickListener init(PauseDialogView pauseDialogView) {
                this.pauseDialogView = pauseDialogView;
                return this;
            }
            @Override
            public void onClick(View v) {
                engine.resume();
                view.restartDraw();
                gameTimer.startTimer();
                alert.dismiss();
            }
        }.init(alert));
        pauseGame();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        displayDialog();
    }

    private void displayDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Warning!");

        // set dialog message
        alertDialogBuilder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
//                                            MainActivity.this.finish();
//                        resetGame();
                        GamePlayActivity.super.onBackPressed();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if this button is clicked, just close
                // the dialog box and do nothing
                engine.resume();
                view.restartDraw();
                gameTimer.startTimer();
                dialog.cancel();
            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        pauseGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseGame();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        pauseGame();
//    }

    public void pauseGame() {
        engine.stop();
        view.stop();
        gameTimer.stopTimerTask();
    }

    public void resumeGame() {
        engine.resume();
        view.resume();
        gameTimer.startTimer();
    }

    public void resetGame() {
        gameTimer.setTimeValueSeconds(30);
        engine.reset();
        view.restartDraw();
        gameTimer.startTimer();
    }
}
