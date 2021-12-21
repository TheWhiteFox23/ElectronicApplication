package cz.thewhiterabbit.electronicapp.model.rawdocument;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RawObject {
    private RawProperty id;
    private RawProperty type;
    private Map<String, RawProperty> propertyMap;

    public RawObject(String id, String type){
        this.id = new RawProperty("name", id);
        this.type = new RawProperty("type", type);
        propertyMap = new HashMap<>();
    }

    public String getId(){
        return id.getValue();
    }

    public String getType(){
        return type.getValue();
    }

    public RawProperty idProperty(){
        return id;
    }

    public RawProperty typeProperty(){
        return type;
    }

    public void addProperty(RawProperty rawProperty){
        propertyMap.put(rawProperty.getName(), rawProperty);
    }

    public RawProperty getProperty(String name){
        return propertyMap.get(name);
    }

    public List<RawProperty> getProperties(){
        return (List<RawProperty>) propertyMap.values();
    }

}
