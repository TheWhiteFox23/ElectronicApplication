package cz.thewhiterabbit.electronicapp.controllers;

import cz.thewhiterabbit.electronicapp.canvas.DrawingAreaEvent;
import cz.thewhiterabbit.electronicapp.canvas.DrawingCanvas;
import cz.thewhiterabbit.electronicapp.canvas.model.GridModel;
import cz.thewhiterabbit.electronicapp.canvas.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.canvas.objects.GeneralCanvasObject;
import cz.thewhiterabbit.electronicapp.canvas.objects.RelativePointBackground;
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

        /***** REGISTER LISTENERS *****/
        gridLayout.addEventHandler(DrawingAreaEvent.ANY, e-> {
            //System.out.println(e.getEventType());
        });


        gridLayout.add(new RelativePointBackground(drawingArea.getCanvas()));
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

        /*GeneralCanvasObject generalCanvasObject = new GeneralCanvasObject();
        generalCanvasObject.set(0,0,2,2);

        ActivePoint activePoint = new ActivePoint();
        activePoint.set(0,0,1,1);
        generalCanvasObject.addChildren(activePoint);

        activePoint = new ActivePoint();
        activePoint.set(0,1,1,1);
        generalCanvasObject.addChildren(activePoint);

        gridLayout.add(generalCanvasObject);*/
    }


}
