package cz.thewhiterabbit.electronicapp.controllers;

import cz.thewhiterabbit.electronicapp.DocumentManager;
import cz.thewhiterabbit.electronicapp.listeners.ControlPaneListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class WindowController {
    @FXML
    private DrawingAreaController drawingAreaController;
    @FXML
    private ControlPaneController controlPaneController;

    //logic
    DocumentManager documentManager = DocumentManager.getInstance();

    @FXML
    private void initialize(){
        controlPaneController.addControlPaneListener(new ControlPaneListener() {
            @Override
            public void onNewFileClicked(ActionEvent e) {
                documentManager.createNewDocument();
            }
        });
    }



}
