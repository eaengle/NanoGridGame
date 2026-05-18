package my.nanogrid;

import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MaxValueChangeListener implements ChangeListener {

    private final GridSizeDialog gui;

    MaxValueChangeListener(GridSizeDialog gui) {
        this.gui = gui;
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        SpinnerNumberModel source = (SpinnerNumberModel) ce.getSource();
        gui.validateChange(source);
    }
}
