package cz.thewhiterabbit.electronicapp.view.controllers;

import cz.thewhiterabbit.electronicapp.EventAggregator;
import cz.thewhiterabbit.electronicapp.GUIEventAggregator;
import cz.thewhiterabbit.electronicapp.model.documnet.Document;
import cz.thewhiterabbit.electronicapp.model.documnet.DocumentManager;
import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import cz.thewhiterabbit.electronicapp.view.canvas.DrawingAreaEvent;
import cz.thewhiterabbit.electronicapp.view.events.EditControlEvent;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.lang.annotation.*;
import java.lang.reflect.Field;

public class PropertiesPaneController {

    @FXML Label objectLabel;
    @FXML VBox propertiesPane;

    private EventAggregator eventAggregator = GUIEventAggregator.getInstance();
    private DocumentObject activeObject;

    @FXML private void initialize(){
        eventAggregator.addEventHandler(EditControlEvent.ACTIVE_OBJECT_CHANGE, h->{
            CanvasObject o = ((EditControlEvent) h).getNewObject();
            setActiveObject((DocumentObject)o);
        });

        eventAggregator.addEventHandler(DocumentManager.DocumentManagerEvent.ACTIVE_DOCUMENT_CHANGED, h->{
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
        if(activeObject == null){
            this.propertiesPane.setVisible(false);
        }else if(validateAO(activeObject)){
            this.propertiesPane.setVisible(true);
            printProperties(activeObject);
            //objectLabel.setText(activeObject.getType());
        }
    }

    private void printProperties(DocumentObject activeObject) {
        propertiesPane.getChildren().clear();
        Class clazz = activeObject.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field: fields){
            if(field.isAnnotationPresent(Property.class)){
                try {
                    Property annotation = field.getDeclaredAnnotation(Property.class);
                    if(!annotation.editable()){
                        field.setAccessible(true);
                        IntegerProperty integerProperty = (IntegerProperty) field.get(activeObject);
                        propertiesPane.getChildren().add(new Label(integerProperty.getName() + ": " + integerProperty.get()));
                    }else{
                        field.setAccessible(true);
                        IntegerProperty integerProperty = (IntegerProperty) field.get(activeObject);
                        HBox hBox = new HBox();
                        hBox.getChildren().add(new Label(integerProperty.getName() + ":"));
                        TextField textField = new TextField(String.valueOf(integerProperty.get()));
                        textField.addEventHandler(KeyEvent.KEY_PRESSED, h->{
                            if(h.getCode() == KeyCode.ENTER){
                                int value = Integer.parseInt(textField.getText());
                                eventAggregator.fireEvent(new DrawingAreaEvent(DrawingAreaEvent.OBJECT_PROPERTY_CHANGE, activeObject, integerProperty, integerProperty.get(), value));
                            }
                        });
                        hBox.getChildren().add(textField);
                        propertiesPane.getChildren().add(hBox);
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private boolean validateAO(DocumentObject activeObject) {
        Class clazz = activeObject.getClass();
        if(clazz.isAnnotationPresent(Component.class)){
            return true;
        }
        return false;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Component{

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Property{
        boolean editable() default true;
        PropertyType propertyType();

    }

    public enum PropertyType {
        INTEGER,
        FLOAT,
        STRING
    }


}
