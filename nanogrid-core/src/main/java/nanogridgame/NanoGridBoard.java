package nanogridgame;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

public class NanoGridBoard {

    private static final Logger LOG = Logger.getLogger(NanoGridBoard.class.getName());

    private char[][] board;
    private Random rnd = new Random();
    private NanoGridParameters settings;

    public static final char FillChar = '#';
    public static final char MarkChar = 'X';
    public static final char EmptyChar = ' ';

    public NanoGridBoard(NanoGridParameters p) {
        settings = p;
        create(p.getColumns(), p.getRows());
    }

    public void create(int sz) {
        create(sz, sz);
    }

    public void copy(char[][] src) {
        settings.setColumns(src.length);
        settings.setRows(src[0].length);
        board = new char[settings.getColumns()][settings.getRows()];
        for (int c = 0; c < settings.getColumns(); c++) {
            for (int r = 0; r < settings.getRows(); r++) {
                char ch = src[c][r];
                board[c][r] = ch == '_' ? EmptyChar : ch;
            }
        }
    }

    public void create(int cols, int rows) {
        settings.setColumns(cols);
        settings.setRows(rows);
        rnd = settings.isUseSeed() ? new Random(settings.getSeed()) : new Random();
        board = new char[cols][rows];
        fillBoard();
        ensurePlayable();
        LOG.fine("Board created: " + cols + "x" + rows);
    }

    public char getCell(int col, int row) {
        return board[col][row];
    }

    public Integer[][] getColumnCounts() {
        ArrayList<Integer[]> lst = new ArrayList<>();
        for (int c = 0; c < board.length; c++) {
            lst.add(getColumnCounts(c));
        }
        Integer[][] ary = new Integer[lst.size()][];
        return lst.toArray(ary);
    }

    public Integer[] getColumnCounts(int col) {
        ArrayList<Integer> lst = new ArrayList<>();
        int cnt = 0;
        for (int r = 0; r < board[0].length; r++) {
            if (board[col][r] == FillChar) {
                ++cnt;
            } else if (cnt > 0) {
                lst.add(cnt);
                cnt = 0;
            }
        }
        if (cnt > 0) {
            lst.add(cnt);
        }
        Integer[] ary = new Integer[lst.size()];
        return lst.toArray(ary);
    }

    public Integer[][] getRowCounts() {
        ArrayList<Integer[]> lst = new ArrayList<>();
        for (int r = 0; r < board[0].length; r++) {
            lst.add(getRowCounts(r));
        }
        Integer[][] ary = new Integer[lst.size()][];
        return lst.toArray(ary);
    }

    public Integer[] getRowCounts(int row) {
        ArrayList<Integer> lst = new ArrayList<>();
        int cnt = 0;
        for (int c = 0; c < board.length; c++) {
            if (board[c][row] == FillChar) {
                ++cnt;
            } else if (cnt > 0) {
                lst.add(cnt);
                cnt = 0;
            }
        }
        if (cnt > 0) {
            lst.add(cnt);
        }
        Integer[] ary = new Integer[lst.size()];
        return lst.toArray(ary);
    }

    private void fillBoard() {
        int fillPercent = settings.getDifficulty().getFillPercent();
        int continuationPercent = settings.getDifficulty().getContinuationPercent();
        for (int c = 0; c < settings.getColumns(); c++) {
            for (int r = 0; r < settings.getRows(); r++) {
                if (settings.isSymmetric() && isMirroredCellAlreadyHandled(c, r)) {
                    continue;
                }
                boolean nearFilledCell = hasFilledNeighbor(c, r);
                int chance = nearFilledCell ? continuationPercent : fillPercent;
                if (rnd.nextInt(100) < chance) {
                    setGeneratedCell(c, r, FillChar);
                }
            }
        }
    }

    private boolean isMirroredCellAlreadyHandled(int col, int row) {
        int mirrorCol = settings.getColumns() - col - 1;
        int mirrorRow = settings.getRows() - row - 1;
        return col > mirrorCol || (col == mirrorCol && row > mirrorRow);
    }

