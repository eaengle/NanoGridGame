package my.nanogrid;

import javax.swing.UIManager;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

class ThemeManager {

    private static ColorTheme current = ColorTheme.LIGHT;
    private static Map<String, Object> lightDefaults;

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

    // Applies dark/light color tokens to UIManager so all Swing components
    // update when the caller follows up with SwingUtilities.updateComponentTreeUI.
    static void applySwingTheme(boolean dark) {
        if (lightDefaults == null) {
            lightDefaults = new HashMap<>();
            for (String key : THEME_KEYS) {
                Object val = UIManager.get(key);
                if (val != null) lightDefaults.put(key, val);
            }
        }
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
            for (Map.Entry<String, Object> entry : lightDefaults.entrySet()) {
                UIManager.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
