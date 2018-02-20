package hepia.app.game;

import android.app.Activity;
import android.app.Service;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hepia.app.R;
import hepia.app.activities.GamePlayActivity;
import hepia.app.model.Ball;
import hepia.app.model.Block;
import hepia.app.model.BonusBlock;
import hepia.app.model.EmptyScoreBlock;
import hepia.app.model.MalusBlock;
import hepia.app.model.ScoreBlock;

public class GamePlayEngine {
    private SensorManager sensorManager;
    private Sensor accelerometre;
    private SensorEventListener sensorEventListener;
    private List<Block> blocks;
    private List<Integer> scoreValues;
    private Ball ball;

    public int getEarnedPoints() {
        return earnedPoints;
    }

    private int earnedPoints = 0;
    private GamePlayActivity gamePlayActivity;
    private Random r = new Random();
    private int size;

    public GamePlayEngine(final GamePlayActivity gamePlayActivity, int size) {
        this.size = size;
        sensorManager = (SensorManager) gamePlayActivity.getBaseContext().getSystemService(Service.SENSOR_SERVICE);
        assert sensorManager != null;
        accelerometre = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.gamePlayActivity = gamePlayActivity;

        sensorEventListener = new SensorEventListener() {
            private GamePlayActivity activity;
            private SensorEventListener init(GamePlayActivity activity) {
                this.activity = activity;
                return this;
            }

            @Override
            public void onSensorChanged(SensorEvent pEvent) {
                float y = pEvent.values[0];

                if (ball != null) {
                    RectF inter1 = new RectF(ball.getContainer());
                    RectF inter2 = null;
                    boolean detectCollision = false;
                    Block blockInCollision = null;
                    for (Block block : blocks) {
                        inter2 = new RectF(block.getRectangle());
                        if (inter1.intersect(inter2)) {
                            if (block.getType() == Block.Type.H_OBSTACLE
                                    || block.getType() == Block.Type.V_OBSTACLE
                                    || block.getType() == Block.Type.SCORE_BLOCK
                                    || block.getType() == Block.Type.EMPTY_SCORE_BLOCK) {
                                detectCollision = true;
                                blockInCollision = block;
                                break;
                            } else if (block.getType() == Block.Type.BONUS) {
                                activity.getGameTimer().addBonusToTime(((BonusBlock)block).getValue());
                                detectCollision = true;
                                blockInCollision = block;
                            } else if (block.getType() == Block.Type.MALUS) {
                                activity.getGameTimer().substractFromTime(((MalusBlock)block).getValue());
                                detectCollision = true;
                                blockInCollision = block;
                            }
                        }
                    }
                    if (detectCollision) {
                        if (blockInCollision.getType() == Block.Type.SCORE_BLOCK
                                || blockInCollision.getType() == Block.Type.EMPTY_SCORE_BLOCK) {
                            earnedPoints = ((ScoreBlock)blockInCollision).getValue();
                            activity.getView().addEarnedPoints(earnedPoints);
                            ball.reset();
                            updateScoreBlocks();
                            activity.getView().setScores(scoreValues);
                        } else if (blockInCollision.getType() == Block.Type.MALUS
                                || blockInCollision.getType() == Block.Type.BONUS) {
                            synchronized (blocks) {
                                blocks.remove(blockInCollision);
                                gamePlayActivity.getView().setBlocks(blocks);
                            }
                        }
                        else {
                            ball.manageCollision(blockInCollision, inter2);
                        }
                    } else {
                        ball.updateXY(y);
                    }
                    ball.updateContainerPos();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor pSensor, int pAccuracy) {

            }
        }.init(gamePlayActivity);
    }

    private void updateScoreBlocks() {
        buildScores();
        int i = 0;
        for (Block sb: blocks) {
            if (sb.getType() == Block.Type.SCORE_BLOCK) {
                ((ScoreBlock)sb).setValue(scoreValues.get(i / 2));
                i++;
            }
        }
    }

    // Remet à zéro l'emplacement de la boule
    public void reset() {
        earnedPoints = 0;
        gamePlayActivity.getView().resetEaernedPoints();
//        ball.reset();
        blocks = buildMap(size);
        gamePlayActivity.getView().setBlocks(blocks);
        updateScoreBlocks();
        gamePlayActivity.getView().setScores(scoreValues);
        resume();
    }

    // Arr�te le capteur
    public void stop() {
        sensorManager.unregisterListener(sensorEventListener, accelerometre);
    }

    // Red�marre le capteur
    public void resume() {
        sensorManager.registerListener(sensorEventListener, accelerometre, SensorManager.SENSOR_DELAY_GAME);
    }

    // Construit le labyrinthe
    public List<Block> buildMap(int rayon) {
        buildScores();

        blocks = new ArrayList<>();
        List<String> map = readMap(gamePlayActivity);
        int y = 0;
        for (String str: map) {
            int x = 0;
            int i = 0;
            for (char c: str.toCharArray()) {
                switch (c) {
                    case '*':
                        blocks.add(new Block(Block.Type.BORDER, x, y, rayon));
                        break;
                    case 'p':
                        blocks.add(new Block(Block.Type.H_OBSTACLE, x, y, rayon));
                        break;
                    case 'w':
                        blocks.add(new Block(Block.Type.V_OBSTACLE, x, y, rayon));
                        break;
                    case 'x':
                        blocks.add(new EmptyScoreBlock(x, y, rayon));
                        break;
                    case 's':
//                        int val = i / 2;
//                        if (i % 2 = 1)
                        blocks.add(new ScoreBlock(x, y, rayon, scoreValues.get(i / 2)));
                        i++;
                    default:
                        break;
                    case 'm':
                        blocks.add(new MalusBlock(x, y, rayon));
                        break;
                    case 'b':
                        blocks.add(new BonusBlock(x, y, rayon));
                        break;
                }
                x++;
            }
            y++;
        }
//
        // initial ball container
        Block block = new Block(Block.Type.NULL, 2, 2, rayon);
        ball.setInitialContainer(new RectF(block.getRectangle()));

//        blocks.add(block);
//
//        blocks.add(new Block(Block.Type.ARRIVAL, 8, 11, rayon));

        return blocks;
    }

    public void buildScores() {
        scoreValues = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            scoreValues.add(r.nextInt(10) * 10);
        }
    }

