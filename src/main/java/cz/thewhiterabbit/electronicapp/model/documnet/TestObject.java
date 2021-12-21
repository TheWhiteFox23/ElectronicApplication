package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.objects.GeneralCanvasObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawProperty;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;

public class TestObject extends DocumentObject{
    public TestObject(RawObject rawObject) {
        super(rawObject);

        RawProperty stringGridX = rawObject.getProperty("gridX");
        RawProperty stringGridY = rawObject.getProperty("gridY");
        RawProperty stringGridWidth = rawObject.getProperty("gridWidth");
        RawProperty stringGridHeight = rawObject.getProperty("gridHeight");

        int gridX = Integer.parseInt(stringGridX.getValue());
        int gridY = Integer.parseInt(stringGridY.getValue());
        int gridWidth = Integer.parseInt(stringGridWidth.getValue());
        int gridHeight = Integer.parseInt(stringGridHeight.getValue());

        CanvasObject object = new GeneralCanvasObject();
        object.set(gridX, gridY, gridWidth, gridHeight);
        setCanvasObject(object);
    }
}
