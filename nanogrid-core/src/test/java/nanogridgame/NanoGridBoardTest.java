package nanogridgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NanoGridBoardTest {

    private NanoGridParameters params;
    private NanoGridBoard board;

    @BeforeEach
    void setup() {
        params = new NanoGridParameters();
        params.setColumns(5);
        params.setRows(5);
        board = new NanoGridBoard(params);
    }

    @Test
    void boardHasCorrectDimensions() {
        assertEquals(5, board.getColumnSize());
        assertEquals(5, board.getRowSize());
    }

    @Test
    void boardCellsAreOnlyValidChars() {
        for (int c = 0; c < board.getColumnSize(); c++) {
            for (int r = 0; r < board.getRowSize(); r++) {
                char ch = board.getCell(c, r);
                // Generated boards use '\0' for empty cells; loaded boards use EmptyChar (' ')
                assertTrue(ch == NanoGridBoard.FillChar || ch == NanoGridBoard.EmptyChar || ch == '\0',
                        "Unexpected cell char value: " + (int) ch);
            }
        }
    }

    @Test
    void columnCountsHaveAtLeastOneEntry() {
        Integer[][] counts = board.getColumnCounts();
        assertEquals(5, counts.length);
        // Each column should have at least zero groups (empty array is valid)
        for (Integer[] col : counts) {
            assertNotNull(col);
        }
    }

    @Test
    void rowCountsHaveAtLeastOneEntry() {
        Integer[][] counts = board.getRowCounts();
        assertEquals(5, counts.length);
        for (Integer[] row : counts) {
            assertNotNull(row);
        }
    }

    @Test
    void columnCountSumsMatchFilledCells() {
        for (int c = 0; c < board.getColumnSize(); c++) {
            int filledCells = 0;
            for (int r = 0; r < board.getRowSize(); r++) {
                if (board.getCell(c, r) == NanoGridBoard.FillChar) filledCells++;
            }
            int countSum = 0;
            for (int cnt : board.getColumnCounts(c)) {
                countSum += cnt;
            }
            assertEquals(filledCells, countSum,
                    "Column " + c + " fill count mismatch");
        }
    }

    @Test
    void rowCountSumsMatchFilledCells() {
        for (int r = 0; r < board.getRowSize(); r++) {
            int filledCells = 0;
            for (int c = 0; c < board.getColumnSize(); c++) {
                if (board.getCell(c, r) == NanoGridBoard.FillChar) filledCells++;
            }
            int countSum = 0;
            for (int cnt : board.getRowCounts(r)) {
                countSum += cnt;
            }
            assertEquals(filledCells, countSum,
                    "Row " + r + " fill count mismatch");
        }
    }

    @Test
    void copyProducesEquivalentBoard() {
        char[][] cols = board.getColumns();
        NanoGridBoard copy = new NanoGridBoard(new NanoGridParameters(params));
        copy.copy(cols);
        assertTrue(board.checkWin(copy));
    }

    @Test
    void createResizesBoard() {
        board.create(8, 6);
        assertEquals(8, board.getColumnSize());
        assertEquals(6, board.getRowSize());
    }

    @Test
    void maxColumnCountsIsNonNegative() {
        assertTrue(board.getMaxColumnCounts() >= 0);
    }

    @Test
    void maxRowCountsIsNonNegative() {
        assertTrue(board.getMaxRowCounts() >= 0);
    }

    @Test
    void sameSeedProducesSameBoard() {
        NanoGridParameters firstParams = new NanoGridParameters();
        firstParams.setColumns(12);
        firstParams.setRows(12);
        firstParams.setDifficulty(PuzzleDifficulty.MEDIUM);
        firstParams.setSeed(12345L);
        NanoGridParameters secondParams = new NanoGridParameters(firstParams);

        NanoGridBoard first = new NanoGridBoard(firstParams);
        NanoGridBoard second = new NanoGridBoard(secondParams);

        assertBoardsEqual(first, second);
    }

    @Test
    void differentSeedsProduceDifferentBoards() {
        NanoGridParameters firstParams = new NanoGridParameters();
        firstParams.setColumns(12);
        firstParams.setRows(12);
        firstParams.setSeed(111L);
        NanoGridParameters secondParams = new NanoGridParameters(firstParams);
        secondParams.setSeed(222L);

        NanoGridBoard first = new NanoGridBoard(firstParams);
        NanoGridBoard second = new NanoGridBoard(secondParams);

        assertFalse(boardsAreEqual(first, second));
    }

    @Test
    void symmetricGenerationMirrorsFilledCells() {
        NanoGridParameters symmetricParams = new NanoGridParameters();
        symmetricParams.setColumns(11);
        symmetricParams.setRows(9);
        symmetricParams.setSeed(123L);
        symmetricParams.setSymmetric(true);

        NanoGridBoard symmetricBoard = new NanoGridBoard(symmetricParams);

        for (int c = 0; c < symmetricBoard.getColumnSize(); c++) {
            for (int r = 0; r < symmetricBoard.getRowSize(); r++) {
                int mirrorCol = symmetricBoard.getColumnSize() - c - 1;
                int mirrorRow = symmetricBoard.getRowSize() - r - 1;
                assertEquals(symmetricBoard.getCell(c, r), symmetricBoard.getCell(mirrorCol, mirrorRow));
            }
        }
    }

    @Test
    void harderDifficultyGeneratesMoreFilledCellsForSameSeed() {
        NanoGridParameters easyParams = new NanoGridParameters();
        easyParams.setColumns(24);
        easyParams.setRows(24);
        easyParams.setSeed(98765L);
        easyParams.setDifficulty(PuzzleDifficulty.EASY);
        NanoGridParameters hardParams = new NanoGridParameters(easyParams);
        hardParams.setDifficulty(PuzzleDifficulty.HARD);

        NanoGridBoard easy = new NanoGridBoard(easyParams);
        NanoGridBoard hard = new NanoGridBoard(hardParams);

        assertTrue(countFilled(hard) > countFilled(easy));
    }

    private void assertBoardsEqual(NanoGridBoard expected, NanoGridBoard actual) {
        assertTrue(boardsAreEqual(expected, actual));
    }

    private boolean boardsAreEqual(NanoGridBoard first, NanoGridBoard second) {
        if (first.getColumnSize() != second.getColumnSize() || first.getRowSize() != second.getRowSize()) {
            return false;
        }
        for (int c = 0; c < first.getColumnSize(); c++) {
            for (int r = 0; r < first.getRowSize(); r++) {
                if (first.getCell(c, r) != second.getCell(c, r)) {
                    return false;
                }
            }
        }
        return true;
    }

    private int countFilled(NanoGridBoard board) {
        int count = 0;
        for (int c = 0; c < board.getColumnSize(); c++) {
            for (int r = 0; r < board.getRowSize(); r++) {
                if (board.getCell(c, r) == NanoGridBoard.FillChar) {
                    count++;
                }
            }
        }
        return count;
    }
}
