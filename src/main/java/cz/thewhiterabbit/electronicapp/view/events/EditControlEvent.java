package cz.thewhiterabbit.electronicapp.view.events;

import cz.thewhiterabbit.electronicapp.view.controllers.FileMenuController;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class EditControlEvent extends Event {
    public static final EventType<EditControlEvent> ANY = new EventType<>(Event.ANY, "EDIT_CONTROL_ENY");
    public static final EventType<EditControlEvent> UNDO = new EventType<>(ANY, "UNDO");
    public static final EventType<EditControlEvent> REDO = new EventType<>(ANY, "REDO");


    public EditControlEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public EditControlEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType) {
        super(o, eventTarget, eventType);
    }
}
