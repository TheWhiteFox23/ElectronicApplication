package cz.thewhiterabbit.electronicapp.view.controllers;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.model.documnet.DocumentManager;
import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;
import cz.thewhiterabbit.electronicapp.model.property.*;
import cz.thewhiterabbit.electronicapp.utilities.ValueValidator;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import cz.thewhiterabbit.electronicapp.view.canvas.DrawingAreaEvent;
import cz.thewhiterabbit.electronicapp.view.events.EditControlEvent;
import cz.thewhiterabbit.electronicapp.view.events.MenuEvent;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.*;

public class PropertiesPaneController {

    @FXML
    ScrollPane propertiesPane;
    @FXML
    VBox propertiesHolder;


    private EventAggregator eventAggregator = GUIEventAggregator.getInstance();
    private DocumentObject activeObject;


    @FXML
    private void initialize() {
        propertiesPane.setVisible(false);

        eventAggregator.addEventHandler(EditControlEvent.ACTIVE_OBJECT_CHANGE, h -> {
            CanvasObject o = ((EditControlEvent) h).getNewObject();
            setActiveObject((DocumentObject) o);
        });

        eventAggregator.addEventHandler(DocumentManager.DocumentManagerEvent.ACTIVE_DOCUMENT_CHANGED, h->{
            propertiesPane.requestFocus();
            DocumentManager.DocumentManagerEvent event = (DocumentManager.DocumentManagerEvent) h;
            DocumentObject o = (DocumentObject) event.getDocument().getGridModel().getActiveElement();
            setActiveObject(o);
        });

        eventAggregator.addEventHandler(MenuEvent.CLEAN_WORKPLACE, h->{
            propertiesPane.setVisible(false);
        });
    }

    private void setActiveObject(DocumentObject o) {
        this.activeObject = o;
        adjust();
    }

