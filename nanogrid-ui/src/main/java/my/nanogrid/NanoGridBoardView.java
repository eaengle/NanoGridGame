package my.nanogrid;

import nanogridgame.NanoGridBoard;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

    NanoGridBoardView(GameController controller, Runnable winHandler, CellMoveListener moveListener) {
        this.controller = controller;
        this.winHandler = winHandler;
        this.moveListener = moveListener;
        setOpaque(true);
        setFocusable(true);
        BoardMouseListener mouseListener = new BoardMouseListener();
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }

    void refreshBoard() {
        showSolution = false;
        winAnnounced = false;
        measureStart = null;
        measureEnd = null;
        lastAppliedCell = null;
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
            paintClues(g);
            paintHighlights(g);
            paintCells(g);
            paintGrid(g);
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
        g.setColor(theme.clueBackground);
        g.fillRect(boardBounds.x - clueWidth, boardBounds.y, clueWidth, boardBounds.height);
        g.fillRect(boardBounds.x, boardBounds.y - clueHeight, boardBounds.width, clueHeight);
        g.fillRect(boardBounds.x - clueWidth, boardBounds.y - clueHeight, clueWidth, clueHeight);
    }

    private void paintClues(Graphics2D g) {
        g.setFont(getClueFont());
        g.setColor(ThemeManager.current().text);
        FontMetrics metrics = g.getFontMetrics();
        Integer[][] rowCounts = controller.getBoard().getRowCounts();
        Integer[][] columnCounts = controller.getBoard().getColumnCounts();

        for (int r = 0; r < rowCounts.length; r++) {
            String text = join(rowCounts[r], " ");
            int x = boardBounds.x - 10 - metrics.stringWidth(text);
            int y = boardBounds.y + r * cellSize + (cellSize + metrics.getAscent() - metrics.getDescent()) / 2;
            g.drawString(text, x, y);
        }

        for (int c = 0; c < columnCounts.length; c++) {
            Integer[] counts = columnCounts[c];
            int startY = boardBounds.y - 8 - (counts.length - 1) * metrics.getHeight();
            for (int i = 0; i < counts.length; i++) {
                String text = counts[i].toString();
                int x = boardBounds.x + c * cellSize + (cellSize - metrics.stringWidth(text)) / 2;
                int y = startY + i * metrics.getHeight();
                g.drawString(text, x, y);
            }
        }
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
            g.fillRect(boardBounds.x, boardBounds.y + hoverCell.y * cellSize, boardBounds.width, cellSize);
            g.fillRect(boardBounds.x + hoverCell.x * cellSize, boardBounds.y, cellSize, boardBounds.height);
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
                if (showSolution && board.getCell(c, r) == NanoGridBoard.FillChar) {
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
            showSolution = true;
            winAnnounced = true;
            winHandler.run();
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
        return new Font("Segoe UI", Font.BOLD, Math.max(12, Math.min(18, cellSize / 2 + 4)));
    }

    private Font getMeasureFont() {
        return new Font("Segoe UI", Font.BOLD, 22);
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
