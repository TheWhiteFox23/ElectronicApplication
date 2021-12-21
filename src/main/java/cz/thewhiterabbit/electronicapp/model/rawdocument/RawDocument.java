package cz.thewhiterabbit.electronicapp.model.rawdocument;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Typeless Document. Store the data and is used for XML import/export. It is the backbone of the application
 */
public class RawDocument {
    private RawProperty name;
    private RawProperty filePath;
    private Map<String, RawObject> objectMap;

    public RawDocument(String name){
        this.name = new RawProperty("name", name);
        this.filePath = new RawProperty("file_path", "");
        objectMap = new HashMap<>();
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
    }

    public RawObject getObject(String objectID){
        return objectMap.get(objectID);
    }

    public void removeObject(String objectID){
        if(objectMap.containsKey(objectID))objectMap.remove(objectID);
    }

    public Collection<RawObject> getObjects(){
        return  objectMap.values();
    }
}
