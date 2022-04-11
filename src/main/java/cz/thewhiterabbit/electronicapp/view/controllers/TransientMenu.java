package cz.thewhiterabbit.electronicapp.view.controllers;

import cz.thewhiterabbit.electronicapp.App;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class TransientMenu extends SimulationMenuPanel {

    @FXML private TextField stopTimeTF;
    @FXML private TextField stepIncrementTF;
    @FXML private CheckBox useStartTimeCB;
    @FXML private TextField startTimeTF;
    @FXML private CheckBox useInternalStepCB;
    @FXML private TextField maxStepSizeTF;

    //Properties
    private DoubleProperty stopTime = new SimpleDoubleProperty(5);
    private DoubleProperty stepIncrement = new SimpleDoubleProperty(1e-3);
    private BooleanProperty useStartTime=new SimpleBooleanProperty(false);
    private DoubleProperty startTime = new SimpleDoubleProperty(0);
    private BooleanProperty useInternalStep = new SimpleBooleanProperty(false);
    private DoubleProperty maxStepSize = new SimpleDoubleProperty(5e-2);

    public TransientMenu() {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/transient_menu.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bindTextFieldsProperty();
    }

    private void bindTextFieldsProperty() {
        bindDoubleTF(startTimeTF, startTime);
        bindDoubleTF(stopTimeTF, stopTime);
        bindDoubleTF(maxStepSizeTF, maxStepSize);
        bindDoubleTF(stepIncrementTF, stepIncrement);
        useInternalStepCB.selectedProperty().bindBidirectional(useInternalStepProperty());
        useStartTimeCB.selectedProperty().bindBidirectional(useStartTimeProperty());
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

    public DoubleProperty stopTimeProperty() {
        return stopTime;
    }

    public void setStopTime(double stopTime) {
        this.stopTime.set(stopTime);
    }

    public DoubleProperty stepIncrementProperty() {
        return stepIncrement;
    }

    public void setStepIncrement(double stepIncrement) {
        this.stepIncrement.set(stepIncrement);
    }

    public boolean isUseStartTime() {
        return useStartTime.get();
    }

    public BooleanProperty useStartTimeProperty() {
        return useStartTime;
    }

    public void setUseStartTime(boolean useStartTime) {
        this.useStartTime.set(useStartTime);
    }

    public DoubleProperty startTimeProperty() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime.set(startTime);
    }

    public boolean isUseInternalStep() {
        return useInternalStep.get();
    }

    public BooleanProperty useInternalStepProperty() {
        return useInternalStep;
    }

    public void setUseInternalStep(boolean useInternalStep) {
        this.useInternalStep.set(useInternalStep);
    }

    public DoubleProperty maxStepSizeProperty() {
        return maxStepSize;
    }

    public void setMaxStepSize(double maxStepSize) {
        this.maxStepSize.set(maxStepSize);
    }
}
