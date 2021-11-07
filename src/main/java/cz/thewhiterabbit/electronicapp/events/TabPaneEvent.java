package cz.thewhiterabbit.electronicapp.events;

import cz.thewhiterabbit.electronicapp.Document;
import javafx.event.Event;
import javafx.event.EventType;

public class TabPaneEvent extends Event {
    public static final EventType<TabPaneEvent> TAB_CLOSED = new EventType<>(Event.ANY, "TAB_CLOSED");

    private Document document;

    public TabPaneEvent(EventType<? extends Event> eventType, Document document) {
        super(eventType);
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }
}
