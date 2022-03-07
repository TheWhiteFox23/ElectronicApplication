package cz.thewhiterabbit.electronicapp.model.objects;

import cz.thewhiterabbit.electronicapp.model.components.Component;
import cz.thewhiterabbit.electronicapp.utilities.LineDrawingUtilities;
import cz.thewhiterabbit.electronicapp.view.canvas.model.CanvasModel;
import cz.thewhiterabbit.electronicapp.view.canvas.model.GridModel;

import cz.thewhiterabbit.electronicapp.view.events.CanvasMouseEvent;
import javafx.event.Event;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class ActivePoint extends GeneralMappingComponent {
    private final LineDrawingUtilities lineDrawingUtilities = new LineDrawingUtilities();
    private final Component component = Component.ACTIVE_POINT;

    private int relativeLocationX = 0;
    private int relativeLocationY = 0;


    public ActivePoint() {
        setGridHeight(1);
        setGridWidth(1);
        setPriority(CanvasModel.Priority.ALWAYS_ON_TOP);
        setRotationStrategy(RotationStrategy.MOVE_WITH_PARENT_ROTATION);
    }

    public ActivePoint(int relativeLocationX, int relativeLocationY) {
        this.relativeLocationX = relativeLocationX;
        this.relativeLocationY = relativeLocationY;
        setGridHeight(1);
        setGridWidth(1);
        setPriority(CanvasModel.Priority.ALWAYS_ON_TOP);
        setRotationStrategy(RotationStrategy.MOVE_WITH_PARENT_ROTATION);
    }

    /***** OVERRIDES *****/
    @Override
    public void doPaint(GraphicsContext gc) {
        double height = getHeight() * 0.4;
        double locationX = -height / 2;
        double locationY = -height / 2;

        if (isHovered()) {
            gc.setFill(Color.BLUEVIOLET);
        } else {
            gc.setFill(Color.DARKSLATEGRAY);
        }

        gc.fillOval(locationX, locationY, height, height);
    }

    @Override
    public boolean isInBounds(double x, double y) {
        double height = getHeight() * 0.5;
        double locationX = getLocationX() - height / 2;
        double locationY = getLocationY() - height / 2;
        return ((x >= locationX && x <= locationX + height) && (y >= locationY && y <= locationY + height));
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    protected void onObjectDropped(Event e) {
        e.consume();
        lineDrawingUtilities.onActivePointDropped(getParentModel(), getParent(), getEventAggregator());
    }

    @Override
    protected void onObjectDragged(Event e) {
        e.consume();
        if (getParentModel() != null && getParentModel() instanceof GridModel) {
            lineDrawingUtilities.onActivePointDragged((GridModel)getParentModel(), getParent(),this, (CanvasMouseEvent) e);
        }
    }

    public int getRelativeLocationX() {
        return relativeLocationX;
    }

    public void setRelativeLocationX(int relativeLocationX) {
        this.relativeLocationX = relativeLocationX;
    }

    public int getRelativeLocationY() {
        return relativeLocationY;
    }

    public void setRelativeLocationY(int relativeLocationY) {
        this.relativeLocationY = relativeLocationY;
    }

    @Override
    public void clean(GraphicsContext gc) {}

    @Override
    public Component getComponent() {
        return component;
    }
}
