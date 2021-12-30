package cz.thewhiterabbit.electronicapp.model.components;

import cz.thewhiterabbit.electronicapp.model.objects.GeneralComponent;
import javafx.scene.canvas.GraphicsContext;

public class Resistor extends GeneralComponent {
    private final Component component = Component.RESISTOR;


    @Override
    public Component getComponent() {
        return component;
    }

    @Override
    protected void doPaint(GraphicsContext gc) {
        //TODO paint resistor
        gc.fillText(getType(), 0, getHeight());
    }
}
