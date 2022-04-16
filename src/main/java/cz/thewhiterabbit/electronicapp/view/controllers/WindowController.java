package cz.thewhiterabbit.electronicapp.view.controllers;


import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.view.components.ComponentTreeItem;
import cz.thewhiterabbit.electronicapp.view.events.MenuEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Window;

public class WindowController {
    @FXML
    private StackPane window;
    @FXML
    private AnchorPane canvas;
    @FXML
    private StackPane black;
    @FXML
    private WebView componentInfoWebView;
    @FXML
    private AnchorPane simulationPanel;

    @FXML
    private void initialize() {
        black.setVisible(false);
        initializeListeners();
    }

    private void initializeListeners() {
        GUIEventAggregator.getInstance().addEventHandler(MenuEvent.SHOW_INFO_DIALOG, h -> {
            black.setVisible(true);
        });
        GUIEventAggregator.getInstance().addEventHandler(MenuEvent.HIDE_INFO_DIALOG, h -> {
            black.setVisible(false);
        });

        black.addEventHandler(MouseEvent.MOUSE_CLICKED, h -> {
            if (h.getTarget() == black)
                GUIEventAggregator.getInstance().fireEvent(new MenuEvent(MenuEvent.HIDE_INFO_DIALOG));
        });

        black.addEventHandler(KeyEvent.ANY, e -> {
            if (e.getCode() == KeyCode.ESCAPE)
                GUIEventAggregator.getInstance().fireEvent(new MenuEvent(MenuEvent.HIDE_INFO_DIALOG));
        });

        GUIEventAggregator.getInstance().addEventHandler(MenuEvent.MODE_CHANGED, e -> {
            switch (((MenuEvent) e).getDocument().getMode()) {
                case SCHEMATIC: {
                    canvas.setVisible(true);
                    simulationPanel.setVisible(false);
                    break;
                }
                case SIMULATION: {
                    canvas.setVisible(false);
                    simulationPanel.setVisible(true);
                    break;
                }
            }
        });

        MenuEvent menuEvent = new MenuEvent(MenuEvent.NEW_DOCUMENT);
        GUIEventAggregator.getInstance().fireEvent(menuEvent);
    }


}
