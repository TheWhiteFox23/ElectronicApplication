package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Extends concrete RawObject. One side mapping of the properties.
 */
public abstract class DocumentObject {
    private RawObject rawObject;
    private CanvasObject canvasObject;
    private final List<DocumentObject> children;

    public DocumentObject(RawObject rawObject){
        this.rawObject = rawObject;
        this.children = new ArrayList<>();
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

    public List<DocumentObject> getChildren() {
        return children;
    }
}
