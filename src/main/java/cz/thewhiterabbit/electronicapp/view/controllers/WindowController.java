package cz.thewhiterabbit.electronicapp.view.controllers;


import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.view.components.ComponentTreeItem;
import cz.thewhiterabbit.electronicapp.view.events.MenuEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;

public class WindowController {
    @FXML
    private StackPane window;
    @FXML
    private DrawingAreaController drawingAreaController;
    @FXML
    private ControlPaneController controlPaneController;
    @FXML
    private StackPane black;

    @FXML
    private void initialize(){
        black.setVisible(false);
        initializeListeners();
    }

    private void initializeListeners() {
        GUIEventAggregator.getInstance().addEventHandler(MenuEvent.SHOW_INFO_DIALOG, h->{
            if(((MenuEvent)h).getComponent() != null){
                //componentInfoPanel.setActiveComponent(((MenuEvent) h).getComponent());
                black.setVisible(true);
            }else{
                System.out.println("Show component info dialog on default component");
                //window.getChildren().remove(componentInfoPanel);
                black.setVisible(true);
            }
        });
        GUIEventAggregator.getInstance().addEventHandler(MenuEvent.HIDE_INFO_DIALOG, h->{
            black.setVisible(false);
        });
    }


}
