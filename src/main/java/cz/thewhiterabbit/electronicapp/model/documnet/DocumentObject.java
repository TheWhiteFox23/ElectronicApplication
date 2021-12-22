package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;

import java.util.List;

/**
 * Extends concrete RawObject. One side mapping of the properties.
 */
public interface DocumentObject {
    void init();
    RawObject getRawObject();
    void setRawObject(RawObject rawObject);
    CanvasObject getCanvasObject();
    void setCanvasObject(CanvasObject canvasObject);
    List<CanvasObject> getChildren();
}
