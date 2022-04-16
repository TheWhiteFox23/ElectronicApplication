package cz.thewhiterabbit.electronicapp.view.controllers;

import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.model.documnet.Document;
import cz.thewhiterabbit.electronicapp.model.documnet.DocumentManager;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawDocument;
import cz.thewhiterabbit.electronicapp.model.similation.SimulationFile;
import cz.thewhiterabbit.electronicapp.view.components.CategoryButton;
import cz.thewhiterabbit.electronicapp.view.events.MenuEvent;
import cz.thewhiterabbit.electronicapp.view.events.SimulationEvents;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class SimulationMenuController {
    private Document document = new Document(new RawDocument("MT"));

    @FXML
    private Button simulationButton;
    @FXML
    private CategoryButton transientButton;
    @FXML
    private HBox transientMenu;
    @FXML
    private VBox menuVBox;
    @FXML
    private Button simulateButton;
    @FXML
    private TransientMenu transientPanel;


    private HashMap<CategoryButton, HBox> panelMap; //TODO can be refactored out(only one panel)


    @FXML
    private void initialize() {
        GUIEventAggregator.getInstance().addEventHandler(DocumentManager.DocumentManagerEvent.ACTIVE_DOCUMENT_CHANGED, e -> {
            setDocument(((DocumentManager.DocumentManagerEvent) e).getDocument());
        });
        simulationButton.setOnAction(e -> {
            GUIEventAggregator.getInstance().fireEvent(new MenuEvent(MenuEvent.SWITCH_MODE_SCHEMATIC));
        });
        panelMap = new HashMap<>() {{
            put(transientButton, transientMenu);
        }};
        setActive(transientButton);
        initializeButtons();
        simulateButton.setOnAction(e -> {
            onSimulate();
        });
        initProperties();
    }

    private void setDocument(Document document) {
        unbindDocument(this.document);
        this.document = document;
        loadProperties(document);
        bindDocument(document);
    }

    private void bindDocument(Document document) {
        //TRANSIENT
        document.getSimulationFile().stopTimeProperty().bind(transientPanel.stopTimeProperty());
        document.getSimulationFile().stepIncrementProperty().bind(transientPanel.stepIncrementProperty());
        document.getSimulationFile().useStartTimeProperty().bind(transientPanel.useStartTimeProperty());
        document.getSimulationFile().startTimeProperty().bind(transientPanel.startTimeProperty());
        document.getSimulationFile().useInternalStepProperty().bind(transientPanel.useInternalStepProperty());
        document.getSimulationFile().maxStepSizeProperty().bind(transientPanel.maxStepSizeProperty());
    }

    private void loadProperties(Document document) {
        //TRANSIENT
        transientPanel.stopTimeProperty().set(document.getSimulationFile().stopTimeProperty().get());
        transientPanel.stepIncrementProperty().set(document.getSimulationFile().stepIncrementProperty().get());
        transientPanel.useStartTimeProperty().set(document.getSimulationFile().useStartTimeProperty().get());
        transientPanel.startTimeProperty().set(document.getSimulationFile().startTimeProperty().get());
        transientPanel.useInternalStepProperty().set(document.getSimulationFile().useInternalStepProperty().get());
        transientPanel.maxStepSizeProperty().set(document.getSimulationFile().maxStepSizeProperty().get());
    }

    private void initProperties() {
        //TRANSIENT
        transientPanel.stopTimeProperty().set(0);
        transientPanel.stepIncrementProperty().set(0);
        transientPanel.useStartTimeProperty().set(true);
        transientPanel.startTimeProperty().set(0);
        transientPanel.useInternalStepProperty().set(true);
        transientPanel.maxStepSizeProperty().set(0);
    }


    private void unbindDocument(Document document) {
        //TRANSIENT
        document.getSimulationFile().stopTimeProperty().unbind();
        document.getSimulationFile().stepIncrementProperty().unbind();
        document.getSimulationFile().useStartTimeProperty().unbind();
        document.getSimulationFile().startTimeProperty().unbind();
        document.getSimulationFile().useInternalStepProperty().unbind();
        document.getSimulationFile().maxStepSizeProperty().unbind();
    }

    private void onSimulate() {
        SimulationFile simulationFile;
        simulationFile = document.getSimulationFile();
        simulationFile.setMode(SimulationFile.SimulationMode.TRANSIENT);
        GUIEventAggregator.getInstance().fireEvent(new SimulationEvents(SimulationEvents.SIMULATE_CLICKED, simulationFile));
    }


    private void initializeButtons() {
        panelMap.forEach((button, panel) -> {
            button.setOnAction(e -> setActive(button));
        });
    }

    private void setActive(CategoryButton categoryButton) {
        panelMap.forEach((button, panel) -> {
            button.setActive(false);
            panel.setVisible(false);
            menuVBox.getChildren().remove(panel);
        });
        categoryButton.setActive(true);
        panelMap.get(categoryButton).setVisible(true);
        menuVBox.getChildren().add(panelMap.get(categoryButton));
    }
}
