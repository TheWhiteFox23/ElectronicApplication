package cz.thewhiterabbit.electronicapp.view.controllers;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.model.documnet.DocumentManager;
import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;
import cz.thewhiterabbit.electronicapp.model.property.*;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import cz.thewhiterabbit.electronicapp.view.canvas.DrawingAreaEvent;
import cz.thewhiterabbit.electronicapp.view.events.EditControlEvent;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class PropertiesPaneController {

    @FXML
    VBox propertiesPane;

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
    }

    private void setActiveObject(DocumentObject o) {
        this.activeObject = o;
        adjust();
    }

    private void adjust() {
        if (activeObject == null) {
            this.propertiesPane.setVisible(false);
        } else if (validateAO(activeObject)) {
            this.propertiesPane.getChildren().clear();
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
            propertiesPane.getChildren().addAll(getNodes(property));
        }
    }

    private List<? extends Node> getNodes(VisibleProperty property) {
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Label(property.getName()));
        nodes.add(getPropertyNode(property));
        nodes.add(new Label(property.getUnit()));
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
        property1.addListener((obs, oldVal, newVal) -> {
            textArea.setText(newVal.toString());
        });

        return textArea;
    }

    private void firePropertyChange(Property property1, String oldVal, String newVal) {
        if(!oldVal.equals(newVal)){
            System.out.println("Change property");
            eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_PROPERTY_CHANGE, (CanvasObject) property1.getBean(),
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

        comboBox.setOnAction(e -> firePropertyChange(property, property.getValue().toString(),
                comboBox.getSelectionModel().getSelectedItem().toString()));
        property.addListener((obs, oldVal, newVal) -> {
            comboBox.getSelectionModel().select(newVal);
        });

        return comboBox;
    }

    private Node getCheckBoxNode(VisibleProperty property) {
        CheckBox checkBox = new CheckBox();
        BooleanProperty booleanProperty = (BooleanProperty) property.getLinkedProperty();
        checkBox.setSelected(booleanProperty.getValue());
        checkBox.setOnAction(e -> firePropertyChange(booleanProperty, booleanProperty.getValue().toString(),
                String.valueOf(checkBox.isSelected())));
        booleanProperty.addListener((obs, oldVal, newVal) -> {
            checkBox.setSelected(newVal);
        });
        return checkBox;
    }

    private Node getLabelNode(VisibleProperty visibleProperty) {
        Label label = new Label();
        Property linkedProperty = (Property)visibleProperty.getLinkedProperty();
        label.setText(linkedProperty.getValue().toString());
        linkedProperty.addListener((obs, oldVal, newVal) -> {
            label.setText(newVal.toString());
        });
        return label;
    }

    private Node getTextField(VisibleProperty visibleProperty) {
        TextField textField = new TextField();
        Property linkedProperty = (Property)visibleProperty.getLinkedProperty();
        textField.setText(linkedProperty.getValue().toString());
        textField.focusedProperty().addListener(e ->firePropertyChange(linkedProperty, linkedProperty.getValue().toString(),
                textField.getText()));
        linkedProperty.addListener((obs, oldVal, newVal) -> {
            textField.setText(newVal.toString());
        });
        return textField;
    }


    private boolean validateAO(DocumentObject activeObject) {
        return ComponentAnnotationProcessor.isComponent(activeObject);
    }

}
