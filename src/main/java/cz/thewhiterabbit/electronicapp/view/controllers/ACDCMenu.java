package cz.thewhiterabbit.electronicapp.view.controllers;

import cz.thewhiterabbit.electronicapp.App;
import cz.thewhiterabbit.electronicapp.model.similation.SimulationFile;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ACDCMenu extends SimulationMenuPanel {
    private final StringProperty startValueText = new SimpleStringProperty();
    private final StringProperty stopValueText = new SimpleStringProperty();

    @FXML private TextField startValueTF;
    @FXML private Label startValueLabel;
    @FXML private TextField stopValueTF;
    @FXML private Label stopValueLabel;
    @FXML private TextField numberOfPointsTF;
    @FXML private TextField scaleTopTF;
    @FXML private TextField scaleBottomTF;

    private DoubleProperty startValue = new SimpleDoubleProperty(1e-3);
    private DoubleProperty stopValue = new SimpleDoubleProperty(1e+3);
    private IntegerProperty numberOfPoints = new SimpleIntegerProperty(500);
    private IntegerProperty scaleTop = new SimpleIntegerProperty(-180);
    private IntegerProperty scaleBottom = new SimpleIntegerProperty(180);



    public ACDCMenu(){
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/ac_dc_menu.fxml"));
        loader.setResources(App.localization);
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initializeComponents();
        bindTextFieldsProperty();
    }

    private void initializeComponents() {
        startValueText.addListener(e->{
            startValueLabel.setText(startValueText.getValue());
        });
        stopValueText.addListener(e->{
            stopValueLabel.setText(stopValueText.getValue());
        });
    }

    private void bindTextFieldsProperty() {
        bindDoubleTF(startValueTF, startValue);
        bindDoubleTF(stopValueTF, stopValue);
        bindIntegerTF(numberOfPointsTF, numberOfPoints);
        bindIntegerTF(scaleTopTF, scaleTop);
        bindIntegerTF(scaleBottomTF, scaleBottom);
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

    public int getStartValue(){
        return Integer.parseInt(startValueTF.getText());
    }

    public int getStopValue(){
        return Integer.parseInt(stopValueTF.getText());
    }

    public int getNumberOfPoints(){
        return Integer.parseInt(numberOfPointsTF.getText());
    }

    public DoubleProperty startValueProperty() {
        return startValue;
    }

    public void setStartValue(double startValue) {
        this.startValue.set(startValue);
    }

    public DoubleProperty stopValueProperty() {
        return stopValue;
    }

    public void setStopValue(double stopValue) {
        this.stopValue.set(stopValue);
    }

    public IntegerProperty numberOfPointsProperty() {
        return numberOfPoints;
    }

    public void setNumberOfPoints(int numberOfPoints) {
        this.numberOfPoints.set(numberOfPoints);
    }

    public int getScaleTop() {
        return scaleTop.get();
    }

    public IntegerProperty scaleTopProperty() {
        return scaleTop;
    }

    public void setScaleTop(int scaleTop) {
        this.scaleTop.set(scaleTop);
    }

    public int getScaleBottom() {
        return scaleBottom.get();
    }

    public IntegerProperty scaleBottomProperty() {
        return scaleBottom;
    }

    public void setScaleBottom(int scaleBottom) {
        this.scaleBottom.set(scaleBottom);
    }
}
