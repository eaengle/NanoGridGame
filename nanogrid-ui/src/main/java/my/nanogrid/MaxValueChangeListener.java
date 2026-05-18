package my.nanogrid;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MaxValueChangeListener implements ChangeListener {

    GridSizeDialog GUI;

    MaxValueChangeListener(GridSizeDialog gui) {
        GUI = gui;
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        SpinnerNumberModel Source = (SpinnerNumberModel) ce.getSource();
        GUI.validateChange(Source);
    }
}
