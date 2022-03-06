package cz.thewhiterabbit.electronicapp.model.objects;

import cz.thewhiterabbit.electronicapp.model.components.Component;
import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;
import cz.thewhiterabbit.electronicapp.model.property.ComponentAnnotationProcessor;
import cz.thewhiterabbit.electronicapp.model.property.ComponentPropertyType;
import cz.thewhiterabbit.electronicapp.model.property.PropertyDialogField;
import cz.thewhiterabbit.electronicapp.model.property.RawPropertyMapping;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawProperty;
import javafx.beans.property.*;

import java.util.List;

public abstract class GeneralComponent extends DocumentObject {

    @RawPropertyMapping
    private final IntegerProperty _gridX = gridXProperty();
    @RawPropertyMapping
    private final IntegerProperty _gridY = gridYProperty();
    @RawPropertyMapping
    private final IntegerProperty _gridWidth = gridWidthProperty();
    @RawPropertyMapping
    private final IntegerProperty _gridHeight = gridHeightProperty();

    public GeneralComponent(){
        setGridHeight(2);
        setGridWidth(2);
    }

    @Override
    public void init() {
        List<Property> properties = ComponentAnnotationProcessor.getMappingProperties(this);
        properties.forEach(p -> {
            if (p instanceof IntegerProperty) {
                p.setValue(Integer.parseInt(getProperty(p.getName()).getValue()));
            } else if (p instanceof DoubleProperty) {
                p.setValue(Double.parseDouble(getProperty(p.getName()).getValue()));
            } else if (p instanceof FloatProperty) {
                p.setValue(Float.parseFloat(getProperty(p.getName()).getValue()));
            } else if (p instanceof StringProperty) {
                p.setValue(getProperty(p.getName()).getValue());
            }
        });
    }

    @Override
    public void mapProperties() {
        List<Property> properties = ComponentAnnotationProcessor.getMappingProperties(this);
        properties.forEach(p -> {
            if (p instanceof IntegerProperty) {
                getRawObject().getProperty(p.getName()).valueProperty().addListener((obs, oldVal, newVal) -> {
                    p.setValue(Integer.parseInt(newVal));
                });
            } else if (p instanceof DoubleProperty) {
                getRawObject().getProperty(p.getName()).valueProperty().addListener((obs, oldVal, newVal) -> {
                    p.setValue(Double.parseDouble(newVal));
                });
            } else if (p instanceof FloatProperty) {
                getRawObject().getProperty(p.getName()).valueProperty().addListener((obs, oldVal, newVal) -> {
                    p.setValue(Float.parseFloat(newVal));
                });
            } else if (p instanceof StringProperty) {
                getRawObject().getProperty(p.getName()).valueProperty().addListener((obs, oldVal, newVal) -> {
                    p.setValue(newVal);
                });
            }
        });
    }

    public RawObject toRawObject() {
        RawObject rawObject = new RawObject(getType());
        List<Property> properties = ComponentAnnotationProcessor.getMappingProperties(this);
        properties.forEach(p -> {
            rawObject.addProperty(new RawProperty(p.getName(), String.valueOf(p.getValue())));
        });
        return rawObject;
    }


    public String getType() {
        return getComponent().getType();
    }

    public abstract Component getComponent();


}
