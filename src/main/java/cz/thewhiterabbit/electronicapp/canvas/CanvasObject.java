package cz.thewhiterabbit.electronicapp.canvas;

import javafx.scene.canvas.GraphicsContext;

/**
 * General canvas object. Not effected by origin position or zoomAspect
 */
public abstract class CanvasObject {
    /******BOUNDS******/
    private double locationX;
    private double locationY;
    private double width;
    private double height;

    /******EVENTS STATES*******/
    private boolean hovered;
    private boolean selected;
    private boolean dragged;


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
        this.hovered = false;
        this.selected = false;
        this.dragged = false;
    }

    /***********GETTERS AND SETTERS*************/
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

    /********** EVENT DETECTION LOGIC ************/
    /**
     * Return true if given coordinates are within the bounds of the object
     * @param x
     * @param y
     * @return
     */
    public boolean isInBounds(double x, double y){
        return ((x>= locationX && x<= locationX+height) &&
                (y>= locationY && y<= locationY+width));
    }

    /********* PAINT *****************/
    /**
     * Paint object on the screen
     */
    public abstract void paint(GraphicsContext gc);

}
