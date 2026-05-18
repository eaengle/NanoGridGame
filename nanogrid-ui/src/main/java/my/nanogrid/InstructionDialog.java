package my.nanogrid;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InstructionDialog extends javax.swing.JDialog {

    private static final Logger LOG = Logger.getLogger(InstructionDialog.class.getName());

    public InstructionDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initCustom();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        instructionPane = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jScrollPane1.setViewportView(instructionPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane instructionPane;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    private void initCustom() {
        URL url = InstructionDialog.class.getResource("/instructions.html");
        if (url != null) {
            try {
                instructionPane.setPage(url);
            } catch (IOException ex) {
                LOG.log(Level.WARNING, "Could not load instructions page", ex);
                showFallback();
            }
        } else {
            showFallback();
        }
        instructionPane.setEditable(false);
    }

    private void showFallback() {
        instructionPane.setContentType("text/html");
        instructionPane.setText(
            "<html><body>" +
            "<h2>How to Play Nano Grid</h2>" +
            "<p>Fill in squares according to the number clues on each row and column.</p>" +
            "<ul>" +
            "<li><b>Left-click</b> a cell to fill it.</li>" +
            "<li><b>Left-click</b> a filled cell to mark it (X), then again to clear.</li>" +
            "<li><b>Right-click</b> a cell to start a counting guide along a row or column.</li>" +
            "</ul>" +
            "<p>Use the <b>Hint</b> menu to check your progress, peek at the solution briefly, or reveal it.</p>" +
            "</body></html>"
        );
    }
}
