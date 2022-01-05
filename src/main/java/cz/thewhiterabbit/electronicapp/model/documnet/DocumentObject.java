package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawProperty;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;

import java.util.List;

/**
 * Extends concrete RawObject. One side mapping of the properties.
 */
public abstract class DocumentObject extends CanvasObject {
    private RawObject rawObject;

    public RawObject getRawObject() {
        return rawObject;
    }

    public void setRawObject(RawObject rawObject)
    {
        this.rawObject = rawObject;
        //init();
        //mapProperties();
    }

    public abstract void init();

    public abstract void mapProperties();

    public abstract RawObject toRawObject();

    public abstract String getType();

    public void addProperty(RawProperty rawProperty) {
        rawObject.addProperty(rawProperty);
    }

    public RawProperty getProperty(String name) {
        if(rawObject == null)toRawObject();
        return rawObject.getProperty(name);
    }

    public List<RawProperty> getProperties() {
        return rawObject.getProperties();
    }

    public List<RawObject> getChildren() {
        return rawObject.getChildren();
    }
}
