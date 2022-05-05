package cz.thewhiterabbit.electronicapp.view.dialogs;

import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.view.controllers.InfoDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class InfoDialog extends Stage {
    private final InfoDialogController infoDialogController =  new InfoDialogController();

    private Response response = Response.CANCEL;

    public InfoDialog(String title, String text){
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/info_dialog.fxml"));
        loader.setResources(App.localization);
        loader.setController(infoDialogController);
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
        infoDialogController.setTitle(title);
        infoDialogController.setMessage(text);
        infoDialogController.getConfirmButton().setOnAction(e->{
            response = Response.YES;
            close();
        });
    }

    public Response getResponse() {
        return response;
    }

    public enum Response{
        CANCEL,
        YES
    }
}
