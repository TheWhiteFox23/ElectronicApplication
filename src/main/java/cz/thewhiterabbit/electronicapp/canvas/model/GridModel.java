package cz.thewhiterabbit.electronicapp.canvas.model;


import cz.thewhiterabbit.electronicapp.canvas.DrawingAreaEvent;
import cz.thewhiterabbit.electronicapp.canvas.objects.CanvasObject;
import cz.thewhiterabbit.electronicapp.events.CanvasMouseEvent;
import cz.thewhiterabbit.electronicapp.events.CanvasPaintEvent;

import java.util.function.Consumer;


public class GridModel extends RelativeModel {
    private double gridSize = 10;

    public GridModel() {
        super();
    }


    /*********** GETTERS AND SETTERS *************/
    public double getGridSize() {
        return gridSize;
    }

    public void setGridSize(double gridSize) {
        this.gridSize = gridSize;
    }


    /********** METHODS -> OVERRIDES *************/
    @Override
    protected void onObjectDragged(CanvasMouseEvent e) {

    }

    @Override
    protected void onObjectDragDropped(CanvasMouseEvent e) {

    }

    @Override
    protected void onObjectMoved(CanvasMouseEvent e) {
        CanvasObject canvasObject = e.getObject();
        if (canvasObject != null && containsObject(canvasObject) && canvasObject.isSelected()) {
            CanvasMouseEvent event = e;
            int deltaX = getGridCoordinate(event.getX(), getOriginX()) - getGridCoordinate(event.getLastX(), getOriginX());
            int deltaY = getGridCoordinate(event.getY(), getOriginY()) - getGridCoordinate(event.getLastY(), getOriginY());
            getAll().forEach(o -> {
                if (o.isSelected()) {
                    int gridX = getGridCoordinate(o.getLocationX(), getOriginX()) + deltaX;
                    int gridY = getGridCoordinate(o.getLocationY(), getOriginY()) + deltaY;
                    //DocumentObject properties = o.getDocumentComponent();
                    //properties.setGridX(gridX);
                    //properties.setGridY(gridY);
                    /*EventAggregator eventAggregator = GUIEventAggregator.getInstance();
                    DocumentObjectCommand command = new DocumentObjectCommand(DocumentObjectCommand.CHANGE_PROPERTY,o.getDocumentComponent(), o.getDocumentComponent().gridX(), o.getDocumentComponent().gridX().getValue(), gridX);
                    eventAggregator.fireEvent(command);
                    command = new DocumentObjectCommand(DocumentObjectCommand.CHANGE_PROPERTY,o.getDocumentComponent(), o.getDocumentComponent().gridY(), o.getDocumentComponent().gridY().getValue(), gridY);
                    eventAggregator.fireEvent(command);*/
                    //TODO fire property change event
                    updatePaintProperties(o);
                }
            });

            getInnerEventAggregator().fireEvent(new CanvasPaintEvent(CanvasPaintEvent.REPAINT));
        }

    }

    @Override
    protected void onObjectMoving(CanvasMouseEvent e) {
        CanvasObject canvasObject = e.getObject();
        if (canvasObject != null && containsObject(canvasObject) && canvasObject.isSelected()) {
            CanvasMouseEvent event = e;
            int deltaX = getGridCoordinate(event.getX(), getOriginX()) - getGridCoordinate(event.getLastX(), getOriginX());
            int deltaY = getGridCoordinate(event.getY(), getOriginY()) - getGridCoordinate(event.getLastY(), getOriginY());

            if (deltaX != 0 || deltaY != 0) {
                getSelectedObject().forEach(o -> {
                    moveObject(o, deltaX, deltaY);
                });
            }
            getInnerEventAggregator().fireEvent(new CanvasPaintEvent(CanvasPaintEvent.REPAINT));
        }
    }

    private void moveChildren(CanvasObject object, int deltaX, int deltaY){
        if(object.getChildrenList().size() == 0) return;
        object.getChildrenList().forEach(o -> {
            if(!o.isSelected()){
                moveObject(o, deltaX, deltaY);
            }
        });

    }
    private void moveObject(CanvasObject o, int deltaX, int deltaY) {
        doMoveObject(o, deltaX, deltaY);
        moveChildren(o, deltaX,deltaY);
    }

    private void doMoveObject(CanvasObject o, int deltaX, int deltaY) {
        int gridX = getGridCoordinate(o.getLocationX(), getOriginX()) + deltaX;
        int gridY = getGridCoordinate(o.getLocationY(), getOriginY()) + deltaY;
        o.setLocationX(getGridLocation(gridX, getOriginX()));
        o.setLocationY(getGridLocation(gridY, getOriginY()));
    }

    @Override
    public void updatePaintProperties(CanvasObject object) {
        object.setLocationX(getGridLocation(object.getGridX(), getOriginX()));
        object.setLocationY(getGridLocation(object.getGridY(), getOriginY()));
        object.setWidth(object.getGridWidth() * gridSize * getZoomAspect());
        object.setHeight(object.getGridHeight() * gridSize * getZoomAspect());
    }

    /********* SEARCHING IN GRID *********/
    public int getGridCoordinate(double location, double originLocation) {
        double locationDelta = location - originLocation;
        int gridCoordinate = (int) (locationDelta / (gridSize * getZoomAspect()));
        if (location < originLocation && locationDelta % (gridSize * getZoomAspect()) != 0) gridCoordinate--;
        return gridCoordinate;
    }

    public double getGridLocation(int coordinate, double originLocation) {
        double location = originLocation + (coordinate * gridSize * getZoomAspect());
        return location;
    }
}
