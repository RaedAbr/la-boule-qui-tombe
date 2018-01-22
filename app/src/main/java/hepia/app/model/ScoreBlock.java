package hepia.app.model;

public class ScoreBlock extends Block {
    private int value;

    public ScoreBlock(int x, int y, float size, int value) {
        super(Block.Type.SCORE_BLOCK, x, y, size);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
