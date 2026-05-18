package nanogridapp;

import nanogridgame.INanoGridHost;
import nanogridgame.NanoGridBoard;
import nanogridgame.NanoGridGame;
import nanogridgame.NanoGridParameters;

public class NanoGridAppHost implements INanoGridHost {

    private NanoGridGame game;

    public void createGame(int cols, int rows) {
        NanoGridParameters p = new NanoGridParameters();
        game = new NanoGridGame(p);
        game.create(cols, rows);
    }

    @Override
    public void displayGame() {
        NanoGridBoard board = game.getBoard();
        Integer[][] rcnts = board.getRowCounts();
        int rmax = getMaxLength(rcnts);

        displayColumnCounts(rmax * 2);

        for (int r = 0; r < board.getColumnSize(); r++) {
            displayRowCounts(r, rcnts, rmax);
            for (int c = 0; c < board.getRowSize(); c++) {
                System.out.printf("%c ", board.getCell(r, c));
            }
            System.out.println();
        }
    }

    private int getMaxLength(Integer[][] cnts) {
        int max = 0;
        for (Integer[] cnt : cnts) {
            if (cnt.length > max) max = cnt.length;
        }
        return max;
    }

    private String getPadding(int rmax) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rmax; i++) sb.append(' ');
        return sb.toString();
    }

    private void displayColumnCounts(int rmax) {
        NanoGridBoard board = game.getBoard();
        Integer[][] ccnts = board.getColumnCounts();
        int cmax = getMaxLength(ccnts);
        for (int i = cmax; i > 0; i--) {
            System.out.print(getPadding(rmax));
            for (Integer[] col : ccnts) {
                if (col.length >= i) {
                    System.out.printf("%s ", col[col.length - i].toString());
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }

    private void displayRowCounts(int r, Integer[][] rcnts, int rmax) {
        Integer[] row = rcnts[r];
        for (int i = 0; i < rmax; i++) {
            if (i < row.length) {
                System.out.printf("%s ", row[i].toString());
            } else {
                System.out.print("  ");
            }
        }
    }
}
