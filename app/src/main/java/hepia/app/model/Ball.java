package hepia.app.model;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;

public class Ball {
    public static final int RAY = 10;

    private Point position;
    private double verticalSpeed;
    private double horizontalSpeed;
    private final double MAX_SPEED = 1;
    private int color = Color.GREEN;
    private RectF container;

    public void moveDown(int dy) {
        position.y += dy;
    }

    public void moveLeft(int dx) {
        position.y += dx;
    }

    public void moveRight(int dx) {
        position.y -= dx;
    }
}
