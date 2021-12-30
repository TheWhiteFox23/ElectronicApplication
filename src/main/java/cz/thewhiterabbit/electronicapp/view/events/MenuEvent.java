package cz.thewhiterabbit.electronicapp.view.events;

import cz.thewhiterabbit.electronicapp.model.documnet.Document;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class MenuEvent extends Event {
    public static final EventType<MenuEvent> ANY = new EventType<>(Event.ANY, "MENU_ANY");
    public static final EventType<MenuEvent> NEW_DOCUMENT = new EventType<>(ANY, "NEW_DOCUMENT");
    public static final EventType<MenuEvent> CLOSE_DOCUMENT = new EventType<>(ANY, "CLOSE_DOCUMENT");
    public static final EventType<MenuEvent> CHANGE_ACTIVE_DOCUMENT = new EventType<>(ANY, "CHANGE_ACTIVE_DOCUMENT");
    public static final EventType<MenuEvent> OPEN_FILE = new EventType<>(ANY, "OPEN_FILE");
    public static final EventType<MenuEvent> SAVE_FILE = new EventType<>(ANY, "SAVE_FILE");
    public static final EventType<MenuEvent> SAVE_FILE_AS = new EventType<>(ANY, "SAVE_FILE_AS");

    private Document document;

    public MenuEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public MenuEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType) {
        super(o, eventTarget, eventType);
    }

    public MenuEvent(EventType<? extends Event> eventType, Document document) {
        super(eventType);
        this.document = document;
    }

    public MenuEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType, Document document) {
        super(o, eventTarget, eventType);
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }
}
