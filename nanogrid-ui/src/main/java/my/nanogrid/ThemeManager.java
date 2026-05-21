package my.nanogrid;

import javax.swing.UIManager;
import java.awt.Color;

class ThemeManager {

    private static ColorTheme current = ColorTheme.LIGHT;

    private static final String[] THEME_KEYS = {
        "nimbusBase", "nimbusBlueGrey", "control",
        "text", "controlText", "infoText", "menuText", "toolTipText",
        "nimbusLightBackground", "nimbusFocus",
        "nimbusSelectedText", "nimbusSelectionBackground", "info"
    };

    static ColorTheme current() {
        return current;
    }

    static boolean isDark() {
        return current == ColorTheme.DARK;
    }

    static void setDark(boolean dark) {
        current = dark ? ColorTheme.DARK : ColorTheme.LIGHT;
    }

    static void applySwingTheme(boolean dark) {
        if (dark) {
            UIManager.put("nimbusBase",                new Color(24, 34, 48));
            UIManager.put("nimbusBlueGrey",            new Color(42, 50, 60));
            UIManager.put("control",                   new Color(42, 50, 60));
            UIManager.put("text",                      new Color(190, 202, 218));
            UIManager.put("controlText",               new Color(190, 202, 218));
            UIManager.put("infoText",                  new Color(190, 202, 218));
            UIManager.put("menuText",                  new Color(190, 202, 218));
            UIManager.put("toolTipText",               new Color(190, 202, 218));
            UIManager.put("nimbusLightBackground",     new Color(28, 31, 35));
            UIManager.put("nimbusFocus",               new Color(90, 150, 230));
            UIManager.put("nimbusSelectedText",        new Color(215, 225, 242));
            UIManager.put("nimbusSelectionBackground", new Color(38, 55, 78));
            UIManager.put("info",                      new Color(20, 23, 27));
        } else {
            for (String key : THEME_KEYS) {
                UIManager.put(key, null);
            }
        }
        // Nimbus caches derived colors in NimbusStyle objects. Reinstalling the
        // same L&F instance clears that cache so the next updateComponentTreeUI
        // picks up the correct colors.
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (javax.swing.UnsupportedLookAndFeelException ignored) {}
    }
}
