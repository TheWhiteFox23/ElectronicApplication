package cz.thewhiterabbit.electronicapp.view.controllers;

import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.view.events.MenuEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SimulationMenuController {
    @FXML
    Button simulationButton;
    @FXML
    HBox simulationMenu;

    @FXML
    private void initialize(){
        simulationButton.setOnAction(e->{
            GUIEventAggregator.getInstance().fireEvent(new MenuEvent(MenuEvent.SWITCH_MODE_SCHEMATIC));
        });
    }
}
