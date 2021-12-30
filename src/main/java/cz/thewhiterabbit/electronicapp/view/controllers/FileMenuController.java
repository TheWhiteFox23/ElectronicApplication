package cz.thewhiterabbit.electronicapp.view.controllers;


import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.view.events.MenuEvent;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;



public class FileMenuController {

    @FXML private MenuItem newFile;
    @FXML private MenuItem openFile;
    @FXML private MenuItem saveFile;
    @FXML private MenuItem saveFileAs;


    @FXML private void initialize(){
        newFile.setOnAction(e -> {
            MenuEvent menuClicked = new MenuEvent(MenuEvent.NEW_DOCUMENT);
            GUIEventAggregator.getInstance().fireEvent(menuClicked);
        });
        openFile.setOnAction(e->{
            MenuEvent menuEvent = new MenuEvent(MenuEvent.OPEN_FILE);
            GUIEventAggregator.getInstance().fireEvent(menuEvent);
        });
        saveFile.setOnAction(e->{
            MenuEvent menuEvent = new MenuEvent(MenuEvent.SAVE_FILE);
            GUIEventAggregator.getInstance().fireEvent(menuEvent);
        });
        saveFileAs.setOnAction(e->{
            MenuEvent menuEvent = new MenuEvent(MenuEvent.SAVE_FILE_AS);
            GUIEventAggregator.getInstance().fireEvent(menuEvent);
        });
    }
}
