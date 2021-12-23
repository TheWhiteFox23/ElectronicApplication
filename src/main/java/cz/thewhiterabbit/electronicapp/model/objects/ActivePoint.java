package cz.thewhiterabbit.electronicapp.model.objects;

import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawProperty;
import cz.thewhiterabbit.electronicapp.view.canvas.DrawingAreaEvent;
import cz.thewhiterabbit.electronicapp.view.canvas.model.CanvasModel;
import cz.thewhiterabbit.electronicapp.view.canvas.model.GridModel;

import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import cz.thewhiterabbit.electronicapp.view.events.CanvasMouseEvent;
import cz.thewhiterabbit.electronicapp.view.events.CanvasPaintEvent;
import javafx.event.Event;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;


public class ActivePoint extends GeneralObject {
    //Logic
    private final LineOptimizer lineOptimizer = new LineOptimizer();

    //Line drawing
    private TwoPointLineObject firstLine;
    private TwoPointLineObject secondLine;
    private boolean deleteOrigin = false;


    public ActivePoint() {
        setPriority(CanvasModel.Priority.ALWAYS_ON_TOP);
        setRotationStrategy(RotationStrategy.MOVE_WITH_PARENT_ROTATION);
    }

    public ActivePoint(RawObject rawObject){
        this();
        setRawObject(rawObject);
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
        if (firstLine != null) {
            lineOptimizer.initLineActivePoints(firstLine);
            getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_ADDED, firstLine));
            firstLine = null;
        }

        if (secondLine != null) {
            lineOptimizer.initLineActivePoints(secondLine);
            getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_ADDED, secondLine));
            secondLine = null;
        }

        if (deleteOrigin) {
            getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_DELETED, getParent()));
            getParent().getChildrenList().forEach(o -> getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_DELETED, o)));
        }

        LineOptimizer lineOptimizer = new LineOptimizer();
        lineOptimizer.optimize((GridModel)getParentModel());
        //todo: fire editing completed event
    }

    @Override
    protected void onObjectDragged(Event e) {
        e.consume();
        if (getParentModel() != null && getParentModel() instanceof GridModel) {
            onActivePointDragged((CanvasMouseEvent) e);
        }

    }

    private void onActivePointDragged(CanvasMouseEvent e) {
        if (getParent() instanceof TwoPointLineObject) {
            List<CanvasObject> objects = getParentModel().getInBounds(getLocationX(), getLocationY(), getWidth(), getHeight());
            if (objects.size() == 2) {

                TwoPointLineObject line = (TwoPointLineObject) getParent();
                CanvasMouseEvent event = e;
                GridModel m = (GridModel) getParentModel();
                //shortening the line
                if(getLineOrientation(line) == Orientation.HORIZONTAL){
                    int x = m.getGridCoordinate(event.getX(), m.getOriginX());
                    int y = m.getGridCoordinate(event.getY(), m.getOriginY());
                    if(line.getY2() == y &&  x >= line.getLoverX() && x<= line.getHigherX()){
                        firstLine = null;
                        secondLine = null;
                        adjustCurrantLine(line, event, m);
                    }else {
                        line.setVisible(true);
                        deleteOrigin = false;
                        onDraggedNewLines(e);
                    }
                }else if(getLineOrientation(line) == Orientation.VERTICAL){
                    int x = m.getGridCoordinate(event.getX(), m.getOriginX());
                    int y = m.getGridCoordinate(event.getY(), m.getOriginY());
                    if(line.getX2() == x &&  y >= line.getLoverY() && y<= line.getHigherY()){
                        firstLine = null;
                        secondLine = null;
                        adjustCurrantLine(line, event, m);
                    }else{
                        line.setVisible(true);
                        deleteOrigin = false;
                        onDraggedNewLines(e);
                    }
                }
            } else {
                onDraggedNewLines(e);
            }
        } else {
            onDraggedNewLines(e);
        }


    }

    private void adjustCurrantLine(TwoPointLineObject line, CanvasMouseEvent event, GridModel m) {
        line.setVisible(false); //TODO active points still visible

        int gridStartX = (line.getX2() == getGridX() ? line.getX1() : line.getX2());
        int gridStartY = (line.getY2()) == getGridY() ? line.getY1() : line.getY2();
        int gridX = m.getGridCoordinate(event.getX(), m.getOriginX());
        int gridY = m.getGridCoordinate(event.getY(), m.getOriginY());
        int deltaX = gridX - gridStartX;
        int deltaY = gridY - gridStartY;

        adjustLines(gridStartX, gridStartY, gridX, gridY, deltaX, deltaY);
        managePainting(m, deltaX, deltaY);
        deleteOrigin = true;
    }

    private void onDraggedNewLines(CanvasMouseEvent e) {
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
        if ((firstLine != null || secondLine != null) || (deltaY == 0 && deltaX == 0)) {
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
        firstLine.mapLineProperties();
        m.updatePaintProperties(firstLine);
        firstLine.paint(m.getCanvas().getGraphicsContext2D());
    }

    private void setLine(Orientation orientation, int firstPoint, int secondPoint, int level1, int level2) {
        if (firstLine == null) {
            if (orientation == Orientation.HORIZONTAL) {
                firstLine = new TwoPointLineObject(firstPoint, level1, secondPoint, level1);
            } else {
                firstLine = new TwoPointLineObject(level1, firstPoint, level1, secondPoint);
            }
        } else if (secondLine == null && getLineOrientation(firstLine) != orientation) {
            if (orientation == Orientation.HORIZONTAL) {
                secondLine = new TwoPointLineObject(firstPoint, level2, secondPoint, level2);
            } else {
                secondLine = new TwoPointLineObject(level2, firstPoint, level2, secondPoint);
            }
        } else {
            adjustLines(orientation, secondPoint, level2);
        }
    }

    private void adjustLines(Orientation orientation, int secondPoint, int level2) {
        if (getLineOrientation(firstLine) == orientation) {
            if (orientation == Orientation.HORIZONTAL) {
                firstLine.setX2(secondPoint);
            } else {
                firstLine.setY2(secondPoint);
            }
        } else if (getLineOrientation(secondLine) == orientation) {
            if (orientation == Orientation.HORIZONTAL) {
                secondLine.setX2(secondPoint);
                secondLine.setY1(level2);
                secondLine.setY2(level2);
            } else {
                secondLine.setY2(secondPoint);
                secondLine.setX1(level2);
                secondLine.setX2(level2);
            }

        }
    }

    private void deleteLine(Orientation orientation, int newLevel) {
        if (firstLine != null && getLineOrientation(firstLine) == orientation) {
            firstLine = secondLine;
            if (firstLine != null) {
                if (orientation == Orientation.HORIZONTAL) {
                    firstLine.setX1(newLevel);
                    firstLine.setX2(newLevel);
                } else {
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

    }

    private Orientation getLineOrientation(TwoPointLineObject lineObject) {
        if (lineObject.getY2() == lineObject.getY1()) {
            return Orientation.HORIZONTAL;
        } else if (lineObject.getX1() == lineObject.getX2()) {
            return Orientation.VERTICAL;
        }
        return null;
    }

    @Override
    public RawObject toRawObject() {
        if(getRawObject() == null){
            RawObject rawObject = new RawObject("ACTIVE_POINT");
            rawObject.addProperty(new RawProperty("gridX", String.valueOf(getGridX())));
            rawObject.addProperty(new RawProperty("gridY", String.valueOf(getGridY())));
            rawObject.addProperty(new RawProperty("gridWidth", String.valueOf(getGridWidth())));
            rawObject.addProperty(new RawProperty("gridHeight", String.valueOf(getGridHeight())));
            getChildrenList().forEach(l -> {
                rawObject.getChildren().add(((DocumentObject)l).toRawObject());
            });
            setRawObject(rawObject);
        }
        return getRawObject();
    }


    private enum Orientation {
        HORIZONTAL,
        VERTICAL
    }
}
