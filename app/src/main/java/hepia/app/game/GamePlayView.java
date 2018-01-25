package hepia.app.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;

import java.util.List;

import hepia.app.R;
import hepia.app.model.Ball;
import hepia.app.model.Block;
import hepia.app.model.ScoreBlock;

import static hepia.app.activities.GamePlayActivity.BLOCKINLINE;

public class GamePlayView extends SurfaceView implements SurfaceHolder.Callback {
    final SurfaceHolder surfaceHolder = this.getHolder();
    private final Bitmap borderBitmap;
    private final Bitmap malusBitmap;
    private final Bitmap bonusBitmap;
    private final Bitmap ballBitmap;
    private DrawingThread drawingThread;
    private Paint paint;
    private List<Block> blocks;
    private List<Integer> scores;
    private Ball ball;

    public int getEarnedPoints() {
        return earnedPoints;
    }

    private int earnedPoints = 0;
    private String userName;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTime(int timeValueSeconds) {
        this.timeValueSeconds = timeValueSeconds;
    }

    private int timeValueSeconds;
    final private Bitmap wallBitmap;

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

        wallBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.wall);
        borderBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.border);
        bonusBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.plus5);
        malusBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.minuis5);
        ballBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ball);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Dessiner le fond de l'�cran en premier
        canvas.drawColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.LEFT);
        float size = 0;
        if (blocks != null) {
            size = blocks.get(0).getRectangle().width();
            // Dessiner tous les blocs du labyrinthe
            synchronized (blocks) {
                for (Block b : blocks) {
                    switch (b.getType()) {
                        case V_OBSTACLE:
                        case H_OBSTACLE:
//                            paint.setColor(Color.BLUE);
//                            canvas.drawRect(b.getRectangle(), paint);
                            canvas.drawBitmap(wallBitmap, null, b.getRectangle(), paint);
                            break;
//                            paint.setColor(Color.BLUE);
//                            canvas.drawRect(b.getRectangle(), paint);
//                            break;
                        //                    case ARRIVAL:
                        //                        paint.setColor(Color.RED);
                        //                        canvas.drawRect(b.getRectangle(), paint);
                        //                        break;
                        case BORDER:
//                            paint.setColor(Color.BLACK);
//                            canvas.drawRect(b.getRectangle(), paint);
                            canvas.drawBitmap(borderBitmap, null, b.getRectangle(), paint);
                            break;
                        case SCORE_BLOCK:
                        case EMPTY_SCORE_BLOCK:
//                            paint.setColor(Color.RED);
//                            paint.setTextSize(20);
//                            canvas.drawText(Integer.toString(((ScoreBlock) b).getValue()), b.getRectangle().centerX(), b.getRectangle().centerY(), paint);
                            break;
                        case MALUS:
//                            paint.setColor(Color.RED);
//                            canvas.drawRect(b.getRectangle(), paint);
                            canvas.drawBitmap(malusBitmap, null, b.getRectangle(), paint);
                            break;
                        case BONUS:
//                            paint.setColor(Color.GREEN);
//                            canvas.drawRect(b.getRectangle(), paint);
                            canvas.drawBitmap(bonusBitmap, null, b.getRectangle(), paint);
                            break;
                    }
                }
            }
            if (scores != null) {
                paint.setTextSize(40);
                paint.setColor(Color.BLACK);
                int i = 0;
                for (int sc : scores) {
                    String str;
                    if (sc == 0)
                        str = "0" + Integer.toString(sc);
                    else
                        str = Integer.toString(sc);
                    canvas.drawText(str, size * 5 + (6 * i * size), size * 29 - 5, paint);
                    i++;
                }
            }
        }
        // Dessiner la boule
        if (ball != null) {
//            paint.setColor(Color.GRAY);
//            canvas.drawRect(ball.getContainer(), paint);
//            paint.setColor(ball.getColor());
//            canvas.drawCircle(ball.getPosY(), ball.getPosX(), ball.getRay(), paint);
            canvas.drawBitmap(ballBitmap, null, ball.getContainer(), paint);
        }

        float posY = size * BLOCKINLINE + size * 2;
        paint.setColor(Color.RED);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.CENTER);
        View parent = (View)getParent();
        float centerX = parent.getWidth() / 2;
        canvas.drawText(userName, centerX, posY, paint);

        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Score: " + Integer.toString(earnedPoints), 20, posY + 70, paint);

        int minutes = timeValueSeconds / 60;
        int seconds = timeValueSeconds % 60;
        String timeStr = (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Time: " + timeStr, parent.getWidth() - 20, posY + 70, paint);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        restartDraw();
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
        stop();
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
        boolean retry = true;
        while (retry) {
            try {
                drawingThread.join();
                retry = false;
            } catch (InterruptedException ignored) {}
        }
    }

    public void resume() {
        drawingThread.keepDrawing = true;
    }

    public void restartDraw() {
        drawingThread = new DrawingThread();
        drawingThread.keepDrawing = true;
        drawingThread.start();
    }

    public void resetEaernedPoints() {
        this.earnedPoints = 0;
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
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {}
            }
        }
    }
}