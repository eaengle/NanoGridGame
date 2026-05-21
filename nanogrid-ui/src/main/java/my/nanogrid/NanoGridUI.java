package my.nanogrid;

import nanogridgame.NanoGridParameters;
import nanogridgame.GameMetadata;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.TransferHandler;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.WARNING_MESSAGE;

public class NanoGridUI extends JFrame {

    private final JPanel mainPanel = new JPanel(new BorderLayout());
    private final JLabel statusLabel = new JLabel("Ready");
    private final JLabel timerLabel = new JLabel("00:00");
    private final JLabel modeLabel = new JLabel("Cycle");
    private InstructionDialog instructions;
    private NewPuzzleDialog newPuzzleDialog;
    private GameController controller;
    private NanoGridBoardView boardView;
    private Timer timer;
    private long startedAt;
    private int moveCount;
    private final Deque<CellMove> undoStack = new ArrayDeque<>();
    private final Deque<CellMove> redoStack = new ArrayDeque<>();

    private Action newPuzzleAction;
    private Action undoAction;
    private Action redoAction;
    private Action refreshAction;
    private Action loadGameAction;
    private Action saveGameAction;
    private Action loadPuzzleAction;
    private Action savePuzzleAction;
    private Action checkAction;
    private Action peekAction;
    private Action showAction;
    private Action instructionsAction;
    private Action aboutAction;
    private Action loadBackgroundAction;
    private Action clearBackgroundAction;

    public NanoGridUI() {
        initCustom();
    }

