package nanogridgame;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerGridTest {

    @Test
    void newGridStartsEmpty() {
        PlayerGrid grid = new PlayerGrid(3, 2);

        assertEquals(CellState.EMPTY, grid.getCell(0, 0));
        assertEquals(0, grid.toColumnChars()[0][0]);
        assertEquals(0, grid.toRowChars()[0][0]);
    }

    @Test
    void settingCellKeepsColumnAndRowViewsSynchronized() {
        PlayerGrid grid = new PlayerGrid(3, 2);

        grid.setCell(2, 1, CellState.FILLED);

        assertEquals(CellState.FILLED, grid.getCell(2, 1));
        assertEquals(NanoGridBoard.FillChar, grid.toColumnChars()[2][1]);
        assertEquals(NanoGridBoard.FillChar, grid.toRowChars()[1][2]);
    }

    @Test
    void canBuildFromColumnCharacters() {
        char[][] columns = new char[][]{
                {NanoGridBoard.FillChar, '_'},
                {0, NanoGridBoard.MarkChar}
        };

        PlayerGrid grid = PlayerGrid.fromColumns(columns);

        assertEquals(CellState.FILLED, grid.getCell(0, 0));
        assertEquals(CellState.EMPTY, grid.getCell(0, 1));
        assertEquals(CellState.EMPTY, grid.getCell(1, 0));
        assertEquals(CellState.MARKED, grid.getCell(1, 1));
    }

    @Test
    void canBuildFromRowCharacters() {
        char[][] rows = new char[][]{
                {NanoGridBoard.FillChar, 0},
                {'_', NanoGridBoard.MarkChar}
        };

        PlayerGrid grid = PlayerGrid.fromRows(rows);

        assertEquals(CellState.FILLED, grid.getCell(0, 0));
        assertEquals(CellState.EMPTY, grid.getCell(1, 0));
        assertEquals(CellState.EMPTY, grid.getCell(0, 1));
        assertEquals(CellState.MARKED, grid.getCell(1, 1));
    }
}
