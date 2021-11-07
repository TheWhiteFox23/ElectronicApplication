package cz.thewhiterabbit.electronicapp.events;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class MenuEvent extends Event {
    //public static final EventType<MenuEvent> ANY = new EventType<>(Event.ANY, "ANY");
    public static final EventType<MenuEvent> NEW_FILE = new EventType<>(Event.ANY, "NEW_FILE");

    public MenuEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public MenuEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType) {
        super(o, eventTarget, eventType);
    }


}
