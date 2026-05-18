package nanogridgame;

import java.util.ArrayList;

public class GridSolutions {
    GridSolution[] Rows;
    Integer[][] RowCounts;
    char[][] Board;
    NanoGridBoard ControlBoard;
    NanoGridParameters Settings;
    int DuplicateCount;

    public GridSolutions(NanoGridParameters p) {
        Settings = p;
    }

    private GridSolution[] createRowSolutions() {
        GridSolution[] sols = new GridSolution[Settings.Rows];
        long total = 1;
        for (int r = 0; r < Settings.Rows; r++) {
            Integer[] ans = RowCounts[r];
            GridSolution s = new GridSolution();
            s.createSolutions(ans, Settings.Rows);
            sols[r] = s;
            long cnt = s.getSolutionCount();
            total = total * cnt;
            System.out.printf("ROW %d, count:%d\n", r, cnt);
        }
        System.out.printf("TOTAL: %d\n", total);
        return sols;
    }

    long Counter;

    public boolean checkDuplicateSolutions(NanoGridBoard control) {
        ControlBoard = control;
        RowCounts = control.getRowCounts();
        Rows = createRowSolutions();
        char[][] b = new char[Settings.Columns][Settings.Rows];
        Board = createNewBoard(b);
        DuplicateCount = 0;
        Counter = 0;
        createBoards(0);
        return DuplicateCount > 1;
    }

    private void createBoards(int r) {
        if (DuplicateCount > 1 || r >= Rows.length) {
            return;
        }
        GridSolution row = Rows[r];
        int cnt = row.getSolutionCount();
        for (int i = 0; i < cnt; i++) {
            char[] ans = row.getSolution(i);
            for (int c = 0; c < Board.length; c++) {
                Board[c][r] = ans[c];
            }
            ++Counter;
            if (Counter % 100000 == 0) {
                System.out.println(Counter);
            }
            if (r == Rows.length - 1) {
                NanoGridBoard ngb = new NanoGridBoard(Settings);
                ngb.copy(Board);
                if (ControlBoard.checkWin(ngb)) {
                    System.out.printf("DUP:%d\n", ++DuplicateCount);
                }
            } else {
                createBoards(r + 1);
            }
        }
    }

    private char[][] createNewBoard(char[][] board) {
        char[][] b = new char[Settings.Columns][Settings.Rows];
        for (int c = 0; c < board.length; c++) {
            b[c] = board[c].clone();
        }
        return b;
    }
}
