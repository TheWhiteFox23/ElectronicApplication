package cz.thewhiterabbit.electronicapp.model.rawdocument;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.*;

/**
 * Typeless Document. Store the data and is used for XML import/export. It is the backbone of the application
 */
public class RawDocument {
    private List<RawDocumentListener> listeners;
    private RawProperty name;
    private RawProperty filePath;
    private ObservableMap<String, RawObject> objectMap;

    public RawDocument(String name){
        this.name = new RawProperty("name", name);
        this.filePath = new RawProperty("file_path", "");
        this.objectMap = FXCollections.observableMap(new HashMap<>());
        this.listeners = new ArrayList<>();
    }

    public String getName(){
        return name.getValue();
    }

    public void setName(String name){
        this.name.setValue(name);
    }

    public String getFilePath(){
        return filePath.getValue();
    }

    public void setFilePath(String filePath){
        this.filePath.setValue(filePath);
    }

    public RawProperty nameProperty(){
        return this.name;
    }

    public RawProperty filePathProperty(){
        return this.filePath;
    }

    public void addObject(RawObject object){
        objectMap.put(object.getId(), object);
        listeners.forEach(l -> l.onRawObjectAdded(object));
    }

    public RawObject getObject(String objectID){
        return objectMap.get(objectID);
    }

    public void removeObject(String objectID){
        if(objectMap.containsKey(objectID)){
            listeners.forEach(l -> l.onRawObjectRemoved(objectMap.get(objectID)));
            objectMap.remove(objectID);
        }
    }

    public ObservableMap<String, RawObject> getObjects(){
        return  objectMap;
    }

    public void addListener(RawDocumentListener listener){
        this.listeners.add(listener);
    }

    public interface RawDocumentListener{
        void onRawObjectRemoved(RawObject rawObject);
        void onRawObjectAdded(RawObject rawObject);
    }
}
