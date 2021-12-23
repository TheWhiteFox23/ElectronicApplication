package cz.thewhiterabbit.electronicapp.model.rawdocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RawObject {
    private RawProperty type;
    private Map<String, RawProperty> propertyMap;
    private final List<RawObject> children;

    public RawObject(String type){
        this.type = new RawProperty("type", type);
        this.propertyMap = new HashMap<>();
        this.children = new ArrayList<>();
    }

    public String getType(){
        return type.getValue();
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

    public List<RawObject> getChildren() {
        return children;
    }
}
