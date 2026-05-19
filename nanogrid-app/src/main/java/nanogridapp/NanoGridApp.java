package nanogridapp;

import nanogridgame.GridSolution;
import nanogridgame.GridSolutions;
import nanogridgame.NanoGridBoard;
import nanogridgame.NanoGridParameters;

public class NanoGridApp {

    public static void main(String[] args) {
        GridSolution s = new GridSolution();
        Integer[] cnt = {1};
        s.createSolutions(cnt, 5);

        NanoGridParameters p = new NanoGridParameters();
        p.setColumns(15);
        p.setRows(15);

        NanoGridBoard control = new NanoGridBoard(p);
        control.printBoard(System.out);
        GridSolutions sols = new GridSolutions(p);
        boolean dup = sols.checkDuplicateSolutions(control);
        System.out.printf("%nDuplicate found = %s%n", dup);
    }
}
