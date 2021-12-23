package cz.thewhiterabbit.electronicapp.model.rawdocument;

import java.util.*;

/**
 * Typeless Document. Store the data and is used for XML import/export. It is the backbone of the application
 */
public class RawDocument {
    private final String NAME_PROPERTY = "NAME";
    private final String FILE_PATH_PROPERTY = "FILE_PATH";

    private List<RawDocumentListener> listeners;
    private RawProperty name;
    private RawProperty filePath;
    private List<RawObject> objectMap;

    public RawDocument(String name){
        this.name = new RawProperty(NAME_PROPERTY, name);
        this.filePath = new RawProperty(FILE_PATH_PROPERTY, "");
        this.objectMap = new ArrayList<>();
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
        objectMap.add(object);
        listeners.forEach(l -> l.onRawObjectAdded(object));
    }

    public void removeObject(RawObject rawObject){
        if(objectMap.contains(rawObject)){
            listeners.forEach(l -> l.onRawObjectRemoved(rawObject));
            objectMap.remove(rawObject);
        }
    }

    public List<RawObject> getObjects(){
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
