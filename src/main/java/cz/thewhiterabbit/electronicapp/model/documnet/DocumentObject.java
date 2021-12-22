package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObjectImpl;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawProperty;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;

import java.util.List;

/**
 * Extends concrete RawObjectImpl. One side mapping of the properties.
 */
public abstract class DocumentObject extends CanvasObject implements RawObject {
    private RawObject rawObject;

    public RawObject getRawObject() {
        return rawObject;
    }

    public void setRawObject(RawObject rawObject) {
        this.rawObject = rawObject;
    }

    public abstract void init();

    public String getId(){
        return rawObject.getId();
    }
    public String getType(){
        return rawObject.getType();
    }
    public void addProperty(RawProperty rawProperty){
        rawObject.addProperty(rawProperty);
    }
    public RawProperty getProperty(String name){
        return rawObject.getProperty(name);
    }
    public List<RawProperty> getProperties(){
        return rawObject.getProperties();
    }
    public List<RawObjectImpl> getChildren(){
        return rawObject.getChildren();
    }
}
