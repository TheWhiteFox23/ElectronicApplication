package cz.thewhiterabbit.electronicapp.controllers;

import cz.thewhiterabbit.electronicapp.listeners.DrawingAresListener;

import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.List;

public class DrawingAreaController {
    @FXML PropertiesPaneController propertiesPaneController;
    @FXML ComponentInfoPaneController componentInfoPaneController;
    @FXML DrawingAreaControlMenuController drawingAreaControlMenuController;

    private List<DrawingAresListener> listeners = new ArrayList<>();

    public void addDrawingAresListener(DrawingAresListener listener){
        listeners.add(listener);
    }

    @FXML private void initialize(){}

}
