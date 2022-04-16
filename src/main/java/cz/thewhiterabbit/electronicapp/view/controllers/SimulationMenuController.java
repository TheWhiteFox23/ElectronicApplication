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
    private HBox simulationMenu;
    @FXML
    private HBox categoriesBox;
    /*@FXML
    private CategoryButton acSweepButton;*/
    @FXML
    private CategoryButton transientButton;
    /*@FXML
    private CategoryButton dcAnalysisButton;*/
    /*@FXML
    private HBox acSweepMenu;*/
    @FXML
    private HBox transientMenu;
    /*@FXML
    private HBox dcAnalysisMenu;*/
    @FXML
    private VBox menuVBox;
    @FXML
    private Button simulateButton;

    /**
     * PANELS
     **/
    /*@FXML
    private ACDCMenu acSweepPanel;
    @FXML
    private ACDCMenu dcAnalysisPanel;*/
    @FXML
    private TransientMenu transientPanel;

    /*@FXML
    private CheckBox optimizeCB;
    @FXML
    private CheckBox onlySelectedCB;*/


    private HashMap<CategoryButton, HBox> panelMap;


    @FXML
    private void initialize() {
        GUIEventAggregator.getInstance().addEventHandler(DocumentManager.DocumentManagerEvent.ACTIVE_DOCUMENT_CHANGED, e -> {
            setDocument(((DocumentManager.DocumentManagerEvent) e).getDocument());
        });
        simulationButton.setOnAction(e -> {
            GUIEventAggregator.getInstance().fireEvent(new MenuEvent(MenuEvent.SWITCH_MODE_SCHEMATIC));
        });
        panelMap = new HashMap<>() {{
            //put(acSweepButton, acSweepMenu);
            put(transientButton, transientMenu);
            //put(dcAnalysisButton, dcAnalysisMenu);
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
        //AC SWEEP
        /*document.getSimulationFile().startValueACProperty().bind(acSweepPanel.startValueProperty());
        document.getSimulationFile().stopValueACProperty().bind(acSweepPanel.stopValueProperty());
        document.getSimulationFile().numberOfPointsACProperty().bind(acSweepPanel.numberOfPointsProperty());
        document.getSimulationFile().scaleTopACProperty().bind(acSweepPanel.scaleTopProperty());
        document.getSimulationFile().scaleBottomACProperty().bind(acSweepPanel.scaleBottomProperty());
        //DC SWEEP
        document.getSimulationFile().startValueDCProperty().bind(dcAnalysisPanel.startValueProperty());
        document.getSimulationFile().stopValueDCProperty().bind(dcAnalysisPanel.stopValueProperty());
        document.getSimulationFile().numberOfPointsDCProperty().bind(dcAnalysisPanel.numberOfPointsProperty());
        document.getSimulationFile().scaleTopDCProperty().bind(dcAnalysisPanel.scaleTopProperty());
        document.getSimulationFile().scaleBottomDCProperty().bind(dcAnalysisPanel.scaleBottomProperty());
        //GENERAL
        //document.getSimulationFile().optimizeProperty().bind(optimizeCB.selectedProperty());
        //document.getSimulationFile().onlyAnalyzeNodesProperty().bind(onlySelectedCB.selectedProperty());*/
    }

    private void loadProperties(Document document) {
        //TRANSIENT
        transientPanel.stopTimeProperty().set(document.getSimulationFile().stopTimeProperty().get());
        transientPanel.stepIncrementProperty().set(document.getSimulationFile().stepIncrementProperty().get());
        transientPanel.useStartTimeProperty().set(document.getSimulationFile().useStartTimeProperty().get());
        transientPanel.startTimeProperty().set(document.getSimulationFile().startTimeProperty().get());
        transientPanel.useInternalStepProperty().set(document.getSimulationFile().useInternalStepProperty().get());
        transientPanel.maxStepSizeProperty().set(document.getSimulationFile().maxStepSizeProperty().get());
        /*//AC SWEEP
        acSweepPanel.startValueProperty().set(document.getSimulationFile().startValueACProperty().get());
        acSweepPanel.stopValueProperty().set(document.getSimulationFile().stopValueACProperty().get());
        acSweepPanel.numberOfPointsProperty().set(document.getSimulationFile().numberOfPointsACProperty().get());
        acSweepPanel.scaleTopProperty().set(document.getSimulationFile().scaleTopACProperty().get());
        acSweepPanel.scaleBottomProperty().set(document.getSimulationFile().scaleBottomACProperty().get());
        //DC SWEEP
        dcAnalysisPanel.startValueProperty().set(document.getSimulationFile().startValueDCProperty().get());
        dcAnalysisPanel.stopValueProperty().set(document.getSimulationFile().stopValueDCProperty().get());
        dcAnalysisPanel.numberOfPointsProperty().set(document.getSimulationFile().numberOfPointsDCProperty().get());
        dcAnalysisPanel.scaleTopProperty().set(document.getSimulationFile().scaleTopDCProperty().get());
        dcAnalysisPanel.scaleBottomProperty().set(document.getSimulationFile().scaleBottomDCProperty().get());
        //GENERAL
        //optimizeCB.selectedProperty().set(document.getSimulationFile().optimizeProperty().get());
        //onlySelectedCB.selectedProperty().set(document.getSimulationFile().onlyAnalyzeNodesProperty().get());*/
    }

    private void initProperties() {
        //TRANSIENT
        transientPanel.stopTimeProperty().set(0);
        transientPanel.stepIncrementProperty().set(0);
        transientPanel.useStartTimeProperty().set(true);
        transientPanel.startTimeProperty().set(0);
        transientPanel.useInternalStepProperty().set(true);
        transientPanel.maxStepSizeProperty().set(0);
        //AC SWEEP
        /*acSweepPanel.startValueProperty().set(0);
        acSweepPanel.stopValueProperty().set(0);
        acSweepPanel.numberOfPointsProperty().set(0);
        acSweepPanel.scaleTopProperty().set(0);
        acSweepPanel.scaleBottomProperty().set(0);
        //DC SWEEP
        dcAnalysisPanel.startValueProperty().set(0);
        dcAnalysisPanel.stopValueProperty().set(0);
        dcAnalysisPanel.numberOfPointsProperty().set(0);
        dcAnalysisPanel.scaleTopProperty().set(0);
        dcAnalysisPanel.scaleBottomProperty().set(0);*/
        //GENERAL
        //optimizeCB.selectedProperty().set(true);
        //onlySelectedCB.selectedProperty().set(false);
    }


    private void unbindDocument(Document document) {
        //TRANSIENT
        document.getSimulationFile().stopTimeProperty().unbind();
        document.getSimulationFile().stepIncrementProperty().unbind();
        document.getSimulationFile().useStartTimeProperty().unbind();
        document.getSimulationFile().startTimeProperty().unbind();
        document.getSimulationFile().useInternalStepProperty().unbind();
        document.getSimulationFile().maxStepSizeProperty().unbind();
        //AC SWEEP
        document.getSimulationFile().startValueACProperty().unbind();
        document.getSimulationFile().stopValueACProperty().unbind();
        document.getSimulationFile().numberOfPointsACProperty().unbind();
        document.getSimulationFile().scaleTopACProperty().unbind();
        document.getSimulationFile().scaleBottomACProperty().unbind();
        //DC SWEEP
        document.getSimulationFile().startValueDCProperty().unbind();
        document.getSimulationFile().stopValueDCProperty().unbind();
        document.getSimulationFile().numberOfPointsDCProperty().unbind();
        document.getSimulationFile().scaleTopDCProperty().unbind();
        document.getSimulationFile().scaleBottomDCProperty().unbind();
        //GENERAL
        document.getSimulationFile().optimizeProperty().unbind();
        document.getSimulationFile().onlyAnalyzeNodesProperty().unbind();
    }

    private void onSimulate() {
        SimulationFile simulationFile;
        /*HBox selected = getSelectedPanel();
        if (acSweepMenu.equals(selected)) {
            simulationFile = document.getSimulationFile();
            simulationFile.setMode(SimulationFile.SimulationMode.AC_SWEEP);
        } else if (dcAnalysisMenu.equals(selected)) {
            simulationFile = document.getSimulationFile();
            simulationFile.setMode(SimulationFile.SimulationMode.DC_ANALYSIS);
        } else {*/
            simulationFile = document.getSimulationFile();
            simulationFile.setMode(SimulationFile.SimulationMode.TRANSIENT);
        /*}*/
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

    private HBox getSelectedPanel() {
        AtomicReference<HBox> toReturn = new AtomicReference<>();
        panelMap.forEach((b, p) -> {
            if (p.isVisible()) toReturn.set(p);
        });
        return toReturn.get();
    }

}
