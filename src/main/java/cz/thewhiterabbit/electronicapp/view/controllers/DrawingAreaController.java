package cz.thewhiterabbit.electronicapp.view.controllers;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.model.components.Component;
import cz.thewhiterabbit.electronicapp.model.documnet.*;
import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import cz.thewhiterabbit.electronicapp.view.canvas.DrawingAreaEvent;
import cz.thewhiterabbit.electronicapp.view.canvas.DrawingCanvas;
import cz.thewhiterabbit.electronicapp.view.canvas.model.GridModel;
import cz.thewhiterabbit.electronicapp.view.events.CanvasPaintEvent;
import cz.thewhiterabbit.electronicapp.view.events.EditControlEvent;
import cz.thewhiterabbit.electronicapp.view.events.MenuEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrawingAreaController {
    @FXML
    AnchorPane canvas;
    @FXML
    DrawingCanvas drawingArea;
    @FXML
    PropertiesPaneController propertiesPaneController;
    @FXML
    DrawingAreaControlMenuController drawingAreaControlMenuController;

    private final DocumentManager documentManager = new DocumentManager();
    private final EventAggregator eventAggregator = GUIEventAggregator.getInstance();
    private final HashMap<EventType, EventHandler> handlerMap = new HashMap<>();
    private final List<CanvasObject> copyMemory = new ArrayList<>();

    private DocumentObject toPaint;

    private final ContextMenu contextMenu = new ContextMenu();

    @FXML
    private void initialize() {
        registerListeners();
        prepareHandlers();
        initDrawingAreaDropDetection(drawingArea);
    }

    private void registerListeners() {
        //event propagation
        documentManager.addEventHandler(DocumentManager.DocumentManagerEvent.ANY, h -> eventAggregator.fireEvent(h.getEventType(), h));
        documentManager.addEventHandler(DocumentManager.DocumentManagerEvent.ACTIVE_DOCUMENT_CHANGED, h -> {
            setDocument(((DocumentManager.DocumentManagerEvent) h).getDocument());
        });

        eventAggregator.addEventHandler(MenuEvent.NEW_DOCUMENT, e -> {
            documentManager.createNewDocument();
        });
        eventAggregator.addEventHandler(MenuEvent.CLOSE_DOCUMENT, e -> {
            Document document = ((MenuEvent) e).getDocument();
            documentManager.closeDocument(document);
        });
        eventAggregator.addEventHandler(MenuEvent.CHANGE_ACTIVE_DOCUMENT, e -> {
            Document document = ((MenuEvent) e).getDocument();
            documentManager.setActiveDocument(document);
        });
        eventAggregator.addEventHandler(MenuEvent.SAVE_FILE, e -> {
            onSaveFile();
        });
        eventAggregator.addEventHandler(MenuEvent.SAVE_FILE_AS, e -> {
            onSaveFileAs();
        });
        eventAggregator.addEventHandler(MenuEvent.OPEN_FILE, e -> {
            onOpenFile();
        });
        eventAggregator.addEventHandler(DrawingAreaEvent.ANY, e -> {
            documentManager.getActiveDocument().applyCommand((DrawingAreaEvent) e);
        });

        eventAggregator.addEventHandler(EditControlEvent.COPY, h -> {
            onCopy();
        });
        eventAggregator.addEventHandler(EditControlEvent.PASTE, h -> {
            onPaste();
        });
        eventAggregator.addEventHandler(EditControlEvent.SELECT_ALL, h -> {
            onSelectAll();
        });
        eventAggregator.addEventHandler(EditControlEvent.DESELECT_ALL, h -> {
            onDeselectAll();
        });
        eventAggregator.addEventHandler(EditControlEvent.CUT, h -> {
            onCut();
        });
        eventAggregator.addEventHandler(EditControlEvent.DELETE, h -> {
            onDelete();
        });
        eventAggregator.addEventHandler(EditControlEvent.ROTATE_CLOCKWISE, h -> {
            onRotateRight();
        });
        eventAggregator.addEventHandler(EditControlEvent.ROTATE_COUNTER_CLOCKWISE, h -> {
            onRotateLeft();
        });

        eventAggregator.addEventHandler(MenuEvent.CLEAN_CANVAS, h->{
            drawingArea.clear();
            drawingArea.setModel(null);
        });
        eventAggregator.addEventHandler(EditControlEvent.UNDO, h -> {
            if (documentManager.getActiveDocument() != null) {
                documentManager.getActiveDocument().undo();
            }

        });
        eventAggregator.addEventHandler(EditControlEvent.REDO, h -> {
            if (documentManager.getActiveDocument() != null) {
                documentManager.getActiveDocument().redo();
            }
        });

        drawingArea.addEventHandler(MouseEvent.MOUSE_PRESSED, h->{
            if(drawingArea.getModel() != null){
                if(h.isSecondaryButtonDown()){
                    //showContextMenu(h, drawingArea.getModel().getSelected());
                    //System.out.println("Show context menu");
                }else if(h.isPrimaryButtonDown()){
                    contextMenu.hide();
                }
            }
        });

        drawingArea.setOnContextMenuRequested(e->{
            showContextMenu();
            contextMenu.show(canvas.getScene().getWindow(), e.getScreenX(), e.getScreenY());
        });
        eventAggregator.addEventHandler(MenuEvent.SWITCH_MODE_SIMULATION, e->{
            if(documentManager.getActiveDocument() != null){
                documentManager.getActiveDocument().setMode(DocumentMode.SIMULATION);
                eventAggregator.fireEvent(new MenuEvent(MenuEvent.MODE_CHANGED, documentManager.getActiveDocument()));
            }
        });
        eventAggregator.addEventHandler(MenuEvent.SWITCH_MODE_SCHEMATIC, e->{
            if(documentManager.getActiveDocument() != null) {
                documentManager.getActiveDocument().setMode(DocumentMode.SCHEMATIC);
                eventAggregator.fireEvent(new MenuEvent(MenuEvent.MODE_CHANGED, documentManager.getActiveDocument()));
            }
        });
    }

    private void showContextMenu() {
        // create menuitems
        if(contextMenu.getItems().size() == 0){
            MenuItem menuItem1 = new MenuItem("menu item 1");
            MenuItem menuItem2 = new MenuItem("menu item 2");
            MenuItem menuItem3 = new MenuItem("menu item 3");

            // add menu items to menu
            contextMenu.getItems().add(menuItem1);
            contextMenu.getItems().add(menuItem2);
            contextMenu.getItems().add(menuItem3);
        }
        //contextMenu.show(drawingArea,);
    }

    private void onRotateRight() {
        GridModel parentModel = documentManager.getActiveDocument().getGridModel();
        List<CanvasObject> selected = parentModel.getSelected();
        selected.forEach(o -> {
            //o.setRotation((o.getRotation() + 1) % 4);
            eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_PROPERTY_CHANGE, o, o.rotationProperty(), o.getRotation(),(o.getRotation() + 1) % 4));
            manageChildMoves(o);
            drawingArea.clear();
            drawingArea.paint();
        });
        eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.EDITING_FINISHED));
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
                eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_PROPERTY_CHANGE, activePoint, activePoint.gridXProperty(), activePoint.getGridX(), x));
                eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_PROPERTY_CHANGE, activePoint, activePoint.gridYProperty(), activePoint.getGridY(), y));
            }

        });

    }

    private void onRotateLeft() {
        GridModel parentModel = documentManager.getActiveDocument().getGridModel();
        List<CanvasObject> selected = parentModel.getSelected();
        selected.forEach(o -> {
            int rotation = 0;
            if(o.getRotation() == 0){
                rotation = 3;
            }else{
                rotation = o.getRotation() - 1;
            }
            eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_PROPERTY_CHANGE, o, o.rotationProperty(), o.getRotation(),rotation));
            manageChildMoves(o);
            drawingArea.clear();
            drawingArea.paint();
        });
        eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.EDITING_FINISHED));

    }

    private void onOpenFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        File file = fileChooser.showOpenDialog(drawingArea.getScene().getWindow());
        documentManager.loadDocument(file);
    }

    private void onSaveFileAs() {
        if (documentManager.getActiveDocument() != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save as..."); //TODO move to string properties
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("AEON", "*.aeon", "*.AEON"));//TODO replace with final file type
            File file = fileChooser.showSaveDialog(drawingArea.getScene().getWindow());

            Document document = documentManager.getActiveDocument();
            document.setFile(file);
            document.getRawDocument().setName(file.getName().replace(".aeon", ""));//TODO notify listeners
            if(documentManager.saveDocument(file)){
                eventAggregator.fireEvent(new DocumentManager.DocumentManagerEvent(DocumentManager.DocumentManagerEvent.DOCUMENT_RENAMED, document));
            }

        }
    }

    private void onSaveFile() {
        if (documentManager.getActiveDocument() != null) {
            if (documentManager.getActiveDocument().getFile() != null) {
                documentManager.saveDocument(documentManager.getActiveDocument().getFile());
            } else {
                onSaveFileAs();
            }
        }

    }

    private void setDocument(Document document) {
        GridModel model = document.getGridModel();
        drawingArea.setModel(model);
        handlerMap.forEach((type, handler) -> {
            if (!model.getModelEventAggregator().contains(handler)) {
                model.getModelEventAggregator().addEventHandler(type, handler);
            }
        });
        drawingArea.clear();
        drawingArea.paint();
    }

    private void prepareHandlers() {
        handlerMap.put(DrawingAreaEvent.ANY, h -> {
            documentManager.getActiveDocument().applyCommand((DrawingAreaEvent) h);
        });

        handlerMap.put(EditControlEvent.ANY, h -> {
            eventAggregator.fireEvent(h);
        });
    }

    private void onCut() {
        onCopy();
        onDelete();
    }

    private void onDeselectAll() {
        Document document = documentManager.getActiveDocument();
        if (document != null) {
            List<CanvasObject> canvasObjects = document.getGridModel().getAll();
            canvasObjects.forEach(o -> {
                if (o.isSelected()) {
                    eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.SELECTION_CHANGED, o, true, false));
                }
            });
            eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.EDITING_FINISHED));
        }
    }

    private void onSelectAll() {
        Document document = documentManager.getActiveDocument();
        if (document != null) {
            List<CanvasObject> canvasObjects = document.getGridModel().getAll();
            canvasObjects.forEach(o -> {
                if (!o.isSelected()) {
                    eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.SELECTION_CHANGED, o, false, true));
                }
            });
            eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.EDITING_FINISHED));
        }
    }

    private void onPaste() {
        copyMemory.forEach(o -> {
            o.setSelected(true);
            eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_ADDED, o));
        });
        eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.EDITING_FINISHED));
    }

    private void onCopy() {
        Document document = documentManager.getActiveDocument();
        if (document != null) {
            List<CanvasObject> copyObjects = document.getGridModel().getSelected();
            copyMemory.clear();
            copyObjects.forEach(o -> copyMemory.add(o));
        }
    }

    private void onDelete() {
        Document document = documentManager.getActiveDocument();
        if (document != null) {
            List<CanvasObject> deleteObjects = document.getGridModel().getSelected();
            deleteObjects.forEach(o -> {
                eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_DELETED, o));
            });
            eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.EDITING_FINISHED));
        }
    }


    private void initDrawingAreaDropDetection(DrawingCanvas drawingArea) {
        drawingArea.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (event.getGestureSource() != drawingArea &&
                        event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    if (toPaint != null) paintDraggedComponent(event);
                }

                event.consume();
            }
        });

        drawingArea.setOnDragEntered(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    for (Component c : Component.values()) {
                        if (c.getType() == db.getString()) {
                            toPaint = DocumentObjectFactory.createDocumentObject(c);
                            if (toPaint != null) paintDraggedComponent(event);
                        }
                    }
                }

                event.consume();
            }
        });

        drawingArea.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    if (drawingArea.getModel() != null) {
                        drawingArea.getModel().getInnerEventAggregator().fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_ADDED, toPaint));
                    }
                    toPaint = null;
                }
                event.setDropCompleted(true);

                event.consume();
            }
        });
    }

    private void paintDraggedComponent(DragEvent dragEvent) {
        if (drawingArea.getModel() != null && drawingArea.getModel() instanceof GridModel) {
            GridModel gridModel = (GridModel) drawingArea.getModel();
            int gridX = gridModel.getGridCoordinate(dragEvent.getX(), gridModel.getOriginX());
            int gridY = gridModel.getGridCoordinate(dragEvent.getY(), gridModel.getOriginY());
            toPaint.setGridX(gridX);
            toPaint.setGridY(gridY);
            gridModel.updatePaintProperties(toPaint);
            gridModel.getInnerEventAggregator().fireEvent(new CanvasPaintEvent(CanvasPaintEvent.REPAINT));
            toPaint.paint(gridModel.getCanvas().getGraphicsContext2D());
        }


    }


}
