package cz.thewhiterabbit.electronicapp.model.objects;

import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;

import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


public class GeneralCanvasObject extends GeneralObject {
    public GeneralCanvasObject(){
    }

    public GeneralCanvasObject(RawObject rawObject){
        setRawObject(rawObject);
    }

    @Override
    public void doPaint(GraphicsContext gc) {
        gc.setFill(Color.DARKSLATEGRAY);
        gc.fillRect(0, 0, getHeight(), getHeight());
        gc.setFill(getColor());
        gc.fillRect(0, 0, getWidth()/2, getHeight()/2);
        gc.fillRect(getWidth()/2, getHeight()/2, getWidth()/2, getHeight()/2);
        gc.restore();

    }

    private Paint getColor(){
        if(isHovered()){
            return Color.RED;
        }else if (isSelected()){
            return Color.BLUEVIOLET;
        }else{
            return Color.GREENYELLOW;
        }
    }

    @Override
    public RawObject toRawObject() {
        if(getRawObject() == null){
            RawObject rawObject = new RawObject("TEST_OBJECT");
            rawObject.addProperty(new RawProperty("gridX", String.valueOf(getGridX())));
            rawObject.addProperty(new RawProperty("gridY", String.valueOf(getGridY())));
            rawObject.addProperty(new RawProperty("gridWidth", String.valueOf(getGridWidth())));
            rawObject.addProperty(new RawProperty("gridHeight", String.valueOf(getGridHeight())));
            getChildrenList().forEach(l -> {
                rawObject.getChildren().add(((DocumentObject)l).toRawObject());
            });
            setRawObject(rawObject);
            //TODO children and make universal
        }
        return getRawObject();
    }
}
