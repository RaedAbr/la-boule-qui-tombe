package hepia.app.model;

public class BonusBlock extends Block {
    private int value = 5;

    public BonusBlock(int x, int y, int rayon) {
        super(Type.BONUS, x, y, rayon);
    }

    public int getValue() {
        return value;
    }
}
