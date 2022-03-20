package cz.thewhiterabbit.electronicapp.view.dialogs;

import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.view.controllers.ConfirmDialogController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class ConfirmDialog extends Stage {
    private final ConfirmDialogController confirmDialogController =  new ConfirmDialogController();

    private Response response = Response.CANCEL;

    public ConfirmDialog(String title, String text){
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/confirm_dialog.fxml"));
        loader.setController(confirmDialogController);
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        setTitle("");
        initComponents(title, text);

        setScene(scene);
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        showAndWait();
    }

    private void initComponents(String title, String text) {
        confirmDialogController.setTitle(title);
        confirmDialogController.setMessage(text);
        confirmDialogController.getConfirmButton().setOnAction(e->{
            response = Response.YES;
            close();
        });

        confirmDialogController.getCancelButton().setOnAction(e->{
            response = Response.NO;
            close();
        });
    }

    public Response getResponse() {
        return response;
    }

    public enum Response{
        CANCEL,
        YES,
        NO
    }
}
