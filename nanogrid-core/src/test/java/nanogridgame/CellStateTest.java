package nanogridgame;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CellStateTest {

    @Test
    void convertsFillCharToFilled() {
        assertEquals(CellState.FILLED, CellState.fromChar(NanoGridBoard.FillChar));
    }

    @Test
    void convertsMarkCharToMarked() {
        assertEquals(CellState.MARKED, CellState.fromChar(NanoGridBoard.MarkChar));
    }

    @Test
    void convertsEmptyRepresentationsToEmpty() {
        assertEquals(CellState.EMPTY, CellState.fromChar('\0'));
        assertEquals(CellState.EMPTY, CellState.fromChar('_'));
        assertEquals(CellState.EMPTY, CellState.fromChar(NanoGridBoard.EmptyChar));
    }
}
