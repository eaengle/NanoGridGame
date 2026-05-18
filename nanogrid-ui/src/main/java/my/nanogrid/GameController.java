package my.nanogrid;

import nanogridgame.NanoGridBoard;
import nanogridgame.NanoGridGame;
import nanogridgame.NanoGridParameters;

import java.io.File;
import java.io.IOException;

public class GameController {

    private NanoGridGame game;
    private NanoGridParameters settings;

    public GameController(NanoGridParameters settings) {
        this.settings = settings;
    }

    public void newGame() {
        game = new NanoGridGame(settings);
    }

    public void setSettings(NanoGridParameters newSettings) {
        this.settings = newSettings;
    }

    public NanoGridParameters getSettings() {
        return settings;
    }

    public NanoGridBoard getBoard() {
        return game.getBoard();
    }

    public char getCellState(int col, int row) {
        return game.getPlayColumns()[col][row];
    }

    public char[][] getPlayColumns() {
        return game.getPlayColumns();
    }

    public void setCell(int col, int row) {
        game.setCell(col, row);
    }

    public void clearCell(int col, int row) {
        game.clearCell(col, row);
    }

    public void setMark(int col, int row) {
        game.setMark(col, row);
    }

    public boolean checkWin() {
        return game.checkWin();
    }

    public int getIncorrectMoves() {
        return game.getIncorrectMoves();
    }

    public void loadGame(File file) throws IOException {
        game.loadBoard(file);
        settings = game.getSettings();
    }

    public void loadPuzzle(File file) throws IOException {
        game.resetBoard(file);
        settings = game.getSettings();
    }

    public void saveGame(File file) throws IOException {
        game.saveGame(file);
    }
}
