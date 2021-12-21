package cz.thewhiterabbit.electronicapp.view.canvas.model;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import cz.thewhiterabbit.electronicapp.view.canvas.DrawingAreaEvent;
import cz.thewhiterabbit.electronicapp.view.events.CanvasPaintEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.canvas.Canvas;

import java.util.ArrayList;
import java.util.List;

/**
 * Is used as the paint cache for canvas, is specific to given Document
 */
public abstract class CanvasModel {
    private Canvas canvas; //todo manage width and height different way
    private final EventAggregator innerEventAggregator;
    private final EventAggregator modelEventAggregator;
    private List<CanvasObject> canvasObjects;
    private final ModelEventManager modelEventManager;

    public CanvasModel(){
        this.innerEventAggregator = new EventAggregator();
        this.modelEventAggregator = new EventAggregator();
        this.canvasObjects = new ArrayList<>();
        this.modelEventManager = new ModelEventManager(this, innerEventAggregator);
    }

    /************GETTERS AND SETTERS***********/
    public Canvas getCanvas() {return canvas;}
    //public EventAggregator getCanvasEventAggregator() {return canvasEventAggregator;}
    public void setCanvas(Canvas canvas) {this.canvas = canvas;}
    //public void setCanvasEventAggregator(EventAggregator canvasEventAggregator) {this.canvasEventAggregator = canvasEventAggregator;}
    public List<CanvasObject> getCanvasObjects() {return canvasObjects;}
    public void setCanvasObjects(List<CanvasObject> canvasObjects) {this.canvasObjects = canvasObjects;}
    public EventAggregator getInnerEventAggregator() {return innerEventAggregator;}
    public EventAggregator getModelEventAggregator() {return modelEventAggregator;}

    /************METHODS**********************/
    //TODO Modify to add and remove handlers
    public void add(CanvasObject object){
        object.setParentModel(this);
        object.setEventAggregator(innerEventAggregator);
        updatePaintProperties(object);
        object.getChildrenList().forEach(this::add);
        addObject(object);
    }

    public abstract void updatePaintProperties(CanvasObject object);

    /**
     * Return list of objects currently visible on the screen
     * @return
     */
    public List<CanvasObject> getVisible(){
        List<CanvasObject> visibleObjects = new ArrayList<>();
        double canvasHeight = getCanvas().getHeight();
        double canvasWidth = getCanvas().getWidth();
        canvasObjects.forEach(o ->{
            if(o.isVisible(canvasWidth, canvasHeight))visibleObjects.add(o);
        });
        return visibleObjects;
    }

    //TODO link to remove DocumentComponent
    public void remove(CanvasObject o){
        canvasObjects.remove(o);
        o.setParentModel(null);
        innerEventAggregator.fireEvent(new CanvasPaintEvent(CanvasPaintEvent.REPAINT));
    }

    public void delete(CanvasObject o){
        innerEventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_DELETED, o));
        if(o.getChildrenList().size() != 0)o.getChildrenList().forEach(ob -> new DrawingAreaEvent(DrawingAreaEvent.OBJECT_DELETED, ob));
    }

    public boolean containsObject(CanvasObject o){
        return canvasObjects.contains(o);
    }

    /**
     * Look for abject on given scree coordinates and returns it if there is any.
     * @param x X coordinate on the screen
     * @param y Y coordinate on the screen
     * @return Object if there is any or NULL
     */
    public CanvasObject getObject(double x, double y){
        List<CanvasObject> visible = getVisible();
        for (int i = visible.size()-1; i>=0; i--){
            CanvasObject o = visible.get(i);
            if(o.isInBounds(x, y)) return o;
        }
        return null;
    }

    /**
     * Get all object in the given bounds of the screen
     * @param locationX
     * @param locationY
     * @param width
     * @param height
     * @return List of CanvasObjects in given bounds
     */
    public List<CanvasObject> getInBounds(double locationX, double locationY, double width, double height){
        List<CanvasObject> visibleObjects = new ArrayList<>();
        canvasObjects.forEach(o ->{
            if(o.isInBounds(locationX, locationY, width, height))visibleObjects.add(o);
        });
        return visibleObjects;
    }

    /**
     * Add object to the canvasObjects list to the proper index
     * @param canvasObject
     */
    private void addObject(CanvasObject canvasObject){
        Priority objectPriority = canvasObject.getPriority();
        int index = getIndex(objectPriority);
        canvasObjects.add(index, canvasObject);

    }

    public List<CanvasObject> getAll(){
        return canvasObjects;
    }

    /**
     * Calculate index for inserting the object base on the priority
     * @param priority
     * @return
     */
    private int getIndex(Priority priority){
        for(int i = 0; i< canvasObjects.size(); i++){
            CanvasObject object = canvasObjects.get(i);
            if(object.getPriority().getPriorityIndex()> priority.getPriorityIndex()){
                return i;
            }
        }
        return canvasObjects.size();
    }

    /***** EVENTS *****/
    public void addEventHandler(EventType eventType, EventHandler eventHandler){
        modelEventAggregator.addEventHandler(eventType, eventHandler);
    }

    public void notify(Event event){
        modelEventAggregator.fireEvent(event);
    }

    public void removeEventHandler(EventType eventType, EventHandler eventHandler){
        modelEventAggregator.removeEventHandler(eventType, eventHandler);
    }

    public List<CanvasObject> getSelectedObject(){
        List<CanvasObject> selected = new ArrayList<>();
        getCanvasObjects().forEach(o -> {
            if(o.isSelected())selected.add(o);
        });
        return selected;
    }

    public enum Priority {
        ALWAYS_ON_BOTTOM(0),
        LOW(25),
        NONE(50),
        HIGH(75),
        ALWAYS_ON_TOP(100);

        private final int priorityIndex;

        Priority(int priorityIndex){
            this.priorityIndex = priorityIndex;
        }

        public int getPriorityIndex() {
            return priorityIndex;
        }
    }









}

