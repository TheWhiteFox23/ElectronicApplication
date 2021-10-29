package cz.thewhiterabbit.electronicapp;

import java.util.List;

public interface DocumentManagerInterface {
    void createNewDocument();
    List<Document> getDocumentList();
    void closeDocument(Document document);
}
