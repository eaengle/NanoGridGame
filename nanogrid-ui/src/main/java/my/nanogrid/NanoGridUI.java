package my.nanogrid;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.Timer;
import javax.swing.UIDefaults;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import nanogridgame.NanoGridBoard;
import nanogridgame.NanoGridParameters;

public class NanoGridUI extends javax.swing.JFrame {

    private static final Logger LOG = Logger.getLogger(NanoGridUI.class.getName());

    public NanoGridUI() {
        initComponents();
        initCustom();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        menuBarMain = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuLoadGame = new javax.swing.JMenuItem();
        menuSaveGame = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuLoadPuzzle = new javax.swing.JMenuItem();
        menuSavePuzzle = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        menuExit = new javax.swing.JMenuItem();
        menuSettings = new javax.swing.JMenu();
        menuOptions = new javax.swing.JMenuItem();
        menuRefresh = new javax.swing.JMenuItem();
        menuNewPuzzle = new javax.swing.JMenuItem();
        menuHint = new javax.swing.JMenu();
        menuCheck = new javax.swing.JMenuItem();
        menuPeek = new javax.swing.JMenuItem();
        menuShow = new javax.swing.JMenuItem();
        menuHelp = new javax.swing.JMenu();
        menuInstructions = new javax.swing.JMenuItem();
        menuAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Nano Grid");
        setResizable(false);
        setSize(new java.awt.Dimension(100, 100));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { formMouseEntered(evt); }
            public void mouseExited(java.awt.event.MouseEvent evt) { formMouseExited(evt); }
            public void mousePressed(java.awt.event.MouseEvent evt) { formMousePressed(evt); }
            public void mouseReleased(java.awt.event.MouseEvent evt) { formMouseReleased(evt); }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) { formWindowClosing(evt); }
        });

        mainPanel.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 381, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );

        menuFile.setText("File");
        menuLoadGame.setText("Load Game...");
        menuLoadGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { menuLoadGameActionPerformed(evt); }
        });
        menuFile.add(menuLoadGame);
        menuSaveGame.setText("Save Game...");
        menuSaveGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { menuSaveGameActionPerformed(evt); }
        });
        menuFile.add(menuSaveGame);
        menuFile.add(jSeparator1);
        menuLoadPuzzle.setText("Load Puzzle..");
        menuLoadPuzzle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { menuLoadPuzzleActionPerformed(evt); }
        });
        menuFile.add(menuLoadPuzzle);
        menuSavePuzzle.setText("Save Puzzle...");
        menuSavePuzzle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { menuSavePuzzleActionPerformed(evt); }
        });
        menuFile.add(menuSavePuzzle);
        menuFile.add(jSeparator2);
        menuExit.setText("Exit");
        menuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { menuExitActionPerformed(evt); }
        });
        menuFile.add(menuExit);
        menuBarMain.add(menuFile);

        menuSettings.setText("Settings");
        menuOptions.setText("Grid Options...");
        menuOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { menuOptionsActionPerformed(evt); }
        });
        menuSettings.add(menuOptions);
        menuRefresh.setText("Refresh Puzzle");
        menuRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { menuRefreshActionPerformed(evt); }
        });
        menuSettings.add(menuRefresh);
        menuNewPuzzle.setText("New Puzzle");
        menuNewPuzzle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { menuNewPuzzleActionPerformed(evt); }
        });
        menuSettings.add(menuNewPuzzle);
        menuBarMain.add(menuSettings);

        menuHint.setText("Hint");
        menuCheck.setText("Check");
        menuCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { menuCheckActionPerformed(evt); }
        });
        menuHint.add(menuCheck);
        menuPeek.setText("Peek");
        menuPeek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { menuPeekActionPerformed(evt); }
        });
        menuHint.add(menuPeek);
        menuShow.setText("Show");
        menuShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { menuShowActionPerformed(evt); }
        });
        menuHint.add(menuShow);
        menuBarMain.add(menuHint);

        menuHelp.setText("Help");
        menuInstructions.setText("Instructions");
        menuInstructions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { menuInstructionsActionPerformed(evt); }
        });
        menuHelp.add(menuInstructions);
        menuAbout.setText("About");
        menuAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { menuAboutActionPerformed(evt); }
        });
        menuHelp.add(menuAbout);
        menuBarMain.add(menuHelp);

        setJMenuBar(menuBarMain);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private GridSizeDialog gridDialog;

    private void menuOptionsActionPerformed(java.awt.event.ActionEvent evt) {
        gridDialog.setUI(this);
        gridDialog.setVisible(true);
    }

    private void menuLoadPuzzleActionPerformed(java.awt.event.ActionEvent evt) {
        File loadFile = getBoardFile();
        if (loadFile == null) return;
        try {
            controller.loadPuzzle(loadFile);
            redraw();
        } catch (IOException ex) {
            showError("Failed to load puzzle: " + ex.getMessage());
        }
    }

    private void menuExitActionPerformed(java.awt.event.ActionEvent evt) {
        verifyExit();
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        verifyExit();
    }

    private void menuSaveGameActionPerformed(java.awt.event.ActionEvent evt) {
        saveGame();
    }

    private void menuLoadGameActionPerformed(java.awt.event.ActionEvent evt) {
        File loadFile = getBoardFile();
        if (loadFile == null) return;
        try {
            controller.loadGame(loadFile);
            redraw();
            placeMarks();
        } catch (IOException ex) {
            showError("Failed to load game: " + ex.getMessage());
        }
    }

    private void menuSavePuzzleActionPerformed(java.awt.event.ActionEvent evt) {
        savePuzzle();
    }

    private void menuRefreshActionPerformed(java.awt.event.ActionEvent evt) {
        refreshGame();
    }

    private void formMouseEntered(java.awt.event.MouseEvent evt) {}

    private void formMouseExited(java.awt.event.MouseEvent evt) {}

    private boolean mouseDown = false;

    private void formMousePressed(java.awt.event.MouseEvent evt) {
        mouseDown = true;
    }

    private void formMouseReleased(java.awt.event.MouseEvent evt) {
        mouseDown = false;
    }

    private void menuNewPuzzleActionPerformed(java.awt.event.ActionEvent evt) {
        reset();
    }

    private void menuCheckActionPerformed(java.awt.event.ActionEvent evt) {
        int cnt = controller.getIncorrectMoves();
        JOptionPane.showMessageDialog(this,
                String.format("You have %d incorrect move(s).", cnt),
                "Puzzle Check",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void menuShowActionPerformed(java.awt.event.ActionEvent evt) {
        displayGame(true);
    }

    private void menuPeekActionPerformed(java.awt.event.ActionEvent evt) {
        revealGame();
        int delay = 1000;
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                placeMarks();
            }
        };
        Timer t = new Timer(delay, taskPerformer);
        t.setRepeats(false);
        t.start();
    }

    private void menuAboutActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane.showMessageDialog(this,
                "Nano Grid\n by Eric Engle",
                "Nano Grid",
                JOptionPane.PLAIN_MESSAGE);
    }

    private void menuInstructionsActionPerformed(java.awt.event.ActionEvent evt) {
        instructions.setVisible(true);
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException |
                 IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NanoGridUI.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NanoGridUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuItem menuAbout;
    private javax.swing.JMenuBar menuBarMain;
    private javax.swing.JMenuItem menuCheck;
    private javax.swing.JMenuItem menuExit;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenu menuHelp;
    private javax.swing.JMenu menuHint;
    private javax.swing.JMenuItem menuInstructions;
    private javax.swing.JMenuItem menuLoadGame;
    private javax.swing.JMenuItem menuLoadPuzzle;
    private javax.swing.JMenuItem menuNewPuzzle;
    private javax.swing.JMenuItem menuOptions;
    private javax.swing.JMenuItem menuPeek;
    private javax.swing.JMenuItem menuRefresh;
    private javax.swing.JMenuItem menuSaveGame;
    private javax.swing.JMenuItem menuSavePuzzle;
    private javax.swing.JMenu menuSettings;
    private javax.swing.JMenuItem menuShow;
    // End of variables declaration//GEN-END:variables

    private void setColor(JTextPane pane, Color bgColor) {
        UIDefaults defaults = new UIDefaults();
        defaults.put("TextPane[Enabled].backgroundPainter", bgColor);
        pane.putClientProperty("Nimbus.Overrides", defaults);
        pane.putClientProperty("Nimbus.Overrides.InheritDefaults", true);
        pane.setBackground(bgColor);
        updateTextPane(pane);
    }

    private InstructionDialog instructions;
    private GameController controller;

    private void initCustom() {
        NanoGridParameters p = new NanoGridParameters();
        p.setColumns(15);
        p.setRows(15);
        p.setMaxColumnSquares(6);
        p.setMaxRowSquares(6);
        p.setRowBreakChance(0);
        controller = new GameController(p);
        gridDialog = new GridSizeDialog(this, true);
        gridDialog.setUI(this);
        instructions = new InstructionDialog(this, true);
        setup();
    }

    public void reset() {
        createGame();
        setPanes();
        redrawGrid();
        displayGame(false);
    }

    public void reset(NanoGridParameters newSettings) {
        controller.setSettings(newSettings);
        createGame();
        setPanes();
        redrawGrid();
        displayGame(false);
    }

    void redraw() {
        setPanes();
        redrawGrid();
        displayGame(false);
    }

    public void setup() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        createGame();
        setPanes();
        displayGame(false);
    }

    public NanoGridParameters getSettings() {
        return controller.getSettings();
    }

    public GameController getController() {
        return controller;
    }

    private JTextPane[][] panes;
    private JTextPane[] rowPanes;
    private JTextPane[] colPanes;
    private JTextPane corner;

    private void setPanes() {
        panes = new JTextPane[getSettings().getColumns()][getSettings().getRows()];
        colPanes = new JTextPane[getSettings().getColumns()];
        rowPanes = new JTextPane[getSettings().getRows()];

        GroupLayout layout = (GroupLayout) mainPanel.getLayout();

        GroupLayout.ParallelGroup hGroup = layout.createParallelGroup();
        layout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addPreferredGap(ComponentPlacement.RELATED, 1, 1);
        layout.setVerticalGroup(vGroup);
        int rowWidth = getRowWidth();
        int colHeight = getColHeight();
        int cellSize = getCellSize();
        for (int row = 0; row < getSettings().getRows() + 1; row++) {
            GroupLayout.SequentialGroup h1Group = layout.createSequentialGroup();
            h1Group.addPreferredGap(ComponentPlacement.RELATED, 1, 1);
            hGroup.addGroup(h1Group);

            GroupLayout.ParallelGroup v1Group = layout.createParallelGroup();
            vGroup.addGroup(v1Group);

            for (int col = 0; col < getSettings().getColumns() + 1; col++) {
                JTextPane pane = createPane();
                if (col > 0 && row > 0) {
                    panes[col - 1][row - 1] = pane;
                    h1Group.addComponent(pane, cellSize, cellSize, cellSize);
                    v1Group.addComponent(pane, cellSize, cellSize, cellSize);
                } else if (col == 0 && row > 0) {
                    rowPanes[row - 1] = pane;
                    updateRowPane(pane);
                    h1Group.addComponent(pane, rowWidth, rowWidth, rowWidth);
                    v1Group.addComponent(pane, cellSize, cellSize, cellSize);
                } else if (row == 0 && col > 0) {
                    colPanes[col - 1] = pane;
                    updateColumnPane(pane);
                    h1Group.addComponent(pane, cellSize, cellSize, cellSize);
                    v1Group.addComponent(pane, colHeight, colHeight, colHeight);
                } else {
                    updateCornerPane(pane);
                    h1Group.addComponent(pane, rowWidth, rowWidth, rowWidth);
                    v1Group.addComponent(pane, colHeight, colHeight, colHeight);
                }
            }
        }
        resize();
    }

    private char drawChar;

    private void jTextPaneMousePressed(MouseEvent evt) {
        JTextPane pane = (JTextPane) evt.getComponent();
        Point p = new Point();
        if (!getPane(pane, p)) return;

        if (evt.getButton() == MouseEvent.BUTTON3) {
            startX = p.x;
            startY = p.y;
            highlightStartPane();
        } else if (evt.getButton() == MouseEvent.BUTTON1) {
            clearBorders();
            cellClicked(pane);
            mouseDown = true;
            char[] chrs = pane.getText().toCharArray();
            if (chrs.length > 0) drawChar = chrs[0];
            startX = -1;
            startY = -1;
            corner.setText("");
        }
    }

    private void jTextPaneMouseReleased(MouseEvent evt) {
        mouseDown = false;
    }

    private int startX = -1;
    private int startY = -1;

    private void jTextPaneMouseEntered(MouseEvent evt) {
        JTextPane pane = (JTextPane) evt.getComponent();
        Point p = new Point();
        if (!getPane(pane, p)) return;

        if (mouseDown) setCell(pane, drawChar);

        if (startX == p.x || startY == p.y) {
            int xmod = startX <= p.x ? 1 : -1;
            int ymod = startY <= p.y ? 1 : -1;
            int val = p.x - startX + xmod;
            if (p.x == startX) val = p.y - startY + ymod;
            String str = String.format("%d", val);
            corner.setText(str);
            char ch = controller.getCellState(p.x, p.y);
            if (ch == NanoGridBoard.FillChar) pane.setForeground(Color.cyan);
            pane.setText(String.valueOf(Math.abs(val)));
            highlightBorders(p);
        }
    }

    private void jTextPaneMouseExited(MouseEvent evt) {
        JTextPane pane = (JTextPane) evt.getComponent();
        Point p = new Point();
        if (!getPane(pane, p)) return;

        if (startX >= 0 && startY >= 0) {
            char ch = controller.getCellState(p.x, p.y);
            pane.setText(String.valueOf(ch));
            if (ch == NanoGridBoard.MarkChar) {
                pane.setForeground(Color.red);
            } else {
                pane.setForeground(Color.black);
            }
            clearBorders();
            highlightStartPane();
        }
    }

    private JTextPane createPane() {
        JTextPane pane = new javax.swing.JTextPane();
        updateTextPane(pane);
        pane.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) { jTextPaneMouseEntered(evt); }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) { jTextPaneMouseExited(evt); }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) { jTextPaneMouseReleased(evt); }
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) { jTextPaneMousePressed(evt); }
        });
        return pane;
    }

    void redrawGrid() {
        mainPanel.removeAll();
        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 381, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 257, Short.MAX_VALUE)
        );
        setPanes();
        repaint();
    }

    private void createGame() {
        controller.newGame();
        LOG.fine("New puzzle generated");
    }

    public void revealGame() {
        NanoGridBoard board = controller.getBoard();
        for (int r = 0; r < getSettings().getRows(); r++) {
            for (int c = 0; c < getSettings().getColumns(); c++) {
                JTextPane pane = panes[c][r];
                char ch = board.getCell(c, r);
                if (pane.getText().equals(String.valueOf(NanoGridBoard.FillChar))) {
                    pane.setForeground(Color.green);
                    setColor(pane, Color.green);
                } else if (ch == NanoGridBoard.FillChar) {
                    setColor(pane, Color.black);
                    pane.setForeground(Color.black);
                }
                pane.setText(String.valueOf(ch));
            }
        }
    }

    public void displayGame(boolean show) {
        NanoGridBoard board = controller.getBoard();
        displayColumnCounts();
        displayRowCounts();
        for (int r = 0; r < getSettings().getRows(); r++) {
            for (int c = 0; c < getSettings().getColumns(); c++) {
                JTextPane pane = panes[c][r];
                if (show && pane.getText().equals(String.valueOf(NanoGridBoard.FillChar))) {
                    pane.setForeground(Color.green);
                    setColor(pane, Color.green);
                } else if (show && board.getCell(c, r) == NanoGridBoard.FillChar) {
                    setCell(pane);
                } else if (show) {
                    setClear(pane);
                }
            }
        }
    }

    private void displayColumnCounts() {
        for (JTextPane p : colPanes) p.setText("");
        NanoGridBoard board = controller.getBoard();
        Integer[][] ccnts = board.getColumnCounts();
        for (int i = 0; i < ccnts.length; i++) {
            colPanes[i].setText(getColumnText(i, ccnts));
        }
    }

    public String getColumnText(int col, Integer[][] ccnts) {
        StringBuilder sb = new StringBuilder();
        Integer[] cols = ccnts[col];
        int cmax = controller.getBoard().getMaxColumnCounts();
        for (int c = 0; c < cmax; c++) {
            int idx = cols.length - cmax + c;
            if (idx >= 0) {
                if (idx != 0) sb.append("\n");
                sb.append(cols[idx].toString());
            } else {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private void displayRowCounts() {
        for (JTextPane p : rowPanes) p.setText("");
        NanoGridBoard board = controller.getBoard();
        Integer[][] rcnts = board.getRowCounts();
        for (int r = 0; r < rcnts.length; r++) {
            rowPanes[r].setText(getRowText(r, rcnts));
        }
    }

    private String getRowText(int r, Integer[][] rcnts) {
        Integer[] row = rcnts[r];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < row.length; i++) {
            if (i > 0) sb.append(" ");
            sb.append(row[i].toString());
        }
        return sb.toString();
    }

    private void updateTextPane(JTextPane pane) {
        Font f = getBoardFont();
        pane.setFont(f);
        StyledDocument doc = pane.getStyledDocument();
        SimpleAttributeSet align = new SimpleAttributeSet();
        StyleConstants.setAlignment(align, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), align, false);
        pane.setEditable(false);
        javax.swing.border.LineBorder bdr = new LineBorder(Color.LIGHT_GRAY, 1);
        pane.setBorder(bdr);
        pane.setSelectedTextColor(new java.awt.Color(255, 153, 153));
        pane.setFocusable(false);
    }

    private boolean getPane(JTextPane pane, Point p) {
        for (int c = 0; c < getSettings().getColumns(); c++) {
            for (int r = 0; r < getSettings().getRows(); r++) {
                if (panes[c][r] == pane) {
                    p.x = c;
                    p.y = r;
                    return true;
                }
            }
        }
        return false;
    }

    private int[] getBoardCoord(JTextPane pane) {
        for (int c = 0; c < getSettings().getColumns(); c++) {
            for (int r = 0; r < getSettings().getRows(); r++) {
                if (panes[c][r] == pane) return new int[]{c, r};
            }
        }
        return new int[]{0, 0};
    }

    private void winGame() {
        JOptionPane.showMessageDialog(this, "You Won the Game", "You Won!", JOptionPane.OK_OPTION);
        mouseDown = false;
        displayGame(true);
    }

    private void setClear(JTextPane pane) {
        int[] coord = getBoardCoord(pane);
        pane.setText(" ");
        setColor(pane, Color.white);
        pane.setForeground(Color.black);
        controller.clearCell(coord[0], coord[1]);
    }

    private void setMark(JTextPane pane) {
        int[] coord = getBoardCoord(pane);
        pane.setText(String.valueOf(NanoGridBoard.MarkChar));
        setColor(pane, Color.white);
        pane.setForeground(Color.red);
        controller.setMark(coord[0], coord[1]);
    }

    private void setCell(JTextPane pane) {
        int[] coord = getBoardCoord(pane);
        pane.setText(String.valueOf(NanoGridBoard.FillChar));
        setColor(pane, Color.black);
        pane.setForeground(Color.black);
        controller.setCell(coord[0], coord[1]);
    }

    private void setCell(JTextPane pane, char ch) {
        if (ch == NanoGridBoard.FillChar) {
            setCell(pane);
        } else if (ch == NanoGridBoard.MarkChar) {
            setMark(pane);
        } else {
            setClear(pane);
        }
    }

    private void verifyExit() {
        int ans = JOptionPane.showConfirmDialog(this, "Quit? Are you sure?", "Warning",
                OK_CANCEL_OPTION, WARNING_MESSAGE);
        if (ans == OK_OPTION) dispose();
    }

    private void saveGame() {
        saveBoardFile(true);
    }

    private void savePuzzle() {
        saveBoardFile(false);
    }

    private void saveBoardFile(boolean includeProgress) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Nanogrid Save Files", "xml");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String ext = getFileExtension(file);
            if (!ext.equals("xml")) {
                file = new File(chooser.getCurrentDirectory(), file.getName() + ".xml");
            }
            try {
                if (includeProgress) {
                    controller.saveGame(file);
                } else {
                    controller.savePuzzle(file);
                }
            } catch (IOException ex) {
                showError("Failed to save: " + ex.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
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

    private File getBoardFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Nanogrid Save Files", "xml");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) return chooser.getSelectedFile();
        return null;
    }

    private void placeMarks() {
        char[][] board = controller.getPlayColumns();
        for (int c = 0; c < board.length; c++) {
            for (int r = 0; r < board[0].length; r++) {
                JTextPane pane = panes[c][r];
                if (board[c][r] == NanoGridBoard.FillChar) {
                    setCell(pane);
                } else if (board[c][r] == NanoGridBoard.MarkChar) {
                    setMark(pane);
                } else {
                    setClear(pane);
                }
            }
        }
    }

    private void resize() {
        this.setResizable(true);
        int rowWidth = getRowWidth();
        int colHeight = getColHeight();
        int cellWdt = getCellSize();
        int cellHgt = getCellSize();
        int menuHgt = menuBarMain.getHeight();
        int titleHeight = 30;
        int bufHgt = 20;
        int bufWdt = 27;
        int wdt = rowWidth + cellWdt * getSettings().getColumns() + bufWdt;
        int hgt = colHeight + cellHgt * getSettings().getRows() + menuHgt + titleHeight + bufHgt;
        setSize(new java.awt.Dimension(wdt, hgt));
    }

    private Font getBoardFont() {
        return new Font("Consolas", Font.BOLD, 14);
    }

    private int getRowWidth() {
        Font f = getBoardFont();
        Graphics g = this.getGraphics();
        int max = 0;
        Integer[][] rcnts = controller.getBoard().getRowCounts();
        FontMetrics metrics = g.getFontMetrics(f);
        for (int r = 0; r < getSettings().getRows(); r++) {
            String txt = getRowText(r, rcnts);
            int wdt = metrics.stringWidth(txt);
            if (wdt > max) max = wdt;
        }
        return max + 10;
    }

    private int getColHeight() {
        Font f = getBoardFont();
        Graphics g = this.getGraphics();
        int cnt = controller.getBoard().getMaxColumnCounts();
        FontMetrics metrics = g.getFontMetrics(f);
        return metrics.getHeight() * cnt + (cnt * 2);
    }

    private int getCellSize() {
        Font f = getBoardFont();
        Graphics g = this.getGraphics();
        FontMetrics metrics = g.getFontMetrics(f);
        return metrics.getHeight() + 2;
    }

    private void cellClicked(JTextPane pane) {
        if (pane != null) {
            String tx = pane.getText();
            if (tx.equals("X")) {
                setClear(pane);
            } else if (tx.equals("#")) {
                setMark(pane);
            } else {
                setCell(pane);
            }
            if (controller.checkWin()) winGame();
        }
    }

    private void clearBorders() {
        for (int c = 0; c < panes.length; c++) {
            for (int r = 0; r < panes[0].length; r++) {
                setRegularBorder(panes[c][r]);
            }
        }
    }

    private void setHighlightBorder(JTextPane pane, Point p) {
        if (startX == p.x || startY == p.y) {
            pane.setBorder(new LineBorder(Color.cyan, 2));
        }
    }

    private void setRegularBorder(JTextPane pane) {
        pane.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
    }

    private void highlightBorders(Point p) {
        if (p.x == startX) {
            highlightX(p);
        } else if (p.y == startY) {
            highlightY(p);
        }
        highlightStartPane();
    }

    private void highlightStartPane() {
        panes[startX][startY].setBorder(new LineBorder(Color.red, 2));
    }

    private void highlightX(Point p) {
        int d = p.y > startY ? 1 : -1;
        int r = startY;
        while (r != p.y) {
            setHighlightBorder(panes[p.x][r], new Point(p.x, r));
            r = r + d;
        }
        setHighlightBorder(panes[p.x][p.y], new Point(p.x, p.y));
    }

    private void highlightY(Point p) {
        int d = p.x > startX ? 1 : -1;
        int c = startX;
        while (c != p.x) {
            setHighlightBorder(panes[c][p.y], new Point(c, p.y));
            c = c + d;
        }
        setHighlightBorder(panes[p.x][p.y], new Point(p.x, p.y));
    }

    private void updateCornerPane(JTextPane pane) {
        pane.setFont(new Font("Consolas", Font.BOLD, 18));
        corner = pane;
    }

    private void updateRowPane(JTextPane pane) {
        StyledDocument doc = pane.getStyledDocument();
        SimpleAttributeSet align = new SimpleAttributeSet();
        StyleConstants.setAlignment(align, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setRightIndent(align, 1);
        doc.setParagraphAttributes(0, doc.getLength(), align, false);
    }

    private void updateColumnPane(JTextPane pane) {
        StyledDocument doc = pane.getStyledDocument();
        SimpleAttributeSet align = new SimpleAttributeSet();
        StyleConstants.setSpaceBelow(align, 2);
        doc.setParagraphAttributes(0, doc.getLength(), align, false);
    }

    private void refreshGame() {
        for (int c = 0; c < panes.length; c++) {
            for (int r = 0; r < panes[0].length; r++) {
                setClear(panes[c][r]);
            }
        }
    }
}
