package cz.thewhiterabbit.electronicapp;

import java.util.ArrayList;
import java.util.List;

public class Document {
    /** DOCUMENT PROPERTIES **/
    private String fileName;

    /** COMPONENTS LIST **/
    private List<DocumentComponent> componentList;

    /**** CONSTRUCTORS ****/
    public Document(String fileName){
        this.fileName = fileName;
        componentList = new ArrayList<>();
    }

    /****GETTERS AND SETTERS ****/
    public String getFileName() {return fileName;}
    public void setFileName(String fileName) {this.fileName = fileName;}
    public List<DocumentComponent> getComponentList() {return componentList;}
    public void setComponentList(List<DocumentComponent> componentList) {this.componentList = componentList;}

    /****METHODS ****/
    public void addComponent(DocumentComponent documentComponent){
        this.componentList.add(documentComponent);
    }

    public void removeComponent(DocumentComponent documentComponent){
        this.componentList.remove(documentComponent);
    }
}
