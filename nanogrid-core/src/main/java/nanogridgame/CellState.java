package nanogridgame;

public enum CellState {
    EMPTY((char) 0),
    FILLED(NanoGridBoard.FillChar),
    MARKED(NanoGridBoard.MarkChar);

    private final char saveChar;

    CellState(char saveChar) {
        this.saveChar = saveChar;
    }

    public char toChar() {
        return saveChar;
    }

    public static CellState fromChar(char ch) {
        if (ch == NanoGridBoard.FillChar) {
            return FILLED;
        }
        if (ch == NanoGridBoard.MarkChar) {
            return MARKED;
        }
        return EMPTY;
    }
}
