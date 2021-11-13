package cz.thewhiterabbit.electronicapp.canvas.objects;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.canvas.CanvasEventAggregator;
import cz.thewhiterabbit.electronicapp.events.CanvasEvent;
import cz.thewhiterabbit.electronicapp.events.CanvasMouseEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.canvas.GraphicsContext;

/**
 * General canvas object. Not effected by origin position or zoomAspect
 */
//TODO Add factory to differ object for the layouts
public abstract class CanvasObject {
    /******PROPERTIES***/
    private double heightProperty;
    private double widthProperty;
    private double relativeLocationX;
    private double relativeLocationY;

    /******LAYOUT BOUNDS GRID*******/
    private int gridX;
    private int gridY;
    private int gridHeight;
    private int gridWidth;

    /******DRAWING BOUNDS******/
    private double locationX;
    private double locationY;
    private double width;
    private double height;

    /******EVENTS STATES*******/
    private boolean hovered;
    private boolean selected;
    private boolean dragged;

    /******EVENT HANDLING******/
    private EventAggregator eventAggregator = CanvasEventAggregator.getInstance();


    /************CONSTRUCTORS*******************/
   public CanvasObject(){
        this(0,0,0,0);
    }

    public CanvasObject(double width, double height) {
        this(0,0, width, height);
    }

    public CanvasObject(double locationX, double locationY, double width, double height) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.width = width;
        this.height = height;
        this.relativeLocationX = 0;
        this.relativeLocationY = 0;
        this.gridX = 0;
        this.gridY = 0;
        this.gridHeight = 1;
        this.gridWidth = 1;
        this.hovered = false;
        this.selected = false;
        this.dragged = false;
        registerListeners();
    }

    private void registerListeners() {
       eventAggregator.registerHandler(CanvasMouseEvent.OBJECT_ENTERED, e->{
           if(((CanvasMouseEvent) e).getObject() == this){
               hovered = true;
               eventAggregator.fireEvent(new CanvasEvent(CanvasEvent.PAINT_OBJECT, this));
           }else if(hovered == true){
               hovered = false;
               eventAggregator.fireEvent(new CanvasEvent(CanvasEvent.PAINT_OBJECT, this));
           }
       });
       eventAggregator.registerHandler(CanvasMouseEvent.OBJECT_EXITED, e -> {
           if(((CanvasMouseEvent)e).getObject() == this){
               hovered = false;
               eventAggregator.fireEvent(new CanvasEvent(CanvasEvent.PAINT_OBJECT, this));
           }
       });
        eventAggregator.registerHandler(CanvasMouseEvent.OBJECT_SELECTED, e -> {
            if(((CanvasMouseEvent)e).getObject() == this){
                selected = true;
                eventAggregator.fireEvent(new CanvasEvent(CanvasEvent.PAINT_OBJECT, this));
            }
        });
        eventAggregator.registerHandler(CanvasMouseEvent.OBJECT_DESELECTED, e -> {
            if(((CanvasMouseEvent)e).getObject() == this){
                selected = false;
                eventAggregator.fireEvent(new CanvasEvent(CanvasEvent.PAINT_OBJECT, this));
            }
        });
        eventAggregator.registerHandler(CanvasMouseEvent.DESELECT_ALL, e -> {
            selected = false;
            eventAggregator.fireEvent(new CanvasEvent(CanvasEvent.PAINT_OBJECT, this));
        });
    }

    /***********GETTERS AND SETTERS*************/

    public double getHeightProperty() {return heightProperty;}

    public void setHeightProperty(double heightProperty) {this.heightProperty = heightProperty;}

    public double getWidthProperty() {return widthProperty;}

    public void setWidthProperty(double widthProperty) {this.widthProperty = widthProperty;}

    public double getLocationX() {return locationX;}

    public void setLocationX(double locationX) {this.locationX = locationX;}

    public double getLocationY() {return locationY;}

    public void setLocationY(double locationY) {this.locationY = locationY;}

    public double getWidth() {return width;}

    public void setWidth(double width) {this.width = width;}

    public double getHeight() {return height;}

    public void setHeight(double height) {this.height = height;}

    public boolean isHovered() {return hovered;}

    public void setHovered(boolean hovered) {this.hovered = hovered;}

    public boolean isSelected() {return selected;}

    public void setSelected(boolean selected) {this.selected = selected;}

    public boolean isDragged() {return dragged;}

    public void setDragged(boolean dragged) {this.dragged = dragged;}

    public double getRelativeLocationX() {return relativeLocationX;}

    public void setRelativeLocationX(double relativeLocationX) {this.relativeLocationX = relativeLocationX;}

    public double getRelativeLocationY() {return relativeLocationY;}

    public void setRelativeLocationY(double relativeLocationY) {this.relativeLocationY = relativeLocationY;}

    public int getGridX() {return gridX;}

    public void setGridX(int gridX) {this.gridX = gridX;}

    public int getGridY() {return gridY;}

    public void setGridY(int gridY) {this.gridY = gridY;}

    public int getGridHeight() {return gridHeight;}

    public void setGridHeight(int gridHeight) {this.gridHeight = gridHeight;}

    public int getGridWidth() {return gridWidth;}

    public void setGridWidth(int gridWidth) {this.gridWidth = gridWidth;}

    /********** EVENT DETECTION LOGIC AND EVENT HANDLING ************/
    /**
     * Return true if given coordinates are within the bounds of the object
     * @param x
     * @param y
     * @return
     */
    public boolean isInBounds(double x, double y){
        return ((x>= getLocationX() && x<= getLocationX()+getHeight()) &&
                (y >= getLocationY() && y <= getLocationY()+getWidth()));
    }

    public <T extends Event> void fireEvent(T event) {
        eventAggregator.fireEvent(event);
    }

    public <T extends EventType> void registerHandler(T eventType, EventHandler handler) {
        eventAggregator.registerHandler(eventType, handler);
    }



    /********* PAINT *****************/
    /**
     * Paint object on the screen
     */
    public abstract void paint(GraphicsContext gc);

    public boolean isVisible(double canvasWidth, double canvasHeight) {
        return (getLocationY()<canvasHeight
                && getLocationY()+getHeight()>0
                && getLocationX()<canvasWidth
                && getLocationX()+getWidth()>0);
    }
}
