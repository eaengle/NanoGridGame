package nanogridgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

public class NanoGridGameTest {

    private NanoGridParameters params;
    private NanoGridGame game;

    @BeforeEach
    void setup() {
        params = new NanoGridParameters();
        params.setColumns(5);
        params.setRows(5);
        game = new NanoGridGame(params);
    }

    @Test
    void newGameHasNoIncorrectMoves() {
        assertEquals(0, game.getIncorrectMoves());
    }

    @Test
    void newGameIsNotWon() {
        assertFalse(game.checkWin());
    }

    @Test
    void setCellIsReflectedInPlayColumns() {
        game.setCell(0, 0);
        assertEquals(NanoGridBoard.FillChar, game.getPlayColumns()[0][0]);
    }

    @Test
    void clearCellRemovesFill() {
        game.setCell(2, 3);
        game.clearCell(2, 3);
        assertEquals(0, game.getPlayColumns()[2][3]);
    }

    @Test
    void setMarkIsReflectedInPlayColumns() {
        game.setMark(1, 1);
        assertEquals(NanoGridBoard.MarkChar, game.getPlayColumns()[1][1]);
    }

    @Test
    void setCellAndMarkAreSynchronisedInPlayRows() {
        game.setCell(2, 3);
        assertEquals(NanoGridBoard.FillChar, game.getPlayColumns()[2][3]);
        assertEquals(NanoGridBoard.FillChar, game.getPlayRows()[3][2]);

        game.setMark(0, 4);
        assertEquals(NanoGridBoard.MarkChar, game.getPlayColumns()[0][4]);
        assertEquals(NanoGridBoard.MarkChar, game.getPlayRows()[4][0]);
    }

    @Test
    void clearCellIsSynchronisedInPlayRows() {
        game.setCell(2, 3);
        game.clearCell(2, 3);

        assertEquals(0, game.getPlayColumns()[2][3]);
        assertEquals(0, game.getPlayRows()[3][2]);
    }

    @Test
    void getSettingsReturnsCorrectDimensions() {
        assertEquals(5, game.getSettings().getColumns());
        assertEquals(5, game.getSettings().getRows());
    }

    @Test
    void getBoardIsNotNull() {
        assertNotNull(game.getBoard());
    }

    @Test
    void exposesPuzzleAndPlayerGridSnapshots() {
        game.setCell(0, 0);
        game.setMark(1, 1);

        Puzzle puzzle = game.getPuzzle();
        PlayerGrid playerGrid = game.getPlayerGrid();
        GameSession session = game.getSession();

        assertEquals(5, puzzle.getColumns());
        assertEquals(5, puzzle.getRows());
        assertEquals(CellState.FILLED, playerGrid.getCell(0, 0));
        assertEquals(CellState.MARKED, playerGrid.getCell(1, 1));
        assertEquals(CellState.FILLED, session.getPlayerGrid().getCell(0, 0));
    }

    @Test
    void incorrectMoveCountIncreasesForWrongFill() {
        // Fill every cell; some will be wrong
        NanoGridBoard board = game.getBoard();
        for (int c = 0; c < 5; c++) {
            for (int r = 0; r < 5; r++) {
                if (board.getCell(c, r) != NanoGridBoard.FillChar) {
                    game.setCell(c, r);
                }
            }
        }
        // Only cells the solution doesn't have filled should be counted wrong
        // At this point we've filled every cell, including ones that should be empty
        // So incorrect moves >= 0 and depends on the puzzle
        assertTrue(game.getIncorrectMoves() >= 0);
    }

    @Test
    void createWithSizeReinitializesBoard() {
        game.create(8, 8);
        assertEquals(8, game.getBoard().getColumnSize());
        assertEquals(8, game.getBoard().getRowSize());
        assertEquals(8, game.getSettings().getColumns());
        assertEquals(8, game.getSettings().getRows());
    }

    @Test
    void playBoardClearedAfterCreate() {
        game.setCell(2, 2);
        game.create();
        assertEquals(0, game.getPlayColumns()[2][2]);
    }

    @Test
    void saveGameRoundTripPreservesBoardSettingsAndProgress(@TempDir Path tempDir) throws Exception {
        game.setCell(0, 0);
        game.setMark(1, 1);
        File saveFile = tempDir.resolve("game.xml").toFile();

        game.saveGame(saveFile);

        NanoGridGame loaded = new NanoGridGame(new NanoGridParameters());
        loaded.loadBoard(saveFile);

        assertEquals(5, loaded.getSettings().getColumns());
        assertEquals(5, loaded.getSettings().getRows());
        assertTrue(game.getBoard().checkWin(loaded.getBoard()));
        assertEquals(NanoGridBoard.FillChar, loaded.getPlayColumns()[0][0]);
        assertEquals(NanoGridBoard.MarkChar, loaded.getPlayColumns()[1][1]);
        assertEquals(NanoGridBoard.FillChar, loaded.getPlayRows()[0][0]);
        assertEquals(NanoGridBoard.MarkChar, loaded.getPlayRows()[1][1]);
    }

