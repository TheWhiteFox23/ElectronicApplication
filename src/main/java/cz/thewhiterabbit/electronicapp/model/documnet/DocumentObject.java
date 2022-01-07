package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.property.ComponentAnnotationProcessor;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawProperty;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import javafx.beans.property.Property;

import java.util.List;

/**
 * Extends concrete RawObject. One side mapping of the properties.
 */
public abstract class DocumentObject extends CanvasObject {
    private RawObject rawObject;

    public RawObject getRawObject() {
        if(rawObject == null) setRawObject(toRawObject());
        return rawObject;
    }

    public void setRawObject(RawObject rawObject)
    {
        this.rawObject = rawObject;
        init();
        mapProperties();
    }

    public abstract void init();

    public abstract void mapProperties();

    public abstract RawObject toRawObject();

    public abstract String getType();

    public RawProperty getProperty(String name) {
        if(rawObject == null)setRawObject(toRawObject());
        return rawObject.getProperty(name);
    }

    public List<RawProperty> getProperties() {
        if(rawObject == null)setRawObject(toRawObject());
        return rawObject.getProperties();
    }

    public List<RawObject> getChildren() {
        return rawObject.getChildren();
    }
}
