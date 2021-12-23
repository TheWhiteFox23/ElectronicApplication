package cz.thewhiterabbit.electronicapp.model.objects;

import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawProperty;

public abstract class GeneralObject extends DocumentObject{


    @Override
    public void init() {
        setGridX(Integer.parseInt(getRawObject().getProperty("gridX").getValue()));
        setGridY(Integer.parseInt(getRawObject().getProperty("gridY").getValue()));
        setGridWidth(Integer.parseInt(getRawObject().getProperty("gridWidth").getValue()));
        setGridHeight(Integer.parseInt(getRawObject().getProperty("gridHeight").getValue()));
        mapProperties();
    }

    @Override
    public void mapProperties() {
        getRawObject().getProperty("gridX").valueProperty().addListener((obs, oldVal, newVal) -> {
            setGridX(Integer.parseInt(newVal));
        });
        getRawObject().getProperty("gridY").valueProperty().addListener((obs, oldVal, newVal) -> {
            setGridY(Integer.parseInt(newVal));
        });
        getRawObject().getProperty("gridWidth").valueProperty().addListener((obs, oldVal, newVal) -> {
            setGridWidth(Integer.parseInt(newVal));
        });
        getRawObject().getProperty("gridHeight").valueProperty().addListener((obs, oldVal, newVal) -> {
            setGridHeight(Integer.parseInt(newVal));
        });
    }

    @Override
    public RawObject toRawObject() {
        if(getRawObject() == null){
            RawObject rawObject = new RawObject(getType());
            rawObject.addProperty(new RawProperty("gridX", String.valueOf(getGridX())));
            rawObject.addProperty(new RawProperty("gridY", String.valueOf(getGridY())));
            rawObject.addProperty(new RawProperty("gridWidth", String.valueOf(getGridWidth())));
            rawObject.addProperty(new RawProperty("gridHeight", String.valueOf(getGridHeight())));
            getChildrenList().forEach(l -> {
                rawObject.getChildren().add(((DocumentObject)l).toRawObject());
            });
            setRawObject(rawObject);
        }
        return getRawObject();
    }

    public abstract String getType();

}
