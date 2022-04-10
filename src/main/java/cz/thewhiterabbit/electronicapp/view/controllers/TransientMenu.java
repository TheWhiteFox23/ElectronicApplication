package cz.thewhiterabbit.electronicapp.view.controllers;

import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.model.similation.SimulationFile;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class TransientMenu extends GridPane {

    @FXML
    private TextField stopTimeTF;
    @FXML
    private TextField stepIncrementTF;
    @FXML
    private ComboBox stopTimeCoB;
    @FXML
    private ComboBox stepIncrementCoB;
    @FXML
    private CheckBox useStartTimeCB;
    @FXML
    private TextField startTimeTF;
    @FXML
    private ComboBox startTimeCoB;
    @FXML
    private CheckBox useInternalStepCB;
    @FXML
    private TextField maxStepSizeTF;
    @FXML
    private ComboBox maxStepSizeCoB;

    public TransientMenu() {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/transient_menu.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initializeComponents();


    }

    private void initializeComponents() {
        initializeComboBoxContent(startTimeCoB);
        initializeComboBoxContent(stopTimeCoB);
        initializeComboBoxContent(stepIncrementCoB);
        initializeComboBoxContent(maxStepSizeCoB);
    }

    private void initializeComboBoxContent(ComboBox comboBox) {
        for (int i = 0; i < SimulationFile.Unit.values().length; i++) {
            comboBox.getItems().add(SimulationFile.Unit.values()[i].getText());
        }
        comboBox.getSelectionModel().select(5);
    }

    private SimulationFile.Unit getUnit(ComboBox comboBox) {
        String value = comboBox.getValue().toString();
        for (int i = 0; i < SimulationFile.Unit.values().length; i++) {
            if (value.equals(SimulationFile.Unit.values()[i])) {
                return SimulationFile.Unit.values()[i];
            }
        }
        return SimulationFile.Unit.NONE;
    }

    public int getStopTime(){
        return Integer.parseInt(stopTimeTF.getText());
    }
    public int getStartTime(){
        return Integer.parseInt(startTimeTF.getText());
    }
    public int getStepIncrement(){
        return Integer.parseInt(stepIncrementTF.getText());
    }
    public int getMaxStepSize(){
        return Integer.parseInt(maxStepSizeTF.getText());
    }
    public boolean isStartTime(){
        return useStartTimeCB.isSelected();
    }
    public boolean isInternalStep(){
        return useInternalStepCB.isSelected();
    }

    public SimulationFile.Unit getStopTimeUnit(){
        return getUnit(stopTimeCoB);
    }
    public SimulationFile.Unit getStartTimeUnit(){
        return getUnit(startTimeCoB);
    }
    public SimulationFile.Unit getStepIncrementUnit(){
        return getUnit(stepIncrementCoB);
    }
    public SimulationFile.Unit getMaxStepSizeUnit(){
        return getUnit(maxStepSizeCoB);
    }
}
