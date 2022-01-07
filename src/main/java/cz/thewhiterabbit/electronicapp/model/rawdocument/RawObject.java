package cz.thewhiterabbit.electronicapp.model.rawdocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RawObject {
    private final String TYPE_PROPERTY = "TYPE";

    private Map<String, RawProperty> propertyMap;
    private final List<RawObject> children;

    public RawObject(String type){
        this.propertyMap = new HashMap<>();
        this.children = new ArrayList<>();
        addProperty(new RawProperty(TYPE_PROPERTY, type));
    }

    public RawObject(){
        this.propertyMap = new HashMap<>();
        this.children = new ArrayList<>();
    }

    public String getType(){
        return propertyMap.get(TYPE_PROPERTY).getValue();
    }

    public void addProperty(RawProperty rawProperty){
        propertyMap.put(rawProperty.getName(), rawProperty);
    }

    public void removeProperty(RawProperty rawProperty){
        propertyMap.remove(rawProperty.getName());
    }

    public RawProperty getProperty(String name){
        return propertyMap.get(name);
    }

    public List<RawProperty> getProperties(){
        List<RawProperty> properties = new ArrayList<>();
        propertyMap.values().forEach(p ->{
            properties.add(p);
        });
        return properties;
    }

    public List<RawObject> getChildren() {
        return children;
    }
}