    private void setGeneratedCell(int col, int row, char value) {
        board[col][row] = value;
        if (settings.isSymmetric()) {
            int mirrorCol = settings.getColumns() - col - 1;
            int mirrorRow = settings.getRows() - row - 1;
            board[mirrorCol][mirrorRow] = value;
        }
    }

    private boolean hasFilledNeighbor(int col, int row) {
        return isFilled(col - 1, row) || isFilled(col, row - 1);
    }

    private boolean isFilled(int col, int row) {
        return col >= 0 && row >= 0 &&
                col < settings.getColumns() && row < settings.getRows() &&
                board[col][row] == FillChar;
    }

    private void ensurePlayable() {
        for (int c = 0; c < settings.getColumns(); c++) {
            if (getColumnCounts(c).length == 0) {
                setGeneratedCell(c, rnd.nextInt(settings.getRows()), FillChar);
            }
        }
        for (int r = 0; r < settings.getRows(); r++) {
            if (getRowCounts(r).length == 0) {
                setGeneratedCell(rnd.nextInt(settings.getColumns()), r, FillChar);
            }
        }
    }

    public int getColumnSize() {
        return board.length;
    }

    public int getRowSize() {
        return board[0].length;
    }

    public char[][] getColumns() {
        return board;
    }

    public int getMaxColumnCounts() {
        return getMaxLength(getColumnCounts());
    }

    public int getMaxRowCounts() {
        return getMaxLength(getRowCounts());
    }

    private int getMaxLength(Integer[][] cnts) {
        int max = 0;
        for (Integer[] cnt : cnts) {
            if (cnt.length > max) {
                max = cnt.length;
            }
        }
        return max;
    }

    public void printBoard(PrintStream out) {
        printColumnHeaders(out);
        for (int r = 0; r < board[0].length; r++) {
            printRowHeader(out, r);
            for (int c = 0; c < board.length; c++) {
                out.printf("%c ", getCell(c, r));
            }
            out.println();
        }
    }

    private void printColumnHeaders(PrintStream out) {
        Integer[][] ccnts = getColumnCounts();
        int cmax = getMaxLength(ccnts);
        Integer[][] rcnts = getRowCounts();
        int rmax = getMaxLength(rcnts);
        for (int r = 0; r < cmax; r++) {
            printPadding(out, rmax * 2);
            for (Integer[] col : ccnts) {
                int idx = col.length - cmax + r;
                if (idx >= 0) {
                    out.printf("%s ", col[idx]);
                } else {
                    out.print("  ");
                }
            }
            out.println();
        }
    }

    private void printPadding(PrintStream out, int max) {
        for (int i = 0; i < max; i++) {
            out.print(" ");
        }
    }

    private void printRowHeader(PrintStream out, int r) {
        Integer[][] rcnts = getRowCounts();
        int rmax = getMaxLength(rcnts);
        Integer[] row = rcnts[r];
        for (int i = 0; i < rmax; i++) {
            int idx = row.length - rmax + i;
            if (idx >= 0) {
                out.printf("%s ", row[idx]);
            } else {
                out.print("  ");
            }
        }
    }

    public boolean checkWin(NanoGridBoard brd) {
        Integer[][] ctrl = getColumnCounts();
        Integer[][] test = brd.getColumnCounts();
        for (int c = 0; c < ctrl.length; c++) {
            if (!areEqual(ctrl[c], test[c])) {
                return false;
            }
        }
        ctrl = getRowCounts();
        test = brd.getRowCounts();
        for (int i = 0; i < ctrl.length; i++) {
            if (!areEqual(ctrl[i], test[i])) {
                return false;
            }
        }
        return true;
    }

    private boolean areEqual(Integer[] ary1, Integer[] ary2) {
        if (ary1.length != ary2.length) {
            return false;
        }
        for (int i = 0; i < ary1.length; i++) {
            if (!ary1[i].equals(ary2[i])) {
                return false;
            }
        }
        return true;
    }
}
