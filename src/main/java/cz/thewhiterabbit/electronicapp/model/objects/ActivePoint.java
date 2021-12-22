package cz.thewhiterabbit.electronicapp.model.objects;

import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;
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


public class ActivePoint extends DocumentObject {
    //Line drawing
    private TwoPointLineObject firstLine;
    private TwoPointLineObject secondLine;
    private boolean deleteOrigin = false;


    public ActivePoint() {
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
        onActivePointDropped();
    }

    private void onActivePointDropped() {
        if (firstLine != null) {
            initLineActivePoints(firstLine);
            getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_ADDED, firstLine));
            //optimizeLine(firstLine);
            firstLine = null;
        }

        if (secondLine != null) {
            initLineActivePoints(secondLine);
            getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_ADDED, secondLine));
            //optimizeLine(secondLine);
            secondLine = null;
        }

        if (deleteOrigin) {
            getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_DELETED, getParent()));
            getParent().getChildrenList().forEach(o -> getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_DELETED, o)));
        }

        LineOptimizer lineOptimizer = new LineOptimizer();
        lineOptimizer.optimize((GridModel)getParentModel());

        //todo: optimize lines

        //todo: fire editing completed event
    }

    private void optimizeLine(TwoPointLineObject lineObject) {
        //detect line splitting by node
        lineObject.getChildrenList().forEach(o ->{
            if(o instanceof ActivePoint){
                List<CanvasObject> objectOnLocation = o.getParentModel().getInBounds(o.getLocationX(), o.getLocationY(), o.getWidth(), o.getHeight());
                if(objectOnLocation.contains(o))objectOnLocation.remove(o);
                if(objectOnLocation.contains(o.getParent()))objectOnLocation.remove(o.getParent());
                if(objectOnLocation.size() != 0){
                    objectOnLocation.forEach(l -> {
                        if(l instanceof TwoPointLineObject){
                            TwoPointLineObject line = (TwoPointLineObject) l;
                            if((line.getY2() == o.getGridY() && line.getX2() == o.getGridX()) ||
                                    line.getY1() == o.getGridY() && line.getX1() == o.getGridX()){
                                return;
                            }else{
                                if(getLineOrientation(line) != getLineOrientation((TwoPointLineObject) o.getParent())){
                                    splitTheLine(line, o.getGridX(), o.getGridY());
                                }else{
                                    mergeLines(line,((TwoPointLineObject) o.getParent()));
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void mergeLines(TwoPointLineObject lineObject, TwoPointLineObject lineObject2) {
        if(getLineOrientation(lineObject)!= getLineOrientation(lineObject2)){
            return;
        }
        if(getLineOrientation(lineObject)==Orientation.HORIZONTAL){
            int y = lineObject.getY1();
            int x1 = Math.min(lineObject.getLoverX(), lineObject2.getLoverX());
            int x2 = Math.max(lineObject.getHigherX(), lineObject2.getHigherX());
            lineObject.callForDelete();
            lineObject2.callForDelete();
            TwoPointLineObject lineObject1 = new TwoPointLineObject(x1, y, x2, y);
            initLineActivePoints(lineObject1);
            getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_ADDED, lineObject1));
        }else{
            int x = lineObject.getX1();
            int y1 = Math.min(lineObject.getLoverY(), lineObject2.getLoverY());
            int y2 = Math.max(lineObject.getHigherY(), lineObject2.getHigherY());
            lineObject.callForDelete();
            lineObject2.callForDelete();
            TwoPointLineObject lineObject1 = new TwoPointLineObject(x, y1, x, y2);
            initLineActivePoints(lineObject1);
            getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_ADDED, lineObject1));
        }
    }

    private void splitTheLine(TwoPointLineObject line, int splitX, int splitY) {
        if(splitX >= Math.min(line.getX1(), line.getX2()) && splitX <= Math.max(line.getX1(),line.getX2())
                && splitY >= Math.min(line.getY1(), line.getY2()) && splitY <= Math.max(line.getY1(), line.getY2())){
            line.callForDelete();
            TwoPointLineObject lineObject = new TwoPointLineObject(line.getX1(), line.getY1(), splitX, splitY);
            initLineActivePoints(lineObject);
            getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_ADDED,lineObject));
            lineObject = new TwoPointLineObject(line.getX2(), line.getY2(), splitX, splitY);
            initLineActivePoints(lineObject);
            getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_ADDED,lineObject));
        }
    }


    private void initLineActivePoints(TwoPointLineObject line) {
        ActivePoint activePoint = new ActivePoint();
        activePoint.set(line.getX1(), line.getY1(), 1, 1);
        line.addChildren(activePoint);
        activePoint = new ActivePoint();
        activePoint.set(line.getX2(), line.getY2(), 1, 1);
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
        firstLine.mapProperties();
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
        //super.clean(gc);
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
    public void init() {
    }

    @Override
    public void mapProperties() {
        getRawObject().getProperty("gridX").valueProperty().addListener((obs, oldVal, newVal) -> {
            setGridX(Integer.parseInt(newVal));
        });
        getRawObject().getProperty("gridY").valueProperty().addListener((obs, oldVal, newVal) -> {
            setGridY(Integer.parseInt(newVal));
        });
        getRawObject().getProperty("gridWidth").valueProperty().addListener((obs, oldVal, newVal) -> {
            setGridWidth(Integer.parseInt(newVal));
        });
        getRawObject().getProperty("gridHeight").valueProperty().addListener((obs, oldVal, newVal) -> {
            setGridHeight(Integer.parseInt(newVal));
        });
    }

    private enum Orientation {
        HORIZONTAL,
        VERTICAL
    }
}
