package cz.thewhiterabbit.electronicapp.view.controllers;

import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.model.documnet.Document;
import cz.thewhiterabbit.electronicapp.model.documnet.DocumentManager;
import cz.thewhiterabbit.electronicapp.model.similation.SimulationFile;
import cz.thewhiterabbit.electronicapp.view.components.CategoryButton;
import cz.thewhiterabbit.electronicapp.view.events.MenuEvent;
import cz.thewhiterabbit.electronicapp.view.events.SimulationEvents;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class SimulationMenuController {
    @FXML
    private Button simulationButton;
    @FXML
    private HBox simulationMenu;
    @FXML
    private HBox categoriesBox;
    @FXML
    private CategoryButton acSweepButton;
    @FXML
    private CategoryButton transientButton;
    @FXML
    private CategoryButton dcAnalysisButton;
    @FXML
    private HBox acSweepMenu;
    @FXML
    private HBox transientMenu;
    @FXML
    private HBox dcAnalysisMenu;
    @FXML
    private VBox menuVBox;
    @FXML
    private Button simulateButton;

    /** PANELS **/
    @FXML private ACDCMenu acSweepPanel;
    @FXML private ACDCMenu dcAnalysisPanel;
    @FXML private TransientMenu transientPanel;

    @FXML private CheckBox optimizeCB;
    @FXML private CheckBox onlySelectedCB;


    private HashMap<CategoryButton, HBox> panelMap;


    @FXML
    private void initialize() {
        simulationButton.setOnAction(e -> {
            GUIEventAggregator.getInstance().fireEvent(new MenuEvent(MenuEvent.SWITCH_MODE_SCHEMATIC));
        });
        panelMap = new HashMap<>(){{
            put(acSweepButton, acSweepMenu);
            put(transientButton, transientMenu);
            put(dcAnalysisButton, dcAnalysisMenu);
        }};
        setActive(transientButton);
        initializeButtons();
        simulateButton.setOnAction(e->{
            onSimulate();
        });
    }

    private void onSimulate() {
        SimulationFile simulationFile;
        HBox selected = getSelectedPanel();
        if (acSweepMenu.equals(selected)) {
            simulationFile = getACDCSimulationFile(acSweepPanel, SimulationFile.SimulationMode.AC_SWEEP);
        } else if (dcAnalysisMenu.equals(selected)) {
            simulationFile = getACDCSimulationFile(dcAnalysisPanel, SimulationFile.SimulationMode.DC_ANALYSIS);
        } else {
            simulationFile = getTransientSimulationFile(transientPanel);
        }
        simulationFile.setOptimize(optimizeCB.isSelected());
        simulationFile.setOnlyAnalyzeNodes(onlySelectedCB.isSelected());

        GUIEventAggregator.getInstance().fireEvent(new SimulationEvents(SimulationEvents.SIMULATE_CLICKED, simulationFile));
    }
    private SimulationFile getACDCSimulationFile(ACDCMenu acdcMenu, SimulationFile.SimulationMode mode){
        SimulationFile simulationFile = new SimulationFile();
        simulationFile.setMode(mode);
        simulationFile.setStartTime(acdcMenu.getStartValue());
        simulationFile.setStopTime(acdcMenu.getStopValue());
        simulationFile.setNumberOfPoints(acdcMenu.getNumberOfPoints());
        simulationFile.setScale(acdcMenu.getSelectedScale());
        return simulationFile;
    }

    private SimulationFile getTransientSimulationFile(TransientMenu transientMenu){
        SimulationFile simulationFile = new SimulationFile();
        simulationFile.setMode(SimulationFile.SimulationMode.TRANSIENT);
        simulationFile.setStopTime(transientMenu.getStopTime());
        simulationFile.setStepIncrement(transientMenu.getStepIncrement());

        simulationFile.setUseStartTime(transientMenu.isStartTime());
        simulationFile.setStartTime(transientMenu.getStartTime());

        simulationFile.setUseInternalStep(transientMenu.isInternalStep());
        simulationFile.setMaxStepSize(transientMenu.getMaxStepSize());
        return simulationFile;
    }

    private void initializeButtons() {
        panelMap.forEach((button, panel)->{
            button.setOnAction(e->setActive(button));
        });
    }

    private void setActive(CategoryButton categoryButton){
        panelMap.forEach((button, panel)->{
            button.setActive(false);
            panel.setVisible(false);
            menuVBox.getChildren().remove(panel);
        });
        categoryButton.setActive(true);
        panelMap.get(categoryButton).setVisible(true);
        menuVBox.getChildren().add(panelMap.get(categoryButton));
    }

    private HBox getSelectedPanel(){
        AtomicReference<HBox> toReturn = new AtomicReference<>();
        panelMap.forEach((b,p)->{
            if(p.isVisible()) toReturn.set(p);
        });
        return toReturn.get();
    }

}
