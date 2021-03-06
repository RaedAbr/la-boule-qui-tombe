package hepia.app.model;

import android.graphics.Color;
import android.graphics.RectF;

import java.util.List;

import hepia.app.resources.Direction;
import hepia.app.resources.GameDifficulty;

public class Ball {
    private int ray;
    // Vitesse maximale autorisée pour la boule
    private static final float MAX_SPEED = 20.0f;
    // Permet à la boule d'accélérer moins vite
    private static final float COMPENSATOR = 8.0f;
    // Utilisé pour compenser les rebonds
    private static final float REBOUND = 2f;

    private float posY;
    private float posX;
    private float speedX;
    private float speedY;
    private float fallForce;
    private int screenWidth;

    private int screenHeight;
    private RectF container;
    private RectF initialContainer;

    public Ball(int ray, GameDifficulty difficulty) {
        this.ray = ray;
        switch (difficulty) {
            case MEDIUM:
                this.fallForce = 5f;
                break;
            case HARD:
                this.fallForce = 10f;
                break;
            default:
                this.fallForce = 2f;
                break;
        }
        this.container = new RectF();
    }

    public Ball() {
    }

    public int getRay() {
        return ray;
    }

    public RectF getContainer() {
        return container;
    }

    private void updatePosY(float posY) {
        this.posY = posY;
        // Si la boule sort du cadre, on rebondit
        if (this.posY < ray * 3) {
            this.posY = ray * 3;
            // Rebondir, c'est changer la direction de la balle
            this.speedY = -this.speedY / REBOUND;
        } else if (this.posY > this.screenWidth - ray * 3) {
            this.posY = this.screenWidth - ray * 3;
            this.speedY = -this.speedY / REBOUND;
        }
    }

    private void reboundToLeft(float posY, float left) {
        updatePosY(posY);
        if (this.posY > left - ray && this.posY < left + ray) {
            this.posY = left - ray;
            this.speedY = -this.speedY / REBOUND;
        }
    }

    private void reboundToRight(float posY, float right) {
        updatePosY(posY);
        if (this.posY < right + ray && this.posY > right - ray) {
            this.posY = right + ray;
            this.speedY = -this.speedY / REBOUND;
        }
    }

    private void updatePosX(float posX) {
        this.posX = posX;
        if(this.posX > this.screenHeight - ray * 3) {
            this.posX = this.screenHeight - ray * 3;
            this.speedX = -this.speedX / REBOUND;
        }
    }

    private void reboundToUp(float posX, float top) {
        updatePosX(posX);
        if (this.posX + ray < top + ray * 4) {
            this.posX = top - ray;
            this.speedX = -this.speedX / REBOUND;
        }
    }

    private void reboundToDown(float posX, float bottom) {
        updatePosX(posX);
        if (this.posX - ray > bottom - ray * 4) {
            this.posX = bottom + ray;
            this.speedX = -this.speedX / REBOUND;
        }
    }

    public void setScreenSize(int screenSize) {
        this.screenWidth = screenSize;
        this.screenHeight = screenSize;
    }

    public void setInitialContainer(RectF initialContainer) {
        this.initialContainer = initialContainer;
        this.posY = initialContainer.left + ray;
        this.posX = initialContainer.top + ray;
    }

    public void setContainer(RectF container) {
        this.container = container;
    }

    public void updateXY(float y) {
        speedX += fallForce / COMPENSATOR;
        if(speedX > MAX_SPEED)
            speedX = MAX_SPEED;

        speedY -= y / COMPENSATOR;
        if(speedY > MAX_SPEED)
            speedY = MAX_SPEED;
        if(speedY < -MAX_SPEED)
            speedY = -MAX_SPEED;

        updatePosX(posX + speedX);
        updatePosY(posY + speedY);
    }

    public void manageCollision(/*boolean detectCollision, */Block blockInCollision, RectF inter2) {
        Direction dir = new Direction(speedX, speedY);
        if (blockInCollision.getType() == Block.Type.H_OBSTACLE) {
            if (dir.getVerticalDir() == Direction.Dir.DOWN) {
                reboundToUp(posX + speedX, inter2.top);
            } else if (dir.getVerticalDir() == Direction.Dir.UP) {
                reboundToDown(posX + speedX, inter2.bottom);
            }
            updatePosY(posY + speedY);
        } else if (blockInCollision.getType() == Block.Type.V_OBSTACLE) {
            if (dir.getHotizontalDir() == Direction.Dir.RIGHT) {
                reboundToLeft(posY + speedY, inter2.left);
            } else if (dir.getHotizontalDir() == Direction.Dir.LEFT) {
                reboundToRight(posY + speedY, inter2.right);
            }
            updatePosX(posX + speedX);
        }
    }

    public void updateContainerPos() {
        // Met à jour les coordonnées du rectangle de collision
        container.set(posY - ray, posX - ray, posY + ray, posX + ray);
    }

    public void reset() {
        speedX = 0;
        speedY = 0;
        this.posY = initialContainer.left + ray;
        this.posX = initialContainer.top + ray;
    }
}
