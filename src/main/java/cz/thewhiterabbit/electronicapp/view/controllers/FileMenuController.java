package cz.thewhiterabbit.electronicapp.view.controllers;


import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.view.events.EditControlEvent;
import cz.thewhiterabbit.electronicapp.view.events.MenuEvent;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;



public class FileMenuController {

    @FXML private MenuItem newFile;
    @FXML private MenuItem openFile;
    @FXML private MenuItem saveFile;
    @FXML private MenuItem saveFileAs;
    @FXML private MenuItem quit;
    @FXML private MenuItem cut;
    @FXML private MenuItem copy;
    @FXML private MenuItem paste;
    @FXML private MenuItem delete;
    @FXML private MenuItem selectAll;
    @FXML private MenuItem deselectAll;
    @FXML private MenuItem undo;
    @FXML private MenuItem redo;


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
        cut.setOnAction(e->{
            EditControlEvent event = new EditControlEvent(EditControlEvent.CUT);
            GUIEventAggregator.getInstance().fireEvent(event);
        });
        copy.setOnAction(e->{
            EditControlEvent event = new EditControlEvent(EditControlEvent.COPY);
            GUIEventAggregator.getInstance().fireEvent(event);
        });
        paste.setOnAction(e->{
            EditControlEvent event = new EditControlEvent(EditControlEvent.PASTE);
            GUIEventAggregator.getInstance().fireEvent(event);
        });
        selectAll.setOnAction(e->{
            EditControlEvent event = new EditControlEvent(EditControlEvent.SELECT_ALL);
            GUIEventAggregator.getInstance().fireEvent(event);
        });
        deselectAll.setOnAction(e->{
            EditControlEvent event = new EditControlEvent(EditControlEvent.DESELECT_ALL);
            GUIEventAggregator.getInstance().fireEvent(event);
        });
        undo.setOnAction(e->{
            EditControlEvent event = new EditControlEvent(EditControlEvent.UNDO);
            GUIEventAggregator.getInstance().fireEvent(event);
        });
        redo.setOnAction(e->{
            EditControlEvent event = new EditControlEvent(EditControlEvent.REDO);
            GUIEventAggregator.getInstance().fireEvent(event);
        });
        delete.setOnAction(e->{
            EditControlEvent event = new EditControlEvent(EditControlEvent.DELETE);
            GUIEventAggregator.getInstance().fireEvent(event);
        });


    }
}
