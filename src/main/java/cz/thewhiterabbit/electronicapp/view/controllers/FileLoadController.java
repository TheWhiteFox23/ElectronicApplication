package cz.thewhiterabbit.electronicapp.view.controllers;

import cz.thewhiterabbit.electronicapp.view.dialogs.FileLoadError;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class FileLoadController implements Initializable {
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;
    @FXML private RadioButton fixButton;
    @FXML private RadioButton excludeButton;
    @FXML private RadioButton doNotLoadButton;
    @FXML private ListView listView;
    @FXML private Label titleLabel;

    private FileLoadError.Response response = FileLoadError.Response.FIX;
    private final ToggleGroup toggleGroup = new ToggleGroup();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fixButton.setToggleGroup(toggleGroup);
        excludeButton.setToggleGroup(toggleGroup);
        doNotLoadButton.setToggleGroup(toggleGroup);
        toggleGroup.selectToggle(fixButton);
        toggleGroup.selectedToggleProperty().addListener(l -> {
            if(toggleGroup.getSelectedToggle() == fixButton){
                response = FileLoadError.Response.FIX;
            }else if (toggleGroup.getSelectedToggle() == excludeButton){
                response = FileLoadError.Response.IGNORE;
            }else{
                response = FileLoadError.Response.CANCEL;
            }
        });
    }

    public Button getConfirmButton() {
        return confirmButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public ListView getListView() {
        return listView;
    }

    public Label getTitleLabel() {
        return titleLabel;
    }

    public ToggleGroup getToggleGroup() {
        return toggleGroup;
    }

    public FileLoadError.Response getResponse() {
        return response;
    }
}
