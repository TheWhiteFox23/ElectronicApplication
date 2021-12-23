package cz.thewhiterabbit.electronicapp.model.objects;

import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


public class GeneralCanvasObject extends GeneralObject {
    private final String type = "TEST_OBJECT";

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
    public String getType() {
        return type;
    }
}
