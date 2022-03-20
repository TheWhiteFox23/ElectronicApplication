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
    public static final EventType<EditControlEvent> COPY = new EventType<>(ANY, "COPY");
    public static final EventType<EditControlEvent> PASTE = new EventType<>(ANY, "PASTE");
    public static final EventType<EditControlEvent> SELECT_ALL = new EventType<>(ANY, "SELECT_ALL");
    public static final EventType<EditControlEvent> DESELECT_ALL = new EventType<>(ANY, "DESELECT_ALL");
    public static final EventType<EditControlEvent> CUT = new EventType<>(ANY, "CUT");
    public static final EventType<EditControlEvent> DELETE = new EventType<>(ANY, "DELETE");
    public static final EventType<EditControlEvent> ROTATE_CLOCKWISE = new EventType<>(ANY, "ROTATE_CLOCKWISE");
    public static final EventType<EditControlEvent> ROTATE_COUNTER_CLOCKWISE = new EventType<>(ANY, "ROTATE_COUNTER_CLOCKWISE");

    private CanvasObject oldObject;
    private CanvasObject newObject;
    private CanvasObject editedObject;

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

    public EditControlEvent(EventType<? extends Event> eventType, CanvasObject editedObject) {
        super(eventType);
        this.editedObject = editedObject;
    }

    public EditControlEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType, CanvasObject editedObject) {
        super(o, eventTarget, eventType);
        this.editedObject = editedObject;
    }

    public CanvasObject getEditedObject() {
        return editedObject;
    }

    public CanvasObject getOldObject() {
        return oldObject;
    }

    public CanvasObject getNewObject() {
        return newObject;
    }
}
