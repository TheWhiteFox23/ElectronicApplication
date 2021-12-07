package cz.thewhiterabbit.electronicapp.canvas.objects;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.canvas.model.CanvasModel;
import cz.thewhiterabbit.electronicapp.canvas.model.Priority;
import cz.thewhiterabbit.electronicapp.events.CanvasMouseEvent;
import cz.thewhiterabbit.electronicapp.events.CanvasPaintEvent;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

/**
 * General canvas object. Not effected by origin position or zoomAspect
 */
public abstract class CanvasObject  { //TODO should wrap
    private CanvasModel parentModel;

    private Priority priority = Priority.NONE;
    private List<CanvasObject> childrenList;
    private CanvasObject parent;
    //private List<ActivePoint> activePoints;

    /***** Properties -> communicate change to observers ******/
    private IntegerProperty gridX;
    private IntegerProperty gridY;
    private IntegerProperty gridWidth;
    private IntegerProperty gridHeight;
    private IntegerProperty rotation;

    /******DRAWING BOUNDS******/
    private DoubleProperty locationX;
    private DoubleProperty locationY;
    private DoubleProperty width;
    private DoubleProperty height;

    /******EVENTS STATES*******/
    private boolean hovered;
    private boolean selected;
    private boolean dragged;

    private RotationStrategy rotationStrategy;

    /******EVENT HANDLING******/
    private EventAggregator eventAggregator;

    /************CONSTRUCTORS*******************/
    public CanvasObject(){
        this(0,0,0,0);
    }

    public CanvasObject(double width, double height) {
        this(0,0, width, height);
    }

    public CanvasObject(double locationX, double locationY, double width, double height) {
        //this.activePoints = new ArrayList<>();
        this.locationX = new SimpleDoubleProperty(locationX);
        this.locationY = new SimpleDoubleProperty(locationY);
        this.width = new SimpleDoubleProperty(width);
        this.height = new SimpleDoubleProperty(height);
        this.hovered = false;
        this.selected = false;
        this.dragged = false;
        //PROPERTIES
        this.gridY = new SimpleIntegerProperty();
        this.gridX = new SimpleIntegerProperty();
        this.gridWidth = new SimpleIntegerProperty();
        this.gridHeight = new SimpleIntegerProperty();
        this.rotation = new SimpleIntegerProperty();

        this.childrenList = new ArrayList<>();

        this.rotationStrategy = RotationStrategy.ROTATE;
    }

    protected void registerListeners(EventAggregator eventAggregator) {
       eventAggregator.addEventHandler(CanvasMouseEvent.OBJECT_ENTERED, e->{
           if(((CanvasMouseEvent) e).getObject() == this){
               onObjectEntered(e);
           }
       });
       eventAggregator.addEventHandler(CanvasMouseEvent.OBJECT_EXITED, e -> {
           if(((CanvasMouseEvent)e).getObject() == this){
               onObjectExited(e);
           }
       });
        eventAggregator.addEventHandler(CanvasMouseEvent.OBJECT_SELECTED, e -> {
            if(((CanvasMouseEvent)e).getObject() == this){
                onObjectSelected(e);
            }
        });
        eventAggregator.addEventHandler(CanvasMouseEvent.OBJECT_DESELECTED, e -> {
            if(((CanvasMouseEvent)e).getObject() == this){
                onObjectDeselected(e);
            }
        });
        eventAggregator.addEventHandler(CanvasMouseEvent.DESELECT_ALL, e -> {
            onObjectDeselected(e);
        });
        eventAggregator.addEventHandler(CanvasMouseEvent.OBJECTS_DRAG_DETECTED, e -> {
            if(((CanvasMouseEvent)e).getObject() == this){
                onDragDetected(e);
            }
        });
        eventAggregator.addEventHandler(CanvasMouseEvent.OBJECT_DRAGGED, e -> {
            if(((CanvasMouseEvent)e).getObject() == this ){
                onObjectDragged(e);
            }
        });
        eventAggregator.addEventHandler(CanvasMouseEvent.OBJECT_DRAG_DROPPED, e -> {
            if(((CanvasMouseEvent)e).getObject() == this){
                onObjectDropped(e);
            }
        });
    }

    public double getLocationX() {return locationX.get();}

    public DoubleProperty locationXProperty() {return locationX;}

    public void setLocationX(double locationX) {this.locationX.set(locationX);}

    public double getLocationY() {return locationY.get();}

    public DoubleProperty locationYProperty() {return locationY;}

    public void setLocationY(double locationY) {this.locationY.set(locationY);}

    public double getWidth() {return width.get();}

    public DoubleProperty widthProperty() {return width;}

    public void setWidth(double width) {this.width.set(width);}

    public double getHeight() {return height.get();}

    public DoubleProperty heightProperty() {return height;}

    public void setHeight(double height) {this.height.set(height);}

    public boolean isHovered() {return hovered;}

    public void setHovered(boolean hovered) {this.hovered = hovered;}

    public boolean isSelected() {return selected;}

    public void setSelected(boolean selected) {this.selected = selected;}

    public boolean isDragged() {return dragged;}

    public void setDragged(boolean dragged) {this.dragged = dragged;}

    public Priority getPriority() {return priority;}

    public void setPriority(Priority priority) {this.priority = priority;}

    public EventAggregator getEventAggregator() {return eventAggregator;}

    public RotationStrategy getRotationStrategy() {
        return rotationStrategy;
    }

    public void setRotationStrategy(RotationStrategy rotationStrategy) {
        this.rotationStrategy = rotationStrategy;
    }

    public void setEventAggregator(EventAggregator eventAggregator) {
        this.eventAggregator = eventAggregator;
        registerListeners(eventAggregator);
    }

