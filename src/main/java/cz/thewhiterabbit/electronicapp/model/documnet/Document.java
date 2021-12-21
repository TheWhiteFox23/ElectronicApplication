package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.rawdocument.RawDocument;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import cz.thewhiterabbit.electronicapp.view.canvas.model.GridModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Extension of the RawDocument. Respond to the changes in the raw Document and adjust the corresponding grid model.
 */
public class Document {
    private GridModel gridModel;
    private RawDocument rawDocument;
    private Map<String, DocumentObject> objectMap;

    public Document(RawDocument rawDocument){
        this.rawDocument = rawDocument;
        //TODO init raw document listeners -> property listening is managed by objects its self, manage only object added/deleted
        this.gridModel = new GridModel();

        gridModel.setGridSize(10);
        gridModel.setOriginX(600);
        gridModel.setOriginY(400);

        this.objectMap = new HashMap<>();

        for(RawObject ro: rawDocument.getObjects()){
            DocumentObject o = DocumentObjectFactory.createDocumentObject(ro);
            objectMap.put(ro.getId(), o);
            gridModel.add(o.getCanvasObject());
        }
    }

    public GridModel getGridModel() {
        return gridModel;
    }

    public void setGridModel(GridModel gridModel) {
        this.gridModel = gridModel;
    }

    public RawDocument getRawDocument() {
        return rawDocument;
    }

    public void setRawDocument(RawDocument rawDocument) {
        this.rawDocument = rawDocument;
    }

    public Map<String, DocumentObject> getObjectMap() {
        return objectMap;
    }

    public void setObjectMap(Map<String, DocumentObject> objectMap) {
        this.objectMap = objectMap;
    }
}
