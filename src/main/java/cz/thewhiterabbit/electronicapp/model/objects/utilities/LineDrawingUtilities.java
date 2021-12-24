package cz.thewhiterabbit.electronicapp.model.objects.utilities;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.model.objects.TwoPointLineObject;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import cz.thewhiterabbit.electronicapp.view.canvas.DrawingAreaEvent;
import cz.thewhiterabbit.electronicapp.view.canvas.model.CanvasModel;
import cz.thewhiterabbit.electronicapp.view.canvas.model.GridModel;
import cz.thewhiterabbit.electronicapp.view.events.CanvasMouseEvent;
import cz.thewhiterabbit.electronicapp.view.events.CanvasPaintEvent;

import java.util.List;

public class LineDrawingUtilities {
    private final LineOptimizer lineOptimizer = new LineOptimizer();

    private TwoPointLineObject firstLine;
    private TwoPointLineObject secondLine;
    private boolean deleteOrigin = false;
    private boolean edited = false;

    public void onActivePointDragged(GridModel m, CanvasObject parent, CanvasObject object, CanvasMouseEvent e) {
        if (parent instanceof TwoPointLineObject) {
            List<CanvasObject> objects = m.getInBounds(object.getLocationX(), object.getLocationY(),
                    object.getWidth(), object.getHeight());
            if (objects.size() == 2) {

                TwoPointLineObject line = (TwoPointLineObject) parent;
                CanvasMouseEvent event = e;
                //shortening the line
                if(getLineOrientation(line) == Orientation.HORIZONTAL){
                    int x = m.getGridCoordinate(event.getX(), m.getOriginX());
                    int y = m.getGridCoordinate(event.getY(), m.getOriginY());
                    if(line.getY2() == y &&  x >= line.getLoverX() && x<= line.getHigherX()){
                        firstLine = null;
                        secondLine = null;
                        adjustCurrantLine(object, line, event, m);
                    }else {
                        line.setVisible(true);
                        deleteOrigin = false;
                        onDraggedNewLines(object,m, e);
                    }
                }else if(getLineOrientation(line) == Orientation.VERTICAL){
                    int x = m.getGridCoordinate(event.getX(), m.getOriginX());
                    int y = m.getGridCoordinate(event.getY(), m.getOriginY());
                    if(line.getX2() == x &&  y >= line.getLoverY() && y<= line.getHigherY()){
                        firstLine = null;
                        secondLine = null;
                        adjustCurrantLine(object, line, event, m);
                    }else{
                        line.setVisible(true);
                        deleteOrigin = false;
                        onDraggedNewLines(object, m, e);
                    }
                }
            } else {
                onDraggedNewLines(object, m, e);
            }
        } else {
            onDraggedNewLines(object, m, e);
        }


    }

    public void onActivePointDropped(CanvasModel model, CanvasObject parent, EventAggregator eventAggregator) {
        if (firstLine != null) {
            lineOptimizer.initLineActivePoints(firstLine);
            eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_ADDED, firstLine));
            firstLine = null;
            edited = true;
        }

        if (secondLine != null) {
            lineOptimizer.initLineActivePoints(secondLine);
            eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_ADDED, secondLine));
            secondLine = null;
            edited = true;
        }

        if (deleteOrigin) {
            eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_DELETED, parent));
            parent.getChildrenList().forEach(o -> eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_DELETED, o)));
            edited = true;
        }

        LineOptimizer lineOptimizer = new LineOptimizer();
        lineOptimizer.optimize((GridModel)model);
        if(edited){
            edited = false;
            eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.EDITING_FINISHED));
        }
        //todo: fire editing completed event
    }

    private void adjustCurrantLine(CanvasObject o, TwoPointLineObject line, CanvasMouseEvent event, GridModel m) {
        line.setVisible(false); //TODO active points still visible

        int gridStartX = (line.getX2() == o.getGridX() ? line.getX1() : line.getX2());
        int gridStartY = (line.getY2()) == o.getGridY() ? line.getY1() : line.getY2();
        int gridX = m.getGridCoordinate(event.getX(), m.getOriginX());
        int gridY = m.getGridCoordinate(event.getY(), m.getOriginY());
        int deltaX = gridX - gridStartX;
        int deltaY = gridY - gridStartY;

        adjustLines(gridStartX, gridStartY, gridX, gridY, deltaX, deltaY);
        managePainting(m, deltaX, deltaY);
        deleteOrigin = true;
    }

    private void onDraggedNewLines(CanvasObject o, CanvasModel model, CanvasMouseEvent e) {
        CanvasMouseEvent event = e;
        GridModel m = (GridModel) model;

        int gridStartX = o.getGridX();
        int gridStartY = o.getGridY();
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
            m.getInnerEventAggregator().fireEvent(new CanvasPaintEvent(CanvasPaintEvent.REPAINT));
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



    private Orientation getLineOrientation(TwoPointLineObject lineObject) {
        if (lineObject.getY2() == lineObject.getY1()) {
            return Orientation.HORIZONTAL;
        } else if (lineObject.getX1() == lineObject.getX2()) {
            return Orientation.VERTICAL;
        }
        return null;
    }


    private enum Orientation {
        HORIZONTAL,
        VERTICAL
    }
}
