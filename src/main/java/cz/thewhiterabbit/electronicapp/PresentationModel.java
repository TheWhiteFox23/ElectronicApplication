package cz.thewhiterabbit.electronicapp;

import java.util.ArrayList;
import java.util.List;

public class PresentationModel {
    private List<Document> openDocuments = new ArrayList<>();

    public List<Document> getOpenDocuments(){
        return openDocuments;
    }
}
