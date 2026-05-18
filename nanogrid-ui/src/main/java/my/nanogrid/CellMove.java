package my.nanogrid;

class CellMove {

    private final int column;
    private final int row;
    private final char before;
    private final char after;

    CellMove(int column, int row, char before, char after) {
        this.column = column;
        this.row = row;
        this.before = before;
        this.after = after;
    }

    int getColumn() {
        return column;
    }

    int getRow() {
        return row;
    }

    char getBefore() {
        return before;
    }

    char getAfter() {
        return after;
    }
}
