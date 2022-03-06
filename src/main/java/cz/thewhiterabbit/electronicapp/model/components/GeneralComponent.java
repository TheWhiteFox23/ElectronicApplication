package cz.thewhiterabbit.electronicapp.model.components;

import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.objects.GeneralMappingComponent;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class GeneralComponent extends GeneralMappingComponent {
    private String path = "";
    private Component component = Component.RESISTOR;

    private double scaleMultiplier = 100;
    private double translateX = 0.0;
    private double translateY = 0.0;
    private Color defaultColor = Color.BLACK;
    private Color selectedColor = Color.GREENYELLOW;
    private Color highlightColor = Color.BLUEVIOLET;

    public GeneralComponent(){
        setGridWidth(2);
        setGridHeight(2);
    }

    @Override
    public Component getComponent() {
        if(component != null)return component;
        return Component.TEST_COMPONENT; //TODO add default component
    }

    @Override
    protected void doPaint(GraphicsContext gc) {
        gc.translate(translateX, translateY);
        gc.setFill(getColor());

        double scale = getWidth()/scaleMultiplier;
        gc.scale(scale, scale);
        gc.beginPath();

        gc.appendSVGPath(path);
        gc.fill();

        gc.restore();

    }

    private Paint getColor() {
        if (isHovered()) {
            return highlightColor;
        } else if (isSelected()) {
            return selectedColor;
        } else {
            return defaultColor;
        }
    }

    public void addActivePoint(int relativeX, int relativeY){
        ActivePoint activePoint = new ActivePoint();
        activePoint.setGridY(getGridY() + relativeY);
        activePoint.setGridX(getGridX() + relativeX);
        addChildren(activePoint);
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public double getScaleMultiplier() {
        return scaleMultiplier;
    }

    public void setScaleMultiplier(double scaleMultiplier) {
        this.scaleMultiplier = scaleMultiplier;
    }

    public double getTranslateX() {
        return translateX;
    }

    public void setTranslateX(double translateX) {
        this.translateX = translateX;
    }

    public double getTranslateY() {
        return translateY;
    }

    public void setTranslateY(double translateY) {
        this.translateY = translateY;
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    public Color getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(Color highlightColor) {
        this.highlightColor = highlightColor;
    }


}
