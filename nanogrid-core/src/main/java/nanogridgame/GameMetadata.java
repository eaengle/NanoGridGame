package nanogridgame;

public class GameMetadata {

    public static final String TYPE_GAME = "game";
    public static final String TYPE_PUZZLE = "puzzle";

    private String saveType = TYPE_GAME;
    private long elapsedSeconds;
    private int moveCount;

    public GameMetadata() {
    }

    public GameMetadata(GameMetadata metadata) {
        saveType = metadata.saveType;
        elapsedSeconds = metadata.elapsedSeconds;
        moveCount = metadata.moveCount;
    }

    public String getSaveType() {
        return saveType;
    }

    public void setSaveType(String saveType) {
        if (!TYPE_GAME.equals(saveType) && !TYPE_PUZZLE.equals(saveType)) {
            throw new IllegalArgumentException("unsupported save type: " + saveType);
        }
        this.saveType = saveType;
    }

    public long getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(long elapsedSeconds) {
        this.elapsedSeconds = Math.max(0, elapsedSeconds);
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = Math.max(0, moveCount);
    }
}
