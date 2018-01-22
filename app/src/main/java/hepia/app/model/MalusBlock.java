package hepia.app.model;

public class MalusBlock extends Block {
    private int value = 5;

    public MalusBlock(int x, int y, int rayon) {
        super(Type.MALUS, x, y, rayon);
    }

    public int getValue() {
        return value;
    }
}
