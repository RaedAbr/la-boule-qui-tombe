package hepia.app.model;

import android.graphics.RectF;

public class Block {
    public enum Type { TROU, DEPART, ARRIVEE }

    private float size = 0f;

    private Type type = null;
    private RectF rectangle = null;

    public Block(Type type, int x, int y, float size) {
        this.type = type;
        this.size = size;
        this.rectangle = new RectF(x * this.size, y * this.size, (x + 1) * this.size, (y + 1) * this.size);
    }

    public Type getType() {
        return type;
    }

    public RectF getRectangle() {
        return rectangle;
    }
}
