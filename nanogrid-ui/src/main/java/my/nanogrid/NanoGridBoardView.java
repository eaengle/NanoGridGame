package my.nanogrid;

import nanogridgame.NanoGridBoard;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;

class NanoGridBoardView extends JComponent {

    private final GameController controller;
    private final Runnable winHandler;
    private final CellMoveListener moveListener;

    private Rectangle boardBounds = new Rectangle();
    private int cellSize = 24;
    private int clueWidth = 80;
    private int clueHeight = 80;
    private Point hoverCell;
    private Point measureStart;
    private Point measureEnd;
    private Point lastAppliedCell;
    private char dragState;
    private InteractionMode interactionMode = InteractionMode.CYCLE;
    private boolean showSolution;
    private boolean winAnnounced;
    private Point cursorCell;
    private int winAnimColumn = -1;
    private Timer winAnimTimer;

    NanoGridBoardView(GameController controller, Runnable winHandler, CellMoveListener moveListener) {
        this.controller = controller;
        this.winHandler = winHandler;
        this.moveListener = moveListener;
        setOpaque(true);
        setFocusable(true);
        BoardMouseListener mouseListener = new BoardMouseListener();
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
        installKeyBindings();
    }

    private void installKeyBindings() {
        InputMap im = getInputMap(WHEN_FOCUSED);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,    0), "cursorUp");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,  0), "cursorDown");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,  0), "cursorLeft");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "cursorRight");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "cursorApply");

        getActionMap().put("cursorUp",    cursorMoveAction( 0, -1));
        getActionMap().put("cursorDown",  cursorMoveAction( 0,  1));
        getActionMap().put("cursorLeft",  cursorMoveAction(-1,  0));
        getActionMap().put("cursorRight", cursorMoveAction( 1,  0));
        getActionMap().put("cursorApply", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cursorCell != null) {
                    lastAppliedCell = null;
                    applyCellState(cursorCell, nextState(cursorCell));
                }
            }
        });
    }

    private Action cursorMoveAction(int dx, int dy) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int columns = controller.getSettings().getColumns();
                int rows = controller.getSettings().getRows();
                if (cursorCell == null) {
                    cursorCell = new Point(0, 0);
                } else {
                    cursorCell = new Point(
                            Math.max(0, Math.min(columns - 1, cursorCell.x + dx)),
                            Math.max(0, Math.min(rows - 1, cursorCell.y + dy)));
                }
                repaint();
            }
        };
    }

    void refreshBoard() {
        showSolution = false;
        winAnnounced = false;
        measureStart = null;
        measureEnd = null;
        lastAppliedCell = null;
        cursorCell = null;
        if (winAnimTimer != null) {
            winAnimTimer.stop();
            winAnimTimer = null;
        }
        winAnimColumn = -1;
        revalidate();
        repaint();
    }

    void setShowSolution(boolean showSolution) {
        this.showSolution = showSolution;
        repaint();
    }

    void setInteractionMode(InteractionMode interactionMode) {
        this.interactionMode = interactionMode;
    }

    boolean refreshAfterProgrammaticMove() {
        lastAppliedCell = null;
        boolean solved = controller.checkWin();
        if (solved) {
            showSolution = true;
            if (!winAnnounced) {
                winAnnounced = true;
                repaint();
                return true;
            }
        } else {
            showSolution = false;
            winAnnounced = false;
        }
        repaint();
        return false;
    }

    @Override
    public Dimension getPreferredSize() {
        int columns = controller.getSettings().getColumns();
        int rows = controller.getSettings().getRows();
        return new Dimension(Math.max(520, columns * 28 + 170), Math.max(420, rows * 28 + 170));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            calculateLayout(g);
            paintBackground(g);
            paintHighlights(g);
            paintClues(g);
            paintCells(g);
            paintGrid(g);
            paintCursor(g);
            paintMeasureValue(g);
        } finally {
            g.dispose();
        }
    }

    private void calculateLayout(Graphics2D g) {
        int columns = controller.getSettings().getColumns();
        int rows = controller.getSettings().getRows();
        g.setFont(getClueFont());
        FontMetrics metrics = g.getFontMetrics();
        clueWidth = Math.max(56, controller.getBoard().getMaxRowCounts() * metrics.stringWidth(" 00"));
        clueHeight = Math.max(56, controller.getBoard().getMaxColumnCounts() * metrics.getHeight() + 14);
        int availableWidth = Math.max(1, getWidth() - clueWidth - 24);
        int availableHeight = Math.max(1, getHeight() - clueHeight - 24);
        cellSize = Math.max(12, Math.min(availableWidth / columns, availableHeight / rows));
        int boardWidth = columns * cellSize;
        int boardHeight = rows * cellSize;
        int left = Math.max(12, clueWidth + (getWidth() - clueWidth - boardWidth) / 2);
        int top = Math.max(12, clueHeight + (getHeight() - clueHeight - boardHeight) / 2);
        boardBounds = new Rectangle(left, top, boardWidth, boardHeight);
    }

    private void paintBackground(Graphics2D g) {
        ColorTheme theme = ThemeManager.current();
        g.setColor(theme.background);
        g.fillRect(0, 0, getWidth(), getHeight());
        paintMarginArtwork(g);
        g.setColor(theme.clueBackground);
        g.fillRect(boardBounds.x - clueWidth, boardBounds.y, clueWidth, boardBounds.height);
        g.fillRect(boardBounds.x, boardBounds.y - clueHeight, boardBounds.width, clueHeight);
        g.fillRect(boardBounds.x - clueWidth, boardBounds.y - clueHeight, clueWidth, clueHeight);
    }

    private void paintMarginArtwork(Graphics2D g) {
        int occupiedX = boardBounds.x - clueWidth;
        int occupiedY = boardBounds.y - clueHeight;
        int occupiedWidth = boardBounds.width + clueWidth;
        int occupiedHeight = boardBounds.height + clueHeight;

        Area marginArea = new Area(new Rectangle(0, 0, getWidth(), getHeight()));
        marginArea.subtract(new Area(new Rectangle(
                occupiedX - 8,
                occupiedY - 8,
                occupiedWidth + 16,
                occupiedHeight + 16)));

        Shape oldClip = g.getClip();
        Composite oldComposite = g.getComposite();
        g.clip(marginArea);
        try {
            g.setComposite(java.awt.AlphaComposite.SrcOver);
            paintPixelCluster(g, Math.max(18, occupiedX / 3), Math.max(18, occupiedY / 3), 5, false);
            paintPixelCluster(g, getWidth() - 126, Math.max(18, occupiedY / 2), 6, true);
            paintPixelCluster(g, getWidth() - 154, getHeight() - 132, 5, false);
            paintPixelCluster(g, Math.max(18, occupiedX / 4), getHeight() - 124, 4, true);
            paintPixelCluster(g, getWidth() - 102, getHeight() / 2 - 28, 4, false);
            paintPixelCluster(g, Math.max(22, occupiedX / 2 - 18), getHeight() / 2 + 38, 4, true);
            paintMiniGrid(g, 22, Math.max(20, occupiedY + 26), 74, 54);
            paintMiniGrid(g, getWidth() - 112, getHeight() - 86, 82, 50);
            paintCornerBrackets(g, occupiedX, occupiedY, occupiedWidth, occupiedHeight);
            paintCircuitTrace(g, occupiedX + occupiedWidth + 28, occupiedY + 18, 88, 58);
            paintCircuitTrace(g, occupiedX - 116, occupiedY + occupiedHeight + 28, 96, 48);
            paintCircuitTrace(g, getWidth() - 128, occupiedY + occupiedHeight / 2, 104, 64);
            paintCircuitTrace(g, 24, occupiedY + occupiedHeight / 2 - 70, 98, 54);
            paintSoftDots(g);
        } finally {
            g.setComposite(oldComposite);
            g.setClip(oldClip);
        }
    }

    private void paintPixelCluster(Graphics2D g, int x, int y, int block, boolean flip) {
        ColorTheme theme = ThemeManager.current();
        int[][] cells = {
                {0, 1}, {1, 0}, {1, 1}, {2, 1}, {3, 0},
                {0, 3}, {1, 3}, {2, 2}, {3, 3}, {4, 1}, {4, 4}
        };
        int step = block + 6;
        g.setColor(withAlpha(theme.fill, ThemeManager.isDark() ? 42 : 28));
        for (int i = 0; i < cells.length; i++) {
            int cx = flip ? 4 - cells[i][0] : cells[i][0];
            int cy = cells[i][1];
            g.fillRoundRect(x + cx * step, y + cy * step, block, block, 2, 2);
        }

        g.setColor(withAlpha(theme.measure, ThemeManager.isDark() ? 64 : 48));
        g.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(x + step, y + step, x + 3 * step, y + step);
        g.drawLine(x + 2 * step, y + 2 * step, x + 2 * step, y + 4 * step);
        g.drawLine(x, y + 3 * step, x + 3 * step, y + 3 * step);
    }

    private void paintCircuitTrace(Graphics2D g, int x, int y, int width, int height) {
        ColorTheme theme = ThemeManager.current();
        if (x < -width || y < -height || x > getWidth() || y > getHeight()) {
            return;
        }
        g.setColor(withAlpha(theme.measure, ThemeManager.isDark() ? 54 : 38));
        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int midY = y + height / 2;
        g.drawLine(x, midY, x + width / 3, midY);
        g.drawLine(x + width / 3, midY, x + width / 3, y);
        g.drawLine(x + width / 3, y, x + width, y);
        g.drawLine(x + width / 2, midY, x + width / 2, y + height);
        g.drawLine(x + width / 2, y + height, x + width, y + height);

        g.setColor(withAlpha(theme.mark, ThemeManager.isDark() ? 72 : 50));
        int node = 7;
        g.fillOval(x - node / 2, midY - node / 2, node, node);
        g.fillOval(x + width / 3 - node / 2, y - node / 2, node, node);
        g.fillOval(x + width - node / 2, y + height - node / 2, node, node);
    }

    private void paintMiniGrid(Graphics2D g, int x, int y, int width, int height) {
        ColorTheme theme = ThemeManager.current();
        if (x < -width || y < -height || x > getWidth() || y > getHeight()) {
            return;
        }
        g.setColor(withAlpha(theme.clueBackground, ThemeManager.isDark() ? 90 : 115));
        g.fillRoundRect(x, y, width, height, 6, 6);
        g.setColor(withAlpha(theme.gridLineStrong, ThemeManager.isDark() ? 55 : 45));
        g.setStroke(new BasicStroke(1f));
        int cell = 10;
        for (int gx = x + 8; gx <= x + width - 8; gx += cell) {
            g.drawLine(gx, y + 7, gx, y + height - 7);
        }
        for (int gy = y + 7; gy <= y + height - 7; gy += cell) {
            g.drawLine(x + 8, gy, x + width - 8, gy);
        }

        g.setColor(withAlpha(theme.fill, ThemeManager.isDark() ? 64 : 42));
        g.fillRect(x + 18, y + 17, 9, 9);
        g.fillRect(x + 38, y + 27, 9, 9);
        g.fillRect(x + width - 28, y + 17, 9, 9);
        g.setColor(withAlpha(theme.mark, ThemeManager.isDark() ? 88 : 64));
        g.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(x + 18, y + height - 18, x + 27, y + height - 9);
        g.drawLine(x + 27, y + height - 18, x + 18, y + height - 9);
    }

    private void paintCornerBrackets(Graphics2D g, int occupiedX, int occupiedY, int occupiedWidth, int occupiedHeight) {
        ColorTheme theme = ThemeManager.current();
        g.setColor(withAlpha(theme.gridLineStrong, ThemeManager.isDark() ? 58 : 40));
        g.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int len = 34;
        int gap = 22;
        drawBracket(g, occupiedX - gap, occupiedY - gap, len, len, 1, 1);
        drawBracket(g, occupiedX + occupiedWidth + gap, occupiedY - gap, len, len, -1, 1);
        drawBracket(g, occupiedX - gap, occupiedY + occupiedHeight + gap, len, len, 1, -1);
        drawBracket(g, occupiedX + occupiedWidth + gap, occupiedY + occupiedHeight + gap, len, len, -1, -1);
    }

    private void drawBracket(Graphics2D g, int x, int y, int width, int height, int xDir, int yDir) {
        g.drawLine(x, y, x + xDir * width, y);
        g.drawLine(x, y, x, y + yDir * height);
    }

    private void paintSoftDots(Graphics2D g) {
        Color dot = withAlpha(ThemeManager.current().gridLineStrong, ThemeManager.isDark() ? 30 : 22);
        g.setColor(dot);
        int spacing = 34;
        for (int x = 16; x < getWidth(); x += spacing) {
            for (int y = 18; y < getHeight(); y += spacing) {
                if ((x / spacing + y / spacing) % 3 == 0) {
                    g.fillOval(x, y, 3, 3);
                }
            }
        }
    }

    private Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    private void paintClues(Graphics2D g) {
        ColorTheme theme = ThemeManager.current();
        g.setFont(getClueFont());
        FontMetrics metrics = g.getFontMetrics();
        Integer[][] rowCounts = controller.getBoard().getRowCounts();
        Integer[][] columnCounts = controller.getBoard().getColumnCounts();
        char[][] playerCells = controller.getPlayColumns();
        int columns = controller.getSettings().getColumns();
        int rows = controller.getSettings().getRows();

        for (int r = 0; r < rowCounts.length; r++) {
            char[] rowCells = new char[columns];
            for (int c = 0; c < columns; c++) rowCells[c] = playerCells[c][r];
            g.setColor(matchesClue(rowCells, rowCounts[r]) ? theme.satisfiedText : theme.text);
            String text = join(rowCounts[r], " ");
            int x = boardBounds.x - 10 - metrics.stringWidth(text);
            int y = boardBounds.y + r * cellSize + (cellSize + metrics.getAscent() - metrics.getDescent()) / 2;
            g.drawString(text, x, y);
        }

        for (int c = 0; c < columnCounts.length; c++) {
            char[] colCells = new char[rows];
            for (int r = 0; r < rows; r++) colCells[r] = playerCells[c][r];
            boolean satisfied = matchesClue(colCells, columnCounts[c]);
            Integer[] counts = columnCounts[c];
            int startY = boardBounds.y - 8 - (counts.length - 1) * metrics.getHeight();
            g.setColor(satisfied ? theme.satisfiedText : theme.text);
            for (int i = 0; i < counts.length; i++) {
                String text = counts[i].toString();
                int x = boardBounds.x + c * cellSize + (cellSize - metrics.stringWidth(text)) / 2;
                int y = startY + i * metrics.getHeight();
                g.drawString(text, x, y);
            }
        }
    }

    private boolean matchesClue(char[] cells, Integer[] clue) {
        int runIdx = 0;
        int run = 0;
        for (char cell : cells) {
            if (cell == NanoGridBoard.FillChar) {
                run++;
            } else if (run > 0) {
                if (runIdx >= clue.length || run != clue[runIdx++]) return false;
                run = 0;
            }
        }
        if (run > 0) {
            if (runIdx >= clue.length || run != clue[runIdx++]) return false;
        }
        return runIdx == clue.length;
    }

    private String join(Integer[] values, String delimiter) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                text.append(delimiter);
            }
            text.append(values[i]);
        }
        return text.toString();
    }

    private void paintHighlights(Graphics2D g) {
        ColorTheme theme = ThemeManager.current();
        if (hoverCell != null) {
            g.setColor(theme.hover);
            // row: board + clue band to the left
            g.fillRect(boardBounds.x - clueWidth, boardBounds.y + hoverCell.y * cellSize, boardBounds.width + clueWidth, cellSize);
            // column: board + clue band above
            g.fillRect(boardBounds.x + hoverCell.x * cellSize, boardBounds.y - clueHeight, cellSize, boardBounds.height + clueHeight);
        }

        if (measureStart != null && measureEnd != null &&
                (measureStart.x == measureEnd.x || measureStart.y == measureEnd.y)) {
            Color m = theme.measure;
            g.setColor(new Color(m.getRed(), m.getGreen(), m.getBlue(), 90));
            int minCol = Math.min(measureStart.x, measureEnd.x);
            int maxCol = Math.max(measureStart.x, measureEnd.x);
            int minRow = Math.min(measureStart.y, measureEnd.y);
            int maxRow = Math.max(measureStart.y, measureEnd.y);
            g.fillRect(boardBounds.x + minCol * cellSize,
                    boardBounds.y + minRow * cellSize,
                    (maxCol - minCol + 1) * cellSize,
                    (maxRow - minRow + 1) * cellSize);
        }
    }

    private void paintCells(Graphics2D g) {
        ColorTheme theme = ThemeManager.current();
        NanoGridBoard board = controller.getBoard();
        char[][] playerCells = controller.getPlayColumns();
        for (int c = 0; c < controller.getSettings().getColumns(); c++) {
            for (int r = 0; r < controller.getSettings().getRows(); r++) {
                Rectangle cell = cellRectangle(c, r);
                char player = playerCells[c][r];
                boolean reveal = showSolution || (winAnimColumn >= 0 && c <= winAnimColumn);
                if (reveal && board.getCell(c, r) == NanoGridBoard.FillChar) {
                    g.setColor(player == NanoGridBoard.FillChar ? theme.correctReveal : theme.missedReveal);
                    g.fillRect(cell.x + 2, cell.y + 2, cell.width - 3, cell.height - 3);
                } else if (player == NanoGridBoard.FillChar) {
                    g.setColor(theme.fill);
                    g.fillRect(cell.x + 2, cell.y + 2, cell.width - 3, cell.height - 3);
                } else if (player == NanoGridBoard.MarkChar) {
                    paintMark(g, cell);
                }
            }
        }
    }

    private void startWinAnimation() {
        int columns = controller.getSettings().getColumns();
        int delay = Math.max(16, 600 / columns);
        winAnimColumn = 0;
        winAnimTimer = new Timer(delay, null);
        winAnimTimer.addActionListener(e -> {
            winAnimColumn++;
            repaint();
            if (winAnimColumn >= columns) {
                winAnimTimer.stop();
                winAnimTimer = null;
                winAnimColumn = -1;
                showSolution = true;
                winHandler.run();
            }
        });
        winAnimTimer.start();
        repaint();
    }

    private void paintMark(Graphics2D g, Rectangle cell) {
        int pad = Math.max(4, cellSize / 4);
        g.setColor(ThemeManager.current().mark);
        g.setStroke(new BasicStroke(Math.max(2f, cellSize / 12f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(cell.x + pad, cell.y + pad, cell.x + cell.width - pad, cell.y + cell.height - pad);
        g.drawLine(cell.x + cell.width - pad, cell.y + pad, cell.x + pad, cell.y + cell.height - pad);
    }

    private void paintGrid(Graphics2D g) {
        ColorTheme theme = ThemeManager.current();
        int columns = controller.getSettings().getColumns();
        int rows = controller.getSettings().getRows();
        for (int c = 0; c <= columns; c++) {
            boolean strong = c % 5 == 0;
            g.setColor(strong ? theme.gridLineStrong : theme.gridLine);
            g.setStroke(new BasicStroke(strong ? 1.6f : 1f));
            int x = boardBounds.x + c * cellSize;
            g.drawLine(x, boardBounds.y, x, boardBounds.y + boardBounds.height);
        }
        for (int r = 0; r <= rows; r++) {
            boolean strong = r % 5 == 0;
            g.setColor(strong ? theme.gridLineStrong : theme.gridLine);
            g.setStroke(new BasicStroke(strong ? 1.6f : 1f));
            int y = boardBounds.y + r * cellSize;
            g.drawLine(boardBounds.x, y, boardBounds.x + boardBounds.width, y);
        }
    }

    private void paintCursor(Graphics2D g) {
        if (cursorCell == null) return;
        Rectangle cell = cellRectangle(cursorCell.x, cursorCell.y);
        g.setColor(ThemeManager.current().cursor);
        g.setStroke(new BasicStroke(2f));
        g.drawRect(cell.x + 1, cell.y + 1, cell.width - 2, cell.height - 2);
    }

    private void paintMeasureValue(Graphics2D g) {
        if (measureStart == null || measureEnd == null ||
                (measureStart.x != measureEnd.x && measureStart.y != measureEnd.y)) {
            return;
        }
        int value = measureStart.x == measureEnd.x
                ? Math.abs(measureEnd.y - measureStart.y) + 1
                : Math.abs(measureEnd.x - measureStart.x) + 1;
        String text = String.valueOf(value);
        g.setFont(getMeasureFont());
        FontMetrics metrics = g.getFontMetrics();
        int size = Math.max(42, metrics.stringWidth(text) + 24);
        int x = boardBounds.x - clueWidth + (clueWidth - size) / 2;
        int y = boardBounds.y - clueHeight + (clueHeight - size) / 2;
        ColorTheme theme = ThemeManager.current();
        g.setColor(theme.measure);
        g.fillRoundRect(x, y, size, size, 8, 8);
        g.setColor(theme.measureText);
        g.drawString(text, x + (size - metrics.stringWidth(text)) / 2,
                y + (size + metrics.getAscent() - metrics.getDescent()) / 2);
    }

    private Rectangle cellRectangle(int column, int row) {
        return new Rectangle(boardBounds.x + column * cellSize,
                boardBounds.y + row * cellSize,
                cellSize,
                cellSize);
    }

    private Point cellAt(Point point) {
        if (!boardBounds.contains(point)) {
            return null;
        }
        int column = (point.x - boardBounds.x) / cellSize;
        int row = (point.y - boardBounds.y) / cellSize;
        if (column < 0 || row < 0 ||
                column >= controller.getSettings().getColumns() ||
                row >= controller.getSettings().getRows()) {
            return null;
        }
        return new Point(column, row);
    }

    private void applyCellState(Point cell, char state) {
        if (lastAppliedCell != null && lastAppliedCell.equals(cell)) {
            return;
        }
        char before = controller.getCellState(cell.x, cell.y);
        if (before == state || (before == 0 && state == 0)) {
            lastAppliedCell = cell;
            return;
        }
        if (state == NanoGridBoard.FillChar) {
            controller.setCell(cell.x, cell.y);
        } else if (state == NanoGridBoard.MarkChar) {
            controller.setMark(cell.x, cell.y);
        } else {
            controller.clearCell(cell.x, cell.y);
        }
        lastAppliedCell = cell;
        showSolution = false;
        repaint();
        moveListener.cellChanged(new CellMove(cell.x, cell.y, before, state));
        if (!winAnnounced && controller.checkWin()) {
            winAnnounced = true;
            startWinAnimation();
        }
    }

    private char nextState(Point cell) {
        if (interactionMode == InteractionMode.FILL) {
            return NanoGridBoard.FillChar;
        }
        if (interactionMode == InteractionMode.MARK) {
            return NanoGridBoard.MarkChar;
        }
        if (interactionMode == InteractionMode.ERASE) {
            return 0;
        }
        char current = controller.getCellState(cell.x, cell.y);
        if (current == NanoGridBoard.MarkChar) {
            return 0;
        }
        if (current == NanoGridBoard.FillChar) {
            return NanoGridBoard.MarkChar;
        }
        return NanoGridBoard.FillChar;
    }

    private Font getClueFont() {
        return new Font(Font.SANS_SERIF, Font.BOLD, Math.max(12, Math.min(18, cellSize / 2 + 4)));
    }

    private Font getMeasureFont() {
        return new Font(Font.SANS_SERIF, Font.BOLD, 22);
    }

    private class BoardMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent event) {
            requestFocusInWindow();
            Point cell = cellAt(event.getPoint());
            if (cell == null) {
                return;
            }
            hoverCell = cell;
            if (SwingUtilities.isRightMouseButton(event)) {
                measureStart = cell;
                measureEnd = cell;
            } else if (SwingUtilities.isLeftMouseButton(event)) {
                measureStart = null;
                measureEnd = null;
                lastAppliedCell = null;
                dragState = nextState(cell);
                applyCellState(cell, dragState);
            }
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            Point cell = cellAt(event.getPoint());
            if (cell == null) {
                return;
            }
            hoverCell = cell;
            if (SwingUtilities.isRightMouseButton(event)) {
                if (measureStart != null && (measureStart.x == cell.x || measureStart.y == cell.y)) {
                    measureEnd = cell;
                }
            } else if (SwingUtilities.isLeftMouseButton(event)) {
                applyCellState(cell, dragState);
            }
            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent event) {
            hoverCell = cellAt(event.getPoint());
            repaint();
        }

        @Override
        public void mouseExited(MouseEvent event) {
            hoverCell = null;
            repaint();
        }
    }
}
