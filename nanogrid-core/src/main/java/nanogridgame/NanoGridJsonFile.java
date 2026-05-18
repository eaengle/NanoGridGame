package nanogridgame;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class NanoGridJsonFile {

    private static final int SAVE_VERSION = 3;

    private final NanoGridGame game;

    NanoGridJsonFile(NanoGridGame game) {
        this.game = game;
    }

    void serialize(File output) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"format\": \"NanoGridGame\",\n");
        json.append("  \"version\": ").append(SAVE_VERSION).append(",\n");
        json.append("  \"settings\": {\n");
        appendSetting(json, "columns", game.getSettings().getColumns(), true);
        appendSetting(json, "rows", game.getSettings().getRows(), true);
        appendSetting(json, "maxColumnSquares", game.getSettings().getMaxColumnSquares(), true);
        appendSetting(json, "maxRowSquares", game.getSettings().getMaxRowSquares(), true);
        appendSetting(json, "rowBreakChance", game.getSettings().getRowBreakChance(), true);
        appendSetting(json, "difficulty", game.getSettings().getDifficulty().name(), true);
        appendSetting(json, "useSeed", game.getSettings().isUseSeed(), true);
        appendSetting(json, "seed", game.getSettings().getSeed(), true);
        appendSetting(json, "symmetric", game.getSettings().isSymmetric(), false);
        json.append("  },\n");
        appendBoard(json, "board", game.getBoard().getColumns(), true);
        appendBoard(json, "playColumns", game.getPlayColumns(), true);
        appendBoard(json, "playRows", game.getPlayRows(), false);
        json.append("}\n");
        Files.write(output.toPath(), json.toString().getBytes(StandardCharsets.UTF_8));
    }

    void deserialize(File input) throws IOException {
        String json = new String(Files.readAllBytes(input.toPath()), StandardCharsets.UTF_8);
        try {
            validateVersion(json);
            NanoGridParameters settings = deserializeSettings(json);
            game.updateSettings(settings);
            game.setBoard(deserializeBoard(json, "board"));
            game.setPlayColumns(deserializeBoard(json, "playColumns"));
            game.setPlayRows(deserializeBoard(json, "playRows"));
        } catch (RuntimeException ex) {
            throw new IOException("Failed to load JSON game: " + ex.getMessage(), ex);
        }
    }

    private void appendSetting(StringBuilder json, String name, int value, boolean comma) {
        json.append("    \"").append(name).append("\": ").append(value);
        if (comma) {
            json.append(",");
        }
        json.append("\n");
    }

    private void appendSetting(StringBuilder json, String name, long value, boolean comma) {
        json.append("    \"").append(name).append("\": ").append(value);
        if (comma) {
            json.append(",");
        }
        json.append("\n");
    }

    private void appendSetting(StringBuilder json, String name, boolean value, boolean comma) {
        json.append("    \"").append(name).append("\": ").append(value);
        if (comma) {
            json.append(",");
        }
        json.append("\n");
    }

    private void appendSetting(StringBuilder json, String name, String value, boolean comma) {
        json.append("    \"").append(name).append("\": \"").append(escape(value)).append("\"");
        if (comma) {
            json.append(",");
        }
        json.append("\n");
    }

    private void appendBoard(StringBuilder json, String name, char[][] board, boolean comma) {
        json.append("  \"").append(name).append("\": [\n");
        for (int i = 0; i < board.length; i++) {
            json.append("    \"").append(escape(getRowString(board[i]))).append("\"");
            if (i < board.length - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("  ]");
        if (comma) {
            json.append(",");
        }
        json.append("\n");
    }

    private String getRowString(char[] cells) {
        StringBuilder row = new StringBuilder();
        for (char cell : cells) {
            row.append(cell == 0 ? '_' : cell);
        }
        return row.toString();
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private void validateVersion(String json) {
        int version = readInt(json, "version");
        if (version > SAVE_VERSION) {
            throw new IllegalArgumentException("unsupported save version: " + version);
        }
    }

    private NanoGridParameters deserializeSettings(String json) {
        String settingsJson = readObject(json, "settings");
        NanoGridParameters settings = new NanoGridParameters();
        settings.setColumns(readInt(settingsJson, "columns"));
        settings.setRows(readInt(settingsJson, "rows"));
        settings.setMaxColumnSquares(readInt(settingsJson, "maxColumnSquares"));
        settings.setMaxRowSquares(readInt(settingsJson, "maxRowSquares"));
        settings.setRowBreakChance(readInt(settingsJson, "rowBreakChance"));
        settings.setDifficulty(PuzzleDifficulty.valueOf(readString(settingsJson, "difficulty", settings.getDifficulty().name())));
        settings.setSeed(readLong(settingsJson, "seed", settings.getSeed()));
        settings.setUseSeed(readBoolean(settingsJson, "useSeed", settings.isUseSeed()));
        settings.setSymmetric(readBoolean(settingsJson, "symmetric", settings.isSymmetric()));
        return settings;
    }

    private char[][] deserializeBoard(String json, String name) {
        List<String> rows = readStringArray(json, name);
        if (rows.isEmpty()) {
            throw new IllegalArgumentException(name + " is missing or empty");
        }
        char[][] board = new char[rows.size()][rows.get(0).length()];
        for (int i = 0; i < rows.size(); i++) {
            board[i] = rows.get(i).toCharArray();
        }
        return board;
    }

    private int readInt(String json, String name) {
        Matcher matcher = Pattern.compile("\"" + Pattern.quote(name) + "\"\\s*:\\s*(-?\\d+)").matcher(json);
        if (!matcher.find()) {
            throw new IllegalArgumentException(name + " is missing");
        }
        return Integer.parseInt(matcher.group(1));
    }

    private long readLong(String json, String name, long defaultValue) {
        Matcher matcher = Pattern.compile("\"" + Pattern.quote(name) + "\"\\s*:\\s*(-?\\d+)").matcher(json);
        if (!matcher.find()) {
            return defaultValue;
        }
        return Long.parseLong(matcher.group(1));
    }

    private boolean readBoolean(String json, String name, boolean defaultValue) {
        Matcher matcher = Pattern.compile("\"" + Pattern.quote(name) + "\"\\s*:\\s*(true|false)").matcher(json);
        if (!matcher.find()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(matcher.group(1));
    }

    private String readString(String json, String name, String defaultValue) {
        Matcher matcher = Pattern.compile("\"" + Pattern.quote(name) + "\"\\s*:\\s*\"([^\"]*)\"").matcher(json);
        if (!matcher.find()) {
            return defaultValue;
        }
        return matcher.group(1);
    }

    private String readObject(String json, String name) {
        int start = findValueStart(json, name);
        if (start < 0 || json.charAt(start) != '{') {
            throw new IllegalArgumentException(name + " object is missing");
        }
        return json.substring(start, findMatching(json, start, '{', '}') + 1);
    }

    private List<String> readStringArray(String json, String name) {
        int start = findValueStart(json, name);
        if (start < 0 || json.charAt(start) != '[') {
            throw new IllegalArgumentException(name + " array is missing");
        }
        int end = findMatching(json, start, '[', ']');
        String content = json.substring(start + 1, end);
        List<String> values = new ArrayList<>();
        int pos = 0;
        while (pos < content.length()) {
            while (pos < content.length() && Character.isWhitespace(content.charAt(pos))) {
                pos++;
            }
            if (pos < content.length() && content.charAt(pos) == ',') {
                pos++;
                continue;
            }
            if (pos >= content.length()) {
                break;
            }
            if (content.charAt(pos) != '"') {
                throw new IllegalArgumentException(name + " contains a non-string value");
            }
            StringBuilder value = new StringBuilder();
            pos++;
            while (pos < content.length()) {
                char ch = content.charAt(pos++);
                if (ch == '\\') {
                    if (pos >= content.length()) {
                        throw new IllegalArgumentException(name + " contains an invalid escape");
                    }
                    value.append(content.charAt(pos++));
                } else if (ch == '"') {
                    break;
                } else {
                    value.append(ch);
                }
            }
            values.add(value.toString());
        }
        return values;
    }

    private int findValueStart(String json, String name) {
        Matcher matcher = Pattern.compile("\"" + Pattern.quote(name) + "\"\\s*:").matcher(json);
        if (!matcher.find()) {
            return -1;
        }
        int pos = matcher.end();
        while (pos < json.length() && Character.isWhitespace(json.charAt(pos))) {
            pos++;
        }
        return pos;
    }

    private int findMatching(String json, int start, char open, char close) {
        int depth = 0;
        boolean inString = false;
        boolean escaped = false;
        for (int i = start; i < json.length(); i++) {
            char ch = json.charAt(i);
            if (escaped) {
                escaped = false;
            } else if (ch == '\\') {
                escaped = true;
            } else if (ch == '"') {
                inString = !inString;
            } else if (!inString && ch == open) {
                depth++;
            } else if (!inString && ch == close) {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        throw new IllegalArgumentException("unterminated JSON structure");
    }
}
