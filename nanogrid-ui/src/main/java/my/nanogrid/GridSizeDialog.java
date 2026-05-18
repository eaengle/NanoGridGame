package my.nanogrid;

import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import nanogridgame.NanoGridParameters;

public class GridSizeDialog extends javax.swing.JDialog {

    public GridSizeDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initCustom();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        colSpinner = new javax.swing.JSpinner();
        rowSpinner = new javax.swing.JSpinner();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        maxColsSpinner = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        maxRowsSpinner = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        rowBreakSpinner = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Grid Options");
        setName("gridSize1");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Rows");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Columns");

        colSpinner.setModel(new javax.swing.SpinnerNumberModel(5, 5, 50, 1));
        rowSpinner.setModel(new javax.swing.SpinnerNumberModel(5, 5, 35, 1));

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { cancelButtonActionPerformed(evt); }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { okButtonActionPerformed(evt); }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Column Count");

        maxColsSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 0, null, 1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Row Count");

        maxRowsSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 0, null, 1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Break %");

        rowBreakSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 100, 10));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addContainerGap(14, Short.MAX_VALUE)
                                    .addComponent(jLabel3))
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(maxColsSpinner, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(maxRowsSpinner, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(14, 14, 14)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(rowSpinner)
                                .addComponent(colSpinner))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 17, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cancelButton))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rowBreakSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(colSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rowSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maxColsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maxRowsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rowBreakSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }

    private NanoGridUI ui;
    private NanoGridParameters localSettings;

    public void setUI(NanoGridUI ui) {
        this.ui = ui;
        NanoGridParameters current = ui.getController().getSettings();
        localSettings = new NanoGridParameters(current);
        colSpinner.setValue(localSettings.getColumns());
        rowSpinner.setValue(localSettings.getRows());
        maxRowsSpinner.setValue(localSettings.getMaxRowSquares());
        maxColsSpinner.setValue(localSettings.getMaxColumnSquares());
        rowBreakSpinner.setValue(localSettings.getRowBreakChance());
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        localSettings.setColumns(Integer.parseInt(colSpinner.getValue().toString()));
        localSettings.setRows(Integer.parseInt(rowSpinner.getValue().toString()));
        localSettings.setMaxColumnSquares(Integer.parseInt(maxColsSpinner.getValue().toString()));
        localSettings.setMaxRowSquares(Integer.parseInt(maxRowsSpinner.getValue().toString()));
        localSettings.setRowBreakChance(Integer.parseInt(rowBreakSpinner.getValue().toString()));
        ui.reset(localSettings);
        this.setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JSpinner colSpinner;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSpinner maxColsSpinner;
    private javax.swing.JSpinner maxRowsSpinner;
    private javax.swing.JButton okButton;
    private javax.swing.JSpinner rowBreakSpinner;
    private javax.swing.JSpinner rowSpinner;
    // End of variables declaration//GEN-END:variables

    private void initCustom() {
        ChangeListener cl = new MaxValueChangeListener(this);
        maxColsSpinner.getModel().addChangeListener(cl);
        maxRowsSpinner.getModel().addChangeListener(cl);
        colSpinner.getModel().addChangeListener(cl);
        rowSpinner.getModel().addChangeListener(cl);
    }

    void validateChange(SpinnerNumberModel src) {
        Integer val = (Integer) src.getValue();
        if (src == maxColsSpinner.getModel()) {
            Integer v2 = (Integer) colSpinner.getModel().getValue();
            if (val > v2) src.setValue(v2);
        } else if (src == maxRowsSpinner.getModel()) {
            Integer v2 = (Integer) rowSpinner.getModel().getValue();
            if (val > v2) src.setValue(v2);
        } else if (src == rowSpinner.getModel()) {
            SpinnerNumberModel mdl = (SpinnerNumberModel) maxRowsSpinner.getModel();
            Integer v2 = (Integer) mdl.getValue();
            if (val < v2) mdl.setValue(val);
        } else if (src == colSpinner.getModel()) {
            SpinnerNumberModel mdl = (SpinnerNumberModel) maxColsSpinner.getModel();
            Integer v2 = (Integer) mdl.getValue();
            if (val < v2) mdl.setValue(val);
        }
    }
}
