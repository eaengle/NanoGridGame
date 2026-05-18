package nanogridgame;

public class NanoGridParameters {

    private int columns;
    private int rows;
    private int maxColumnSquares;
    private int maxRowSquares;
    private int rowBreakChance;

    public NanoGridParameters() {
        maxColumnSquares = 5;
        maxRowSquares = 5;
        rowBreakChance = 50;
        columns = 10;
        rows = 10;
    }

    public NanoGridParameters(NanoGridParameters p) {
        columns = p.columns;
        rows = p.rows;
        maxColumnSquares = p.maxColumnSquares;
        maxRowSquares = p.maxRowSquares;
        rowBreakChance = p.rowBreakChance;
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
}
