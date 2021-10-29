package cz.thewhiterabbit.electronicapp;

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
    //Listeners
    private List<DocumentManagerListener> listeners;


    private DocumentManager(){
        openDocuments = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    /**
     * Return instance of the DocumentManager
     * @return
     */
    public static DocumentManager getInstance(){
        return documentManager;
    }

    public void addDocumentManagerListener(DocumentManagerListener listener){
        listeners.add(listener);
    }

    @Override
    public void createNewDocument() {
        openDocuments.add(new Document("new_file" + getDocumentList().size())); //TODO place holder method
        listeners.forEach(a -> a.onDocumentModelChange());
    }

    @Override
    public List<Document> getDocumentList() {
        return openDocuments;
    }

    @Override
    public void closeDocument(Document document) {
        openDocuments.remove(document);
        listeners.forEach(a -> a.onDocumentModelChange());
    }
}