    private void adjust() {
        if (activeObject == null) {
            this.propertiesPane.setVisible(false);
        } else if (validateAO(activeObject)) {
            freePropertyPane();
            this.propertiesPane.setVisible(true);
            try {
                getPropertiesContent();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void getPropertiesContent() throws IllegalAccessException {
        List<VisibleProperty> properties = ComponentAnnotationProcessor.getProperties(activeObject);
        for (VisibleProperty property : properties) {
            propertiesHolder.getChildren().addAll(getNodes(property));
        }
    }

    private List<? extends Node> getNodes(VisibleProperty property) {
        List<Node> nodes = new ArrayList<>();

        Label nameLabel = new Label();
        nameLabel.setText(property.getName());
       //nodes.add(nameLabel);
        Label unitLabel = new Label();
        unitLabel.setText(property.getUnit());

        //nodes.add(getPropertyNode(property));

        nodes.add(new VBox(nameLabel, new HBox(getPropertyNode(property), unitLabel)));
        return nodes;
    }

    private Node getPropertyNode(VisibleProperty property) {
        switch (property.getType()) {
            case TEXT_FIELD :
                return getTextField(property);
            case LABEL :
                return getLabelNode(property);
            case CHECK_BOX:
                return getCheckBoxNode(property);
            case COMBO_BOX:
                return getComboBoxNode(property);
            case TEXT_AREA:
                return getTextArea(property);
            default:
                return null;
        }
    }

    private Node getTextArea(VisibleProperty property) {
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        Property property1 = (Property) property.getLinkedProperty();
        textArea.setText(property1.getValue().toString());
        textArea.focusedProperty().addListener(l ->{
            firePropertyChange(property1, property1.getValue().toString(), textArea.getText());
        });
        property1.addListener(l->{
            textArea.setText(String.valueOf(property1.getValue()));
        });
        return textArea;
    }

    private void firePropertyChange(Property property1, String oldVal, String newVal) {

        if(!oldVal.equals(newVal)){
            eventAggregator.fireEvent(
                    new DrawingAreaEvent(DrawingAreaEvent.OBJECT_PROPERTY_CHANGE, (CanvasObject) property1.getBean(),
                    property1, oldVal, newVal));
            eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.EDITING_FINISHED));
        }
    }

    private Node getComboBoxNode(VisibleProperty<Property> visibleProperty) {
        ComboBox comboBox = new ComboBox();
        Property property = (Property)visibleProperty.getLinkedProperty();
        String[] values = visibleProperty.getValues();

        for(String s: values){
            comboBox.getItems().add(s);
            if(s.equals(property.getValue())) comboBox.getSelectionModel().select(s);
        }

        comboBox.setOnAction(l ->{
            firePropertyChange(property, property.getValue().toString(),
                    comboBox.getSelectionModel().getSelectedItem().toString());
        });

        property.addListener(l->{
            for(String s: values){
                if(s.equals(property.getValue())) comboBox.getSelectionModel().select(s);
            }
        });

        return comboBox;
    }

    private Node getCheckBoxNode(VisibleProperty property) {
        CheckBox checkBox = new CheckBox();
        BooleanProperty booleanProperty = (BooleanProperty) property.getLinkedProperty();
        checkBox.setSelected(booleanProperty.getValue());

        checkBox.setOnAction(l ->{
            if(checkBox.isSelected()){
                firePropertyChange(booleanProperty, "false", "true");
            }else{
                firePropertyChange(booleanProperty,"true", "false");
            }

        });
        booleanProperty.addListener(l->{
            checkBox.setSelected(booleanProperty.getValue());
        });

        return checkBox;
    }

    private Node getLabelNode(VisibleProperty visibleProperty) {
        Label label = new Label();
        Property linkedProperty = (Property)visibleProperty.getLinkedProperty();
        label.setText(linkedProperty.getValue().toString());
        linkedProperty.addListener(l->{
            label.setText(linkedProperty.getValue().toString());
        });
        return label;
    }

    private Node getTextField(VisibleProperty visibleProperty) {
        TextField textField = new TextField();
        Property linkedProperty = (Property)visibleProperty.getLinkedProperty();
        textField.setText(linkedProperty.getValue().toString());

        textField.textProperty().addListener((obs, oldVal, newVal)->{
            manageValidation(textField, linkedProperty, newVal);
            manageChange(textField, linkedProperty);
        });

        textField.focusedProperty().addListener(l->{
            validateChangeAndApply(textField, linkedProperty);
        });

        textField.addEventHandler(KeyEvent.KEY_PRESSED, e->{
            if(e.getCode() == KeyCode.ENTER){
                validateChangeAndApply(textField, linkedProperty);
            }
        });

        linkedProperty.addListener(l->{
            textField.setText(linkedProperty.getValue().toString());
        });
        return textField;
    }

    private void manageChange(TextField textField, Property linkedProperty) {
        if(!String.valueOf(linkedProperty.getValue()).equals(textField.getText()) && !textField.getStyleClass().contains("invalid")){
            textField.getStyleClass().add("changed");
        }else{
            while (textField.getStyleClass().contains("changed")){
                textField.getStyleClass().remove("changed");
            }
        }
    }

    private void manageValidation(TextField textField, Property linkedProperty, String newVal) {
        boolean validation = ValueValidator.validateProperty(linkedProperty, newVal);
        if(!validation){
            textField.getStyleClass().add("invalid");
        }else{
            while (textField.getStyleClass().contains("invalid")){
                textField.getStyleClass().remove("invalid");
            }
        }
    }

    private void validateChangeAndApply(TextField textField, Property linkedProperty) {
        if(!String.valueOf(linkedProperty.getValue()).equals(textField.getText())){
            if(ValueValidator.validateProperty(linkedProperty, textField.getText())){
                firePropertyChange(linkedProperty, linkedProperty.getValue().toString(),
                        textField.getText());
                textField.setText(String.valueOf(linkedProperty.getValue()));
                textField.positionCaret(textField.getText().length());
            }else{
                textField.setText(String.valueOf(linkedProperty.getValue()));
                textField.positionCaret(textField.getText().length());
            }
        }
        manageChange(textField, linkedProperty);
    }

    private void freePropertyPane(){
        propertiesHolder.getChildren().clear();
        System.gc();
    }

    private boolean validateAO(DocumentObject activeObject) {
        return ComponentAnnotationProcessor.isComponent(activeObject);
    }


}