    private List<String> readMap(Activity activity) {
        int randomMap = selectRandomMap();
        List<String> map = new ArrayList<>();
        InputStream inputStream = activity.getResources().openRawResource(randomMap);
        try {
            if (inputStream != null) {
                // open a reader on the inputStream
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                // String used to store the lines
                String str;

                // Read the file
                while ((str = reader.readLine()) != null) {
                    map.add(str);
                }
                // Close streams
                reader.close();
                inputStream.close();
            }
        } catch (IOException e) {
            Toast.makeText(activity, "FileNotFoundException", Toast.LENGTH_LONG).show();
        }
        return map;
    }

    private int selectRandomMap() {
        int x = r.nextInt(3);
        switch (gamePlayActivity.getGameDifficulty()) {
            case EASY:
                switch (x) {
                    case 0:
                        return R.raw.easy_map0;
                    case 1:
                        return R.raw.easy_map1;
                    case 2:
                        return R.raw.easy_map2;
                }
            case MEDIUM:
                switch (x) {
                    case 0:
                        return R.raw.medium_map0;
                    case 1:
                        return R.raw.medium_map1;
                    case 2:
                        return R.raw.medium_map2;
                }
            case HARD:
                switch (x) {
                    case 0:
                        return R.raw.hard_map0;
                    case 1:
                        return R.raw.hard_map1;
                    case 2:
                        return R.raw.hard_map2;
                }
        }
        return 0;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public List<Integer> getScoreValues() {
        return scoreValues;
    }
}
