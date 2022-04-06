package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.objects.GeneralMappingComponent;
import cz.thewhiterabbit.electronicapp.model.objects.RelativePointBackground;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawDocument;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import cz.thewhiterabbit.electronicapp.view.canvas.DrawingAreaEvent;
import cz.thewhiterabbit.electronicapp.view.canvas.model.GridModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private DocumentMode mode = DocumentMode.SCHEMATIC;

    private BooleanProperty changed = new SimpleBooleanProperty(false);

    public Document(RawDocument rawDocument) {
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
                if (!objectMap.containsKey(ro)) {
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

    private void loadDocument(RawDocument rawDocument) {
        for (RawObject ro : rawDocument.getObjects()) {
            DocumentObject o = DocumentObjectFactory.createDocumentObject(ro);
            objectMap.put(ro, o);
            gridModel.add(o);
            if(o instanceof GeneralMappingComponent){
                manageChildMoves(o);
            }else{
                System.out.println(o.getType());
                o.getChildrenList().forEach(System.out::println);
            }
        }
    }

    public GridModel getGridModel() {
        return gridModel;
    }

    public RawDocument getRawDocument() {
        return rawDocument;
    }

    public void add(DocumentObject documentObject) {
        objectMap.put(documentObject.getRawObject(), documentObject);
        gridModel.add(documentObject);
        rawDocument.addObject(documentObject.getRawObject());
    }

    public void remove(DocumentObject documentObject) {
        objectMap.remove(documentObject);
        gridModel.remove(documentObject);
        rawDocument.removeObject(documentObject.getRawObject());
    }

    public void applyCommand(DrawingAreaEvent e) {
        commandService.interpret(e);
        changed.setValue(true);
    }

    public void undo() {
        commandService.undo();
        if (undoEmpty()) changed.setValue(false);
    }

    public void redo() {
        commandService.redo();
    }

    public String getName() {
        return rawDocument.getName();
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public DocumentMode getMode() {
        return mode;
    }

    public void setMode(DocumentMode mode) {
        this.mode = mode;
    }

    public boolean undoEmpty() {
        return commandService.undoEmpty();
    }

    public boolean redoEmpty() {
        return commandService.redoEmpty();
    }

    public boolean isChanged() {
        return changed.get();
    }

    public BooleanProperty changedProperty() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed.set(changed);
    }

    public List<DocumentObject> getDocumentObjects() {
        List<DocumentObject> documentObjectList = new ArrayList<>();
        objectMap.values().forEach(o ->{
            documentObjectList.add(o);
        });
        return documentObjectList;
    }

    private void manageChildMoves(CanvasObject o) {
        int locationX = o.getGridX();
        int locationY = o.getGridY();
        int rotation = o.getRotation();

        o.getChildrenList().forEach(ch -> {
            if(ch instanceof ActivePoint){
                ActivePoint activePoint = (ActivePoint) ch;

                int x = locationX + activePoint.getRelativeLocationX();
                int y = locationY + activePoint.getRelativeLocationY();

                switch (rotation){
                    case 1:{
                        x = locationX + (o.getGridHeight() - activePoint.getRelativeLocationY());
                        y = locationY + activePoint.getRelativeLocationX();
                        break;
                    }
                    case 2:{
                        x = locationX + (o.getGridWidth() - activePoint.getRelativeLocationX());
                        y = locationY + (o.getGridHeight() - activePoint.getRelativeLocationY());
                        break;
                    }
                    case 3:{
                        x = locationX + activePoint.getRelativeLocationY();
                        y = locationY + (o.getGridWidth() - activePoint.getRelativeLocationX());
                        break;
                    }
                }
                activePoint.gridXProperty().setValue(x);
                activePoint.gridYProperty().setValue(y);
            }

        });

    }
}
