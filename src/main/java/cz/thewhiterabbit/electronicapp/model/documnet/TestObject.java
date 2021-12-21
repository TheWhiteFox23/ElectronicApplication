package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.objects.GeneralCanvasObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawProperty;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import javafx.beans.property.SimpleIntegerProperty;

public class TestObject extends DocumentObject{
    private SimpleIntegerProperty gridX;
    private SimpleIntegerProperty gridY;
    private SimpleIntegerProperty gridWidth;
    private SimpleIntegerProperty gridHeight;

    public TestObject(RawObject rawObject) {
        super(rawObject);

        RawProperty stringGridX = rawObject.getProperty("gridX");
        RawProperty stringGridY = rawObject.getProperty("gridY");
        RawProperty stringGridWidth = rawObject.getProperty("gridWidth");
        RawProperty stringGridHeight = rawObject.getProperty("gridHeight");

        if(stringGridWidth != null) System.out.println(stringGridWidth.getValue());

        int gridX = Integer.parseInt(stringGridX.getValue());
        int gridY = Integer.parseInt(stringGridY.getValue());
        int gridWidth = Integer.parseInt(stringGridWidth.getValue());
        int gridHeight = Integer.parseInt(stringGridHeight.getValue());

        this.gridX = new SimpleIntegerProperty(gridX);
        this.gridY = new SimpleIntegerProperty(gridY);
        this.gridHeight = new SimpleIntegerProperty(gridHeight);
        this.gridWidth = new SimpleIntegerProperty(gridWidth);


        CanvasObject object = new GeneralCanvasObject();
        object.set(gridX, gridY, gridWidth, gridHeight);
        setCanvasObject(object);
    }
}
