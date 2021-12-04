package cz.thewhiterabbit.electronicapp.controllers;

import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.events.MenuEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;


public class TabPaneController {
    @FXML private Button newFile;
    @FXML private HBox tabPane;

    @FXML private void initialize(){
        newFile.setOnAction(e -> {
            MenuEvent menuEvent = new MenuEvent(MenuEvent.NEW_FILE);
            GUIEventAggregator.getInstance().fireEvent(menuEvent);
        });
    }

}
