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
        //insert created line or modify currantLine, delete helper lines
    }

    @Override
    protected void onObjectDragged(Event e) {
        e.consume();
        if (getParentModel() != null && getParentModel() instanceof GridModel) {
            CanvasMouseEvent event = (CanvasMouseEvent) e;
            GridModel m = (GridModel) getParentModel();
            int gridStartX = getGridX();
            int gridStartY = getGridY();
            int gridX = m.getGridCoordinate(event.getX(), m.getOriginX());
            int gridY = m.getGridCoordinate(event.getY(), m.getOriginY());

            int deltaX = gridX - gridStartX;
            int deltaY = gridY - gridStartY;

            if (deltaX != 0) {
                if (firstLine == null) {
                    firstLine = new TwoPointLineObject(gridStartX, gridStartY, gridX, gridStartY);
                    //create first line with corresponding orientation
                } else if (secondLine == null && firstLine.getX2() - firstLine.getX1() == 0) {
                    secondLine = new TwoPointLineObject(gridStartX, gridY, gridX, gridY);
                    //create second line with corresponding location
                } else {
                    if (firstLine.getX2() - firstLine.getX1() != 0) {
                        firstLine.setX2(gridX);
                    } else if (secondLine.getX2() - secondLine.getX1() != 0) {
                        secondLine.setX2(gridX);
                        secondLine.setY1(gridY);
                        secondLine.setY2(gridY);
                    }
                }
            } else if (deltaX == 0) {
                if (firstLine != null && firstLine.getX2() - firstLine.getX1() != 0) {
                    firstLine = secondLine;
                    if(firstLine != null) {
                        firstLine.setX1(gridStartX);
                        firstLine.setX2(gridStartX);
                    }
                    secondLine = null;
                } else if (secondLine != null && secondLine.getX2() - secondLine.getX1() != 0) {
                    secondLine = null;
                }
            }

            if (deltaY != 0) {
                if (firstLine == null) {
                    firstLine = new TwoPointLineObject(gridStartX, gridStartY, gridStartX, gridY);
                    //create first line with corresponding orientation
                } else if (secondLine == null && firstLine.getY2() - firstLine.getY1() == 0) {
                    secondLine = new TwoPointLineObject(gridX, gridStartY, gridX, gridY);
                    //create second line with corresponding location
                } else {
                    if (firstLine.getY2() - firstLine.getY1() != 0) {
                        firstLine.setY2(gridY);
                    } else if (secondLine.getY2() - secondLine.getY1() != 0) {
                        secondLine.setY2(gridY);
                        secondLine.setX1(gridX);
                        secondLine.setX2(gridX);
                    }
                }
            } else if (deltaY == 0) {
                if (firstLine != null && firstLine.getY2() - firstLine.getY1() != 0) {
                    firstLine = secondLine;
                    if(firstLine != null){
                        firstLine.setY1(gridStartY);
                        firstLine.setY2(gridStartY);
                    }
                    secondLine = null;
                } else if (secondLine != null && secondLine.getY2() - secondLine.getY1() != 0) {
                    secondLine = null;
                }
            }

            if(firstLine != null || secondLine != null){
                getEventAggregator().fireEvent(new CanvasPaintEvent(CanvasPaintEvent.REPAINT));
            }

            if (firstLine != null) {
                firstLine.setParentModel(m);
                firstLine.mapProperties();
                m.updatePaintProperties(firstLine);
                firstLine.paint(m.getCanvas().getGraphicsContext2D());
            }
            if (secondLine != null) {
                secondLine.setParentModel(m);
                secondLine.mapProperties();
                m.updatePaintProperties(secondLine);
                secondLine.paint(m.getCanvas().getGraphicsContext2D());
            }
        }

    }

    @Override
    public void clean(GraphicsContext gc) {
        //super.clean(gc);
    }
}
