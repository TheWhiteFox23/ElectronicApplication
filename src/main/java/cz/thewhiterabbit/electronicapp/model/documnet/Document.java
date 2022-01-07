package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.objects.RelativePointBackground;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawDocument;
import cz.thewhiterabbit.electronicapp.view.canvas.DrawingAreaEvent;
import cz.thewhiterabbit.electronicapp.view.canvas.model.GridModel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Extension of the RawDocument. Respond to the changes in the raw Document and adjust the corresponding grid model.
 */
public class Document {
    private final CommandService commandService = new CommandService(this);

    private final GridModel gridModel;
    private final RawDocument rawDocument;

    private Map<RawObject, DocumentObject> objectMap;

    private File file;

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
        objectMap.put(documentObject.getRawObject(), documentObject);
        gridModel.add(documentObject);
        rawDocument.addObject(documentObject.getRawObject());
    }

    public void remove(DocumentObject documentObject){
        objectMap.remove(documentObject);
        gridModel.remove(documentObject);
        rawDocument.removeObject(documentObject.getRawObject());
    }

    public void applyCommand(DrawingAreaEvent e){
        commandService.interpret(e);
    }

    public void undo(){
        commandService.undo();
    }

    public void redo(){
        commandService.redo();
    }

    public String getName(){
        return rawDocument.getName();
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
