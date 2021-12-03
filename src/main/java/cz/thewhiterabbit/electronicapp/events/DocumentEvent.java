package cz.thewhiterabbit.electronicapp.events;

import cz.thewhiterabbit.electronicapp.Document;
import cz.thewhiterabbit.electronicapp.DocumentObject;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

import java.util.List;

public class DocumentEvent extends Event {
    public static final EventType<DocumentEvent> COMPONENTS_ADDED = new EventType<>(Event.ANY, "COMPONENTS_ADDED");
    public static final EventType<DocumentEvent> COMPONENTS_REMOVED = new EventType<>(Event.ANY, "COMPONENTS_REMOVED");
    public static final EventType<DocumentEvent> COMPONENTS_MODIFIED = new EventType<>(Event.ANY, "COMPONENTS_MODIFIED");

    private Document document;
    private List<DocumentObject> documentObjects;

    /*** CONSTRUCTORS ***/
    public DocumentEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public DocumentEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType) {
        super(o, eventTarget, eventType);
    }

    public DocumentEvent(EventType<? extends Event> eventType, Document document, List<DocumentObject> documentObjects) {
        super(eventType);
        this.document = document;
        this.documentObjects = documentObjects;
    }

    public DocumentEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType, Document document, List<DocumentObject> documentObjects) {
        super(o, eventTarget, eventType);
        this.document = document;
        this.documentObjects = documentObjects;
    }

    /***GETTERS AND SETTERS***/
    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public List<DocumentObject> getDocumentComponents() {
        return documentObjects;
    }

    public void setDocumentComponents(List<DocumentObject> documentObjects) {
        this.documentObjects = documentObjects;
    }
}
