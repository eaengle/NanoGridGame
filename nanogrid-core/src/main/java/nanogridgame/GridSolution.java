package nanogridgame;

import java.io.PrintStream;
import java.util.ArrayList;

public class GridSolution {
    private ArrayList<char[]> Solutions;
    char[] Ary;
    Integer[] Counts;

    public GridSolution() {
        Solutions = new ArrayList<>();
    }

    public void createSolutions(Integer[] counts, int size) {
        Counts = counts;
        Ary = new char[size];
        fillArray(0, 0);
    }

    public int getSolutionCount() {
        return Solutions.size();
    }

    public char[] getSolution(int idx) {
        if (idx >= Solutions.size()) {
            return null;
        }
        return Solutions.get(idx);
    }

    private void fillArray(int st, int idx) {
        int cnt = Counts[idx];
        for (int ctr = st; ctr <= Ary.length - cnt; ctr++) {
            clearArray(st);
            for (int i = ctr; i < ctr + cnt; i++) {
                Ary[i] = NanoGridBoard.FillChar;
            }
            if (idx == Counts.length - 1) {
                Solutions.add((char[]) Ary.clone());
            } else {
                fillArray(ctr + cnt + 1, idx + 1);
            }
        }
    }

    private void clearArray(int st) {
        for (int ctr = st; ctr < Ary.length; ctr++) {
            Ary[ctr] = NanoGridBoard.EmptyChar;
        }
    }

    public void printSolutions(PrintStream out) {
        for (int i = 0; i < Solutions.size(); i++) {
            printSolution(Solutions.get(i), out);
        }
    }

    private void printSolution(char[] chs, PrintStream out) {
        for (int i = 0; i < chs.length; i++) {
            out.printf("%s ", chs[i]);
        }
        out.println();
    }
}
