package nanogridgame;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoardCoordinateTest {

    @Test
    void storesColumnAndRow() {
        BoardCoordinate coordinate = new BoardCoordinate(2, 3);

        assertEquals(2, coordinate.getColumn());
        assertEquals(3, coordinate.getRow());
    }

    @Test
    void valueEqualityUsesColumnAndRow() {
        assertEquals(new BoardCoordinate(2, 3), new BoardCoordinate(2, 3));
        assertNotEquals(new BoardCoordinate(2, 3), new BoardCoordinate(3, 2));
    }

    @Test
    void rejectsNegativeCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> new BoardCoordinate(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> new BoardCoordinate(0, -1));
    }
}
