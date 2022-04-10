package cz.thewhiterabbit.electronicapp.view.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class SimulationProgressDialogController {
    @FXML private ProgressBar progressBar;
    @FXML private Label messageLabel;

    public SimulationProgressDialogController(){

    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public Label getMessageLabel() {
        return messageLabel;
    }
}
