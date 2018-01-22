package hepia.app.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.List;

import hepia.app.activities.GamePlayActivity;
import hepia.app.model.Ball;
import hepia.app.model.Block;
import hepia.app.model.ScoreBlock;

import static hepia.app.activities.GamePlayActivity.BLOCKINLINE;

public class GamePlayView extends SurfaceView implements SurfaceHolder.Callback {
    final SurfaceHolder surfaceHolder = this.getHolder();
    private DrawingThread drawingThread;
    private Paint paint;
    private List<Block> blocks;
    private List<Integer> scores;
    private Ball ball;
    private int earnedPoints = 0;

    public void setTime(int timeValueSeconds) {
        this.timeValueSeconds = timeValueSeconds;
    }

    private int timeValueSeconds;

    public void addEarnedPoints(int earnedPoints) {
        this.earnedPoints += earnedPoints;
    }

    public GamePlayView(Context context) {
        super(context);
//        this.surfaceHolder = this.getHolder();
        this.surfaceHolder.addCallback(this);
        drawingThread = new DrawingThread();

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        ball = new Ball();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Dessiner le fond de l'�cran en premier
        canvas.drawColor(Color.WHITE);
        float size = 0;
        if (blocks != null) {
            size = blocks.get(0).getRectangle().width();
            // Dessiner tous les blocs du labyrinthe
            for (Block b : blocks) {
                switch (b.getType()) {
                    case H_OBSTACLE:
                        paint.setColor(Color.BLUE);
                        canvas.drawRect(b.getRectangle(), paint);
                        break;
                    case V_OBSTACLE:
                        paint.setColor(Color.BLUE);
                        canvas.drawRect(b.getRectangle(), paint);
                        break;
//                    case ARRIVAL:
//                        paint.setColor(Color.RED);
//                        canvas.drawRect(b.getRectangle(), paint);
//                        break;
                    case BORDER:
                        paint.setColor(Color.BLACK);
                        canvas.drawRect(b.getRectangle(), paint);
                        break;
                    case SCORE_BLOCK:
                    case EMPTY_SCORE_BLOCK:
                        paint.setColor(Color.RED);
                        paint.setTextSize(20);
                        canvas.drawText(Integer.toString(((ScoreBlock)b).getValue()),
                                b.getRectangle().centerX(), b.getRectangle().centerY(), paint);
                        break;
                }
            }
            if (scores != null) {
                int i = 0;
                for (int sc: scores) {
                    paint.setTextSize(40);
                    canvas.drawText(Integer.toString(sc), size * 5 + (6 * i * size), size * 29 - 5, paint);
                    i++;
                }
            }
        }
        // Dessiner la boule
        if(ball != null) {
//            paint.setColor(Color.GRAY);
//            canvas.drawRect(ball.getContainer(), paint);
            paint.setColor(ball.getColor());
            canvas.drawCircle(ball.getPosY(), ball.getPosX(), ball.getRay(), paint);
        }
        float posY = size * BLOCKINLINE+ size * 3;
        paint.setTextSize(50);
        canvas.drawText("Score: " + Integer.toString(earnedPoints), 20, posY, paint);
        int minutes = timeValueSeconds / 60;
        int seconds = timeValueSeconds % 60;
        String timeStr = (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds);
        canvas.drawText("Time: " + timeStr, size * BLOCKINLINE - 280, posY, paint);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawingThread = new DrawingThread();
        drawingThread.keepDrawing = true;
        drawingThread.start();
        // Quand on crée la boule, on lui indique les coordonn�es de l'écran
        if(ball != null ) {
            int blockSize = BLOCKINLINE * ball.getRay() * 2;
            this.ball.setScreenSize(blockSize);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawingThread.keepDrawing = false;
        boolean retry = true;
        while (retry) {
            try {
                drawingThread.join();
                retry = false;
            } catch (InterruptedException ignored) {}
        }
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public void setScores(List<Integer> scores) {
        this.scores = scores;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public void stop() {
        drawingThread.keepDrawing = false;
    }

    public void resume() {
        drawingThread.keepDrawing = true;
    }

    private class DrawingThread extends Thread {
        private boolean keepDrawing = true;

        @SuppressLint("WrongCall")
        @Override
        public void run() {
            Canvas canvas;
            while (keepDrawing) {
                canvas = null;

                try {
                    canvas = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        onDraw(canvas);
                    }
                } finally {
                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }

                // Pour dessiner à 50 fps
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ignored) {}
            }
        }
    }
}