    private void initCustom() {
        NanoGridParameters p = new NanoGridParameters();
        p.setColumns(15);
        p.setRows(15);

        controller = new GameController(p);
        newPuzzleDialog = new NewPuzzleDialog(this);
        instructions = new InstructionDialog(this, true);

        createActions();
        setTitle("Nano Grid");
        setIconImages(NanoGridIcon.createIconImages());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                verifyExit();
            }
        });
        JPanel toolBarPanel = createToolBarPanel();
        JPanel sidePanel = createSidePanel();

        JPanel cornerPanel = new JPanel(new GridBagLayout());
        cornerPanel.setPreferredSize(new Dimension(
                sidePanel.getPreferredSize().width,
                toolBarPanel.getPreferredSize().height));
        JButton helpButton = new JButton("?");
        helpButton.setToolTipText("Help / About");
        JPopupMenu helpPopup = new JPopupMenu();
        helpPopup.add(new JMenuItem(instructionsAction));
        helpPopup.add(new JMenuItem(aboutAction));
        helpButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                helpPopup.show(helpButton, 0, helpButton.getHeight());
            }
        });
        cornerPanel.add(helpButton);

        JPanel leftColumn = new JPanel(new BorderLayout());
        leftColumn.add(cornerPanel, BorderLayout.NORTH);
        leftColumn.add(sidePanel, BorderLayout.CENTER);

        JPanel rightArea = new JPanel(new BorderLayout());
        rightArea.add(toolBarPanel, BorderLayout.NORTH);
        rightArea.add(mainPanel, BorderLayout.CENTER);

        add(leftColumn, BorderLayout.WEST);
        add(rightArea, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
        installShortcuts();
        installDragAndDrop();
        setup();
    }

    private void installDragAndDrop() {
        TransferHandler handler = new ImageDropHandler();
        getRootPane().setTransferHandler(handler);
    }

    public void setup() {
        controller.newGame();
        installBoardView();
        resetSessionStats();
        setMinimumSize(new Dimension(680, 540));
        setResizable(true);
        pack();
        setLocationRelativeTo(null);
    }

    public void reset(NanoGridParameters newSettings) {
        controller.setSettings(newSettings);
        controller.newGame();
        redraw();
        resetSessionStats();
    }

    void applyAfterGeneration() {
        redraw();
        resetSessionStats();
    }

    void redraw() {
        if (boardView == null) {
            installBoardView();
        }
        boardView.refreshBoard();
        pack();
    }

    public NanoGridParameters getSettings() {
        return controller.getSettings();
    }

    public GameController getController() {
        return controller;
    }

    private void installBoardView() {
        boardView = new NanoGridBoardView(controller, new Runnable() {
            @Override
            public void run() {
                winGame();
            }
        }, new CellMoveListener() {
            @Override
            public void cellChanged(CellMove move) {
                recordMove(move);
            }
        });
        boardView.setTransferHandler(new ImageDropHandler());
        mainPanel.removeAll();
        mainPanel.add(boardView, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void createActions() {
        newPuzzleAction = new AbstractAction("New") {
            @Override
            public void actionPerformed(ActionEvent e) {
                newPuzzleDialog.setUI(NanoGridUI.this);
                newPuzzleDialog.setLocationRelativeTo(NanoGridUI.this);
                newPuzzleDialog.setVisible(true);
            }
        };
        newPuzzleAction.putValue(Action.SHORT_DESCRIPTION, "New Puzzle (Ctrl+N)");
        undoAction = new AbstractAction("Undo") {
            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        };
        undoAction.putValue(Action.SHORT_DESCRIPTION, "Undo (Ctrl+Z)");
        undoAction.setEnabled(false);
        redoAction = new AbstractAction("Redo") {
            @Override
            public void actionPerformed(ActionEvent e) {
                redo();
            }
        };
        redoAction.putValue(Action.SHORT_DESCRIPTION, "Redo (Ctrl+Y)");
        redoAction.setEnabled(false);
        refreshAction = new AbstractAction("Refresh") {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshGame();
            }
        };
        refreshAction.putValue(Action.SHORT_DESCRIPTION, "Clear board progress (Ctrl+R)");
        loadGameAction = new AbstractAction("Load Game...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGame();
            }
        };
        loadGameAction.putValue(Action.SHORT_DESCRIPTION, "Load saved game (Ctrl+O)");
        saveGameAction = new AbstractAction("Save Game...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }
        };
        saveGameAction.putValue(Action.SHORT_DESCRIPTION, "Save game (Ctrl+S)");
        loadPuzzleAction = new AbstractAction("Load Puzzle...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPuzzle();
            }
        };
        savePuzzleAction = new AbstractAction("Save Puzzle...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePuzzle();
            }
        };
        checkAction = new AbstractAction("Check") {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkGame();
            }
        };
        checkAction.putValue(Action.SHORT_DESCRIPTION, "Count incorrect moves (Ctrl+K)");
        peekAction = new AbstractAction("Peek") {
            @Override
            public void actionPerformed(ActionEvent e) {
                peekGame();
            }
        };
        peekAction.putValue(Action.SHORT_DESCRIPTION, "Briefly reveal solution for 1 second");
        showAction = new AbstractAction("Show") {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayGame(true);
            }
        };
        showAction.putValue(Action.SHORT_DESCRIPTION, "Reveal the full solution");
        instructionsAction = new AbstractAction("Instructions") {
            @Override
            public void actionPerformed(ActionEvent e) {
                instructions.setVisible(true);
            }
        };
        aboutAction = new AbstractAction("About") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(NanoGridUI.this,
                        "Nano Grid\n by Eric Engle",
                        "Nano Grid",
                        JOptionPane.PLAIN_MESSAGE);
            }
        };
        loadBackgroundAction = new AbstractAction("Load Background...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBackgroundImage();
            }
        };
        loadBackgroundAction.putValue(Action.SHORT_DESCRIPTION, "Load a background image for the margin area");
        clearBackgroundAction = new AbstractAction("Clear Background") {
            @Override
            public void actionPerformed(ActionEvent e) {
                BackgroundImageManager.clearImage();
                if (boardView != null) {
                    boardView.repaint();
                }
            }
        };
        clearBackgroundAction.putValue(Action.SHORT_DESCRIPTION, "Remove the background image");
    }

    private void loadBackgroundImage() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Image Files", "png", "jpg", "jpeg", "gif", "bmp");
        chooser.setFileFilter(filter);
        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = chooser.getSelectedFile();
        if (!BackgroundImageManager.loadFile(file)) {
            showError("Could not load image: " + file.getName());
            return;
        }
        if (boardView != null) {
            boardView.repaint();
        }
    }

    private JPanel createToolBarPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createPlayToolBar());
        return panel;
    }

    private JToolBar createPlayToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.add(newPuzzleAction);
        toolBar.add(undoAction);
        toolBar.add(redoAction);
        toolBar.add(refreshAction);
        toolBar.addSeparator();
        toolBar.add(createPopupButton("Save", "Save game or puzzle", saveGameAction, savePuzzleAction));
        toolBar.add(createPopupButton("Load", "Load game or puzzle", loadGameAction, loadPuzzleAction));
        toolBar.addSeparator();
        toolBar.add(createPopupButton("Background", "Background image", loadBackgroundAction, clearBackgroundAction));
        return toolBar;
    }

    private JButton createPopupButton(String label, String tooltip, Action... actions) {
        JButton button = new JButton(label);
        button.setToolTipText(tooltip);
        JPopupMenu popup = new JPopupMenu();
        for (Action action : actions) {
            popup.add(new JMenuItem(action));
        }
        button.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popup.show(button, 0, button.getHeight());
            }
        });
        return button;
    }

    private JPanel createSidePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 4, 6, 4));

        addSideButton(panel, createModeButton("Cycle", InteractionMode.CYCLE, true));
        addSideButton(panel, createModeButton("Fill", InteractionMode.FILL, false));
        addSideButton(panel, createModeButton("Mark", InteractionMode.MARK, false));
        addSideButton(panel, createModeButton("Erase", InteractionMode.ERASE, false));

        JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        panel.add(javax.swing.Box.createVerticalStrut(6));
        panel.add(sep);
        panel.add(javax.swing.Box.createVerticalStrut(6));

        addSideButton(panel, new JButton(checkAction));
        addSideButton(panel, new JButton(peekAction));
        addSideButton(panel, new JButton(showAction));

        JSeparator sep2 = new JSeparator(JSeparator.HORIZONTAL);
        sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        panel.add(javax.swing.Box.createVerticalStrut(6));
        panel.add(sep2);
        panel.add(javax.swing.Box.createVerticalStrut(6));

        JToggleButton darkButton = new JToggleButton("Dark");
        darkButton.setSelected(ThemeManager.isDark());
        darkButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean dark = darkButton.isSelected();
                ThemeManager.setDark(dark);
                ThemeManager.applySwingTheme(dark);
                SwingUtilities.updateComponentTreeUI(NanoGridUI.this);
            }
        });
        addSideButton(panel, darkButton);

        return panel;
    }

    private void addSideButton(JPanel panel, JComponent button) {
        button.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
        panel.add(button);
        panel.add(javax.swing.Box.createVerticalStrut(2));
    }

    private JToggleButton createModeButton(String text, final InteractionMode mode, boolean selected) {
        JToggleButton button = new JToggleButton(text);
        button.setSelected(selected);
        button.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMode(mode);
            }
        });
        ButtonGroup group = getModeButtonGroup();
        group.add(button);
        return button;
    }

    private ButtonGroup modeButtonGroup;

    private ButtonGroup getModeButtonGroup() {
        if (modeButtonGroup == null) {
            modeButtonGroup = new ButtonGroup();
        }
        return modeButtonGroup;
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 3));
        left.add(statusLabel);
        left.add(new JLabel("Mode:"));
        left.add(modeLabel);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 3));
        right.add(new JLabel("Time:"));
        right.add(timerLabel);
        statusBar.add(left, BorderLayout.WEST);
        statusBar.add(right, BorderLayout.EAST);
        return statusBar;
    }

    private void installShortcuts() {
        bind(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK, "new", newPuzzleAction);
        bind(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK, "undo", undoAction);
        bind(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK, "redo", redoAction);
        bind(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK, "load", loadGameAction);
        bind(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK, "save", saveGameAction);
        bind(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK, "refresh", refreshAction);
        bind(KeyEvent.VK_K, InputEvent.CTRL_DOWN_MASK, "check", checkAction);
        bind(KeyEvent.VK_1, 0, "cycleMode", modeAction(InteractionMode.CYCLE));
        bind(KeyEvent.VK_2, 0, "fillMode", modeAction(InteractionMode.FILL));
        bind(KeyEvent.VK_3, 0, "markMode", modeAction(InteractionMode.MARK));
        bind(KeyEvent.VK_4, 0, "eraseMode", modeAction(InteractionMode.ERASE));
    }

    private void bind(int keyCode, int modifiers, String name, Action action) {
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(keyCode, modifiers), name);
        getRootPane().getActionMap().put(name, action);
    }

    private Action modeAction(final InteractionMode mode) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMode(mode);
            }
        };
    }

    private void setMode(InteractionMode mode) {
        if (boardView != null) {
            boardView.setInteractionMode(mode);
        }
        modeLabel.setText(modeName(mode));
        updateModeButtons(mode);
    }

    private void updateModeButtons(InteractionMode mode) {
        java.util.Enumeration<javax.swing.AbstractButton> buttons = getModeButtonGroup().getElements();
        while (buttons.hasMoreElements()) {
            javax.swing.AbstractButton button = buttons.nextElement();
            button.setSelected(modeName(mode).equals(button.getText()));
        }
    }

    private String modeName(InteractionMode mode) {
        if (mode == InteractionMode.FILL) {
            return "Fill";
        }
        if (mode == InteractionMode.MARK) {
            return "Mark";
        }
        if (mode == InteractionMode.ERASE) {
            return "Erase";
        }
        return "Cycle";
    }

    private void loadGame() {
        File loadFile = getBoardFile();
        if (loadFile == null) {
            return;
        }
        try {
            controller.loadGame(loadFile);
            redraw();
            restoreSessionStats(controller.getMetadata());
            statusLabel.setText("Loaded game");
        } catch (IOException ex) {
            showError("Failed to load game: " + ex.getMessage());
        }
    }

    private void loadPuzzle() {
        File loadFile = getBoardFile();
        if (loadFile == null) {
            return;
        }
        try {
            controller.loadPuzzle(loadFile);
            redraw();
            resetSessionStats();
            statusLabel.setText("Loaded puzzle");
        } catch (IOException ex) {
            showError("Failed to load puzzle: " + ex.getMessage());
        }
    }

    private void saveGame() {
        saveBoardFile(true);
    }

    private void savePuzzle() {
        saveBoardFile(false);
    }

    private void saveBoardFile(boolean includeProgress) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Nanogrid Save Files", "json", "xml");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String ext = getFileExtension(file).toLowerCase();
            if (!ext.equals("json") && !ext.equals("xml")) {
                file = new File(chooser.getCurrentDirectory(), file.getName() + ".json");
            }
            try {
                updateGameMetadata();
                if (includeProgress) {
                    controller.saveGame(file);
                    statusLabel.setText("Saved game");
                } else {
                    controller.savePuzzle(file);
                    statusLabel.setText("Saved puzzle");
                }
            } catch (IOException ex) {
                showError("Failed to save: " + ex.getMessage());
            }
        }
    }

    private File getBoardFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Nanogrid Save Files", "json", "xml");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    public String getFileExtension(File file) {
        String name = file.getName();
        String fileExtension = "";
        int extensionIndex = name.lastIndexOf(".");
        if (extensionIndex >= 0 && extensionIndex < name.length() - 1) {
            fileExtension = name.substring(extensionIndex + 1);
        }
        return fileExtension;
    }

    private void checkGame() {
        int cnt = controller.getIncorrectMoves();
        JOptionPane.showMessageDialog(this,
                String.format("You have %d incorrect move(s).", cnt),
                "Puzzle Check",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void peekGame() {
        displayGame(true);
        Timer t = new Timer(1000, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                displayGame(false);
            }
        });
        t.setRepeats(false);
        t.start();
    }

    public void displayGame(boolean show) {
        if (boardView != null) {
            boardView.setShowSolution(show);
        }
    }

    private void refreshGame() {
        if (moveCount > 0) {
            int ans = JOptionPane.showConfirmDialog(this,
                    "Clear all progress on this puzzle?", "Refresh",
                    OK_CANCEL_OPTION, WARNING_MESSAGE);
            if (ans != OK_OPTION) {
                return;
            }
        }
        for (int c = 0; c < getSettings().getColumns(); c++) {
            for (int r = 0; r < getSettings().getRows(); r++) {
                controller.clearCell(c, r);
            }
        }
        if (boardView != null) {
            boardView.refreshBoard();
        }
        resetSessionStats();
        statusLabel.setText("Board cleared");
    }

    private void undo() {
        if (undoStack.isEmpty()) {
            statusLabel.setText("Nothing to undo");
            return;
        }
        CellMove move = undoStack.pop();
        applyMoveState(move, move.getBefore());
        redoStack.push(move);
        moveCount = Math.max(0, moveCount - 1);
        updateUndoRedoState();
        updateAfterUndoRedo(" (undo)");
    }

    private void redo() {
        if (redoStack.isEmpty()) {
            statusLabel.setText("Nothing to redo");
            return;
        }
        CellMove move = redoStack.pop();
        applyMoveState(move, move.getAfter());
        undoStack.push(move);
        moveCount++;
        updateUndoRedoState();
        updateAfterUndoRedo(" (redo)");
    }

    private void updateUndoRedoState() {
        undoAction.setEnabled(!undoStack.isEmpty());
        redoAction.setEnabled(!redoStack.isEmpty());
    }

    private void applyMoveState(CellMove move, char state) {
        if (state == nanogridgame.NanoGridBoard.FillChar) {
            controller.setCell(move.getColumn(), move.getRow());
        } else if (state == nanogridgame.NanoGridBoard.MarkChar) {
            controller.setMark(move.getColumn(), move.getRow());
        } else {
            controller.clearCell(move.getColumn(), move.getRow());
        }
    }

    private void updateAfterUndoRedo(String statusSuffix) {
        boolean newlySolved = false;
        if (boardView != null) {
            newlySolved = boardView.refreshAfterProgrammaticMove();
        }
        if (newlySolved) {
            winGame();
            return;
        }
        boolean solved = controller.checkWin();
        if (solved) {
            statusLabel.setText("Solved in " + timerLabel.getText() + " with " + moveCount + " moves");
            return;
        }
        if (timer == null) {
            startTimer();
        }
        statusLabel.setText("Moves: " + moveCount + statusSuffix);
    }

    private void winGame() {
        stopTimer();
        statusLabel.setText("Solved in " + timerLabel.getText() + " with " + moveCount + " moves");
        displayGame(true);
    }

    private void recordMove(CellMove move) {
        undoStack.push(move);
        redoStack.clear();
        moveCount++;
        updateUndoRedoState();
        statusLabel.setText("Moves: " + moveCount);
    }

    private void resetSessionStats() {
        moveCount = 0;
        undoStack.clear();
        redoStack.clear();
        updateUndoRedoState();
        statusLabel.setText("Moves: 0");
        startedAt = System.currentTimeMillis();
        timerLabel.setText("00:00");
        startTimer();
        setMode(InteractionMode.CYCLE);
    }

    private void restoreSessionStats(GameMetadata metadata) {
        moveCount = metadata.getMoveCount();
        undoStack.clear();
        redoStack.clear();
        long elapsedSeconds = metadata.getElapsedSeconds();
        startedAt = System.currentTimeMillis() - elapsedSeconds * 1000;
        timerLabel.setText(formatElapsed(elapsedSeconds));
        startTimer();
        setMode(InteractionMode.CYCLE);
        statusLabel.setText("Moves: " + moveCount);
    }

    private void updateGameMetadata() {
        GameMetadata metadata = controller.getMetadata();
        metadata.setElapsedSeconds(getElapsedSeconds());
        metadata.setMoveCount(moveCount);
        controller.setMetadata(metadata);
    }

    private long getElapsedSeconds() {
        return Math.max(0, (System.currentTimeMillis() - startedAt) / 1000);
    }

    private String formatElapsed(long elapsedSeconds) {
        long minutes = elapsedSeconds / 60;
        long seconds = elapsedSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void startTimer() {
        stopTimer();
        timer = new Timer(1000, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsedSeconds = (System.currentTimeMillis() - startedAt) / 1000;
                timerLabel.setText(formatElapsed(elapsedSeconds));
            }
        });
        timer.start();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    private void verifyExit() {
        String msg = moveCount > 0
                ? "You have unsaved progress (" + moveCount + " moves). Quit anyway?"
                : "Quit? Are you sure?";
        int ans = JOptionPane.showConfirmDialog(this, msg, "Quit",
                OK_CANCEL_OPTION, WARNING_MESSAGE);
        if (ans == OK_OPTION) {
            stopTimer();
            dispose();
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private class ImageDropHandler extends TransferHandler {
        private final Set<String> imageExts = new HashSet<>(
                Arrays.asList("png", "jpg", "jpeg", "gif", "bmp"));

        @Override
        public boolean canImport(TransferSupport support) {
            if (!support.isDrop()) return false;
            return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean importData(TransferSupport support) {
            if (!canImport(support)) return false;
            try {
                List<File> files = (List<File>) support.getTransferable()
                        .getTransferData(DataFlavor.javaFileListFlavor);
                if (files.isEmpty()) return false;
                File file = files.get(0);
                String name = file.getName();
                int dot = name.lastIndexOf('.');
                if (dot < 0) return false;
                String ext = name.substring(dot + 1).toLowerCase();
                if (!imageExts.contains(ext)) return false;
                if (!BackgroundImageManager.loadFile(file)) return false;
                if (boardView != null) boardView.repaint();
                return true;
            } catch (UnsupportedFlavorException | IOException e) {
                return false;
            }
        }
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
            // The default look and feel is fine if Nimbus is unavailable.
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NanoGridUI().setVisible(true);
            }
        });
    }
}
