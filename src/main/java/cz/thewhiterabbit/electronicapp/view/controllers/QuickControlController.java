package cz.thewhiterabbit.electronicapp.view.controllers;

import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.view.events.EditControlEvent;
import cz.thewhiterabbit.electronicapp.view.events.MenuEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class QuickControlController {
    @FXML
    private Button saveButton;
    @FXML
    private Button undoButton;
    @FXML
    private Button redoButton;

    @FXML
    private void initialize(){
        saveButton.setOnAction(e->{
            GUIEventAggregator.getInstance().fireEvent(new MenuEvent(MenuEvent.SAVE_FILE));
        });
        undoButton.setOnAction(e->{
            GUIEventAggregator.getInstance().fireEvent(new EditControlEvent(EditControlEvent.UNDO));
        });
        redoButton.setOnAction(e->{
            GUIEventAggregator.getInstance().fireEvent(new EditControlEvent(EditControlEvent.REDO));
        });
    }
}
