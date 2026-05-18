package nanogridgame;

import java.io.File;
import java.util.ArrayList;
import static nanogridgame.NanoGridBoard.FillChar;

public class NanoGridGame {

    private NanoGridBoard Board;
    private char[][] PlayBoardColumns;
    private char[][] PlayBoardRows;
    private NanoGridParameters Settings;

    public NanoGridGame(NanoGridParameters p) {
        Board = new NanoGridBoard(p);
        Settings = p;
        create(p.Columns, p.Rows);
    }

    public void create(int cols, int rows) {
        Settings.Columns = cols;
        Settings.Rows = rows;
        Board.create(cols, rows);
        PlayBoardColumns = new char[cols][rows];
        PlayBoardRows = new char[rows][cols];
    }

    public void create(int sz) {
        create(sz, sz);
    }

    public void create() {
        create(Settings.Columns, Settings.Rows);
    }

    public NanoGridBoard getBoard() {
        return Board;
    }

    public NanoGridParameters getSettings() {
        return Settings;
    }

    public void clearCell(int c, int r) {
        PlayBoardColumns[c][r] = 0;
        PlayBoardRows[r][c] = 0;
    }

    public void setCell(int c, int r) {
        PlayBoardColumns[c][r] = NanoGridBoard.FillChar;
        PlayBoardRows[r][c] = NanoGridBoard.FillChar;
    }

    public void setMark(int c, int r) {
        PlayBoardColumns[c][r] = NanoGridBoard.MarkChar;
        PlayBoardRows[r][c] = NanoGridBoard.MarkChar;
    }

    public boolean checkWin() {
        Integer[][] cols = Board.getColumnCounts();
        for (int c = 0; c < cols.length; c++) {
            Integer[] cnts = getCellCount(PlayBoardColumns[c]);
            if (!areEqual(cnts, cols[c])) {
                return false;
            }
        }
        Integer[][] rows = Board.getRowCounts();
        for (int r = 0; r < rows.length; r++) {
            Integer[] cnts = getCellCount(PlayBoardRows[r]);
            if (!areEqual(cnts, rows[r])) {
                return false;
            }
        }
        return true;
    }

    private Integer[] getCellCount(char[] cary) {
        ArrayList<Integer> lst = new ArrayList<>();
        int cnt = 0;
        for (int c = 0; c < cary.length; c++) {
            if (cary[c] == FillChar) {
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

    public void loadBoard(File loadFile) {
        NanoGridFile file = new NanoGridFile(this);
        try {
            file.deserialize(loadFile);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void resetBoard(File loadFile) {
        loadBoard(loadFile);
        PlayBoardColumns = new char[Settings.Columns][Settings.Rows];
        PlayBoardRows = new char[Settings.Rows][Settings.Columns];
    }

    public void saveGame(File output) {
        NanoGridFile file = new NanoGridFile(this);
        try {
            file.serialize(output);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    void setBoard(char[][] board) {
        Board = new NanoGridBoard(Settings);
        Board.copy(board);
    }

    void updateSettings(NanoGridParameters settings) {
        Settings = settings;
        create();
    }

    public char[][] getPlayColumns() {
        return PlayBoardColumns;
    }

    void setPlayColumns(char[][] cols) {
        PlayBoardColumns = cols;
    }

    char[][] getPlayRows() {
        return PlayBoardRows;
    }

    public void setPlayRows(char[][] rows) {
        PlayBoardRows = rows;
    }

    public int getIncorrectMoves() {
        int cnt = 0;
        for (int c = 0; c < Settings.Columns; c++) {
            for (int r = 0; r < Settings.Rows; r++) {
                if (Board.getCell(c, r) != NanoGridBoard.FillChar &&
                        PlayBoardColumns[c][r] == NanoGridBoard.FillChar) {
                    ++cnt;
                }
            }
        }
        return cnt;
    }
}
