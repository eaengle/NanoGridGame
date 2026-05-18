package nanogridgame;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameSessionTest {

    @Test
    void commandsUpdatePlayerGrid() {
        GameSession session = new GameSession(createPuzzle());

        session.fillCell(0, 0);
        session.markCell(1, 0);
        session.clearCell(1, 0);

        assertEquals(CellState.FILLED, session.getPlayerGrid().getCell(0, 0));
        assertEquals(CellState.EMPTY, session.getPlayerGrid().getCell(1, 0));
    }

    @Test
    void countsIncorrectFilledCells() {
        GameSession session = new GameSession(createPuzzle());

        session.fillCell(1, 0);
        session.markCell(0, 1);

        assertEquals(1, session.getIncorrectMoves());
    }

    @Test
    void rejectsMismatchedPlayerGridDimensions() {
        assertThrows(IllegalArgumentException.class,
                () -> new GameSession(createPuzzle(), new PlayerGrid(4, 4)));
    }

    private Puzzle createPuzzle() {
        NanoGridParameters parameters = new NanoGridParameters();
        parameters.setColumns(2);
        parameters.setRows(2);
        NanoGridBoard board = new NanoGridBoard(parameters);
        board.copy(new char[][]{
                {NanoGridBoard.FillChar, NanoGridBoard.EmptyChar},
                {NanoGridBoard.EmptyChar, NanoGridBoard.FillChar}
        });
        return Puzzle.fromBoard(board);
    }
}
