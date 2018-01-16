package hepia.app.model;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;

/**
 * Created by raed on 30.11.17.
 */

public class Ball {
    private Point position;
    private double verticalSpeed;
    private double horizontalSpeed;
    private final double MAX_SPEED = 1;
    private Color color;
    private double ray;
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
