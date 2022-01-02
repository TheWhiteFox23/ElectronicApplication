package cz.thewhiterabbit.electronicapp.view.events;

import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import cz.thewhiterabbit.electronicapp.view.controllers.FileMenuController;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class EditControlEvent extends Event {
    public static final EventType<EditControlEvent> ANY = new EventType<>(Event.ANY, "EDIT_CONTROL_ENY");
    public static final EventType<EditControlEvent> UNDO = new EventType<>(ANY, "UNDO");
    public static final EventType<EditControlEvent> REDO = new EventType<>(ANY, "REDO");
    public static final EventType<EditControlEvent> ACTIVE_OBJECT_CHANGE = new EventType<>(ANY, "ACTIVE_OBJECT_CHANGE");

    private CanvasObject oldObject;
    private CanvasObject newObject;

    public EditControlEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public EditControlEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType) {
        super(o, eventTarget, eventType);
    }

    public EditControlEvent(EventType<? extends Event> eventType, CanvasObject oldObject, CanvasObject newObject) {
        super(eventType);
        this.oldObject = oldObject;
        this.newObject = newObject;
    }

    public EditControlEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType, CanvasObject oldObject, CanvasObject newObject) {
        super(o, eventTarget, eventType);
        this.oldObject = oldObject;
        this.newObject = newObject;
    }

    public CanvasObject getOldObject() {
        return oldObject;
    }

    public CanvasObject getNewObject() {
        return newObject;
    }
}
