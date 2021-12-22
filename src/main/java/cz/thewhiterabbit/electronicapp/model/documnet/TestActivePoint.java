package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;


import java.util.ArrayList;
import java.util.List;

public class TestActivePoint extends ActivePoint {

    public TestActivePoint(RawObject rawObject) {
        setRawObject(rawObject);
    }

    @Override
    public void init() {
        setGridX(Integer.parseInt(getRawObject().getProperty("gridX").getValue()));
        setGridY(Integer.parseInt(getRawObject().getProperty("gridY").getValue()));
        setGridWidth(Integer.parseInt(getRawObject().getProperty("gridWidth").getValue()));
        setGridHeight(Integer.parseInt(getRawObject().getProperty("gridHeight").getValue()));
    }

}
