package my.nanogrid;

import nanogridgame.NanoGridParameters;
import nanogridgame.PuzzleDifficulty;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.WARNING_MESSAGE;

public class NanoGridUI extends JFrame {

    private final JPanel mainPanel = new JPanel(new BorderLayout());
    private final JLabel statusLabel = new JLabel("Ready");
    private final JLabel timerLabel = new JLabel("00:00");
    private final JLabel modeLabel = new JLabel("Cycle");
    private final JComboBox<PuzzleDifficulty> difficultyCombo = new JComboBox<>(PuzzleDifficulty.values());
    private final JCheckBox symmetricCheckBox = new JCheckBox("Symmetric");
    private final JTextField seedField = new JTextField(9);

    private InstructionDialog instructions;
    private GridSizeDialog gridDialog;
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
    private Action optionsAction;
    private Action loadGameAction;
    private Action saveGameAction;
    private Action loadPuzzleAction;
    private Action savePuzzleAction;
    private Action checkAction;
    private Action peekAction;
    private Action showAction;
    private Action instructionsAction;
    private Action aboutAction;
    private Action exitAction;

    public NanoGridUI() {
        initCustom();
    }

    private void initCustom() {
        NanoGridParameters p = new NanoGridParameters();
        p.setColumns(15);
        p.setRows(15);
        p.setMaxColumnSquares(6);
        p.setMaxRowSquares(6);
        p.setRowBreakChance(0);

        controller = new GameController(p);
        gridDialog = new GridSizeDialog(this, true);
        instructions = new InstructionDialog(this, true);

        createActions();
        setTitle("Nano Grid");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                verifyExit();
            }
        });
        setJMenuBar(createMenuBar());
        add(createToolBar(), BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
        installShortcuts();
        setup();
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

    public void reset() {
        if (!applyGenerationControls(controller.getSettings())) {
            return;
        }
        controller.newGame();
        redraw();
        resetSessionStats();
    }

    public void reset(NanoGridParameters newSettings) {
        if (!applyGenerationControls(newSettings)) {
            return;
        }
        controller.setSettings(newSettings);
        controller.newGame();
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
        mainPanel.removeAll();
        mainPanel.add(boardView, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void createActions() {
        newPuzzleAction = new AbstractAction("New") {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        };
        undoAction = new AbstractAction("Undo") {
            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        };
        redoAction = new AbstractAction("Redo") {
            @Override
            public void actionPerformed(ActionEvent e) {
                redo();
            }
        };
        refreshAction = new AbstractAction("Refresh") {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshGame();
            }
        };
        optionsAction = new AbstractAction("Options...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                gridDialog.setUI(NanoGridUI.this);
                gridDialog.setVisible(true);
            }
        };
        loadGameAction = new AbstractAction("Load Game...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGame();
            }
        };
        saveGameAction = new AbstractAction("Save Game...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }
        };
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
        peekAction = new AbstractAction("Peek") {
            @Override
            public void actionPerformed(ActionEvent e) {
                peekGame();
            }
        };
        showAction = new AbstractAction("Show") {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayGame(true);
            }
        };
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
        exitAction = new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                verifyExit();
            }
        };
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        file.add(new JMenuItem(loadGameAction));
        file.add(new JMenuItem(saveGameAction));
        file.add(new JSeparator());
        file.add(new JMenuItem(loadPuzzleAction));
        file.add(new JMenuItem(savePuzzleAction));
        file.add(new JSeparator());
        file.add(new JMenuItem(exitAction));
        menuBar.add(file);

        JMenu edit = new JMenu("Edit");
        edit.add(new JMenuItem(undoAction));
        edit.add(new JMenuItem(redoAction));
        menuBar.add(edit);

        JMenu settings = new JMenu("Settings");
        settings.add(new JMenuItem(optionsAction));
        settings.add(new JMenuItem(refreshAction));
        settings.add(new JMenuItem(newPuzzleAction));
        menuBar.add(settings);

        JMenu hint = new JMenu("Hint");
        hint.add(new JMenuItem(checkAction));
        hint.add(new JMenuItem(peekAction));
        hint.add(new JMenuItem(showAction));
        menuBar.add(hint);

        JMenu help = new JMenu("Help");
        help.add(new JMenuItem(instructionsAction));
        help.add(new JMenuItem(aboutAction));
        menuBar.add(help);

        return menuBar;
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.add(newPuzzleAction);
        toolBar.add(undoAction);
        toolBar.add(redoAction);
        toolBar.add(refreshAction);
        toolBar.addSeparator();
        toolBar.add(new JLabel("Difficulty "));
        toolBar.add(difficultyCombo);
        toolBar.add(symmetricCheckBox);
        toolBar.add(new JLabel("Seed "));
        seedField.setToolTipText("Optional reproducible puzzle seed");
        toolBar.add(seedField);
        JButton applyButton = new JButton("Apply");
        applyButton.setToolTipText("Generate a new puzzle with these options");
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });
        toolBar.add(applyButton);
        toolBar.addSeparator();
        toolBar.add(createModeButton("Cycle", InteractionMode.CYCLE, true));
        toolBar.add(createModeButton("Fill", InteractionMode.FILL, false));
        toolBar.add(createModeButton("Mark", InteractionMode.MARK, false));
        toolBar.add(createModeButton("Erase", InteractionMode.ERASE, false));
        toolBar.addSeparator();
        toolBar.add(checkAction);
        toolBar.add(peekAction);
        toolBar.add(showAction);
        toolBar.addSeparator();
        toolBar.add(saveGameAction);
        toolBar.add(loadGameAction);
        return toolBar;
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
            updateGenerationControls(controller.getSettings());
            redraw();
            resetSessionStats();
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
            updateGenerationControls(controller.getSettings());
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
        statusLabel.setText("Moves: " + moveCount + " (undo)");
        repaintBoard();
    }

    private void redo() {
        if (redoStack.isEmpty()) {
            statusLabel.setText("Nothing to redo");
            return;
        }
        CellMove move = redoStack.pop();
        applyMoveState(move, move.getAfter());
        undoStack.push(move);
        statusLabel.setText("Moves: " + moveCount + " (redo)");
        repaintBoard();
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

    private void repaintBoard() {
        if (boardView != null) {
            boardView.repaint();
        }
    }

    private void winGame() {
        stopTimer();
        statusLabel.setText("Solved in " + timerLabel.getText() + " with " + moveCount + " moves");
        JOptionPane.showMessageDialog(this, "You Won the Game", "You Won!", JOptionPane.OK_OPTION);
        displayGame(true);
    }

    private void recordMove(CellMove move) {
        undoStack.push(move);
        redoStack.clear();
        moveCount++;
        statusLabel.setText("Moves: " + moveCount);
    }

    private void resetSessionStats() {
        moveCount = 0;
        undoStack.clear();
        redoStack.clear();
        statusLabel.setText("Moves: 0");
        startedAt = System.currentTimeMillis();
        timerLabel.setText("00:00");
        startTimer();
        setMode(InteractionMode.CYCLE);
    }

    private boolean applyGenerationControls(NanoGridParameters settings) {
        settings.setDifficulty((PuzzleDifficulty) difficultyCombo.getSelectedItem());
        settings.setSymmetric(symmetricCheckBox.isSelected());
        String seedText = seedField.getText().trim();
        if (seedText.isEmpty()) {
            settings.clearSeed();
            return true;
        }
        try {
            settings.setSeed(Long.parseLong(seedText));
            return true;
        } catch (NumberFormatException ex) {
            showError("Seed must be a whole number.");
            return false;
        }
    }

    private void updateGenerationControls(NanoGridParameters settings) {
        difficultyCombo.setSelectedItem(settings.getDifficulty());
        symmetricCheckBox.setSelected(settings.isSymmetric());
        seedField.setText(settings.isUseSeed() ? String.valueOf(settings.getSeed()) : "");
    }

    private void startTimer() {
        stopTimer();
        timer = new Timer(1000, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsedSeconds = (System.currentTimeMillis() - startedAt) / 1000;
                long minutes = elapsedSeconds / 60;
                long seconds = elapsedSeconds % 60;
                timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
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
        int ans = JOptionPane.showConfirmDialog(this, "Quit? Are you sure?", "Warning",
                OK_CANCEL_OPTION, WARNING_MESSAGE);
        if (ans == OK_OPTION) {
            stopTimer();
            dispose();
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
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
