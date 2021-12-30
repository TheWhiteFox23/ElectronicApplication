package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.IEventAggregator;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawDocument;

import cz.thewhiterabbit.electronicapp.model.rawdocument.TestRawDocument;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocumentManager implements IEventAggregator {
    private final FileService fileService = new FileService();
    private final List<Document> documents = new ArrayList<>();
    private final EventAggregator eventAggregator = new EventAggregator();
    private Document activeDocument;

    public DocumentManager(){

    }

    public Document createNewDocument(){
        RawDocument rawDocument = new TestRawDocument("new document"); //TODO-> remove after debugging
        Document document = new Document(rawDocument); //TODO -> document names management
        addDocument(document);
        return document;
    }

    private void addDocument(Document document) {
        documents.add(document);
        activeDocument = document;
        fireEvent(new DocumentManagerEvent(DocumentManagerEvent.DOCUMENT_OPENED, document));
        setActiveDocument(document);
    }

    public void loadDocument(File file){
        try {
            Document document = fileService.load(file);
            addDocument(document);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public void closeDocument(Document document){
        if(document == activeDocument){
            if(documents.size() != 1){
                int index = documents.indexOf(document);
                documents.remove(document);
                if(index == documents.size())index--;
                setActiveDocument(documents.get(index));
            }
        }else {
            documents.remove(document);
        }
        fireEvent(new DocumentManagerEvent(DocumentManagerEvent.DOCUMENT_CLOSED, document));
    }
    public void saveDocument(File file){
        try {
            fileService.save(activeDocument, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setActiveDocument(Document document){
        if(documents.contains(document)){
            activeDocument = document;
            fireEvent(new DocumentManagerEvent(DocumentManagerEvent.ACTIVE_DOCUMENT_CHANGED, document));
        }
    }

    @Override
    public <T extends Event> void fireEvent(T event) {
        eventAggregator.fireEvent(event);
    }

    @Override
    public <T extends EventType> void addEventHandler(T eventType, EventHandler handler) {
        eventAggregator.addEventHandler(eventType, handler);
    }

    @Override
    public void removeEventHandler(EventType eventType, EventHandler eventHandler) {
        eventAggregator.removeEventHandler(eventType, eventHandler);
    }

    @Override
    public boolean contains(EventHandler eventHandler) {
        return eventAggregator.contains(eventHandler);
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public EventAggregator getEventAggregator() {
        return eventAggregator;
    }

    public Document getActiveDocument() {
        return activeDocument;
    }

    public static class DocumentManagerEvent extends Event{
        public static final EventType<DocumentManagerEvent> ANY = new EventType<>(Event.ANY, "DOCUMENT_MANAGER_ANY");
        public static final EventType<DocumentManagerEvent> DOCUMENT_OPENED = new EventType<>(ANY, "DOCUMENT_OPENED");
        public static final EventType<DocumentManagerEvent> DOCUMENT_CLOSED = new EventType<>(ANY, "DOCUMENT_CLOSED");
        public static final EventType<DocumentManagerEvent> ACTIVE_DOCUMENT_CHANGED = new EventType<>(ANY, "ACTIVE_DOCUMENT_CHANGED");

        //TODO DOCUMENT_SAVED and DOCUMENT_CHANGED events

        private Document document;

        public DocumentManagerEvent(EventType<? extends Event> eventType, Document document) {
            super(eventType);
            this.document = document;
        }

        public DocumentManagerEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType, Document document) {
            super(o, eventTarget, eventType);
            this.document = document;
        }

        public Document getDocument() {
            return document;
        }
    }
}
