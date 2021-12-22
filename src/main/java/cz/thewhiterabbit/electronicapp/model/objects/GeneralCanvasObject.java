package cz.thewhiterabbit.electronicapp.model.objects;

import cz.thewhiterabbit.electronicapp.model.documnet.DocumentObject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


public class GeneralCanvasObject extends DocumentObject {
    public GeneralCanvasObject(){
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
    public void init() {

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
