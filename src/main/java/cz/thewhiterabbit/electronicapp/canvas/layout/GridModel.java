package cz.thewhiterabbit.electronicapp.canvas.layout;

import cz.thewhiterabbit.electronicapp.DocumentObject;
import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.canvas.objects.CanvasObject;
import cz.thewhiterabbit.electronicapp.events.CanvasEvent;
import cz.thewhiterabbit.electronicapp.events.CanvasMouseEvent;
import cz.thewhiterabbit.electronicapp.events.DocumentObjectCommand;
import javafx.scene.canvas.Canvas;

public class GridModel extends RelativeModel {
    private double gridSize = 10;

    public GridModel(Canvas canvas, EventAggregator eventAggregator) {
        super(canvas, eventAggregator);
    }


    /*********** GETTERS AND SETTERS *************/
    public double getGridSize() {return gridSize;}
    public void setGridSize(double gridSize) {this.gridSize = gridSize;}


    /********** METHODS -> OVERRIDES *************/
    @Override
    protected void onObjectDragged(CanvasMouseEvent e) {
        CanvasObject canvasObject = e.getObject();
        if(canvasObject != null && containsObject(canvasObject) && canvasObject.isSelected()){
            CanvasMouseEvent event = e;
            //grid delta
            int deltaX = getGridCoordinate(event.getX(), getOriginX()) - getGridCoordinate(event.getLastX(), getOriginX());
            int deltaY = getGridCoordinate(event.getY(), getOriginY()) - getGridCoordinate(event.getLastY(), getOriginY());

            if(deltaX != 0 || deltaY != 0){
                getAll().forEach(o -> {
                    if(o.isSelected()){
                        int gridX = getGridCoordinate(o.getLocationX(), getOriginX()) + deltaX;
                        int gridY = getGridCoordinate(o.getLocationY(), getOriginY()) + deltaY;
                        o.setLocationX(getGridLocation(gridX, getOriginX()));
                        o.setLocationY(getGridLocation(gridY, getOriginY()));
                    }
                });
            }
            getCanvasEventAggregator().fireEvent(new CanvasEvent(CanvasEvent.REPAINT_ALL));
        }

    }

    @Override
    protected void onObjectDragDropped(CanvasMouseEvent e) {
        CanvasObject canvasObject = e.getObject();
        if(canvasObject != null && containsObject(canvasObject) && canvasObject.isSelected()){
            CanvasMouseEvent event = e;
            int deltaX = getGridCoordinate(event.getX(), getOriginX()) - getGridCoordinate(event.getLastX(), getOriginX());
            int deltaY = getGridCoordinate(event.getY(), getOriginY()) - getGridCoordinate(event.getLastY(), getOriginY());
            getAll().forEach(o -> {
                if(o.isSelected()){
                    int gridX = getGridCoordinate(o.getLocationX(), getOriginX()) + deltaX;
                    int gridY = getGridCoordinate(o.getLocationY(), getOriginY()) + deltaY;
                    //DocumentObject properties = o.getDocumentComponent();
                    //properties.setGridX(gridX);
                    //properties.setGridY(gridY);
                    EventAggregator eventAggregator = GUIEventAggregator.getInstance();
                    DocumentObjectCommand command = new DocumentObjectCommand(DocumentObjectCommand.CHANGE_PROPERTY,o.getDocumentComponent(), o.getDocumentComponent().gridX(), o.getDocumentComponent().gridX().getValue(), gridX);
                    eventAggregator.fireEvent(command);
                    command = new DocumentObjectCommand(DocumentObjectCommand.CHANGE_PROPERTY,o.getDocumentComponent(), o.getDocumentComponent().gridY(), o.getDocumentComponent().gridY().getValue(), gridY);
                    eventAggregator.fireEvent(command);
                    updatePaintProperties(o);
                }
            });

            getCanvasEventAggregator().fireEvent(new CanvasEvent(CanvasEvent.REPAINT_ALL));
        }
    }

    @Override
    public void updatePaintProperties(CanvasObject object) {
        DocumentObject properties = object.getDocumentComponent();
        object.setLocationX(getGridLocation(properties.getGridX(), getOriginX()));
        object.setLocationY(getGridLocation(properties.getGridY(), getOriginY()));
        object.setWidth(properties.getGridWidth() * gridSize * getZoomAspect());
        object.setHeight(properties.getGridHeight() * gridSize * getZoomAspect());
    }

    /********* SEARCHING IN GRID *********/
   public int getGridCoordinate(double location, double originLocation){
        double locationDelta = location-originLocation;
        int gridCoordinate = (int)(locationDelta/(gridSize * getZoomAspect()));
        if (location< originLocation && locationDelta % (gridSize*getZoomAspect()) != 0)gridCoordinate--;
        return gridCoordinate;
    }

    public double getGridLocation(int coordinate, double originLocation){
        double location = originLocation + (coordinate * gridSize * getZoomAspect());
        return location;
    }
}
