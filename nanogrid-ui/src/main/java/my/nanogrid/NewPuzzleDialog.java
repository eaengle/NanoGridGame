package my.nanogrid;

import nanogridgame.NanoGridParameters;
import nanogridgame.PuzzleDifficulty;

import nanogridgame.NanoGridGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

public class NewPuzzleDialog extends JDialog {

    private static final int MIN_GRID_SIZE = 5;
    private static final int LEGACY_MAX_COLUMNS = 50;
    private static final int LEGACY_MAX_ROWS = 35;
    private static final int BOARD_CELL_PREFERRED_SIZE = 28;
    private static final int BOARD_PREFERRED_PADDING = 170;
    private static final int FRAME_WIDTH_ALLOWANCE = 220;
    private static final int FRAME_HEIGHT_ALLOWANCE = 160;

    private final JSpinner colSpinner = new JSpinner(new SpinnerNumberModel(15, 5, 50, 1));
    private final JSpinner rowSpinner = new JSpinner(new SpinnerNumberModel(15, 5, 35, 1));
    private final JComboBox<PuzzleDifficulty> difficultyCombo = new JComboBox<>(PuzzleDifficulty.values());
    private final JCheckBox symmetricCheckBox = new JCheckBox();
    private final JTextField seedField = new JTextField(10) {
        @Override
        protected void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty() && !isFocusOwner()) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setColor(new java.awt.Color(160, 160, 160));
                java.awt.FontMetrics fm = g2.getFontMetrics();
                java.awt.Insets ins = getInsets();
                g2.drawString("e.g. 42", ins.left + 2, ins.top + fm.getAscent());
                g2.dispose();
            }
        }
    };

    private final JButton generateButton = new JButton("Generate");
    private final JButton cancelButton = new JButton("Cancel");
    private final JLabel progressLabel = new JLabel(" ");

    private NanoGridUI ui;

    public NewPuzzleDialog(Frame parent) {
        super(parent, "New Puzzle", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        buildUI();
        pack();
    }

    private void buildUI() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));

        int row = 0;
        addRow(form, row++, "Columns", colSpinner);
        addRow(form, row++, "Rows", rowSpinner);

        GridBagConstraints sc = new GridBagConstraints();
        sc.gridy = row++;
        sc.gridwidth = 2;
        sc.fill = GridBagConstraints.HORIZONTAL;
        sc.insets = new Insets(6, 8, 6, 8);
        form.add(new JSeparator(), sc);

        difficultyCombo.setToolTipText("Controls how densely filled the puzzle is");
        addRow(form, row++, "Difficulty", difficultyCombo);
        symmetricCheckBox.setToolTipText("Generates a puzzle with 180° rotational symmetry");
        addRow(form, row++, "Symmetric", symmetricCheckBox);
        addRow(form, row, "Seed", seedField);
        seedField.setToolTipText("Optional: enter a whole number for a reproducible puzzle");

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        buttonRow.add(generateButton);
        buttonRow.add(cancelButton);
        getRootPane().setDefaultButton(generateButton);

        JPanel buttons = new JPanel(new BorderLayout(8, 0));
        buttons.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));
        buttons.add(progressLabel, BorderLayout.WEST);
        buttons.add(buttonRow, BorderLayout.EAST);

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apply();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);
    }

    private void addRow(JPanel form, int row, String labelText, JComponent control) {
        GridBagConstraints lc = new GridBagConstraints();
        lc.gridx = 0;
        lc.gridy = row;
        lc.anchor = GridBagConstraints.EAST;
        lc.insets = new Insets(3, 8, 3, 6);
        form.add(new JLabel(labelText), lc);

        GridBagConstraints fc = new GridBagConstraints();
        fc.gridx = 1;
        fc.gridy = row;
        fc.fill = GridBagConstraints.HORIZONTAL;
        fc.weightx = 1;
        fc.insets = new Insets(3, 0, 3, 8);
        form.add(control, fc);
    }

    public void setUI(NanoGridUI ui) {
        this.ui = ui;
        generateButton.setEnabled(true);
        cancelButton.setEnabled(true);
        progressLabel.setText(" ");
        updateScreenSizeLimits();
        NanoGridParameters s = ui.getController().getSettings();
        setClampedValue(colSpinner, s.getColumns());
        setClampedValue(rowSpinner, s.getRows());
        difficultyCombo.setSelectedItem(s.getDifficulty());
        symmetricCheckBox.setSelected(s.isSymmetric());
        seedField.setText(s.isUseSeed() ? String.valueOf(s.getSeed()) : "");
    }

    private void updateScreenSizeLimits() {
        Dimension maxGridSize = calculateMaxGridSize();
        setSpinnerRange(colSpinner, MIN_GRID_SIZE, maxGridSize.width);
        setSpinnerRange(rowSpinner, MIN_GRID_SIZE, maxGridSize.height);

        String tip = "Limited by usable screen size: up to " +
                maxGridSize.width + " columns by " + maxGridSize.height + " rows";
        colSpinner.setToolTipText(tip);
        rowSpinner.setToolTipText(tip);
    }

    private Dimension calculateMaxGridSize() {
        Rectangle usableScreen = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getMaximumWindowBounds();
        int boardWidth = Math.max(1, usableScreen.width - FRAME_WIDTH_ALLOWANCE);
        int boardHeight = Math.max(1, usableScreen.height - FRAME_HEIGHT_ALLOWANCE);
        int maxColumns = (boardWidth - BOARD_PREFERRED_PADDING) / BOARD_CELL_PREFERRED_SIZE;
        int maxRows = (boardHeight - BOARD_PREFERRED_PADDING) / BOARD_CELL_PREFERRED_SIZE;
        maxColumns = Math.max(MIN_GRID_SIZE, Math.min(LEGACY_MAX_COLUMNS, maxColumns));
        maxRows = Math.max(MIN_GRID_SIZE, Math.min(LEGACY_MAX_ROWS, maxRows));
        return new Dimension(maxColumns, maxRows);
    }

    private void setSpinnerRange(JSpinner spinner, int min, int max) {
        SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
        model.setMinimum(min);
        model.setMaximum(max);
        setClampedValue(spinner, (Integer) model.getValue());
    }

    private void setClampedValue(JSpinner spinner, int value) {
        SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
        int min = (Integer) model.getMinimum();
        int max = (Integer) model.getMaximum();
        spinner.setValue(Math.max(min, Math.min(max, value)));
    }

    private void apply() {
        final NanoGridParameters settings = new NanoGridParameters(ui.getController().getSettings());
        settings.setColumns(Integer.parseInt(colSpinner.getValue().toString()));
        settings.setRows(Integer.parseInt(rowSpinner.getValue().toString()));
        settings.setDifficulty((PuzzleDifficulty) difficultyCombo.getSelectedItem());
        settings.setSymmetric(symmetricCheckBox.isSelected());
        String seedText = seedField.getText().trim();
        if (seedText.isEmpty()) {
            settings.clearSeed();
        } else {
            try {
                settings.setSeed(Long.parseLong(seedText));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Seed must be a whole number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        generateButton.setEnabled(false);
        cancelButton.setEnabled(false);
        progressLabel.setText("Generating...");

        new SwingWorker<NanoGridGame, Void>() {
            @Override
            protected NanoGridGame doInBackground() {
                return new NanoGridGame(settings);
            }

            @Override
            protected void done() {
                generateButton.setEnabled(true);
                cancelButton.setEnabled(true);
                progressLabel.setText(" ");
                try {
                    NanoGridGame game = get();
                    ui.getController().installGame(game, settings);
                    setVisible(false);
                    ui.applyAfterGeneration();
                } catch (InterruptedException | ExecutionException ex) {
                    progressLabel.setText("Generation failed.");
                }
            }
        }.execute();
    }
}
