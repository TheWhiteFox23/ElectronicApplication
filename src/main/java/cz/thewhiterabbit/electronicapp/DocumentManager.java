package cz.thewhiterabbit.electronicapp;

import cz.thewhiterabbit.electronicapp.events.DocumentModelEvent;
import javafx.event.ActionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton, holds info about opened document and manages them
 */
public class DocumentManager implements DocumentManagerInterface{
    //Singleton instance
    private static DocumentManager documentManager = new DocumentManager();
    //Logic
    private List<Document> openDocuments;
    private Document activeDocument;



    private DocumentManager(){
        openDocuments = new ArrayList<>();
    }

    /**
     * Return instance of the DocumentManager
     * @return
     */
    public static DocumentManager getInstance(){
        return documentManager;
    }


    @Override
    public void createNewDocument() {
        Document document = new Document("new_file" + getDocumentList().size());
        openDocuments.add(document); //TODO place holder method
        DocumentModelEvent modelEvent = new DocumentModelEvent(DocumentModelEvent.DOCUMENT_OPENED, document);
        GUIEventAggregator.getInstance().fireEvent(modelEvent);
    }

    @Override
    public List<Document> getDocumentList() {
        return openDocuments;
    }

    @Override
    public void closeDocument(Document document) {
        openDocuments.remove(document);
        DocumentModelEvent modelEvent = new DocumentModelEvent(DocumentModelEvent.DOCUMENT_CLOSED, document);
        GUIEventAggregator.getInstance().fireEvent(modelEvent);
    }

}
