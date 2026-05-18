package nanogridgame;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PuzzleTest {

    @Test
    void capturesBoardDimensionsCellsAndClues() {
        NanoGridParameters parameters = new NanoGridParameters();
        parameters.setColumns(3);
        parameters.setRows(2);
        NanoGridBoard board = new NanoGridBoard(parameters);
        board.copy(new char[][]{
                {NanoGridBoard.FillChar, NanoGridBoard.EmptyChar},
                {NanoGridBoard.FillChar, NanoGridBoard.FillChar},
                {NanoGridBoard.EmptyChar, NanoGridBoard.FillChar}
        });

        Puzzle puzzle = Puzzle.fromBoard(board);

        assertEquals(3, puzzle.getColumns());
        assertEquals(2, puzzle.getRows());
        assertTrue(puzzle.isFilled(0, 0));
        assertArrayEquals(board.getColumnCounts(), puzzle.getColumnClues());
        assertArrayEquals(board.getRowCounts(), puzzle.getRowClues());
    }

    @Test
    void exposesDefensiveCopies() {
        NanoGridParameters parameters = new NanoGridParameters();
        parameters.setColumns(2);
        parameters.setRows(2);
        NanoGridBoard board = new NanoGridBoard(parameters);
        board.copy(new char[][]{
                {NanoGridBoard.FillChar, NanoGridBoard.EmptyChar},
                {NanoGridBoard.EmptyChar, NanoGridBoard.FillChar}
        });
        Puzzle puzzle = Puzzle.fromBoard(board);

        char[][] cells = puzzle.toColumnChars();
        cells[0][0] = NanoGridBoard.EmptyChar;

        assertEquals(NanoGridBoard.FillChar, puzzle.getCell(0, 0));
    }
}
