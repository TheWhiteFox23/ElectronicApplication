package cz.thewhiterabbit.electronicapp.events;

import cz.thewhiterabbit.electronicapp.Document;
import javafx.event.Event;
import javafx.event.EventType;

public class DocumentModelEvent extends Event {
    public static final EventType<DocumentModelEvent> ANY = new EventType<>(Event.ANY, "ANY");
    public static final EventType<DocumentModelEvent> DOCUMENT_CLOSED = new EventType<>(Event.ANY, "DOCUMENT_CLOSED");
    public static final EventType<DocumentModelEvent> DOCUMENT_OPENED = new EventType<>(Event.ANY, "DOCUMENT_OPENED");

    private Document document;

    public DocumentModelEvent(EventType<? extends Event> eventType, Document document) {
        super(eventType);
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }

}
