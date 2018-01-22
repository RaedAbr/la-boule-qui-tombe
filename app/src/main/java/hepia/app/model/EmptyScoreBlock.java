package hepia.app.model;

/**
 * Created by raed on 22.01.18.
 */

public class EmptyScoreBlock extends ScoreBlock {
    public EmptyScoreBlock(int x, int y, int rayon) {
        super(x, y, rayon, 0);
        this.setType(Type.EMPTY_SCORE_BLOCK);
    }
}
