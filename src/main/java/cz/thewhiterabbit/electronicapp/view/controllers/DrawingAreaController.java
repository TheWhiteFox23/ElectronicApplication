package cz.thewhiterabbit.electronicapp.view.controllers;

import cz.thewhiterabbit.electronicapp.model.documnet.Document;
import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawProperty;
import cz.thewhiterabbit.electronicapp.model.rawdocument.TestRawDocument;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import cz.thewhiterabbit.electronicapp.view.canvas.DrawingAreaEvent;
import cz.thewhiterabbit.electronicapp.view.canvas.DrawingCanvas;
import cz.thewhiterabbit.electronicapp.view.canvas.model.GridModel;
import javafx.fxml.FXML;

public class DrawingAreaController {
    @FXML DrawingCanvas drawingArea;
    @FXML PropertiesPaneController propertiesPaneController;
    @FXML ComponentInfoPaneController componentInfoPaneController;
    @FXML DrawingAreaControlMenuController drawingAreaControlMenuController;

    @FXML
    private void initialize(){
        GridModel gridLayout = new GridModel();
        gridLayout.setGridSize(10);
        gridLayout.setOriginX(600);
        gridLayout.setOriginY(400);
        drawingArea.setModel(gridLayout);

        TestRawDocument testRawDocument = new TestRawDocument("TestDocument");
        Document document = new Document(testRawDocument);
        drawingArea.setModel(document.getGridModel());

        /***** REGISTER LISTENERS *****/
        document.getGridModel().addEventHandler(DrawingAreaEvent.OBJECT_PROPERTY_CHANGE, e-> {
            DrawingAreaEvent event = (DrawingAreaEvent) e;
            DocumentObject o = (DocumentObject) event.getCanvasObject();
            RawProperty p = o.getProperty(((DrawingAreaEvent) e).getProperty().getName());
            if(p != null){
                p.setValue(String.valueOf(((DrawingAreaEvent) e).getNewVale()));
            }

            //event.getProperty().set(((DrawingAreaEvent) e).getNewVale());
        });

        document.getGridModel().addEventHandler(DrawingAreaEvent.OBJECT_ADDED, e->{
            gridLayout.add(((DrawingAreaEvent)e).getCanvasObject());
        });

        document.getGridModel().addEventHandler(DrawingAreaEvent.OBJECT_DELETED, e ->{
            DrawingAreaEvent event = (DrawingAreaEvent) e;
            DocumentObject o = (DocumentObject) event.getCanvasObject();
            if(o!= null) {
                document.getRawDocument().removeObject(o.getRawObject());
            }
        });




        /*gridLayout.add(new RelativePointBackground(drawingArea.getCanvas()));
        for(int i = 0; i< 100; i+= 4){
            for(int j = 0; j< 100; j+=4){
                GeneralCanvasObject generalCanvasObject = new GeneralCanvasObject();
                ActivePoint linkedObject = new ActivePoint();
                linkedObject.set(i+1,j,1,1);
                generalCanvasObject.addChildren(linkedObject);
                generalCanvasObject.set(i,j,2, 2);
                gridLayout.add(generalCanvasObject);
            }
        }

        GeneralCanvasObject generalCanvasObject = new GeneralCanvasObject();
        generalCanvasObject.set(-10,-10,2,2);

        GeneralCanvasObject activePoint = new GeneralCanvasObject();
        activePoint.set(-8,-8,2,2);
        generalCanvasObject.addChildren(activePoint);

        gridLayout.add(generalCanvasObject);

        TwoPointLineObject line = new TwoPointLineObject(0,0,-30,-10);
        gridLayout.add(line);*/
    }


}
