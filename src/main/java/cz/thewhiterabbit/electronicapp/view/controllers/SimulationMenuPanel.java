package cz.thewhiterabbit.electronicapp.view.controllers;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public abstract class SimulationMenuPanel extends AnchorPane {

    protected void bindDoubleTF(TextField textField, DoubleProperty doubleProperty) {
        textField.textProperty().addListener((obs, oldVal, newVal)->{
            manageValidationDouble(textField);
        });
        textField.focusedProperty().addListener(l->{
            validateDoubleChangeAndApply(textField, doubleProperty);
        });
        textField.addEventHandler(KeyEvent.KEY_PRESSED, e->{
            if(e.getCode() == KeyCode.ENTER){
                validateDoubleChangeAndApply(textField, doubleProperty);
            }
        });
        doubleProperty.addListener(e->{
            double d = Double.parseDouble(textField.getText());
            if(doubleProperty.get() != d){
                textField.setText(String.valueOf(doubleProperty.get()));
                removeStyleClass(textField,"invalid");
                removeStyleClass(textField,"changed");
            }
        });

    }

    protected void bindIntegerTF(TextField textField, IntegerProperty integerProperty) {
        textField.textProperty().addListener((obs, oldVal, newVal)->{
            manageValidationInt(textField);
        });
        textField.focusedProperty().addListener(l->{
            validateIntegerChangeAndApply(textField, integerProperty);
        });
        textField.addEventHandler(KeyEvent.KEY_PRESSED, e->{
            if(e.getCode() == KeyCode.ENTER){
                validateIntegerChangeAndApply(textField, integerProperty);
            }
        });
        integerProperty.addListener(e->{
            int i = Integer.parseInt(textField.getText());
            if(integerProperty.get() != i){
                textField.setText(String.valueOf(integerProperty.get()));
                removeStyleClass(textField,"invalid");
                removeStyleClass(textField,"changed");
            }
        });
    }

    protected void manageValidationDouble(TextField stopTimeTF) {
        try{
            Double d = Double.parseDouble(stopTimeTF.getText());
            removeStyleClass(stopTimeTF,"invalid");
            setStyleClass(stopTimeTF, "changed");
        }catch (Exception e){
            removeStyleClass(stopTimeTF,"changed");
            setStyleClass(stopTimeTF, "invalid");
        }
    }

    protected void manageValidationInt(TextField stopTimeTF) {
        try{
            int i = Integer.parseInt(stopTimeTF.getText());
            removeStyleClass(stopTimeTF,"invalid");
            setStyleClass(stopTimeTF, "changed");
        }catch (Exception e){
            removeStyleClass(stopTimeTF,"changed");
            setStyleClass(stopTimeTF, "invalid");
        }
    }

    protected void validateDoubleChangeAndApply(TextField stopTimeTF, DoubleProperty doubleProperty) {
        double d = 0d;
        try{
            d = Double.parseDouble(stopTimeTF.getText());
        }catch (Exception e){}
        stopTimeTF.setText(String.valueOf(d));
        removeStyleClass(stopTimeTF, "changed");
        removeStyleClass(stopTimeTF,"invalid");
        if(doubleProperty.get() != d)doubleProperty.set(d);
    }

    protected void validateIntegerChangeAndApply(TextField textField, IntegerProperty integerProperty) {
        int i = 0;
        try{
            i = Integer.parseInt(textField.getText());
        }catch (Exception e){}
        textField.setText(String.valueOf(i));
        removeStyleClass(textField, "changed");
        removeStyleClass(textField,"invalid");
        if(integerProperty.get() != i)integerProperty.set(i);
    }

    protected void setStyleClass(TextField textField, String style){
        textField.getStyleClass().add(style);
    }
    protected void removeStyleClass(TextField textField, String style){
        while (textField.getStyleClass().contains(style)){
            textField.getStyleClass().remove(style);
        }
    }
}
