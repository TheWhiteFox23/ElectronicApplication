package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.objects.GeneralCanvasObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;

public class TestObject extends GeneralCanvasObject{


    public TestObject(RawObject rawObject) {
        setRawObject(rawObject);
    }

    @Override
    public void init() {
        setGridX(Integer.parseInt(getRawObject().getProperty("gridX").getValue()));
        setGridY(Integer.parseInt(getRawObject().getProperty("gridY").getValue()));
        setGridWidth(Integer.parseInt(getRawObject().getProperty("gridWidth").getValue()));
        setGridHeight(Integer.parseInt(getRawObject().getProperty("gridHeight").getValue()));
        mapProperties();
    }

}
