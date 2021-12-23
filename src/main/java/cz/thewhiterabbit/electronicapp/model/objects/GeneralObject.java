package cz.thewhiterabbit.electronicapp.model.objects;

import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;

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
}
