package cz.thewhiterabbit.electronicapp.canvas;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class DrawingAreaEvent extends Event {
    public static final EventType<DrawingAreaEvent> ANY = new EventType<>(Event.ANY, "DRAWING_AREA_ANY");
    public static final EventType<DrawingAreaEvent> OBJECT_ADDED = new EventType<>(ANY, "OBJECT_ADDED");
    public static final EventType<DrawingAreaEvent> OBJECT_DELETED = new EventType<>(ANY, "OBJECT_DELETED");
    public static final EventType<DrawingAreaEvent> OBJECT_PROPERTY_CHANGE = new EventType<>(ANY, "OBJECT_PROPERTY_CHANGE");
    public static final EventType<DrawingAreaEvent> EDITING_FINISHED = new EventType<>(ANY, "EDITING_FINISHED");

    public DrawingAreaEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public DrawingAreaEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType) {
        super(o, eventTarget, eventType);
    }
}
