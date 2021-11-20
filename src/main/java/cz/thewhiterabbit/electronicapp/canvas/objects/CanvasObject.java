package cz.thewhiterabbit.electronicapp.canvas.objects;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.canvas.CanvasEventAggregator;
import cz.thewhiterabbit.electronicapp.canvas.layout.LayoutProperties;
import cz.thewhiterabbit.electronicapp.events.CanvasEvent;
import cz.thewhiterabbit.electronicapp.events.CanvasMouseEvent;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

/**
 * General canvas object. Not effected by origin position or zoomAspect
 */
public abstract class CanvasObject {
    private final LayoutProperties layoutProperties = new LayoutProperties();
    private List<ActiveZone> activeZones;

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
        this.activeZones = new ArrayList<>();
        this.locationX = locationX;
        this.locationY = locationY;
        this.width = width;
        this.height = height;
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

    public LayoutProperties getLayoutProperties() {return layoutProperties;}

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

    public boolean isInBounds(double locationX, double locationY, double width, double height){
        return (getLocationX()+getHeight()>locationX &&
                getLocationY()+getWidth()>locationY &&
                getLocationY() <locationY+width &&
                getLocationX() <locationX+height);
    }

    public List<ActiveZone> getActiveZones() {
        return activeZones;
    }

    public void addActiveZone(ActiveZone activeZone){
        activeZones.add(activeZone);
    }
    public void removeActiveZone(ActiveZone activeZone){
        activeZones.remove(activeZone);
    }
}
