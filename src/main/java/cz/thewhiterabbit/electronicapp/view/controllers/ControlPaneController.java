package cz.thewhiterabbit.electronicapp.view.controllers;

import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.model.documnet.DocumentManager;
import cz.thewhiterabbit.electronicapp.view.events.MenuEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.w3c.dom.events.Event;


public class ControlPaneController {
    @FXML
    private HBox componentMenu;
    @FXML
    private HBox simulationMenu;
    @FXML
    private VBox controlPane;

    @FXML
    private void initialize(){
        controlPane.getChildren().remove(simulationMenu);
        GUIEventAggregator.getInstance().addEventHandler(MenuEvent.MODE_CHANGED, e->{
            System.out.println("Mode changed");
            switch (((MenuEvent)e).getDocument().getMode()){
                case SCHEMATIC -> {
                    if(controlPane.getChildren().contains(simulationMenu)){
                        controlPane.getChildren().remove(simulationMenu);
                        controlPane.getChildren().add(componentMenu);
                    }
                }case SIMULATION -> {
                    if(controlPane.getChildren().contains(componentMenu)){
                        controlPane.getChildren().remove(componentMenu);
                        controlPane.getChildren().add(simulationMenu);
                    }
                }
            }
        });
        GUIEventAggregator.getInstance().addEventHandler(DocumentManager.DocumentManagerEvent.ACTIVE_DOCUMENT_CHANGED, e->{
            System.out.println("Document change");
        });
        GUIEventAggregator.getInstance().addEventHandler(MenuEvent.ANY, e->{
            System.out.println(e.getEventType());
        });
    }

}
