package cz.thewhiterabbit.electronicapp.controllers;

import cz.thewhiterabbit.electronicapp.DocumentManager;
import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.events.MenuEvent;

import cz.thewhiterabbit.electronicapp.events.TabPaneEvent;
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
        //register handlers
        GUIEventAggregator.getInstance().registerHandler(MenuEvent.NEW_FILE, event ->{
            documentManager.createNewDocument();
        });
        GUIEventAggregator.getInstance().registerHandler(TabPaneEvent.TAB_CLOSED, event -> {
            documentManager.closeDocument(((TabPaneEvent)event).getDocument());
        });
    }



}
