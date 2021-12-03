package cz.thewhiterabbit.electronicapp.events;

import cz.thewhiterabbit.electronicapp.Document;
import cz.thewhiterabbit.electronicapp.DocumentObject;
import javafx.beans.property.ReadOnlyProperty;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class DocumentObjectCommand extends Event {
    public static final EventType<DocumentObjectCommand> CHANGE_PROPERTY = new EventType<>(Event.ANY, "CHANGE_PROPERTY");

    private Document document;
    private DocumentObject documentObject;
    private ReadOnlyProperty property;
    private Number oldVal;
    private Number newVal;

    public DocumentObjectCommand(EventType<? extends Event> eventType, DocumentObject documentObject, ReadOnlyProperty property, Number oldVal, Number newVal) {
        super(eventType);
        this.documentObject = documentObject;
        this.property = property;
        this.oldVal = oldVal;
        this.newVal = newVal;
    }

    public DocumentObjectCommand(Object o, EventTarget eventTarget, EventType<? extends Event> eventType, DocumentObject documentObject, ReadOnlyProperty property, Number oldVal, Number newVal) {
        super(o, eventTarget, eventType);
        this.documentObject = documentObject;
        this.property = property;
        this.oldVal = oldVal;
        this.newVal = newVal;
    }

    public Number getOldVal() {
        return oldVal;
    }

    public void setOldVal(Number oldVal) {
        this.oldVal = oldVal;
    }

    public Number getNewVal() {
        return newVal;
    }

    public void setNewVal(Number newVal) {
        this.newVal = newVal;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public DocumentObject getDocumentComponent() {
        return documentObject;
    }

    public void setDocumentComponent(DocumentObject documentObject) {
        this.documentObject = documentObject;
    }

    public ReadOnlyProperty fxProperty() {
        return property;
    }

}
