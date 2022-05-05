package cz.thewhiterabbit.electronicapp.view.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class InfoDialogController {
    @FXML
    private Label titleLabel;
    @FXML
    private Label messageLabel;
    @FXML
    private Button confirmButton;


    private boolean response;

    public void setTitle(String title){
        titleLabel.setText(title);
    }

    public void setMessage(String message){
        messageLabel.setText(message);
    }

    public Button getConfirmButton() {
        return confirmButton;
    }
}
