package cz.thewhiterabbit.electronicapp.controllers;


import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.events.MenuEvent;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;



public class FileMenuController {

    @FXML private MenuItem newFile;

    @FXML private void initialize(){
        newFile.setOnAction(e -> {
            MenuEvent menuClicked = new MenuEvent(MenuEvent.NEW_FILE);
            EventAggregator.getInstance().fireEvent(menuClicked);
        });
    }
}
