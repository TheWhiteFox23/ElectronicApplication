package cz.thewhiterabbit.electronicapp.view.canvas;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.view.canvas.model.CanvasModel;
import cz.thewhiterabbit.electronicapp.view.events.CanvasMouseEvent;
import cz.thewhiterabbit.electronicapp.view.events.CanvasPaintEvent;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * General canvas object. Not effected by origin position or zoomAspect
 */
public abstract class CanvasObject {
    private final HashMap<EventType, EventHandler> handlerMap = new HashMap<>();

    private CanvasModel parentModel;

    private CanvasModel.Priority priority = CanvasModel.Priority.NONE;
    private List<CanvasObject> childrenList;
    private CanvasObject parent;

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
    private boolean visible;

    private RotationStrategy rotationStrategy;

    /******EVENT HANDLING******/
    private EventAggregator eventAggregator;

    /************CONSTRUCTORS*******************/
    public CanvasObject() {
        this(0, 0, 0, 0);
    }

    public CanvasObject(double width, double height) {
        this(0, 0, width, height);
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
        this.visible = true;
        //PROPERTIES
        this.gridY = new SimpleIntegerProperty(this, "gridY");
        this.gridX = new SimpleIntegerProperty(this, "gridX");
        this.gridWidth = new SimpleIntegerProperty(this, "gridWidth");
        this.gridHeight = new SimpleIntegerProperty(this, "gridHeight");
        this.rotation = new SimpleIntegerProperty(this, "rotation");

        this.childrenList = new ArrayList<>();
        this.rotationStrategy = RotationStrategy.ROTATE;
        prepareHandlers();
    }

    //TODO deregister handlers
    protected void registerHandlers(EventAggregator eventAggregator) {
        handlerMap.forEach((type, handler)->{
            if(!eventAggregator.contains(handler)){
                eventAggregator.addEventHandler(type,handler);
            }
        });
    }
    private void prepareHandlers() {
        handlerMap.put(CanvasMouseEvent.OBJECT_ENTERED, e -> {
            if (((CanvasMouseEvent) e).getObject() == this) {
                onObjectEntered(e);
            }
        });
        handlerMap.put(CanvasMouseEvent.OBJECT_ENTERED, e -> {
            if (((CanvasMouseEvent) e).getObject() == this) {
                onObjectEntered(e);
            }
        });
        handlerMap.put(CanvasMouseEvent.OBJECT_EXITED, e -> {
            if (((CanvasMouseEvent) e).getObject() == this) {
                onObjectExited(e);
            }
        });
        handlerMap.put(CanvasMouseEvent.OBJECT_SELECTED, e -> {
            if (((CanvasMouseEvent) e).getObject() == this) {
                onObjectSelected(e);
            }
        });
        handlerMap.put(CanvasMouseEvent.OBJECT_DESELECTED, e -> {
            if (((CanvasMouseEvent) e).getObject() == this) {
                onObjectDeselected(e);
            }
        });
        handlerMap.put(CanvasMouseEvent.DESELECT_ALL, e -> onObjectDeselected(e));
        handlerMap.put(CanvasMouseEvent.OBJECTS_DRAG_DETECTED, e -> {
            if (((CanvasMouseEvent) e).getObject() == this) {
                onDragDetected(e);
            }
        });
        handlerMap.put(CanvasMouseEvent.OBJECT_DRAGGED, e -> {
            if (((CanvasMouseEvent) e).getObject() == this) {
                onObjectDragged(e);
            }
        });
        handlerMap.put(CanvasMouseEvent.OBJECT_DRAG_DROPPED, e -> {
            if (((CanvasMouseEvent) e).getObject() == this) {
                onObjectDropped(e);
            }
        });
    }

    public boolean isInBounds(double x, double y) {
        return ((x >= getLocationX() && x <= getLocationX() + getWidth()) &&
                (y >= getLocationY() && y <= getLocationY() + getHeight()));
    }

    public void paint(GraphicsContext gc) {
        prePaint(gc);
        doPaint(gc);
        postPaint(gc);
    }

