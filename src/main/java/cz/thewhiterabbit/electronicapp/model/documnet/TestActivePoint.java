package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;


import java.util.ArrayList;
import java.util.List;

public class TestActivePoint extends ActivePoint implements DocumentObject{
    private RawObject rawObject;
    private List<DocumentObject> children;

    public TestActivePoint(RawObject rawObject) {
        this.rawObject = rawObject;
        children = new ArrayList<>();
    }

    @Override
    public void init() {
        setGridX(Integer.parseInt(rawObject.getProperty("gridX").getValue()));
        setGridY(Integer.parseInt(rawObject.getProperty("gridY").getValue()));
        setGridWidth(Integer.parseInt(rawObject.getProperty("gridWidth").getValue()));
        setGridHeight(Integer.parseInt(rawObject.getProperty("gridHeight").getValue()));
        setCanvasObject(this);
    }

    @Override
    public RawObject getRawObject() {
        return rawObject;
    }

    @Override
    public void setRawObject(RawObject rawObject) {
        this.rawObject = rawObject;
    }

    @Override//todo remove
    public CanvasObject getCanvasObject() {
        return this;
    }

    @Override//todo remove
    public void setCanvasObject(CanvasObject canvasObject) {

    }

    @Override
    public List<CanvasObject> getChildren() {
        return super.getChildrenList();
    }
}
