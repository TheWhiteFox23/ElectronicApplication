package cz.thewhiterabbit.electronicapp;

import java.util.ArrayList;
import java.util.List;

public class Document {
    /** DOCUMENT PROPERTIES **/
    private String fileName;

    /** COMPONENTS LIST **/
    private List<DocumentObject> componentList;

    /**** CONSTRUCTORS ****/
    public Document(String fileName){
        this.fileName = fileName;
        componentList = new ArrayList<>();
    }

    /****GETTERS AND SETTERS ****/
    public String getFileName() {return fileName;}
    public void setFileName(String fileName) {this.fileName = fileName;}
    public List<DocumentObject> getComponentList() {return componentList;}
    public void setComponentList(List<DocumentObject> componentList) {this.componentList = componentList;}

    /****METHODS ****/
    public void addComponent(DocumentObject documentObject){
        this.componentList.add(documentObject);
    }

    public void removeComponent(DocumentObject documentObject){
        this.componentList.remove(documentObject);
    }
}
