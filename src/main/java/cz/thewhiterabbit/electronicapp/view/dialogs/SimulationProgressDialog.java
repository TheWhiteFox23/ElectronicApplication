package cz.thewhiterabbit.electronicapp.view.dialogs;

import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.view.controllers.ConfirmDialogController;
import cz.thewhiterabbit.electronicapp.view.controllers.SimulationProgressDialogController;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SimulationProgressDialog extends Stage {
    private final SimulationProgressDialogController controller = new SimulationProgressDialogController();
    private final Task task;

    public SimulationProgressDialog(Task task){
        this.task = task;
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/simulation_progress_dialog.fxml"));
        loader.setResources(App.localization);
        loader.setController(controller);
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        setTitle(App.localization.getString("simdialog.title"));
        initComponents();

        setScene(scene);
        setResizable(false);
        //initModality(Modality.APPLICATION_MODAL);
    }

    public void showDialog(){
        show();
    }

    public void closeDialog(){
        close();
    }

    private void initComponents() {
        controller.getProgressBar().progressProperty().bind(task.progressProperty());
        controller.getMessageLabel().textProperty().bind(task.messageProperty());
    }
}
