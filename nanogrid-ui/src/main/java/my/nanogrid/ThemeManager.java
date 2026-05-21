package my.nanogrid;

import javax.swing.plaf.ColorUIResource;
import javax.swing.UIManager;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

class ThemeManager {

    private static ColorTheme current = ColorTheme.LIGHT;

    private static final String[] THEME_KEYS = {
        "nimbusBase", "nimbusBlueGrey", "control",
        "text", "controlText", "infoText", "menuText", "toolTipText",
        "nimbusLightBackground", "nimbusFocus",
        "nimbusSelectedText", "nimbusSelectionBackground", "info",
        "Button.background", "Button.foreground",
        "ToggleButton.background", "ToggleButton.foreground"
    };
    private static final Map<String, Color> LIGHT_DEFAULTS = captureLightDefaults();

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
            UIManager.put("nimbusBase",                color(24, 34, 48));
            UIManager.put("nimbusBlueGrey",            color(42, 50, 60));
            UIManager.put("control",                   color(42, 50, 60));
            UIManager.put("text",                      color(190, 202, 218));
            UIManager.put("controlText",               color(190, 202, 218));
            UIManager.put("infoText",                  color(190, 202, 218));
            UIManager.put("menuText",                  color(190, 202, 218));
            UIManager.put("toolTipText",               color(190, 202, 218));
            UIManager.put("nimbusLightBackground",     color(28, 31, 35));
            UIManager.put("nimbusFocus",               color(90, 150, 230));
            UIManager.put("nimbusSelectedText",        color(215, 225, 242));
            UIManager.put("nimbusSelectionBackground", color(38, 55, 78));
            UIManager.put("info",                      color(20, 23, 27));
            UIManager.put("Button.background",         color(42, 50, 60));
            UIManager.put("Button.foreground",         color(190, 202, 218));
            UIManager.put("ToggleButton.background",   color(42, 50, 60));
            UIManager.put("ToggleButton.foreground",   color(190, 202, 218));
        } else {
            for (String key : THEME_KEYS) {
                Color color = LIGHT_DEFAULTS.get(key);
                UIManager.put(key, color == null ? null : new ColorUIResource(color));
            }
        }
        // Nimbus caches derived colors in NimbusStyle objects. Installing a
        // fresh L&F instance clears that cache so the next updateComponentTreeUI
        // picks up the correct colors.
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel().getClass().getName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 javax.swing.UnsupportedLookAndFeelException ignored) {}
    }

    private static Map<String, Color> captureLightDefaults() {
        Map<String, Color> defaults = new HashMap<>();
        for (String key : THEME_KEYS) {
            Color color = UIManager.getColor(key);
            defaults.put(key, color == null ? null : new Color(color.getRGB(), true));
        }
        return defaults;
    }

    private static ColorUIResource color(int red, int green, int blue) {
        return new ColorUIResource(red, green, blue);
    }
}