    private void prePaint(GraphicsContext gc) {
        gc.save();
        manageGCRotation(gc);
    }

    private void manageGCRotation(GraphicsContext gc) {
        if (getRotationStrategy() == RotationStrategy.ROTATE) {
            if (getRotation() % 4 == 0) {
                gc.translate(locationX.get(), locationY.get());
            } else if (getRotation() % 4 == 1) {
                gc.translate(locationX.get() + getWidth(), locationY.get());
                gc.rotate(90);
            } else if (getRotation() % 4 == 2) {
                gc.translate(locationX.get() + getWidth(), locationY.get() + getHeight());
                gc.rotate(180);
            } else if (getRotation() % 4 == 3) {
                gc.translate(locationX.get(), locationY.get() + getHeight());
                gc.rotate(270);
            }
        } else {
            gc.translate(getLocationX(), getLocationY());
        }

    }

    protected abstract void doPaint(GraphicsContext gc);

    private void postPaint(GraphicsContext gc) {
        gc.restore();
    }

    public void clean(GraphicsContext gc) {
        gc.clearRect(getLocationX(), getLocationY(), getWidth(), getHeight());
    }

    public boolean isVisible(double canvasWidth, double canvasHeight) {
        if (!isVisible()) return false;
        return (getLocationY() < canvasHeight
                && getLocationY() + getHeight() > 0
                && getLocationX() < canvasWidth
                && getLocationX() + getWidth() > 0);
    }

    public boolean isInBounds(double locationX, double locationY, double width, double height) {
        if (!isVisible()) return false;
        return (getLocationX() + getWidth() > locationX &&
                getLocationY() + getHeight() > locationY &&
                getLocationY() < locationY + height &&
                getLocationX() < locationX + width);
    }

    public void set(int gridX, int gridY, int gridHeight, int gridWidth) {
        this.gridX.set(gridX);
        this.gridY.set(gridY);
        this.gridHeight.set(gridHeight);
        this.gridWidth.set(gridWidth);
        if (getParentModel() != null) getParentModel().updatePaintProperties(this);
        //TODO repaint call
    }

    /******* EVENT HANDLING *******/

    protected void onObjectEntered(Event e) {
        hovered = true;
        repaint();
    }

    protected void onObjectExited(Event e) {
        hovered = false;
        repaint();
    }

