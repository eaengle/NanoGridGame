package my.nanogrid;

import nanogridgame.NanoGridParameters;
import nanogridgame.PuzzleDifficulty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewPuzzleDialog extends JDialog {

    private static final int MIN_GRID_SIZE = 5;
    private static final int LEGACY_MAX_COLUMNS = 50;
    private static final int LEGACY_MAX_ROWS = 35;
    private static final int BOARD_CELL_PREFERRED_SIZE = 28;
    private static final int BOARD_PREFERRED_PADDING = 170;
    private static final int FRAME_WIDTH_ALLOWANCE = 220;
    private static final int FRAME_HEIGHT_ALLOWANCE = 160;

    private final JComboBox<PuzzleDifficulty> difficultyCombo = new JComboBox<>(PuzzleDifficulty.values());
    private final JCheckBox symmetricCheckBox = new JCheckBox();
    private final JTextField seedField = new JTextField(10);
    private final JSpinner colSpinner = new JSpinner(new SpinnerNumberModel(15, 5, 50, 1));
    private final JSpinner rowSpinner = new JSpinner(new SpinnerNumberModel(15, 5, 35, 1));
    private JPanel advancedPanel;
    private JButton advancedToggle;

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
        addRow(form, row++, "Difficulty", difficultyCombo);
        addRow(form, row++, "Symmetric", symmetricCheckBox);
        addRow(form, row++, "Seed", seedField);
        seedField.setToolTipText("Optional: enter a number for a reproducible puzzle");

        advancedToggle = new JButton("Advanced ▾");
        advancedToggle.setFocusPainted(false);
        GridBagConstraints tc = new GridBagConstraints();
        tc.gridy = row++;
        tc.gridwidth = 2;
        tc.anchor = GridBagConstraints.WEST;
        tc.insets = new Insets(6, 8, 2, 8);
        form.add(advancedToggle, tc);

        advancedPanel = new JPanel(new GridBagLayout());
        advancedPanel.setVisible(false);
        int arow = 0;
        addRow(advancedPanel, arow++, "Columns", colSpinner);
        addRow(advancedPanel, arow, "Rows", rowSpinner);
        GridBagConstraints ac = new GridBagConstraints();
        ac.gridy = row;
        ac.gridwidth = 2;
        ac.fill = GridBagConstraints.HORIZONTAL;
        ac.weightx = 1;
        form.add(advancedPanel, ac);

        advancedToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean show = !advancedPanel.isVisible();
                advancedPanel.setVisible(show);
                advancedToggle.setText(show ? "Advanced ▴" : "Advanced ▾");
                pack();
            }
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        buttons.add(ok);
        buttons.add(cancel);
        getRootPane().setDefaultButton(ok);

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apply();
            }
        });
        cancel.addActionListener(new ActionListener() {
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
        updateScreenSizeLimits();
        NanoGridParameters s = ui.getController().getSettings();
        difficultyCombo.setSelectedItem(s.getDifficulty());
        symmetricCheckBox.setSelected(s.isSymmetric());
        seedField.setText(s.isUseSeed() ? String.valueOf(s.getSeed()) : "");
        setClampedValue(colSpinner, s.getColumns());
        setClampedValue(rowSpinner, s.getRows());
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
        NanoGridParameters settings = new NanoGridParameters(ui.getController().getSettings());
        settings.setDifficulty((PuzzleDifficulty) difficultyCombo.getSelectedItem());
        settings.setSymmetric(symmetricCheckBox.isSelected());
        if (advancedPanel.isVisible()) {
            settings.setColumns(Integer.parseInt(colSpinner.getValue().toString()));
            settings.setRows(Integer.parseInt(rowSpinner.getValue().toString()));
        }
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
        setVisible(false);
        ui.reset(settings);
    }
}
