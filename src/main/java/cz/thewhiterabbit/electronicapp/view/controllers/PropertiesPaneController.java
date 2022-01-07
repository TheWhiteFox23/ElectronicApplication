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
import javafx.scene.layout.VBox;

import java.util.*;

public class PropertiesPaneController {

    @FXML
    VBox propertiesPane;

    private EventAggregator eventAggregator = GUIEventAggregator.getInstance();
    private DocumentObject activeObject;

    private final Stack<TextArea> textAreasFree = new Stack<>();
    private final Stack<TextField> textFieldsFree = new Stack<>();
    private final Stack<Label> labelsFree = new Stack<>();
    private final Stack<CheckBox> checkBoxesFree = new Stack<>();
    private final Stack<ComboBox> comboBoxesFree = new Stack<>();

    private final Map<Node, ListenersCrate> nodeMap = new HashMap<>();

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
            propertiesPane.getChildren().addAll(getNodes(property));
        }
    }

    private List<? extends Node> getNodes(VisibleProperty property) {
        List<Node> nodes = new ArrayList<>();
        Label nameLabel = getLabel();
        nameLabel.setText(property.getName());
        nodes.add(nameLabel);

        nodes.add(getPropertyNode(property));

        Label unitLabel = getLabel();
        unitLabel.setText(property.getUnit());
        nodes.add(unitLabel);
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
        TextArea textArea = getTextArea();
        textArea.setWrapText(true);
        Property property1 = (Property) property.getLinkedProperty();
        textArea.setText(property1.getValue().toString());

        InvalidationListener listener = new WeakInvalidationListener(l->firePropertyChange(property1,
                property1.getValue().toString(), textArea.getText()));
        textArea.focusedProperty().addListener(listener);

        ChangeListener changeListener = new WeakChangeListener((obs, oldVal, newVal) -> textArea.setText(newVal.toString()));
        property1.addListener(changeListener);

        nodeMap.put(textArea, new ListenersCrate(changeListener, listener, property1));

        return textArea;
    }

    private void firePropertyChange(Property property1, String oldVal, String newVal) {
        if(!oldVal.equals(newVal)){
            eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_PROPERTY_CHANGE, (CanvasObject) property1.getBean(),
                    property1, oldVal, newVal));
            eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.EDITING_FINISHED));
        }
    }

    private Node getComboBoxNode(VisibleProperty<Property> visibleProperty) {
        ComboBox comboBox = getComboBox();
        Property property = (Property)visibleProperty.getLinkedProperty();
        String[] values = visibleProperty.getValues();

        for(String s: values){
            comboBox.getItems().add(s);
            if(s.equals(property.getValue())) comboBox.getSelectionModel().select(s);
        }

        EventHandler eventHandler = new WeakEventHandler(h -> firePropertyChange(property, property.getValue().toString(),
                    comboBox.getSelectionModel().getSelectedItem().toString()));
        comboBox.setOnAction(eventHandler);

        ChangeListener changeListener = new WeakChangeListener((obs, oldVal, newVal) -> {
            comboBox.getSelectionModel().select(newVal);
        });
        property.addListener(changeListener);

        nodeMap.put(comboBox, new ListenersCrate(changeListener, eventHandler, property));

        return comboBox;
    }

    private Node getCheckBoxNode(VisibleProperty property) {
        CheckBox checkBox = getCheckBox();
        BooleanProperty booleanProperty = (BooleanProperty) property.getLinkedProperty();
        checkBox.setSelected(booleanProperty.getValue());

        EventHandler eventHandler = new WeakEventHandler(h -> firePropertyChange(booleanProperty, booleanProperty.getValue().toString(),
                String.valueOf(checkBox.isSelected())));
        checkBox.setOnAction(eventHandler);

        ChangeListener changeListener = new WeakChangeListener((obs, oldVal, newVal) -> {
            checkBox.setSelected((Boolean) newVal);
        });
        booleanProperty.addListener(changeListener);

        nodeMap.put(checkBox, new ListenersCrate(changeListener, eventHandler, booleanProperty));
        return checkBox;
    }

    private Node getLabelNode(VisibleProperty visibleProperty) {
        Label label = getLabel();
        Property linkedProperty = (Property)visibleProperty.getLinkedProperty();
        label.setText(linkedProperty.getValue().toString());

        ChangeListener changeListener = new WeakChangeListener((obs, oldVal, newVal) -> {
            label.setText(newVal.toString());
        });
        linkedProperty.addListener(changeListener);
        nodeMap.put(label, new ListenersCrate(changeListener, linkedProperty));
        return label;
    }

    private Node getTextField(VisibleProperty visibleProperty) {
        TextField textField = getTextField();
        Property linkedProperty = (Property)visibleProperty.getLinkedProperty();
        textField.setText(linkedProperty.getValue().toString());

        InvalidationListener textPropertyListener = new WeakInvalidationListener(l -> {
            boolean validation = ValueValidator.validateProperty(linkedProperty, textField.textProperty().getValue());
            if(!validation) System.out.println("InvalidValue");//TODO visually highlight
        });
        textField.textProperty().addListener(textPropertyListener);

        InvalidationListener invalidationListener = new WeakInvalidationListener(e ->firePropertyChange(linkedProperty, linkedProperty.getValue().toString(),
                textField.getText()));
        textField.focusedProperty().addListener(invalidationListener);

        ChangeListener changeListener = initChangeListener(textField, linkedProperty);

        nodeMap.put(textField, new ListenersCrate(changeListener, invalidationListener,textPropertyListener, linkedProperty));
        return textField;
    }

    private ChangeListener initChangeListener(TextField textField, Property linkedProperty) {
        ChangeListener changeListener = new WeakChangeListener((obs, oldVal, newVal) -> {
            textField.setText(newVal.toString());
        });
        linkedProperty.addListener(changeListener);
        return changeListener;
    }

    private TextField getTextField(){
        if(!textFieldsFree.empty()) return textFieldsFree.pop();
        return new TextField();
    }

    private Label getLabel(){
        if(!labelsFree.empty()) return labelsFree.pop();
        return new Label();
    }

    private TextArea getTextArea(){
        if(!textAreasFree.empty()) return textAreasFree.pop();
        return new TextArea();
    }

    private CheckBox getCheckBox(){
        if(!checkBoxesFree.empty()) return checkBoxesFree.pop();
        return new CheckBox();
    }

    private ComboBox getComboBox(){
        if(!comboBoxesFree.empty()) return comboBoxesFree.pop();
        return new ComboBox();
    }

    private void free(Node node){
        //todo remove listeners from node
        if(node instanceof Label){
            labelsFree.push((Label) node);
        }else if(node instanceof TextField){
            textFieldsFree.push((TextField) node);
            node.focusedProperty().removeListener(nodeMap.get(node).invalidationListener);
            ((TextField) node).textProperty().removeListener(nodeMap.get(node).textPropertyListener);
        }else if(node instanceof TextArea){
            textAreasFree.push((TextArea) node);
            node.focusedProperty().removeListener(nodeMap.get(node).invalidationListener);
        }else if(node instanceof CheckBox){
            checkBoxesFree.push((CheckBox) node);
            ((CheckBox) node).setOnAction(actionEvent -> {});
        }else if(node instanceof ComboBox){
            comboBoxesFree.push((ComboBox) node);
            ((ComboBox<?>) node).setOnAction(actionEvent -> {});
            ((ComboBox<?>) node).getSelectionModel().clearSelection();
            ((ComboBox<?>) node).getItems().clear();
        }
        if(nodeMap.containsKey(node)){
            nodeMap.get(node).property.removeListener(nodeMap.get(node).changeListener);
        }
        nodeMap.remove(node);

    }

    private void freePropertyPane(){
        propertiesPane.getChildren().forEach(c -> free(c));
        propertiesPane.getChildren().clear();
    }


    private boolean validateAO(DocumentObject activeObject) {
        return ComponentAnnotationProcessor.isComponent(activeObject);
    }


    private class ListenersCrate{
        ChangeListener changeListener;
        InvalidationListener invalidationListener;
        InvalidationListener textPropertyListener;
        Property property;
        EventHandler eventHandler;

        public ListenersCrate(ChangeListener changeListener, InvalidationListener invalidationListener, Property property) {
            this.changeListener = changeListener;
            this.invalidationListener = invalidationListener;
            this.property = property;
        }

        public ListenersCrate(ChangeListener changeListener, InvalidationListener invalidationListener,
                              InvalidationListener textPropertyListener,Property property) {
            this.changeListener = changeListener;
            this.invalidationListener = invalidationListener;
            this.textPropertyListener = textPropertyListener;
            this.property = property;
        }

        public ListenersCrate(ChangeListener changeListener, EventHandler eventHandler, Property property) {
            this.changeListener = changeListener;
            this.eventHandler = eventHandler;
            this.property = property;
        }

        public ListenersCrate(ChangeListener changeListener, Property property) {
            this.changeListener = changeListener;
            this.property = property;
        }
    }

}
