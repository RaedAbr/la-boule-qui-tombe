package hepia.app.resources;

public class Direction {
    public enum Dir {
        UP, DOWN, LEFT, RIGHT
    }

    private Dir verticalDir;
    private Dir hotizontalDir;

    public Direction(float x, float y) {
        if (x > 0) {
            verticalDir = Dir.DOWN;
        } else {
            verticalDir = Dir.UP;
        }
        if (y > 0) {
            hotizontalDir = Dir.RIGHT;
        } else {
            hotizontalDir = Dir.LEFT;
        }
    }

    public Dir getVerticalDir() {
        return verticalDir;
    }

    public Dir getHotizontalDir() {
        return hotizontalDir;
    }
}
