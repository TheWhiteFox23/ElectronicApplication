package cz.thewhiterabbit.electronicapp.view.canvas;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class DrawingAreaEvent extends Event {
    private CanvasObject canvasObject;
    private Property property;
    private Object oldValue;
    private Object newVale;
    private boolean oldSelected;
    private boolean newSelected;


    public static final EventType<DrawingAreaEvent> ANY = new EventType<>(Event.ANY, "DRAWING_AREA_ANY");
    public static final EventType<DrawingAreaEvent> OBJECT_ADDED = new EventType<>(ANY, "OBJECT_ADDED");
    public static final EventType<DrawingAreaEvent> OBJECT_DELETED = new EventType<>(ANY, "OBJECT_DELETED");
    public static final EventType<DrawingAreaEvent> OBJECT_PROPERTY_CHANGE = new EventType<>(ANY, "OBJECT_PROPERTY_CHANGE");
    public static final EventType<DrawingAreaEvent> EDITING_FINISHED = new EventType<>(ANY, "EDITING_FINISHED");
    public static final EventType<DrawingAreaEvent> SELECTION_CHANGED = new EventType<>(ANY, "SELECTION CHANGED");


    public DrawingAreaEvent(EventType<? extends Event> eventType, CanvasObject canvasObject) {
        super(eventType);
        this.canvasObject = canvasObject;
    }

    public DrawingAreaEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType, CanvasObject canvasObject) {
        super(o, eventTarget, eventType);
        this.canvasObject = canvasObject;
    }
    public DrawingAreaEvent(EventType<? extends Event> eventType, CanvasObject canvasObject, boolean oldSelected, boolean newSelected) {
        super(eventType);
        this.canvasObject = canvasObject;
        this.newSelected = newSelected;
        this.oldSelected = oldSelected;
    }

    public DrawingAreaEvent(EventType<? extends Event> eventType, CanvasObject canvasObject, Property integerProperty, Object oldValue, Object newVale) {
        super(eventType);
        this.canvasObject = canvasObject;
        this.property = integerProperty;
        this.oldValue = oldValue;
        this.newVale = newVale;
    }


    public DrawingAreaEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType, CanvasObject canvasObject, Property integerProperty, Object oldValue, Object newVale) {
        super(o, eventTarget, eventType);
        this.canvasObject = canvasObject;
        this.property = integerProperty;
        this.oldValue = oldValue;
        this.newVale = newVale;
    }

    public DrawingAreaEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public DrawingAreaEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType) {
        super(o, eventTarget, eventType);
    }

    public CanvasObject getCanvasObject() {
        return canvasObject;
    }

    public Property getProperty() {
        return property;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newVale;
    }

    public boolean getOldSelected() {
        return oldSelected;
    }

    public boolean getNewSelected() {
        return newSelected;
    }

}
