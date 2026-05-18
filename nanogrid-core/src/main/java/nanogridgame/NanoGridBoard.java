package nanogridgame;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

public class NanoGridBoard {

    char[][] Board;
    Random Rnd = new Random();
    public static final char FillChar = '#';
    public static final char MarkChar = 'X';
    public static final char EmptyChar = ' ';

    NanoGridParameters Settings;

    public NanoGridBoard(NanoGridParameters p) {
        Settings = p;
        create(p.Columns, p.Rows);
    }

    public void create(int sz) {
        create(sz, sz);
    }

    public void copy(char[][] board) {
        Settings.Columns = board.length;
        Settings.Rows = board[0].length;
        Board = new char[Settings.Columns][Settings.Rows];
        for (int c = 0; c < Settings.Columns; c++) {
            for (int r = 0; r < Settings.Rows; r++) {
                char ch = board[c][r];
                Board[c][r] = ch == '_' ? EmptyChar : ch;
            }
        }
    }

    public void create(int cols, int rows) {
        Settings.Columns = cols;
        Settings.Rows = rows;
        Board = new char[cols][rows];
        int ccnt = 0;
        int rcnt = 0;
        int cst = Rnd.nextInt(cols);
        int rst = Rnd.nextInt(rows);
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
        createUniqueSolution();
    }

    public char getCell(int col, int row) {
        return Board[col][row];
    }

    public Integer[][] getColumnCounts() {
        ArrayList<Integer[]> lst = new ArrayList<>();
        for (int c = 0; c < Board.length; c++) {
            lst.add(NanoGridBoard.this.getColumnCounts(c));
        }
        Integer[][] ary = new Integer[lst.size()][];
        return lst.toArray(ary);
    }

    public Integer[] getColumnCounts(int col) {
        ArrayList<Integer> lst = new ArrayList<>();
        int cnt = 0;
        for (int r = 0; r < Board[0].length; r++) {
            if (Board[col][r] == FillChar) {
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
        for (int r = 0; r < Board[0].length; r++) {
            lst.add(getRowCounts(r));
        }
        Integer[][] ary = new Integer[lst.size()][];
        return lst.toArray(ary);
    }

    public Integer[] getRowCounts(int row) {
        ArrayList<Integer> lst = new ArrayList<>();
        int cnt = 0;
        for (int c = 0; c < Board.length; c++) {
            if (Board[c][row] == FillChar) {
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
        int cnt = Settings.MaxColumnSquares + 1;
        fillArray(cnt, Board[c]);
    }

    private void fillRow(int r) {
        char[] ary = createRowArray(r);
        int cnt = Settings.MaxRowSquares + 1;
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
        int pos = Rnd.nextInt(ary.length);
        boolean filled = false;
        while (!filled) {
            for (int i = 0; i < cnt; i++) {
                int s = Rnd.nextInt(100) + 1;
                pos = ++pos % ary.length;
                if (s == 100 || s > Settings.RowBreakChance) {
                    filled = true;
                    ary[pos] = FillChar;
                }
            }
        }
    }

    private char[] createRowArray(int r) {
        char[] ary = new char[Board.length];
        for (int c = 0; c < Board.length; c++) {
            ary[c] = Board[c][r];
        }
        return ary;
    }

    private void fillRowArray(int r, char[] ary) {
        for (int c = 0; c < Board.length; c++) {
            Board[c][r] = ary[c];
        }
    }

    public int getColumnSize() {
        return Board.length;
    }

    public int getRowSize() {
        return Board[0].length;
    }

    char[][] getColumns() {
        return Board;
    }

    public int getMaxColumnCounts() {
        return getMaxLength(getColumnCounts());
    }

    public int getMaxRowCounts() {
        return getMaxLength(getRowCounts());
    }

    private int getMaxLength(Integer[][] cnts) {
        int max = 0;
        for (int i = 0; i < cnts.length; i++) {
            if (cnts[i].length > max) {
                max = cnts[i].length;
            }
        }
        return max;
    }

    public void printBoard(PrintStream out) {
        printColumnHeaders(out);
        for (int r = 0; r < Board[0].length; r++) {
            printRowHeader(out, r);
            for (int c = 0; c < Board.length; c++) {
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
            for (int c = 0; c < ccnts.length; c++) {
                Integer[] col = ccnts[c];
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

    private void createUniqueSolution() {
        GridSolutions solutions = new GridSolutions(Settings);
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
