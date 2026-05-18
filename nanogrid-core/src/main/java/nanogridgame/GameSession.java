package nanogridgame;

public class GameSession {

    private final Puzzle puzzle;
    private final PlayerGrid playerGrid;

    public GameSession(Puzzle puzzle) {
        this(puzzle, new PlayerGrid(puzzle.getColumns(), puzzle.getRows()));
    }

    public GameSession(Puzzle puzzle, PlayerGrid playerGrid) {
        if (puzzle.getColumns() != playerGrid.getColumns() || puzzle.getRows() != playerGrid.getRows()) {
            throw new IllegalArgumentException("puzzle and player grid dimensions must match");
        }
        this.puzzle = puzzle;
        this.playerGrid = playerGrid;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public PlayerGrid getPlayerGrid() {
        return playerGrid;
    }

    public void fillCell(int column, int row) {
        playerGrid.setCell(column, row, CellState.FILLED);
    }

    public void markCell(int column, int row) {
        playerGrid.setCell(column, row, CellState.MARKED);
    }

    public void clearCell(int column, int row) {
        playerGrid.clearCell(column, row);
    }

    public int getIncorrectMoves() {
        int count = 0;
        for (int c = 0; c < puzzle.getColumns(); c++) {
            for (int r = 0; r < puzzle.getRows(); r++) {
                if (!puzzle.isFilled(c, r) && playerGrid.getCell(c, r) == CellState.FILLED) {
                    count++;
                }
            }
        }
        return count;
    }
}
