package nanogridgame;

import java.util.logging.Logger;

public class GridSolutions {

    private static final Logger LOG = Logger.getLogger(GridSolutions.class.getName());

    private GridSolution[] rows;
    private Integer[][] rowCounts;
    private char[][] board;
    private NanoGridBoard controlBoard;
    private final NanoGridParameters settings;
    private int duplicateCount;
    private long counter;

    public GridSolutions(NanoGridParameters p) {
        settings = p;
    }

    private GridSolution[] createRowSolutions() {
        GridSolution[] sols = new GridSolution[settings.getRows()];
        long total = 1;
        for (int r = 0; r < settings.getRows(); r++) {
            Integer[] ans = rowCounts[r];
            GridSolution s = new GridSolution();
            s.createSolutions(ans, settings.getRows());
            sols[r] = s;
            long cnt = s.getSolutionCount();
            total = total * cnt;
            LOG.fine(String.format("Row %d: %d solution(s)", r, cnt));
        }
        LOG.fine(String.format("Total combinations to check: %d", total));
        return sols;
    }

    public boolean checkDuplicateSolutions(NanoGridBoard control) {
        controlBoard = control;
        rowCounts = control.getRowCounts();
        rows = createRowSolutions();
        board = new char[settings.getColumns()][settings.getRows()];
        board = createNewBoard(board);
        duplicateCount = 0;
        counter = 0;
        createBoards(0);
        return duplicateCount > 1;
    }

    private void createBoards(int r) {
        if (duplicateCount > 1 || r >= rows.length) {
            return;
        }
        GridSolution row = rows[r];
        int cnt = row.getSolutionCount();
        for (int i = 0; i < cnt; i++) {
            char[] ans = row.getSolution(i);
            for (int c = 0; c < board.length; c++) {
                board[c][r] = ans[c];
            }
            ++counter;
            if (counter % 100000 == 0) {
                LOG.fine(String.format("Checked %d combinations...", counter));
            }
            if (r == rows.length - 1) {
                NanoGridBoard ngb = new NanoGridBoard(settings);
                ngb.copy(board);
                if (controlBoard.checkWin(ngb)) {
                    ++duplicateCount;
                    LOG.fine(String.format("Duplicate solution #%d found", duplicateCount));
                }
            } else {
                createBoards(r + 1);
            }
        }
    }

    private char[][] createNewBoard(char[][] src) {
        char[][] b = new char[settings.getColumns()][settings.getRows()];
        for (int c = 0; c < src.length; c++) {
            b[c] = src[c].clone();
        }
        return b;
    }
}
