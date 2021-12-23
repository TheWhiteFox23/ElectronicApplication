package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.objects.RelativePointBackground;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawDocument;
import cz.thewhiterabbit.electronicapp.view.canvas.model.GridModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Extension of the RawDocument. Respond to the changes in the raw Document and adjust the corresponding grid model.
 */
public class Document {
    private final GridModel gridModel;
    private final RawDocument rawDocument;
    private Map<RawObject, DocumentObject> objectMap;

    public Document(RawDocument rawDocument){
        this.rawDocument = rawDocument;
        this.rawDocument.addListener(new RawDocument.RawDocumentListener() {
            @Override
            public void onRawObjectRemoved(RawObject rawObject) {
                DocumentObject object = objectMap.get(rawObject);
                gridModel.remove(object);
                object.getChildrenList().forEach(o -> gridModel.remove(o));//TODO -> cascade remove children
                objectMap.remove(rawObject);
            }

            @Override
            public void onRawObjectAdded(RawObject ro) {
                if(!objectMap.containsKey(ro)){
                    DocumentObject o = DocumentObjectFactory.createDocumentObject(ro);
                    objectMap.put(ro, o);
                    gridModel.add(o);
                }
            }
        });
        //TODO init raw document listeners -> property listening is managed by objects its self, manage only object added/deleted
        this.gridModel = new GridModel();

        gridModel.setGridSize(10);
        gridModel.setOriginX(600);
        gridModel.setOriginY(400);
        gridModel.add(new RelativePointBackground(gridModel.getCanvas()));

        this.objectMap = new HashMap<>();
        loadDocument(rawDocument);
    }

    private void loadDocument(RawDocument rawDocument){
        for(RawObject ro: rawDocument.getObjects()){
            DocumentObject o = DocumentObjectFactory.createDocumentObject(ro);
            objectMap.put(ro, o);
            gridModel.add(o);
        }
    }

    public GridModel getGridModel() {
        return gridModel;
    }

    public RawDocument getRawDocument() {
        return rawDocument;
    }

    public void add(DocumentObject documentObject){
        objectMap.put(documentObject.toRawObject(), documentObject);
        gridModel.add(documentObject);
        rawDocument.addObject(documentObject.toRawObject());
    }

}
