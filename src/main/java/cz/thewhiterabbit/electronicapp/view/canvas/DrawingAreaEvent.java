package cz.thewhiterabbit.electronicapp.view.canvas;

import javafx.beans.property.IntegerProperty;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class DrawingAreaEvent extends Event {
    private CanvasObject canvasObject;
    private IntegerProperty property;
    private int oldValue;
    private int newVale;


    public static final EventType<DrawingAreaEvent> ANY = new EventType<>(Event.ANY, "DRAWING_AREA_ANY");
    public static final EventType<DrawingAreaEvent> OBJECT_ADDED = new EventType<>(ANY, "OBJECT_ADDED");
    public static final EventType<DrawingAreaEvent> OBJECT_DELETED = new EventType<>(ANY, "OBJECT_DELETED");
    public static final EventType<DrawingAreaEvent> OBJECT_PROPERTY_CHANGE = new EventType<>(ANY, "OBJECT_PROPERTY_CHANGE");
    public static final EventType<DrawingAreaEvent> EDITING_FINISHED = new EventType<>(ANY, "EDITING_FINISHED");
    public static final EventType<DrawingAreaEvent> UNDO = new EventType<>(ANY, "UNDO");//TODO probably move to general event


    public DrawingAreaEvent(EventType<? extends Event> eventType, CanvasObject canvasObject) {
        super(eventType);
        this.canvasObject = canvasObject;
    }

    public DrawingAreaEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType, CanvasObject canvasObject) {
        super(o, eventTarget, eventType);
        this.canvasObject = canvasObject;
    }

    public DrawingAreaEvent(EventType<? extends Event> eventType, CanvasObject canvasObject, IntegerProperty property, int oldValue, int newVale) {
        super(eventType);
        this.canvasObject = canvasObject;
        this.property = property;
        this.oldValue = oldValue;
        this.newVale = newVale;
    }

    public DrawingAreaEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType, CanvasObject canvasObject, IntegerProperty property, int oldValue, int newVale) {
        super(o, eventTarget, eventType);
        this.canvasObject = canvasObject;
        this.property = property;
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

    public IntegerProperty getProperty() {
        return property;
    }

    public int getOldValue() {
        return oldValue;
    }

    public int getNewVale() {
        return newVale;
    }
}
