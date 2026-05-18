package nanogridgame;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GridSolutionTest {

    @Test
    void singleGroupOneCellInFiveSpaces() {
        GridSolution s = new GridSolution();
        s.createSolutions(new Integer[]{1}, 5);
        assertEquals(5, s.getSolutionCount());
    }

    @Test
    void singleGroupThreeCellsInFiveSpaces() {
        GridSolution s = new GridSolution();
        s.createSolutions(new Integer[]{3}, 5);
        // positions: [0-2], [1-3], [2-4]
        assertEquals(3, s.getSolutionCount());
    }

    @Test
    void twoGroupsInFiveSpaces() {
        GridSolution s = new GridSolution();
        s.createSolutions(new Integer[]{1, 1}, 5);
        // (0,2),(0,3),(0,4),(1,3),(1,4),(2,4) = 6
        assertEquals(6, s.getSolutionCount());
    }

    @Test
    void exactFitHasOneSolution() {
        GridSolution s = new GridSolution();
        // group of 5 in 5 spaces — only one placement
        s.createSolutions(new Integer[]{5}, 5);
        assertEquals(1, s.getSolutionCount());
    }

    @Test
    void solutionContainsFillChars() {
        GridSolution s = new GridSolution();
        s.createSolutions(new Integer[]{3}, 5);
        char[] sol = s.getSolution(0);
        assertNotNull(sol);
        assertEquals(5, sol.length);
        int fills = 0;
        for (char c : sol) {
            if (c == NanoGridBoard.FillChar) fills++;
        }
        assertEquals(3, fills);
    }

    @Test
    void getOutOfBoundsReturnsNull() {
        GridSolution s = new GridSolution();
        s.createSolutions(new Integer[]{1}, 3);
        assertNull(s.getSolution(999));
    }

    @Test
    void solutionsAreIndependentCopies() {
        GridSolution s = new GridSolution();
        s.createSolutions(new Integer[]{1}, 3);
        char[] sol0 = s.getSolution(0);
        sol0[0] = 'Z';
        assertNotEquals('Z', s.getSolution(1)[0]);
    }
}
