package cz.thewhiterabbit.electronicapp.view.dialogs;


import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;

import cz.thewhiterabbit.electronicapp.view.controllers.FileLoadController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.List;


public class FileLoadError extends Stage {
    private FileLoadController fileLoadController = new FileLoadController();
    private VBox view;
    private Response response = Response.FIX;

    private String fileName;
    private List<RawObject> corruptedObjects;

    public FileLoadError(String fileName, List<RawObject> corruptedObjects) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(fileLoadController);
        loader.setLocation(App.class.getResource("fxml/FileLoadDialog.fxml"));
        view = loader.load();
        setScene(new Scene(view));

        this.fileName = fileName;
        this.corruptedObjects = corruptedObjects;
        init();
    }

    private void init() {
        fileLoadController.getConfirmButton().setOnAction(l -> {
            onConfirm();
        });
        fileLoadController.getCancelButton().setOnAction(l -> {
            onCancel();
        });
        fileLoadController.getTitleLabel().setText("Error occurred while loading the file: " + fileName + ". Following objects was corrupted:");
        fileLoadController.getListView().getItems().addAll(corruptedObjects);
    }

    private void onCancel() {
        response = Response.CANCEL;
        close();
    }

    private void onConfirm() {
        response= fileLoadController.getResponse();
        close();
    }

    public Stage getStage() {
        return this;
    }

    public Response getResponse() {
        return response;
    }

    public enum Response{
        FIX,
        IGNORE,
        CANCEL
    }
}
