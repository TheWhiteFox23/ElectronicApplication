package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
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

    public void setRawObject(RawObject rawObject) {
        this.rawObject = rawObject;
    }

    public abstract void init();
}
