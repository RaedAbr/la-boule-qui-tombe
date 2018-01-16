package hepia.app.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.List;

import hepia.app.model.Block;

public class GamePlayView extends SurfaceView implements SurfaceHolder.Callback {
    final SurfaceHolder surfaceHolder;
    DrawingThread drawingThread;
    Paint paint;
    List<Block> blocks;

    public GamePlayView(Context context) {
        super(context);
        this.surfaceHolder = this.getHolder();
        this.surfaceHolder.addCallback(this);
        drawingThread = new DrawingThread();

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

//        mBoule = new Boule(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Dessiner le fond de l'�cran en premier
        canvas.drawColor(Color.WHITE);
        if (blocks != null) {
            // Dessiner tous les blocs du labyrinthe
            for (Block b : blocks) {
                switch (b.getType()) {
                    case DEPART:
                        paint.setColor(Color.GREEN);
                        break;
                    case ARRIVEE:
                        paint.setColor(Color.RED);
                        break;
                    case TROU:
                        paint.setColor(Color.BLACK);
                        break;
                }
                canvas.drawRect(b.getRectangle(), paint);
            }
        }
        // Dessiner la boule
//        if(mBoule != null) {
//            mPaint.setColor(mBoule.getCouleur());
//            pCanvas.drawCircle(mBoule.getX(), mBoule.getY(), mBoule.rayon, mPaint);
//        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawingThread.keepDrawing = true;
        drawingThread.start();
        // Quand on crée la boule, on lui indique les coordonn�es de l'écran
//        if(mBoule != null ) {
//            this.mBoule.setHeight(getHeight());
//            this.mBoule.setWidth(getWidth());
//        }
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

    private class DrawingThread extends Thread {
        boolean keepDrawing = true;

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

                // Pour dessiner � 50 fps
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ignored) {}
            }
        }
    }
}