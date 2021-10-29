package cz.thewhiterabbit.electronicapp.controllers;

import cz.thewhiterabbit.electronicapp.listeners.ControlPaneListener;
import cz.thewhiterabbit.electronicapp.listeners.FileMenuListener;
import cz.thewhiterabbit.electronicapp.listeners.TabPaneListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.List;

public class ControlPaneController {

    @FXML private TabPaneController tabPaneController;
    @FXML private FileMenuController fileMenuController;
    @FXML private ComponentMenuController componentMenuController;

    private List<ControlPaneListener> listeners = new ArrayList<>();

    public void addControlPaneListener(ControlPaneListener listener){
        this.listeners.add(listener);
    }

    @FXML private void initialize(){
        tabPaneController.addTabPaneListener(new TabPaneListener() {
            @Override
            public void onNewFileClicked(ActionEvent e) {
                listeners.forEach(a -> a.onNewFileClicked(e));
            }
        });
        fileMenuController.addTabPaneListener(new FileMenuListener() {
            @Override
            public void onNewFileClicked(ActionEvent e) {
                listeners.forEach(a -> a.onNewFileClicked(e));
            }
        });
    }
}
