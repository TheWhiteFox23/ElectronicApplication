package cz.thewhiterabbit.electronicapp.controllers;

import cz.thewhiterabbit.electronicapp.listeners.FileMenuListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class FileMenuController {

    @FXML private MenuItem newFile;

    private List<FileMenuListener> listeners = new ArrayList<>();
    public void addTabPaneListener(FileMenuListener listener){
        this.listeners.add(listener);
    }

    @FXML private void initialize(){
        newFile.setOnAction(e -> listeners.forEach(a -> a.onNewFileClicked(e)));
    }
}
