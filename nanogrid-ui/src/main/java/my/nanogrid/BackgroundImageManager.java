package my.nanogrid;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class BackgroundImageManager {

    private static final String PREFS_DIR = System.getProperty("user.home") + File.separator + ".nanogrid";
    private static final String PREFS_FILE = PREFS_DIR + File.separator + "prefs.properties";
    private static final String KEY_BG_IMAGE = "backgroundImage";
    private static final String VALUE_BUNDLED = "bundled";

    private static BufferedImage currentImage;
    private static String currentPath;

    static {
        loadFromPrefs();
    }

    private static void loadFromPrefs() {
        File prefsFile = new File(PREFS_FILE);
        if (!prefsFile.exists()) {
            loadBundled();
            return;
        }
        Properties props = new Properties();
        try (InputStream in = new FileInputStream(prefsFile)) {
            props.load(in);
        } catch (IOException e) {
            loadBundled();
            return;
        }
        String path = props.getProperty(KEY_BG_IMAGE);
        if (path == null || path.isEmpty() || VALUE_BUNDLED.equals(path)) {
            loadBundled();
            return;
        }
        File imageFile = new File(path);
        if (!imageFile.exists() || !readFile(imageFile)) {
            loadBundled();
        }
    }

    private static void loadBundled() {
        try (InputStream in = BackgroundImageManager.class.getResourceAsStream("/images/background.png")) {
            if (in != null) {
                currentImage = ImageIO.read(in);
                currentPath = VALUE_BUNDLED;
            }
        } catch (IOException ignored) {}
    }

    static boolean loadFile(File file) {
        if (!readFile(file)) return false;
        currentPath = file.getAbsolutePath();
        savePrefs();
        return true;
    }

    private static boolean readFile(File file) {
        try {
            BufferedImage img = ImageIO.read(file);
            if (img == null) return false;
            currentImage = img;
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    static void clearImage() {
        currentImage = null;
        currentPath = null;
        savePrefs();
    }

    static BufferedImage getImage() {
        return currentImage;
    }

    static boolean hasImage() {
        return currentImage != null;
    }

    private static void savePrefs() {
        Properties props = new Properties();
        if (currentPath != null) {
            props.setProperty(KEY_BG_IMAGE, currentPath);
        }
        new File(PREFS_DIR).mkdirs();
        try (FileOutputStream out = new FileOutputStream(PREFS_FILE)) {
            props.store(out, "NanoGrid preferences");
        } catch (IOException ignored) {}
    }
}
