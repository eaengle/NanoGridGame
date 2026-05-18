package nanogridgame;

public class NanoGridParameters {

    private int columns;
    private int rows;
    private int maxColumnSquares;
    private int maxRowSquares;
    private int rowBreakChance;
    private PuzzleDifficulty difficulty;
    private boolean useSeed;
    private long seed;
    private boolean symmetric;

    public NanoGridParameters() {
        maxColumnSquares = 5;
        maxRowSquares = 5;
        rowBreakChance = 50;
        columns = 10;
        rows = 10;
        difficulty = PuzzleDifficulty.MEDIUM;
        useSeed = false;
        seed = 0;
        symmetric = false;
    }

    public NanoGridParameters(NanoGridParameters p) {
        columns = p.columns;
        rows = p.rows;
        maxColumnSquares = p.maxColumnSquares;
        maxRowSquares = p.maxRowSquares;
        rowBreakChance = p.rowBreakChance;
        difficulty = p.difficulty;
        useSeed = p.useSeed;
        seed = p.seed;
        symmetric = p.symmetric;
    }

    public int getColumns() { return columns; }
    public void setColumns(int columns) { this.columns = columns; }

    public int getRows() { return rows; }
    public void setRows(int rows) { this.rows = rows; }

    public int getMaxColumnSquares() { return maxColumnSquares; }
    public void setMaxColumnSquares(int maxColumnSquares) { this.maxColumnSquares = maxColumnSquares; }

    public int getMaxRowSquares() { return maxRowSquares; }
    public void setMaxRowSquares(int maxRowSquares) { this.maxRowSquares = maxRowSquares; }

    public int getRowBreakChance() { return rowBreakChance; }
    public void setRowBreakChance(int rowBreakChance) { this.rowBreakChance = rowBreakChance; }

    public PuzzleDifficulty getDifficulty() { return difficulty; }
    public void setDifficulty(PuzzleDifficulty difficulty) {
        if (difficulty == null) {
            throw new IllegalArgumentException("difficulty cannot be null");
        }
        this.difficulty = difficulty;
    }

    public boolean isUseSeed() { return useSeed; }
    public void setUseSeed(boolean useSeed) { this.useSeed = useSeed; }

    public long getSeed() { return seed; }
    public void setSeed(long seed) {
        this.seed = seed;
        this.useSeed = true;
    }

    public void clearSeed() {
        this.seed = 0;
        this.useSeed = false;
    }

    public boolean isSymmetric() { return symmetric; }
    public void setSymmetric(boolean symmetric) { this.symmetric = symmetric; }
}
