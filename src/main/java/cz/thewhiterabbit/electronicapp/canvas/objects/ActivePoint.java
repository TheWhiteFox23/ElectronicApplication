package cz.thewhiterabbit.electronicapp.canvas.objects;

import cz.thewhiterabbit.electronicapp.canvas.DrawingAreaEvent;
import cz.thewhiterabbit.electronicapp.canvas.model.GridModel;
import cz.thewhiterabbit.electronicapp.canvas.model.Priority;

import cz.thewhiterabbit.electronicapp.events.CanvasMouseEvent;
import cz.thewhiterabbit.electronicapp.events.CanvasPaintEvent;
import javafx.event.Event;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class ActivePoint extends CanvasObject {

    //Line drawing
    private TwoPointLineObject firstLine;
    private TwoPointLineObject secondLine;

    public ActivePoint() {
        setPriority(Priority.ALWAYS_ON_TOP);
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
        onActivePointDropped();
    }

    private void onActivePointDropped() {
        if(firstLine != null){
            initLineActivePoints(firstLine);
            getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_ADDED, firstLine));
        }

        if(secondLine != null){
            initLineActivePoints(secondLine);
            getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_ADDED, secondLine));
        }
    }

    private void initLineActivePoints(TwoPointLineObject line) {
        ActivePoint activePoint = new ActivePoint();
        activePoint.set(line.getX1(), line.getY1(), 1,1);
        line.addChildren(activePoint);
        activePoint = new ActivePoint();
        activePoint.set(line.getX2(), line.getY2(), 1,1);
        line.addChildren(activePoint);
    }

    @Override
    protected void onObjectDragged(Event e) {
        e.consume();
        if (getParentModel() != null && getParentModel() instanceof GridModel) {
            onActivePointDragged((CanvasMouseEvent) e);
        }

    }

    private void onActivePointDragged(CanvasMouseEvent e) {
        CanvasMouseEvent event = e;
        GridModel m = (GridModel) getParentModel();

        int gridStartX = getGridX();
        int gridStartY = getGridY();
        int gridX = m.getGridCoordinate(event.getX(), m.getOriginX());
        int gridY = m.getGridCoordinate(event.getY(), m.getOriginY());
        int deltaX = gridX - gridStartX;
        int deltaY = gridY - gridStartY;

        adjustLines(gridStartX, gridStartY, gridX, gridY, deltaX, deltaY);
        managePainting(m, deltaX, deltaY);
    }

    private void adjustLines(int gridStartX, int gridStartY, int gridX, int gridY, int deltaX, int deltaY) {
        if (deltaX != 0) {
            setLine(Orientation.HORIZONTAL, gridStartX, gridX, gridStartY, gridY);
        } else if (deltaX == 0) {
            deleteLine(Orientation.HORIZONTAL, gridStartX);
        }

        if (deltaY != 0) {
            setLine(Orientation.VERTICAL, gridStartY, gridY, gridStartX, gridX);
        } else if (deltaY == 0) {
            deleteLine(Orientation.VERTICAL, gridStartY);
        }
    }

    private void managePainting(GridModel m, int deltaX, int deltaY) {
        if((firstLine != null || secondLine != null) || (deltaY == 0 && deltaX == 0)){
            getEventAggregator().fireEvent(new CanvasPaintEvent(CanvasPaintEvent.REPAINT));
        }

        if (firstLine != null) {
            paintLine(m, firstLine);
        }
        if (secondLine != null) {
            paintLine(m, secondLine);
        }
    }

    private void paintLine(GridModel m, TwoPointLineObject firstLine) {
        firstLine.setParentModel(m);
        firstLine.mapProperties();
        m.updatePaintProperties(firstLine);
        firstLine.paint(m.getCanvas().getGraphicsContext2D());
    }

    private void setLine(Orientation orientation, int firstPoint, int secondPoint, int level1, int level2) {
        if (firstLine == null) {
            if(orientation == Orientation.HORIZONTAL){
                firstLine = new TwoPointLineObject(firstPoint, level1, secondPoint, level1);
            }else{
                firstLine = new TwoPointLineObject(level1, firstPoint, level1, secondPoint);
            }
        } else if (secondLine == null && getLineOrientation(firstLine) != orientation) {
            if(orientation == Orientation.HORIZONTAL){
                secondLine = new TwoPointLineObject(firstPoint, level2, secondPoint, level2);
            }else{
                secondLine = new TwoPointLineObject(level2, firstPoint, level2, secondPoint);
            }
        } else {
            adjustLines(orientation, secondPoint, level2);
        }
    }

    private void adjustLines(Orientation orientation, int secondPoint, int level2) {
        if(getLineOrientation(firstLine) == orientation){
            if(orientation == Orientation.HORIZONTAL){
                firstLine.setX2(secondPoint);
            }else{
                firstLine.setY2(secondPoint);
            }
        } else if (getLineOrientation(secondLine) == orientation) {
            if(orientation == Orientation.HORIZONTAL){
                secondLine.setX2(secondPoint);
                secondLine.setY1(level2);
                secondLine.setY2(level2);
            }else{
                secondLine.setY2(secondPoint);
                secondLine.setX1(level2);
                secondLine.setX2(level2);
            }

        }
    }

    private void deleteLine(Orientation orientation, int newLevel) {
        if (firstLine != null && getLineOrientation(firstLine) == orientation) {
            firstLine = secondLine;
            if(firstLine != null) {
                if(orientation == Orientation.HORIZONTAL){
                    firstLine.setX1(newLevel);
                    firstLine.setX2(newLevel);
                }else{
                    firstLine.setY2(newLevel);
                    firstLine.setY1(newLevel);
                }
            }
            secondLine = null;
        } else if (secondLine != null && getLineOrientation(secondLine) == orientation) {
            secondLine = null;
        }
    }

    @Override
    public void clean(GraphicsContext gc) {
        //super.clean(gc);
    }

    private Orientation getLineOrientation(TwoPointLineObject lineObject){
            if(lineObject.getY2() == lineObject.getY1()){
                return Orientation.HORIZONTAL;
            }else if(lineObject.getX1() == lineObject.getX2()){
                return Orientation.VERTICAL;
            }
            return null;
    }

    private enum Orientation{
        HORIZONTAL,
        VERTICAL
    }
}
