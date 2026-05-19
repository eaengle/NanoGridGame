package my.nanogrid;

class ThemeManager {

    private static ColorTheme current = ColorTheme.LIGHT;

    static ColorTheme current() {
        return current;
    }

    static boolean isDark() {
        return current == ColorTheme.DARK;
    }

    static void setDark(boolean dark) {
        current = dark ? ColorTheme.DARK : ColorTheme.LIGHT;
    }
}
