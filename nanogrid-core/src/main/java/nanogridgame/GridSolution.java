package nanogridgame;

import java.io.PrintStream;
import java.util.ArrayList;

public class GridSolution {

    private final ArrayList<char[]> solutions = new ArrayList<>();
    private char[] ary;
    private Integer[] counts;

    public void createSolutions(Integer[] counts, int size) {
        this.counts = counts;
        ary = new char[size];
        fillArray(0, 0);
    }

    public int getSolutionCount() {
        return solutions.size();
    }

    public char[] getSolution(int idx) {
        if (idx >= solutions.size()) {
            return null;
        }
        return solutions.get(idx);
    }

    private void fillArray(int st, int idx) {
        int cnt = counts[idx];
        for (int ctr = st; ctr <= ary.length - cnt; ctr++) {
            clearArray(st);
            for (int i = ctr; i < ctr + cnt; i++) {
                ary[i] = NanoGridBoard.FillChar;
            }
            if (idx == counts.length - 1) {
                solutions.add(ary.clone());
            } else {
                fillArray(ctr + cnt + 1, idx + 1);
            }
        }
    }

    private void clearArray(int st) {
        for (int ctr = st; ctr < ary.length; ctr++) {
            ary[ctr] = NanoGridBoard.EmptyChar;
        }
    }

    public void printSolutions(PrintStream out) {
        for (char[] solution : solutions) {
            printSolution(solution, out);
        }
    }

    private void printSolution(char[] chs, PrintStream out) {
        for (char ch : chs) {
            out.printf("%s ", ch);
        }
        out.println();
    }
}
