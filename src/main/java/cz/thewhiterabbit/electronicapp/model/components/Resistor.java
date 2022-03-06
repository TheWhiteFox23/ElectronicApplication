package cz.thewhiterabbit.electronicapp.model.components;

import cz.thewhiterabbit.electronicapp.model.objects.GeneralComponent;
import cz.thewhiterabbit.electronicapp.model.property.ComponentPropertyType;
import cz.thewhiterabbit.electronicapp.model.property.ComponentType;
import cz.thewhiterabbit.electronicapp.model.property.PropertyDialogField;
import cz.thewhiterabbit.electronicapp.model.property.RawPropertyMapping;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ComponentType
public class Resistor extends GeneralComponent {
    private final Component component = Component.RESISTOR;

    private final String RESISTANCE = "1";

    @RawPropertyMapping
    @PropertyDialogField(name = "Resistance", type = ComponentPropertyType.TEXT_FIELD)
    private final DoubleProperty resistance = new SimpleDoubleProperty(this, RESISTANCE, 1);

    private final String path ="M66.67,65.57l-11.11-24-11.12,24-11.11-24-11.11,24L15.71,51.5H0v-3H17.63l4.59,9.93," +
            "11.11-24,11.11,24,11.12-24,11.11,24,11.11-24L84.29,48.5H100v3H82.37l-4.59-9.93Z";

    @Override
    public Component getComponent() {
        return component;
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

        /*gc.setFont(new Font(200));
        gc.fillText(String.valueOf(resistance.get()),0,0);*/


        //draw resistance
        gc.restore();
    }

}

