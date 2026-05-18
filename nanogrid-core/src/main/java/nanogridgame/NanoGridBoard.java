package nanogridgame;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

public class NanoGridBoard {

    private static final Logger LOG = Logger.getLogger(NanoGridBoard.class.getName());

    private char[][] board;
    private final Random rnd = new Random();
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
        board = new char[cols][rows];
        int ccnt = 0;
        int rcnt = 0;
        int cst = rnd.nextInt(cols);
        int rst = rnd.nextInt(rows);
        while (ccnt < cols || rcnt < rows) {
            if (rcnt < rows) {
                rst = ++rst % rows;
                fillRow(rst);
                ++rcnt;
            }
            if (ccnt < cols) {
                cst = ++cst % cols;
                fillCol(cst);
                ++ccnt;
            }
        }
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

    private void fillCol(int c) {
        int cnt = settings.getMaxColumnSquares() + 1;
        fillArray(cnt, board[c]);
    }

    private void fillRow(int r) {
        char[] ary = createRowArray(r);
        int cnt = settings.getMaxRowSquares() + 1;
        fillArray(cnt, ary);
        fillRowArray(r, ary);
    }

    private void fillArray(int cnt, char[] ary) {
        if (cnt < 1) {
            cnt = 1;
        }
        if (cnt >= ary.length) {
            cnt = ary.length - 1;
        }
        int pos = rnd.nextInt(ary.length);
        boolean filled = false;
        while (!filled) {
            for (int i = 0; i < cnt; i++) {
                int s = rnd.nextInt(100) + 1;
                pos = ++pos % ary.length;
                if (s == 100 || s > settings.getRowBreakChance()) {
                    filled = true;
                    ary[pos] = FillChar;
                }
            }
        }
    }

    private char[] createRowArray(int r) {
        char[] ary = new char[board.length];
        for (int c = 0; c < board.length; c++) {
            ary[c] = board[c][r];
        }
        return ary;
    }

    private void fillRowArray(int r, char[] ary) {
        for (int c = 0; c < board.length; c++) {
            board[c][r] = ary[c];
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
