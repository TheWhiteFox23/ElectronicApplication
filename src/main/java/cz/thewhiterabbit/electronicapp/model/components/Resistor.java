package cz.thewhiterabbit.electronicapp.model.components;

import cz.thewhiterabbit.electronicapp.model.objects.GeneralObject;
import javafx.scene.canvas.GraphicsContext;

public class Resistor extends GeneralObject {
    private final String _TYPE="RESISTOR";

    @Override
    public String getType() {
        return _TYPE;
    }

    @Override
    protected void doPaint(GraphicsContext gc) {
        //TODO paint resistor
        gc.fillText(_TYPE, 0, getHeight());
    }
}
