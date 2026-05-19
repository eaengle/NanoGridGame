package my.nanogrid;

import java.awt.Color;

class ColorTheme {

    final Color background;
    final Color clueBackground;
    final Color gridLine;
    final Color gridLineStrong;
    final Color fill;
    final Color mark;
    final Color hover;
    final Color measure;
    final Color correctReveal;
    final Color missedReveal;
    final Color text;
    final Color satisfiedText;
    final Color cursor;
    final Color measureText;

    private ColorTheme(Color background, Color clueBackground, Color gridLine, Color gridLineStrong,
            Color fill, Color mark, Color hover, Color measure,
            Color correctReveal, Color missedReveal, Color text, Color satisfiedText,
            Color cursor, Color measureText) {
        this.background = background;
        this.clueBackground = clueBackground;
        this.gridLine = gridLine;
        this.gridLineStrong = gridLineStrong;
        this.fill = fill;
        this.mark = mark;
        this.hover = hover;
        this.measure = measure;
        this.correctReveal = correctReveal;
        this.missedReveal = missedReveal;
        this.text = text;
        this.satisfiedText = satisfiedText;
        this.cursor = cursor;
        this.measureText = measureText;
    }

    static final ColorTheme LIGHT = new ColorTheme(
            new Color(244, 246, 248),
            new Color(232, 236, 241),
            new Color(178, 187, 198),
            new Color(96, 111, 128),
            new Color(35, 44, 54),
            new Color(197, 74, 72),
            new Color(216, 232, 246),
            new Color(93, 173, 226),
            new Color(52, 151, 93),
            new Color(34, 45, 56),
            new Color(42, 50, 60),
            new Color(137, 143, 150),
            new Color(50, 100, 200),
            Color.WHITE
    );

    static final ColorTheme DARK = new ColorTheme(
            new Color(28, 31, 35),
            new Color(20, 23, 27),
            new Color(52, 62, 75),
            new Color(80, 95, 112),
            new Color(200, 210, 228),
            new Color(215, 85, 83),
            new Color(38, 55, 78),
            new Color(58, 128, 188),
            new Color(50, 170, 95),
            new Color(85, 100, 118),
            new Color(190, 202, 218),
            new Color(105, 112, 122),
            new Color(90, 150, 230),
            new Color(215, 225, 242)
    );
}
