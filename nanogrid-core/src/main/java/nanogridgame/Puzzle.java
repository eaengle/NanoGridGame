package nanogridgame;

import java.util.ArrayList;

public final class Puzzle {

    private final char[][] solutionColumns;
    private final Integer[][] columnClues;
    private final Integer[][] rowClues;

    private Puzzle(char[][] solutionColumns) {
        this.solutionColumns = copy(solutionColumns);
        columnClues = createColumnClues(solutionColumns);
        rowClues = createRowClues(solutionColumns);
    }

    public static Puzzle fromBoard(NanoGridBoard board) {
        return new Puzzle(board.getColumns());
    }

    public int getColumns() {
        return solutionColumns.length;
    }

    public int getRows() {
        return solutionColumns[0].length;
    }

    public char getCell(int column, int row) {
        return solutionColumns[column][row];
    }

    public boolean isFilled(int column, int row) {
        return getCell(column, row) == NanoGridBoard.FillChar;
    }

    public Integer[][] getColumnClues() {
        return copy(columnClues);
    }

    public Integer[][] getRowClues() {
        return copy(rowClues);
    }

    public char[][] toColumnChars() {
        return copy(solutionColumns);
    }

    private static char[][] copy(char[][] source) {
        char[][] target = new char[source.length][];
        for (int i = 0; i < source.length; i++) {
            target[i] = source[i].clone();
        }
        return target;
    }

    private static Integer[][] copy(Integer[][] source) {
        Integer[][] target = new Integer[source.length][];
        for (int i = 0; i < source.length; i++) {
            target[i] = source[i].clone();
        }
        return target;
    }

    private static Integer[][] createColumnClues(char[][] columns) {
        Integer[][] clues = new Integer[columns.length][];
        for (int c = 0; c < columns.length; c++) {
            clues[c] = getCellCounts(columns[c]);
        }
        return clues;
    }

    private static Integer[][] createRowClues(char[][] columns) {
        Integer[][] clues = new Integer[columns[0].length][];
        for (int r = 0; r < columns[0].length; r++) {
            char[] row = new char[columns.length];
            for (int c = 0; c < columns.length; c++) {
                row[c] = columns[c][r];
            }
            clues[r] = getCellCounts(row);
        }
        return clues;
    }

    private static Integer[] getCellCounts(char[] cells) {
        ArrayList<Integer> counts = new ArrayList<>();
        int count = 0;
        for (char cell : cells) {
            if (cell == NanoGridBoard.FillChar) {
                count++;
            } else if (count > 0) {
                counts.add(count);
                count = 0;
            }
        }
        if (count > 0) {
            counts.add(count);
        }
        return counts.toArray(new Integer[counts.size()]);
    }
}
