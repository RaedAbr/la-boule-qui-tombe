package hepia.app.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import hepia.app.R;
import hepia.app.game.GamePlayEngine;
import hepia.app.game.GamePlayView;
import hepia.app.game.GameTimer;
import hepia.app.model.Ball;
import hepia.app.model.Block;
import hepia.app.resources.GameDifficulty;
import hepia.app.resources.IntentConst;
import hepia.app.resources.PauseDialogView;

public class GamePlayActivity extends AppCompatActivity {
    public static final int BLOCKINLINE = 30;

    private GamePlayEngine engine;

    public GamePlayView getView() {
        return view;
    }

    private GamePlayView view;
    private GameDifficulty gameDifficulty;
    private String userName;

    public GameTimer getGameTimer() {
        return gameTimer;
    }

    private GameTimer gameTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Calcul de la taille d'un bloc par rapport à 30 blocs par ligne
        Display display = getWindowManager().getDefaultDisplay();
        Point dim = new Point();
        display.getSize(dim);
        int width = dim.x;
        int blockSize = (width / BLOCKINLINE);

        view = new GamePlayView(this, blockSize, width);
        setContentView(view);

        this.engine = new GamePlayEngine(this, blockSize);

        // récupérer le niveau de jeu de l'avtivité précédente
        Intent intent = getIntent();
        int retreivedValue = intent.getIntExtra(IntentConst.DIFFICULTY.name(), 0);
        userName = intent.getStringExtra(IntentConst.USER_NAME.name());
        gameDifficulty = GameDifficulty.updateValue(retreivedValue);

        view.setUserName(userName);

        Ball ball = new Ball(blockSize / 2, gameDifficulty);
        view.setBall(ball);
        engine.setBall(ball);

        // consruction de la grille et des points de score à gagner
        List<Block> blocks = engine.buildMap(blockSize);
        view.setBlocks(blocks);
        view.setScores(engine.getScoreValues());

        gameTimer = new GameTimer(this);
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

    public void saveScore() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("hight_score", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String highScore = sharedPref.getString(IntentConst.HIDHT_SCORE.name(), "");
        if (!highScore.equals("")) {
            highScore = highScore.substring(highScore.indexOf('\n') + 1);
            highScore = highScore.substring(highScore.indexOf('\n'));
            highScore = highScore.substring(highScore.indexOf('\n') + 1, highScore.indexOf("points") - 1);
            int savedScore = Integer.parseInt(highScore);
            if (view.getEarnedPoints() > savedScore) {
                editor.clear();
                editor.apply();
                writeScore(editor);
            }
        } else {
            writeScore(editor);
        }
    }

    private void writeScore(SharedPreferences.Editor editor) {
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        String str = format.format(new Date()) + "\n" + userName + "\n"
                + String.valueOf(view.getEarnedPoints() +
                " points\n" + gameDifficulty.toString() + " mode");
        editor.putString(IntentConst.HIDHT_SCORE.name(), str);
        editor.apply();
        Toast.makeText(this, "New score saved", Toast.LENGTH_LONG).show();
    }
}
