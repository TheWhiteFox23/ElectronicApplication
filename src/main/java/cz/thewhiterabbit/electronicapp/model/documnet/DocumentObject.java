package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;

/**
 * Extends concrete RawObject. One side mapping of the properties.
 */
public abstract class DocumentObject {
    private RawObject rawObject;
    private CanvasObject canvasObject;

    public DocumentObject(RawObject rawObject){
        this.rawObject = rawObject;
    }

    public RawObject getRawObject() {
        return rawObject;
    }

    public void setRawObject(RawObject rawObject) {
        this.rawObject = rawObject;
    }

    public CanvasObject getCanvasObject() {
        return canvasObject;
    }

    public void setCanvasObject(CanvasObject canvasObject) {
        this.canvasObject = canvasObject;
    }
}
