package cz.thewhiterabbit.electronicapp.view.controllers;


import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.view.events.MenuEvent;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;



public class FileMenuController {

    @FXML private MenuItem newFile;

    @FXML private void initialize(){
        newFile.setOnAction(e -> {
            MenuEvent menuClicked = new MenuEvent(MenuEvent.NEW_FILE);
            GUIEventAggregator.getInstance().fireEvent(menuClicked);
        });
    }
}