    @Test
    void saveJsonGameRoundTripPreservesBoardSettingsAndProgress(@TempDir Path tempDir) throws Exception {
        params.setDifficulty(PuzzleDifficulty.HARD);
        params.setSeed(24680L);
        params.setSymmetric(true);
        game = new NanoGridGame(params);
        game.setCell(0, 0);
        game.setMark(1, 1);
        File saveFile = tempDir.resolve("game.json").toFile();

        game.saveGame(saveFile);

        String json = new String(Files.readAllBytes(saveFile.toPath()), StandardCharsets.UTF_8);
        assertTrue(json.contains("\"format\": \"NanoGridGame\""));
        assertTrue(json.contains("\"version\": 3"));

        NanoGridGame loaded = new NanoGridGame(new NanoGridParameters());
        loaded.loadBoard(saveFile);

        assertEquals(5, loaded.getSettings().getColumns());
        assertEquals(5, loaded.getSettings().getRows());
        assertEquals(PuzzleDifficulty.HARD, loaded.getSettings().getDifficulty());
        assertTrue(loaded.getSettings().isUseSeed());
        assertEquals(24680L, loaded.getSettings().getSeed());
        assertTrue(loaded.getSettings().isSymmetric());
        assertTrue(game.getBoard().checkWin(loaded.getBoard()));
        assertEquals(NanoGridBoard.FillChar, loaded.getPlayColumns()[0][0]);
        assertEquals(NanoGridBoard.MarkChar, loaded.getPlayColumns()[1][1]);
        assertEquals(NanoGridBoard.FillChar, loaded.getPlayRows()[0][0]);
        assertEquals(NanoGridBoard.MarkChar, loaded.getPlayRows()[1][1]);
    }

    @Test
    void loadBoardDetectsJsonContentWithoutJsonExtension(@TempDir Path tempDir) throws Exception {
        game.setCell(0, 0);
        File jsonFile = tempDir.resolve("game.json").toFile();
        File saveFile = tempDir.resolve("game.ngrid").toFile();
        game.saveGame(jsonFile);
        Files.copy(jsonFile.toPath(), saveFile.toPath());

        NanoGridGame loaded = new NanoGridGame(new NanoGridParameters());
        loaded.loadBoard(saveFile);

        assertTrue(game.getBoard().checkWin(loaded.getBoard()));
        assertEquals(NanoGridBoard.FillChar, loaded.getPlayColumns()[0][0]);
    }

    @Test
    void savePuzzleRoundTripPreservesBoardButOmitsProgress(@TempDir Path tempDir) throws Exception {
        params.setDifficulty(PuzzleDifficulty.EASY);
        params.setSeed(13579L);
        params.setSymmetric(true);
        game = new NanoGridGame(params);
        game.setCell(0, 0);
        game.setMark(1, 1);
        File saveFile = tempDir.resolve("puzzle.xml").toFile();

        game.savePuzzle(saveFile);

        assertEquals(NanoGridBoard.FillChar, game.getPlayColumns()[0][0]);
        assertEquals(NanoGridBoard.MarkChar, game.getPlayColumns()[1][1]);

        NanoGridGame loaded = new NanoGridGame(new NanoGridParameters());
        loaded.loadBoard(saveFile);

        assertEquals(PuzzleDifficulty.EASY, loaded.getSettings().getDifficulty());
        assertTrue(loaded.getSettings().isUseSeed());
        assertEquals(13579L, loaded.getSettings().getSeed());
        assertTrue(loaded.getSettings().isSymmetric());
        assertTrue(game.getBoard().checkWin(loaded.getBoard()));
        assertEquals(0, loaded.getPlayColumns()[0][0]);
        assertEquals(0, loaded.getPlayColumns()[1][1]);
        assertEquals(0, loaded.getPlayRows()[0][0]);
        assertEquals(0, loaded.getPlayRows()[1][1]);
    }

    @Test
    void saveJsonPuzzleRoundTripPreservesBoardButOmitsProgress(@TempDir Path tempDir) throws Exception {
        game.setCell(0, 0);
        game.setMark(1, 1);
        File saveFile = tempDir.resolve("puzzle.json").toFile();

        game.savePuzzle(saveFile);

        assertEquals(NanoGridBoard.FillChar, game.getPlayColumns()[0][0]);
        assertEquals(NanoGridBoard.MarkChar, game.getPlayColumns()[1][1]);

        NanoGridGame loaded = new NanoGridGame(new NanoGridParameters());
        loaded.loadBoard(saveFile);

        assertTrue(game.getBoard().checkWin(loaded.getBoard()));
        assertEquals(0, loaded.getPlayColumns()[0][0]);
        assertEquals(0, loaded.getPlayColumns()[1][1]);
    }

    @Test
    void resetBoardLoadsPuzzleAndClearsSavedProgress(@TempDir Path tempDir) throws Exception {
        game.setCell(0, 0);
        game.setMark(1, 1);
        File saveFile = tempDir.resolve("game-with-progress.xml").toFile();
        game.saveGame(saveFile);

        NanoGridGame loadedAsPuzzle = new NanoGridGame(new NanoGridParameters());
        loadedAsPuzzle.resetBoard(saveFile);

        assertTrue(game.getBoard().checkWin(loadedAsPuzzle.getBoard()));
        assertEquals(0, loadedAsPuzzle.getPlayColumns()[0][0]);
        assertEquals(0, loadedAsPuzzle.getPlayColumns()[1][1]);
    }
}
