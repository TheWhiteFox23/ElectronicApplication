package cz.thewhiterabbit.electronicapp.view.canvas.model;


import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import cz.thewhiterabbit.electronicapp.view.canvas.DrawingAreaEvent;
import cz.thewhiterabbit.electronicapp.view.events.CanvasMouseEvent;
import cz.thewhiterabbit.electronicapp.view.events.CanvasPaintEvent;



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
    protected void onObjectDragged(CanvasMouseEvent e) {}

    @Override
    protected void onObjectDragDropped(CanvasMouseEvent e) {}

    @Override
    protected void onObjectMoved(CanvasMouseEvent e) {
        CanvasObject canvasObject = e.getObject();
        if (canvasObject != null && containsObject(canvasObject) && canvasObject.isSelected()) {
            CanvasMouseEvent event = e;
            int deltaX = getGridCoordinate(event.getX(), getOriginX()) - getGridCoordinate(event.getStartX(), getOriginX());
            int deltaY = getGridCoordinate(event.getY(), getOriginY()) - getGridCoordinate(event.getStartY(), getOriginY());

            if (deltaX != 0 || deltaY != 0) {
                getSelectedObject().forEach(o -> {
                    setObjectLocations(o, deltaX, deltaY);
                });
                getInnerEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.EDITING_FINISHED));
            }
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
    private void moveObject(CanvasObject o, int deltaX, int deltaY) {//TODO: refactor -> object it self should decide its movement
        doMoveObject(o, deltaX, deltaY);
        moveChildren(o, deltaX,deltaY);
    }

    private void doMoveObject(CanvasObject o, int deltaX, int deltaY) {
        int gridX = getGridCoordinate(o.getLocationX(), getOriginX()) + deltaX;
        int gridY = getGridCoordinate(o.getLocationY(), getOriginY()) + deltaY;
        o.setLocationX(getGridLocation(gridX, getOriginX()));
        o.setLocationY(getGridLocation(gridY, getOriginY()));
    }

    private void doSetObjectLocations(CanvasObject o, int deltaX, int deltaY){
        o.setLocation(deltaX, deltaY);
    }

    private void setChildrenLocation(CanvasObject object, int deltaX, int deltaY){
        if(object.getChildrenList().size() == 0) return;
        object.getChildrenList().forEach(o -> {
            if(!o.isSelected()){
                System.out.println(o + "setting location");
                o.setLocation(deltaX, deltaY);
            }
        });
    }

    private void setObjectLocations(CanvasObject o, int deltaX, int deltaY) {
        doSetObjectLocations(o, deltaX, deltaY);
        setChildrenLocation(o, deltaX, deltaY);
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
