package my.nanogrid;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

final class NanoGridIcon {

    private static final int[] ICON_SIZES = {16, 24, 32, 48, 64, 128};
    private static final int CELLS = 6;
    private static final Color BACKGROUND = new Color(0xF7FAFC);
    private static final Color BORDER = new Color(0x1F2937);
    private static final Color GRID_LINE = new Color(0xCBD5E1);
    private static final Color FILLED_CELL = new Color(0x2563EB);
    private static final Color ACCENT_CELL = new Color(0xF59E0B);

    private NanoGridIcon() {
    }

    static List<Image> createIconImages() {
        List<Image> images = new ArrayList<Image>();
        for (int size : ICON_SIZES) {
            images.add(createIconImage(size));
        }
        return images;
    }

    private static Image createIconImage(int size) {
        if (size <= 24) {
            return createSmallIconImage(size);
        }
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            float margin = Math.max(1.5f, size * 0.08f);
            float outer = size - margin * 2.0f;
            float radius = Math.max(2.0f, size * 0.12f);
            RoundRectangle2D.Float shape = new RoundRectangle2D.Float(margin, margin, outer, outer, radius, radius);

            g.setColor(BACKGROUND);
            g.fill(shape);
            g.setStroke(new BasicStroke(Math.max(1.0f, size * 0.035f)));
            g.setColor(BORDER);
            g.draw(shape);

            float gridMargin = Math.max(3.0f, size * 0.19f);
            float gridSize = size - gridMargin * 2.0f;
            float cell = gridSize / CELLS;

            g.setClip(shape);
            drawGrid(g, gridMargin, gridSize, cell, size, CELLS);
            drawN6(g, gridMargin, cell, size);
        } finally {
            g.dispose();
        }
        return image;
    }

    // 4×4 grid variant tuned for 16–24px title-bar sizes.
    private static Image createSmallIconImage(int size) {
        int cells = 4;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            float margin = Math.max(1.0f, size * 0.06f);
            float outer = size - margin * 2.0f;
            float radius = Math.max(1.5f, size * 0.10f);
            RoundRectangle2D.Float shape = new RoundRectangle2D.Float(margin, margin, outer, outer, radius, radius);

            g.setColor(BACKGROUND);
            g.fill(shape);
            g.setStroke(new BasicStroke(Math.max(0.8f, size * 0.03f)));
            g.setColor(BORDER);
            g.draw(shape);

            float gridMargin = Math.max(1.5f, size * 0.12f);
            float gridSize = size - gridMargin * 2.0f;
            float cell = gridSize / cells;

            g.setClip(shape);
            if (cell >= 3.5f) {
                drawGrid(g, gridMargin, gridSize, cell, size, cells);
            }
            drawN4(g, gridMargin, cell, size);
        } finally {
            g.dispose();
        }
        return image;
    }

    private static void drawGrid(Graphics2D g, float gridMargin, float gridSize, float cell, int size, int cells) {
        g.setColor(GRID_LINE);
        g.setStroke(new BasicStroke(Math.max(0.5f, size * 0.015f)));
        for (int i = 0; i <= cells; i++) {
            float line = gridMargin + i * cell;
            g.drawLine(Math.round(gridMargin), Math.round(line), Math.round(gridMargin + gridSize), Math.round(line));
            g.drawLine(Math.round(line), Math.round(gridMargin), Math.round(line), Math.round(gridMargin + gridSize));
        }
    }

    // "N" in a 6×6 grid — used by sizes ≥ 32.
    private static void drawN6(Graphics2D g, float gridMargin, float cell, int size) {
        int[][] filled = {
                {0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5},
                {1, 1}, {2, 2}, {3, 3}, {4, 4},
                {5, 0}, {5, 1}, {5, 2}, {5, 3}, {5, 4}, {5, 5}
        };
        for (int[] point : filled) {
            Color color = point[0] == 4 && point[1] == 4 ? ACCENT_CELL : FILLED_CELL;
            fillCell(g, gridMargin, cell, point[0], point[1], color, size);
        }
    }

    // "N" in a 4×4 grid — used by sizes ≤ 24. Each cell is ~3 px at 16px,
    // which renders cleanly where the 6×6 version blurs.
    private static void drawN4(Graphics2D g, float gridMargin, float cell, int size) {
        int[][] filled = {
                {0, 0}, {0, 1}, {0, 2}, {0, 3},
                {1, 1}, {2, 2},
                {3, 0}, {3, 1}, {3, 2}, {3, 3}
        };
        for (int[] point : filled) {
            Color color = point[0] == 2 && point[1] == 2 ? ACCENT_CELL : FILLED_CELL;
            fillCell(g, gridMargin, cell, point[0], point[1], color, size);
        }
    }

    private static void fillCell(Graphics2D g, float gridMargin, float cell, int column, int row, Color color, int size) {
        float inset = Math.max(0.5f, size * 0.015f);
        float x = gridMargin + column * cell + inset;
        float y = gridMargin + row * cell + inset;
        float dimension = cell - inset * 2.0f;
        float arc = Math.max(0.5f, size * 0.02f);

        g.setColor(color);
        g.fill(new RoundRectangle2D.Float(x, y, dimension, dimension, arc, arc));
    }
}
