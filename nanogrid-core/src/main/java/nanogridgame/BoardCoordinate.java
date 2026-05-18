package nanogridgame;

public final class BoardCoordinate {

    private final int column;
    private final int row;

    public BoardCoordinate(int column, int row) {
        if (column < 0) {
            throw new IllegalArgumentException("column must be non-negative");
        }
        if (row < 0) {
            throw new IllegalArgumentException("row must be non-negative");
        }
        this.column = column;
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BoardCoordinate)) {
            return false;
        }
        BoardCoordinate other = (BoardCoordinate) obj;
        return column == other.column && row == other.row;
    }

    @Override
    public int hashCode() {
        int result = column;
        result = 31 * result + row;
        return result;
    }

    @Override
    public String toString() {
        return "BoardCoordinate{column=" + column + ", row=" + row + "}";
    }
}
