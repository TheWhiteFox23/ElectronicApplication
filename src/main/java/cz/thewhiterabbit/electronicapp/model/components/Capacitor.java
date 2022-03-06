package cz.thewhiterabbit.electronicapp.model.components;

import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.objects.GeneralComponent;
import cz.thewhiterabbit.electronicapp.model.property.ComponentPropertyType;
import cz.thewhiterabbit.electronicapp.model.property.ComponentType;
import cz.thewhiterabbit.electronicapp.model.property.PropertyDialogField;
import cz.thewhiterabbit.electronicapp.model.property.RawPropertyMapping;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

@ComponentType
public class Capacitor extends GeneralComponent {
    private final Component component =Component.CAPACITOR;

    private final String CAPACITANCE = "1";

    @RawPropertyMapping
    @PropertyDialogField(name = "Capacitance", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty capacitance = new SimpleDoubleProperty(this, CAPACITANCE, 1);

    private final String path ="M46.5,35V65h-3V51.5H0v-3H43.5V35Zm10,13.5V35h-3V65h3V51.5H100v-3ZM61.44,38a1.5,1.5,0,1," +
            "0-1.5-1.5A1.5,1.5,0,0,0,61.44,38Z";

    @Override
    public Component getComponent() {
        return component;
    }

    public Capacitor(){
        initChildren();
    }

    @Override
    protected void doPaint(GraphicsContext gc) {

        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        double scale = getWidth()/100;
        gc.scale(scale, scale);
        gc.beginPath();

        gc.appendSVGPath(path);
        gc.fill();

        /*gc.setFont(new Font(12));
        gc.fillText(String.valueOf(capacitance.get()),0,0);*/


        //draw resistance
        gc.restore();
    }

    private void initChildren() {
        ActivePoint activePoint = new ActivePoint();
        ActivePoint activePoint2 = new ActivePoint();
        activePoint2.setGridX(getGridX() + 1);
        activePoint2.setGridY(getGridY() + 2);
        activePoint.setGridX(getGridX() + 1);
        activePoint.setGridY(getGridY());
        addChildren(activePoint);
        addChildren(activePoint2);
    }

    @Override
    public void addChildren(CanvasObject children) {
        super.addChildren(children);
        gridXProperty().addListener((obs, oldVal, newVal) -> {
            if (getParentModel() == null) {
                int delta = (int) newVal - (int) oldVal;
                children.setGridX(children.getGridX() + delta);
            }
        });
        gridYProperty().addListener((obs, oldVal, newVal) -> {
            if (getParentModel() == null) {
                int delta = (int) newVal - (int) oldVal;
                children.setGridY(children.getGridY() + delta);
            }
        });
    }
}
