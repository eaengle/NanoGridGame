package nanogridgame;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import static nanogridgame.NanoGridBoard.FillChar;

public class NanoGridGame {

    private NanoGridBoard board;
    private PlayerGrid playerGrid;
    private NanoGridParameters settings;
    private GameMetadata metadata = new GameMetadata();

    public NanoGridGame(NanoGridParameters p) {
        board = new NanoGridBoard(p);
        settings = p;
        create(p.getColumns(), p.getRows());
    }

    public void create(int cols, int rows) {
        settings.setColumns(cols);
        settings.setRows(rows);
        board.create(cols, rows);
        playerGrid = new PlayerGrid(cols, rows);
        metadata = new GameMetadata();
    }

    public void create(int sz) {
        create(sz, sz);
    }

    public void create() {
        create(settings.getColumns(), settings.getRows());
    }

    public NanoGridBoard getBoard() {
        return board;
    }

    public NanoGridParameters getSettings() {
        return settings;
    }

    public GameMetadata getMetadata() {
        return new GameMetadata(metadata);
    }

    public void setMetadata(GameMetadata metadata) {
        this.metadata = new GameMetadata(metadata);
    }

    public void clearCell(int c, int r) {
        playerGrid.clearCell(c, r);
    }

    public void setCell(int c, int r) {
        playerGrid.setCell(c, r, CellState.FILLED);
    }

    public void setMark(int c, int r) {
        playerGrid.setCell(c, r, CellState.MARKED);
    }

    public boolean checkWin() {
        Integer[][] cols = board.getColumnCounts();
        char[][] playBoardColumns = getPlayColumns();
        for (int c = 0; c < cols.length; c++) {
            Integer[] cnts = getCellCount(playBoardColumns[c]);
            if (!areEqual(cnts, cols[c])) {
                return false;
            }
        }
        Integer[][] rows = board.getRowCounts();
        char[][] playBoardRows = getPlayRows();
        for (int r = 0; r < rows.length; r++) {
            Integer[] cnts = getCellCount(playBoardRows[r]);
            if (!areEqual(cnts, rows[r])) {
                return false;
            }
        }
        return true;
    }

    private Integer[] getCellCount(char[] cary) {
        ArrayList<Integer> lst = new ArrayList<>();
        int cnt = 0;
        for (char c : cary) {
            if (c == FillChar) {
                ++cnt;
            } else if (cnt > 0) {
                lst.add(cnt);
                cnt = 0;
            }
        }
        if (cnt > 0) {
            lst.add(cnt);
        }
        Integer[] ary = new Integer[lst.size()];
        return lst.toArray(ary);
    }

    private boolean areEqual(Integer[] ary1, Integer[] ary2) {
        if (ary1.length != ary2.length) {
            return false;
        }
        for (int i = 0; i < ary1.length; i++) {
            if (!ary1[i].equals(ary2[i])) {
                return false;
            }
        }
        return true;
    }

    public void loadBoard(File loadFile) throws IOException {
        if (isJsonFile(loadFile)) {
            NanoGridJsonFile file = new NanoGridJsonFile(this);
            file.deserialize(loadFile);
        } else {
            NanoGridFile file = new NanoGridFile(this);
            file.deserialize(loadFile);
        }
    }

    public void resetBoard(File loadFile) throws IOException {
        loadBoard(loadFile);
        playerGrid = new PlayerGrid(settings.getColumns(), settings.getRows());
    }

    public void saveGame(File output) throws IOException {
        String currentSaveType = metadata.getSaveType();
        metadata.setSaveType(GameMetadata.TYPE_GAME);
        if (usesJsonExtension(output)) {
            NanoGridJsonFile file = new NanoGridJsonFile(this);
            file.serialize(output);
        } else {
            NanoGridFile file = new NanoGridFile(this);
            file.serialize(output);
        }
        metadata.setSaveType(currentSaveType);
    }

    public void savePuzzle(File output) throws IOException {
        PlayerGrid currentPlayerGrid = playerGrid;
        GameMetadata currentMetadata = metadata;
        playerGrid = new PlayerGrid(settings.getColumns(), settings.getRows());
        metadata = new GameMetadata();
        metadata.setSaveType(GameMetadata.TYPE_PUZZLE);
        try {
            if (usesJsonExtension(output)) {
                NanoGridJsonFile file = new NanoGridJsonFile(this);
                file.serialize(output);
            } else {
                NanoGridFile file = new NanoGridFile(this);
                file.serialize(output);
            }
        } finally {
            playerGrid = currentPlayerGrid;
            metadata = currentMetadata;
        }
    }

    void setBoard(char[][] src) {
        board = new NanoGridBoard(settings);
        board.copy(src);
    }

    void updateSettings(NanoGridParameters newSettings) {
        settings = newSettings;
        create();
    }

    public char[][] getPlayColumns() {
        return playerGrid.toColumnChars();
    }

    void setPlayColumns(char[][] cols) {
        playerGrid = PlayerGrid.fromColumns(cols);
    }

    char[][] getPlayRows() {
        return playerGrid.toRowChars();
    }

    public void setPlayRows(char[][] rows) {
        playerGrid = PlayerGrid.fromRows(rows);
    }

    public Puzzle getPuzzle() {
        return Puzzle.fromBoard(board);
    }

    public PlayerGrid getPlayerGrid() {
        return PlayerGrid.fromColumns(getPlayColumns());
    }

    public GameSession getSession() {
        return new GameSession(getPuzzle(), getPlayerGrid());
    }

    public int getIncorrectMoves() {
        return getSession().getIncorrectMoves();
    }

    private boolean usesJsonExtension(File file) {
        return file.getName().toLowerCase().endsWith(".json");
    }

    private boolean isJsonFile(File file) throws IOException {
        if (usesJsonExtension(file)) {
            return true;
        }
        String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8).trim();
        return content.startsWith("{");
    }
}