    public CanvasModel getParentModel() {return parentModel;}
    public void setParentModel(CanvasModel parentModel) {this.parentModel = parentModel;}
    public List<CanvasObject> getChildrenList() {return childrenList;}

    public void addChildren(CanvasObject children){
        childrenList.add(children);
        children.setParent(this);
    }

    public void removeChildren(CanvasObject children){
        childrenList.remove(children);
        children.setParent(null);
    }

    public CanvasObject getParent() {return parent;}
    public void setParent(CanvasObject parent) {this.parent = parent;}

    /****** PROPERTIES ******/
    public int getGridX() {return gridX.get();}
    public IntegerProperty gridXProperty() {return gridX;}
    public void setGridX(int gridX) {this.gridX.set(gridX);}
    public int getGridY() {return gridY.get();}
    public IntegerProperty gridYProperty() {return gridY;}
    public void setGridY(int gridY) {this.gridY.set(gridY);}
    public int getGridWidth() {return gridWidth.get();}
    public IntegerProperty gridWidthProperty() {return gridWidth;}
    public void setGridWidth(int gridWidth) {this.gridWidth.set(gridWidth);}
    public int getGridHeight() {return gridHeight.get();}
    public IntegerProperty gridHeightProperty() {return gridHeight;}
    public void setGridHeight(int gridHeight) {this.gridHeight.set(gridHeight);}
    public int getRotation() {return rotation.get();}
    public IntegerProperty rotationProperty() {return rotation;}
    public void setRotation(int rotation) {this.rotation.set(rotation);}



    /********** EVENT DETECTION LOGIC AND EVENT HANDLING ************/
    /**
     * Return true if given coordinates are within the bounds of the object
     * @param x
     * @param y
     * @return
     */
    public boolean isInBounds(double x, double y){
        return ((x>= getLocationX() && x<= getLocationX()+getWidth()) &&
                (y >= getLocationY() && y <= getLocationY()+getHeight()));
    }

    /********* PAINT *****************/
    /**
     * Paint object on the screen
     */
    public void paint(GraphicsContext gc){
        prePaint(gc);
        doPaint(gc);
        postPaint(gc);
    }

    private void prePaint(GraphicsContext gc){
        gc.save();
        manageGCRotation(gc);
    }

    private void manageGCRotation(GraphicsContext gc) {
        if(getRotationStrategy() == RotationStrategy.ROTATE){
            if(getRotation()%4 == 0){
                gc.translate(locationX.get(), locationY.get());
            }else if(getRotation()%4 == 1){
                gc.translate(locationX.get() +getWidth(), locationY.get());
                gc.rotate(90);
            }else if(getRotation()%4 == 2){
                gc.translate(locationX.get() +getWidth(), locationY.get() + getHeight());
                gc.rotate(180);
            }else if(getRotation()%4 == 3){
                gc.translate(locationX.get(), locationY.get() + getHeight());
                gc.rotate(270);
            }
        }else{
            gc.translate(getLocationX(), getLocationY());
        }

    }

    protected abstract void doPaint(GraphicsContext gc);

    private void postPaint(GraphicsContext gc){
        gc.restore();
    }

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

    public void set(int gridX, int gridY, int gridHeight, int gridWidth) {
        this.gridX.set(gridX);
        this.gridY.set(gridY);
        this.gridHeight.set(gridHeight);
        this.gridWidth.set(gridWidth);
        //TODO repaint call
    }

    /******* EVENT HANDLING *******/

    protected void onObjectEntered(Event e){
        hovered = true;
        repaint();
    }
    protected void onObjectExited(Event e){
        hovered = false;
        repaint();
    }
    protected void onObjectDeselected(Event e) {
        selected = false;
        childrenList.forEach(ch -> {
            ch.setSelected(false);
            ch.onObjectDeselected(e);
        });
        repaint();
    }

    protected void onObjectSelected(Event e) {
        selected = true;
        childrenList.forEach(ch -> {
            ch.setSelected(true);
            ch.onObjectSelected(e);
        });
        repaint();
    }

    protected void onObjectDropped(Event event) {
       onObjectMoved(event);
    }

    protected void onObjectDragged(Event event) {
        onObjectMoving(event);
    }

    protected void onObjectMoving(Event event){
        CanvasMouseEvent e = (CanvasMouseEvent)event;
        event.consume();
        eventAggregator.fireEvent( new CanvasMouseEvent(CanvasMouseEvent.OBJECT_MOVING,e.getStartX(), e.getStartY(), e.getLastX(), e.getLastY(), e.getX(), e.getY(), this));
    }

    protected void onObjectMoved(Event event){
        CanvasMouseEvent e = (CanvasMouseEvent)event;
        event.consume();
        eventAggregator.fireEvent( new CanvasMouseEvent(CanvasMouseEvent.OBJECT_MOVED,e.getStartX(), e.getStartY(), e.getLastX(), e.getLastY(), e.getX(), e.getY(), this));
    }


    protected void onDragDetected(Event e) {
    }

    protected void repaint(){//
        eventAggregator.fireEvent(new CanvasPaintEvent(CanvasPaintEvent.REPAINT_OBJECT, this));
    }

    protected void clear(){
        eventAggregator.fireEvent(new CanvasPaintEvent(CanvasPaintEvent.CLEAN_OBJECT, this));
    }

    protected void paint(){
        eventAggregator.fireEvent(new CanvasPaintEvent(CanvasPaintEvent.PAINT_OBJECT, this));
    }

    public enum RotationStrategy{
        DO_NOT_ROTATE,
        ROTATE,
        MOVE_WITH_PARENT_ROTATION
    }

}
