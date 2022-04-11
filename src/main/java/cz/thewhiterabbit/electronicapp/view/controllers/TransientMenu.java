package cz.thewhiterabbit.electronicapp.view.controllers;

import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.model.similation.SimulationFile;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class TransientMenu extends AnchorPane {

    @FXML
    private TextField stopTimeTF;
    @FXML
    private TextField stepIncrementTF;
    @FXML
    private CheckBox useStartTimeCB;
    @FXML
    private TextField startTimeTF;
    @FXML
    private CheckBox useInternalStepCB;
    @FXML
    private TextField maxStepSizeTF;

    public TransientMenu() {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/transient_menu.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public SimulationFile.Unit getStopTimeUnit(){return SimulationFile.Unit.NONE;}
    public SimulationFile.Unit getStartTimeUnit(){
        return SimulationFile.Unit.NONE;
    }
    public SimulationFile.Unit getStepIncrementUnit(){return SimulationFile.Unit.NONE;}
    public SimulationFile.Unit getMaxStepSizeUnit(){
        return SimulationFile.Unit.NONE;
    }
}
