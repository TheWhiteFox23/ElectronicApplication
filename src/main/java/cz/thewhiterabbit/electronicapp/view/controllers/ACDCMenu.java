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

public class ACDCMenu extends GridPane {
    private final StringProperty startValueText = new SimpleStringProperty();
    private final StringProperty stopValueText = new SimpleStringProperty();

    @FXML private TextField startValueTF;
    @FXML private Label startValueLabel;
    @FXML private TextField stopValueTF;
    @FXML private Label stopValueLabel;
    @FXML private TextField numberOfPointsTF;
    @FXML private ComboBox starValueCB;
    @FXML private ComboBox stopValueCB;

    @FXML private RadioButton linRB;
    @FXML private RadioButton decRB;
    @FXML private RadioButton octRB;

    private ToggleGroup scaleToggleGroup;



    public ACDCMenu(){
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/ac_dc_menu.fxml"));
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
        scaleToggleGroup = new ToggleGroup();
        scaleToggleGroup.getToggles().addAll(linRB, decRB, octRB);
        decRB.setSelected(true);
        initializeComboBoxContent(starValueCB);
        initializeComboBoxContent(stopValueCB);
        startValueText.addListener(e->{
            startValueLabel.setText(startValueText.getValue());
        });
        stopValueText.addListener(e->{
            stopValueLabel.setText(stopValueText.getValue());
        });
    }

    private void initializeComboBoxContent(ComboBox comboBox){
        for(int i = 0; i<SimulationFile.Unit.values().length; i++){
            comboBox.getItems().add(SimulationFile.Unit.values()[i].getText());
        }
        comboBox.getSelectionModel().select(5);
    }

    public int getStartValue(){
        return Integer.parseInt(startValueTF.getText());
    }

    public int getStopValue(){
        return Integer.parseInt(stopValueTF.getText());
    }

    public int getNumberOfPoints(){
        return Integer.parseInt(numberOfPointsTF.getText());
    }

    public SimulationFile.Unit getStartValueUnit(){
        return getUnit(starValueCB);
    }

    public SimulationFile.Unit getStopValueUnit(){
        return getUnit(stopValueCB);
    }

    public SimulationFile.Scale getSelectedScale(){
        Toggle selectedToggle = scaleToggleGroup.getSelectedToggle();
        if (linRB.equals(selectedToggle)) {
            return SimulationFile.Scale.LINEAR;
        } else if (decRB.equals(selectedToggle)) {
            return SimulationFile.Scale.DECIMAL;
        } else if (octRB.equals(selectedToggle)) {
            return SimulationFile.Scale.OCTAVE;
        }
        return SimulationFile.Scale.DECIMAL;
    }

    private SimulationFile.Unit getUnit(ComboBox comboBox) {
        String value = comboBox.getValue().toString();
        for(int i = 0; i<SimulationFile.Unit.values().length; i++){
            if(value.equals(SimulationFile.Unit.values()[i])){
                return SimulationFile.Unit.values()[i];
            }
        }
        return SimulationFile.Unit.NONE;
    }

    public String getStartValueText() {
        return startValueText.get();
    }

    public StringProperty startValueTextProperty() {
        return startValueText;
    }

    public void setStartValueText(String startValueText) {
        this.startValueText.set(startValueText);
    }

    public String getStopValueText() {
        return stopValueText.get();
    }

    public StringProperty stopValueTextProperty() {
        return stopValueText;
    }

    public void setStopValueText(String stopValueText) {
        this.stopValueText.set(stopValueText);
    }
}