    protected void onObjectDeselected(Event e) {
        eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.SELECTION_CHANGED,this, isSelected(), false));
        childrenList.forEach(ch ->{
            ch.onObjectDeselected(e);
        });
    }

    protected void onObjectSelected(Event e) {
        eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.SELECTION_CHANGED,this, isSelected(), true));
        childrenList.forEach(ch ->{
            ch.onObjectDeselected(e);
        });
    }

    protected void onObjectDropped(Event event) {
        onObjectMoved(event);
    }

    protected void onObjectDragged(Event event) {
        onObjectMoving(event);
    }

    protected void onObjectMoving(Event event) {
        CanvasMouseEvent e = (CanvasMouseEvent) event;
        event.consume();
        eventAggregator.fireEvent(new CanvasMouseEvent(CanvasMouseEvent.OBJECT_MOVING, e.getStartX(), e.getStartY(), e.getLastX(), e.getLastY(), e.getX(), e.getY(), this));
    }

    protected void onObjectMoved(Event event) {
        CanvasMouseEvent e = (CanvasMouseEvent) event;
        event.consume();
        eventAggregator.fireEvent(new CanvasMouseEvent(CanvasMouseEvent.OBJECT_MOVED, e.getStartX(), e.getStartY(), e.getLastX(), e.getLastY(), e.getX(), e.getY(), this));
    }

    public void setPosition(int deltaX, int deltaY){
        int gridX = getGridX() + deltaX;
        int gridY = getGridY() + deltaY;
        getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_PROPERTY_CHANGE, this, gridXProperty(), getGridX(), gridX));
        getEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_PROPERTY_CHANGE, this, gridYProperty(), getGridY(), gridY));
    }

    protected void onDragDetected(Event e) {}


    /******* PAINTING ******/

    protected void repaint() {//
        if(eventAggregator!= null){
            eventAggregator.fireEvent(new CanvasPaintEvent(CanvasPaintEvent.REPAINT_OBJECT, this));
        }
    }

    protected void clear() {
        eventAggregator.fireEvent(new CanvasPaintEvent(CanvasPaintEvent.CLEAN_OBJECT, this));
    }

    protected void paint() {
        eventAggregator.fireEvent(new CanvasPaintEvent(CanvasPaintEvent.PAINT_OBJECT, this));
    }

    public enum RotationStrategy {
        DO_NOT_ROTATE,
        ROTATE,
        MOVE_WITH_PARENT_ROTATION
    }

    public void callForDelete() { //TODO is obsolete -> responsible for removing children should be another component
        eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_DELETED, this));
        if (getChildrenList().size() == 0) return;
        getChildrenList().forEach(ch -> ch.callForDelete());
    }

    /******* GETTERS AND SETTERS ********/
    public double getLocationX() {
        return locationX.get();
    }

    public DoubleProperty locationXProperty() {
        return locationX;
    }

    public void setLocationX(double locationX) {
        this.locationX.set(locationX);
    }

    public double getLocationY() {
        return locationY.get();
    }

    public DoubleProperty locationYProperty() {
        return locationY;
    }

    public void setLocationY(double locationY) {
        this.locationY.set(locationY);
    }

    public double getWidth() {
        return width.get();
    }

    public DoubleProperty widthProperty() {
        return width;
    }

    public void setWidth(double width) {
        this.width.set(width);
    }

    public double getHeight() {
        return height.get();
    }

    public DoubleProperty heightProperty() {
        return height;
    }

    public void setHeight(double height) {
        this.height.set(height);
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    public boolean isDragged() {
        return dragged;
    }

    public void setDragged(boolean dragged) {
        this.dragged = dragged;
    }

    public CanvasModel.Priority getPriority() {
        return priority;
    }

    public void setPriority(CanvasModel.Priority priority) {
        this.priority = priority;
    }

    public EventAggregator getEventAggregator() {
        return eventAggregator;
    }

    public RotationStrategy getRotationStrategy() {
        return rotationStrategy;
    }

    public void setRotationStrategy(RotationStrategy rotationStrategy) {
        this.rotationStrategy = rotationStrategy;
    }

    public CanvasModel getParentModel() {
        return parentModel;
    }

    public void setParentModel(CanvasModel parentModel) {
        this.parentModel = parentModel;
    }

    public List<CanvasObject> getChildrenList() {
        return childrenList;
    }

    public CanvasObject getParent() {
        return parent;
    }

    public void setParent(CanvasObject parent) {
        this.parent = parent;
    }

    /****** PROPERTIES ******/
    public int getGridX() {
        return gridX.get();
    }

    public IntegerProperty gridXProperty() {
        return gridX;
    }

    public void setGridX(int gridX) {
        this.gridX.set(gridX);
    }

    public int getGridY() {
        return gridY.get();
    }

    public IntegerProperty gridYProperty() {
        return gridY;
    }

    public void setGridY(int gridY) {
        this.gridY.set(gridY);
    }

    public int getGridWidth() {
        return gridWidth.get();
    }

    public IntegerProperty gridWidthProperty() {
        return gridWidth;
    }

    public void setGridWidth(int gridWidth) {
        this.gridWidth.set(gridWidth);
    }

    public int getGridHeight() {
        return gridHeight.get();
    }

    public IntegerProperty gridHeightProperty() {
        return gridHeight;
    }

    public void setGridHeight(int gridHeight) {
        this.gridHeight.set(gridHeight);
    }

    public int getRotation() {
        return rotation.get();
    }

    public IntegerProperty rotationProperty() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation.set(rotation);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setEventAggregator(EventAggregator eventAggregator) {
        this.eventAggregator = eventAggregator;
        registerHandlers(eventAggregator);
    }

    public void addChildren(CanvasObject children) {
        childrenList.add(children);
        children.setParent(this);
    }

    public void removeChildren(CanvasObject children) {
        childrenList.remove(children);
        children.setParent(null);
    }
}
