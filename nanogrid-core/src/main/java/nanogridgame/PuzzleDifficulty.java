package nanogridgame;

public enum PuzzleDifficulty {
    EASY(28, 25),
    MEDIUM(40, 45),
    HARD(55, 65);

    private final int fillPercent;
    private final int continuationPercent;

    PuzzleDifficulty(int fillPercent, int continuationPercent) {
        this.fillPercent = fillPercent;
        this.continuationPercent = continuationPercent;
    }

    int getFillPercent() {
        return fillPercent;
    }

    int getContinuationPercent() {
        return continuationPercent;
    }
}
