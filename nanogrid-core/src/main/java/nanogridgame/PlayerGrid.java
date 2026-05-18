package nanogridgame;

public class PlayerGrid {

    private final CellState[][] cellsByColumn;
    private final CellState[][] cellsByRow;

    public PlayerGrid(int columns, int rows) {
        if (columns < 1) {
            throw new IllegalArgumentException("columns must be positive");
        }
        if (rows < 1) {
            throw new IllegalArgumentException("rows must be positive");
        }
        cellsByColumn = new CellState[columns][rows];
        cellsByRow = new CellState[rows][columns];
        clear();
    }

    public static PlayerGrid fromColumns(char[][] columns) {
        PlayerGrid grid = new PlayerGrid(columns.length, columns[0].length);
        for (int c = 0; c < columns.length; c++) {
            for (int r = 0; r < columns[c].length; r++) {
                grid.setCell(c, r, CellState.fromChar(columns[c][r]));
            }
        }
        return grid;
    }

    public static PlayerGrid fromRows(char[][] rows) {
        PlayerGrid grid = new PlayerGrid(rows[0].length, rows.length);
        for (int r = 0; r < rows.length; r++) {
            for (int c = 0; c < rows[r].length; c++) {
                grid.setCell(c, r, CellState.fromChar(rows[r][c]));
            }
        }
        return grid;
    }

    public int getColumns() {
        return cellsByColumn.length;
    }

    public int getRows() {
        return cellsByColumn[0].length;
    }

    public CellState getCell(int column, int row) {
        return cellsByColumn[column][row];
    }

    public CellState getCell(BoardCoordinate coordinate) {
        return getCell(coordinate.getColumn(), coordinate.getRow());
    }

    public void setCell(int column, int row, CellState state) {
        cellsByColumn[column][row] = state;
        cellsByRow[row][column] = state;
    }

    public void setCell(BoardCoordinate coordinate, CellState state) {
        setCell(coordinate.getColumn(), coordinate.getRow(), state);
    }

    public void clearCell(int column, int row) {
        setCell(column, row, CellState.EMPTY);
    }

    public char[][] toColumnChars() {
        char[][] columns = new char[getColumns()][getRows()];
        for (int c = 0; c < getColumns(); c++) {
            for (int r = 0; r < getRows(); r++) {
                columns[c][r] = cellsByColumn[c][r].toChar();
            }
        }
        return columns;
    }

    public char[][] toRowChars() {
        char[][] rows = new char[getRows()][getColumns()];
        for (int r = 0; r < getRows(); r++) {
            for (int c = 0; c < getColumns(); c++) {
                rows[r][c] = cellsByRow[r][c].toChar();
            }
        }
        return rows;
    }

    private void clear() {
        for (int c = 0; c < cellsByColumn.length; c++) {
            for (int r = 0; r < cellsByColumn[c].length; r++) {
                setCell(c, r, CellState.EMPTY);
            }
        }
    }
}
